from model import Model
from PIL import Image
import numpy as np


def preprocess_image(image_path):
    """Preprocess image for model input"""
    try:
        img = Image.open(image_path).convert("RGB")
        img = img.resize((128, 128))  # Model expects 128x128 images
        img_array = np.array(img) / 255.0  # Normalize pixel values
        return img_array.reshape(1, 128, 128, 3)  # Add batch dimension
    except Exception as e:
        print(f"Error preprocessing image: {str(e)}")
        raise


class MRI(Model):
    def __init__(self):
        super().__init__("MRI_Model.keras")
        self.class_names = {
            0: 'Non Demented',
            1: 'Very Mild Dementia',
            2: 'Mild Dementia',
            3: 'Moderate Dementia'
        }

    def _predict_image(self, image_path, metadata_extractor=None, metadata_scaler=None):
        try:
            img = Image.open(image_path).convert("RGB")
            x = np.array(img.resize((128, 128))) / 255.0
            x = x.reshape(1, 128, 128, 3)
            if metadata_extractor and metadata_scaler:
                metadata = metadata_scaler.transform([metadata_extractor(x[0])])
                res = self.model.predict_on_batch([x, metadata])
            else:
                res = self.model.predict_on_batch(x)

            classification = np.argmax(res, axis=1)[0]
            confidence = res[0][classification] * 100
            confidence = round(confidence, 2)

            return res, classification, confidence

        except Exception as e:
            print(f"Error processing image: {str(e)}")
            return None, None, None

    def predict(self, image_path: str) -> str:
        img_array = preprocess_image(image_path)
        dummy_metadata = np.zeros((1, 5))
        prediction = self.model.predict([img_array, dummy_metadata])
        class_idx = np.argmax(prediction, axis=1)[0]

        return self.class_names.get(class_idx, "Unknown")
