from model import Model


class MRI(Model):
    def __init__(self):
        super().__init__("efficientnetb0_alzheimer.h5")

    def load_model(self):
        super().load_model()

    def predict(self, image_path: str) -> str:
        return "Diagnosis"
