from flask import Flask,request
from mri import MRI
import os
from chestXray import ChestXRay
from breast_cancer import BreastCancer

app = Flask(__name__)

mri_model = MRI()
breast_cancer_model = BreastCancer()
xray_model = ChestXRay()


@app.route("/mri")
def mri():
    project_root = os.path.dirname(os.path.abspath(__file__))

    return mri_model.predict(project_root + "/uploads/" + request.args.get("image_path"))


@app.route("/chest-xray")
def chestxray():
    project_root = os.path.dirname(os.path.abspath(__file__))
    return xray_model.predict(project_root + "/uploads/" + request.args.get("image_path"))


@app.route("/breast-cancer")
def breast_cancer():
    project_root = os.path.dirname(os.path.abspath(__file__))

    return breast_cancer_model.predict(project_root + "/uploads/" + request.args.get("image_path"))