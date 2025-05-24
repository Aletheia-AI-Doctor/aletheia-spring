from flask import Flask,request
from model import Model
import os
from mri import MRI
from chest_xray import ChestXRay
from breast_cancer import BreastCancer

app = Flask(__name__)

mri_model = MRI()
breast_cancer_model = BreastCancer()
xray_model = ChestXRay()


def predict(model: Model, image_path: str):
    project_root = os.path.dirname(os.path.abspath(__file__))
    diagnosis = model.predict(project_root + "/uploads/" + image_path)

    return diagnosis.to_json()


@app.route("/mri")
def mri():
    return predict(mri_model, request.args.get("image_path"))


@app.route("/chest-xray")
def chestxray():
    return predict(xray_model, request.args.get("image_path"))


@app.route("/breast-cancer")
def breast_cancer():
    return predict(breast_cancer_model, request.args.get("image_path"))
