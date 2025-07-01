from model import Model, Diagnosis
import numpy as np
import cv2
from collections import deque


def preprocess_image(img_path):
    img = cv2.imread(img_path)
    return img


def split_image(image, tile_size=50):
    height, width = image.shape[:2]  # Remove the batch dimension
    tiles = []
    for y in range(0, height, tile_size):
        for x in range(0, width, tile_size):
            if x + tile_size <= width and y + tile_size <= height:
                tile = image[y:y + tile_size, x:x + tile_size]
                tiles.append(tile)
    return tiles


class BreastCancer(Model):
    def __init__(self):
        super().__init__("breast_cancer_detection.keras")
        self.class_names = {
            0: 'No Cancer',
            1: 'Cancer',
        }

    def _predict(self, image):
        # Ensure image has the correct shape (1, 50, 50, 3)
        if len(image.shape) == 3:
            image = np.expand_dims(image, axis=0)
        prediction = self.model.predict(image, verbose=0)
        class_idx = np.argmax(prediction, axis=1)[0]
        confidence = prediction[0][class_idx]
        return class_idx, confidence

    def predict(self, image_path: str) -> Diagnosis:
        img = preprocess_image(image_path)
        image_tiles = split_image(img, 50)
        predictions = []

        height, width = img.shape[:2]
        rows, cols = height // 50, width // 50
        cancer_map = [[0 for _ in range(cols)] for _ in range(rows)]

        for i, tile in enumerate(image_tiles):
            if tile.shape[:2] != (50, 50):
                continue
            pred, confidence = self._predict(tile)
            if confidence > 0.85:
                predictions.append(pred)

            r = i // cols
            c = i % cols

            if pred == 1 and confidence > 0.98:
                cancer_map[r][c] = 1  # Mark cancerous tile

        # Flood fill to group adjacent patches
        visited = [[False for _ in range(cols)] for _ in range(rows)]
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        def bfs(r, c):
            queue = deque()
            queue.append((r, c))
            group = [(r, c)]
            visited[r][c] = True
            while queue:
                cr, cc = queue.popleft()
                for dr, dc in directions:
                    nr, nc = cr + dr, cc + dc
                    if 0 <= nr < rows and 0 <= nc < cols:
                        if cancer_map[nr][nc] == 1 and not visited[nr][nc]:
                            visited[nr][nc] = True
                            queue.append((nr, nc))
                            group.append((nr, nc))
            return group

        # Merge and draw rectangles around groups
        for r in range(rows):
            for c in range(cols):
                if cancer_map[r][c] == 1 and not visited[r][c]:
                    group = bfs(r, c)
                    min_x = min(col for _, col in group) * 50
                    min_y = min(row for row, _ in group) * 50
                    max_x = (max(col for _, col in group) + 1) * 50
                    max_y = (max(row for row, _ in group) + 1) * 50
                    cv2.rectangle(img, (min_x, min_y), (max_x, max_y), (0, 0, 255), 2)

        number_of_cancer = predictions.count(1)
        number_of_non = predictions.count(0)
        print(number_of_cancer, number_of_non)
        is_cancer = number_of_cancer > number_of_non

        # Save the annotated image
        image_path_parent = "/".join(image_path.split("/")[:-1])
        image_name_with_extension = image_path.split("/")[-1]
        cv2.imwrite(image_path_parent + "/predictions/" + image_name_with_extension, img)

        diagnosis = Diagnosis(self.class_names[is_cancer])
        if is_cancer:
            diagnosis.image_path = f"predictions,{image_name_with_extension}"

        return diagnosis
