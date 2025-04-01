import tensorflow as tf


class Model:
    def __init__(self, model_name: str):
        self.model_name = model_name
        self.model = None

    def load_model(self):
        self.model = tf.keras.models.load_model("weights/" + self.model_name)

    def predict(self, image_path: str) -> str:
        # Throw an error if the model is not loaded
        raise Exception("Method not implemented")
