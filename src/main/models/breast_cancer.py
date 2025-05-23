from model import Model
import numpy as np
import cv2


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

    def predict(self, image_path: str) -> str:
        img = preprocess_image(image_path)
        image_tiles = split_image(img, 50)
        predictions = []

        for tile in image_tiles:
            # Check if tile is the correct size (might have smaller tiles at edges)
            if tile.shape[0] == 50 and tile.shape[1] == 50:
                pred, confidence = self._predict(tile)
                print(pred, confidence)
                if confidence > 0.85:
                    predictions.append(pred)

        number_of_cancer = predictions.count(1)
        number_of_non = predictions.count(0)
        print("Cancer:", number_of_cancer)
        print("Non Cancer:", number_of_non)
        if number_of_cancer > 2 * number_of_non:
            return "Cancer"

        return "No Cancer"