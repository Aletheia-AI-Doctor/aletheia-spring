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
            
    def _predict_image(self, model, image_path, metadata_extractor=None, metadata_scaler=None):
        """Make prediction for a single image"""
        try:
            # Load and preprocess image
            img = Image.open(image_path).convert("RGB")
            # Resize and normalize
            x = np.array(img.resize((128, 128))) / 255.0
            x = x.reshape(1, 128, 128, 3)
            # Extract metadata if using hybrid model
            if metadata_extractor and metadata_scaler:
                metadata = metadata_scaler.transform([metadata_extractor(x[0])])
                res = model.predict_on_batch([x, metadata])
            else:
                res = model.predict_on_batch(x)

            # Process results
            classification = np.argmax(res, axis=1)[0]
            confidence = res[0][classification] * 100
            confidence = round(confidence, 2)

            return res, classification, confidence

        except Exception as e:
            print(f"Error processing image: {str(e)}")
            return None, None, None

    def _names(self, number):
        if number == 0:
            return 'Non Demented'
        elif number == 1:
            return 'Very mild Dementia'
        elif number == 2:
            return 'Mild Dementia'
        elif number == 3:
            return 'Moderate Dementia'

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
