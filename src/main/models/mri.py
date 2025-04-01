from model import Model
from PIL import Image
import numpy as np


class MRI(Model):
    def __init__(self):
        super().__init__("efficientnetb0_alzheimer.h5")

    def load_model(self):
        super().load_model()

    # ================== PREDICTION FUNCTIONS ==================


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
        img = Image.open(image_path)

        x = np.array(img.resize((128, 128)))
        x = x.reshape(1, 128, 128, 3)
        res, classification, __ = self._predict_image(self.model, image_path)
        print(res)
        return self._names(classification)
