from model import Model, Diagnosis
from ultralytics import YOLO


class ChestXRay(Model):
    def __init__(self):
        super().__init__("last.pt")

    def _load_model(self):
        self.model = YOLO("weights/" + self.model_name)

    def predict(self, image_path: str) -> Diagnosis:
        image_path_parent = "/".join(image_path.split("/")[:-1])

        results = self.model.predict(
            image_path,
            conf=0.7,
            save=True,
            iou=0.3,
            project=image_path_parent,
            exist_ok=True,
            name=f"predictions",
        )

        diagnosis = Diagnosis("normal" if results[0].boxes.conf.numel() == 0 else "pneumonia")

        if diagnosis.name == "pneumonia":
            image_name = image_path.split("/")[-1]
            diagnosis.image_path = f"/predictions/{image_name}"

        return diagnosis
