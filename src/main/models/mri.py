from model import Model
from PIL import Image
import numpy as np
import os

class MRI(Model):
    def __init__(self):
        super().__init__("efficientnetb0_alzheimer.h5")
        self.class_names = {
            0: 'Non Demented',
            1: 'Very Mild Dementia',
            2: 'Mild Dementia',
            3: 'Moderate Dementia'
        }

    def load_model(self):
        """Load the model with error handling"""
        try:
            super().load_model()
            print(f"Successfully loaded model: {self.model_name}")
        except Exception as e:
            print(f"Error loading model: {str(e)}")
            raise

    def preprocess_image(self, image_path):
        """Preprocess image for model input"""
        try:
            img = Image.open(image_path).convert("RGB")
            img = img.resize((128, 128))  # Model expects 128x128 images
            img_array = np.array(img) / 255.0  # Normalize pixel values
            return img_array.reshape(1, 128, 128, 3)  # Add batch dimension
        except Exception as e:
            print(f"Error preprocessing image: {str(e)}")
            raise

    def predict(self, image_path: str) -> str:
        if not self.model:
            raise ValueError("Model not loaded")

        try:
            img_array = self.preprocess_image(image_path)

            # Create dummy metadata if model requires it
            dummy_metadata = np.zeros((1, 5))  # Adjust shape as needed

            prediction = self.model.predict([img_array, dummy_metadata])
            class_idx = np.argmax(prediction, axis=1)[0]

            return self.class_names.get(class_idx, "Unknown")

        except Exception as e:
            print(f"Prediction error: {str(e)}")
            raise