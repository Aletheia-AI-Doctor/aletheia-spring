from model import Model

class ChestXRay(Model):
    def __init__(self):
        super().__init__("last.pt")

    def predict(self, image_path: str) -> str: 
        results=self.model.predict(image_path, conf=0.7,save=True,iou=0.3,project="uploads",exist_ok=True,name=".")
        
        if results[0].boxes.conf.numel() == 0:
            return "normal"
        else:
            return "pneumonia"
    



        