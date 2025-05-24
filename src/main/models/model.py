import tensorflow as tf
from ultralytics import YOLO

class Model:
    def __init__(self, model_name: str):
        
        self.model_name = model_name
        self.model = None
        self._load_model()


    def _load_model(self):
        if self.model_name =="last.pt" or self.model_name =="best.pt":
            self.model = YOLO("weights/" + self.model_name)
        else:    
            self.model = tf.keras.models.load_model("weights/" + self.model_name)

    def predict(self, image_path: str) -> str:
        # Throw an error if the model is not loaded
        raise Exception("Method not implemented")
