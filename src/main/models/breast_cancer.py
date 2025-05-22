from model import Model
import numpy as np
import cv2


def preprocess_image(img_path):
    img = cv2.imread(img_path)
    img = np.expand_dims(img, axis=0)
    return img


class BreastCancer(Model):
    def __init__(self):
        super().__init__("breast_cancer_detection.keras")
        self.class_names = {
            0: 'No Cancer',
            1: 'Cancer',
        }

    def predict(self, image_path: str) -> str:
        img = preprocess_image(image_path)
        prediction = self.model.predict(img, verbose=0)
        class_idx = np.argmax(prediction, axis=1)[0]
        return self.class_names.get(class_idx, "Unknown")
