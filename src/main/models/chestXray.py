import subprocess
from model import Model
from PIL import Image
import numpy as np
import tensorflow as tf
import math
from classification_models.tfkeras import Classifiers
from tensorflow.keras.models import load_model


def preprocess_image(image_path):
    try:
        img = Image.open(image_path).convert("RGB")
        img = img.resize((224, 224))
        img_array = np.array(img) / 255.0  # Normalize
        img_array = img_array.astype(np.float32)
        img_array = np.expand_dims(img_array, axis=0)

        _, preprocess_input = Classifiers.get('seresnet50')
        return preprocess_input(img_array)
    except Exception as e:
        print(f"Error preprocessing image: {e}")
        raise


class ChestXRay(Model):
    def __init__(self):
        super().__init__("NIH_Seresnet152_model.h5")
        self.class_names = [
            'Atelectasis', 'Cardiomegaly', 'Consolidation', 'Edema',
            'Effusion', 'Emphysema', 'Fibrosis', 'Hernia', 'Infiltration',
            'Mass', 'Nodule', 'Pleural_Thickening', 'Pneumonia', 'Pneumothorax'
        ]

    def predict(self, image_path: str) -> dict:
        try:
            img_array = preprocess_image(image_path)
            predictions = self.model.predict(img_array)[0]

            result = {
                class_name: round(float(prob) * 100, 2)
                for class_name, prob in zip(self.class_names, predictions)
            }

            top_class = max(result, key=result.get)
            return {
                "predictions": result,
                "top_class": top_class,
                "confidence": result[top_class]
            }

        except Exception as e:
            print(f"Prediction error: {e}")
            return {
                "error": str(e)
            }