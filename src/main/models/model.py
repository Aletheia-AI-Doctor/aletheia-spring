import tensorflow as tf


class Diagnosis:
    def __init__(self, name: str):
        self.name = name
        self.image_path = None
        self.confidence = None

    def to_json(self):
        return {
            "name": self.name,
            "image_path": "" if self.image_path is None else self.image_path,
            "confidence": "" if self.confidence is None else self.confidence
        }


class Model:
    def __init__(self, model_name: str):
        self.model_name = model_name
        self.model = None
        self._load_model()

    def _load_model(self):
        self.model = tf.keras.models.load_model("weights/" + self.model_name)

    def predict(self, image_path: str) -> Diagnosis:
        # Throw an error if the model is not loaded
        raise Exception("Method not implemented")
