from flask import Flask
from mri import MRI
from flask import request
import os
from chestXray import ChestXRay
app = Flask(__name__)

mri_model = MRI()
# xray_model = ChestXRay()

@app.route("/mri")
def mri():
    project_root = os.path.dirname(os.path.abspath(__file__))

    return mri_model.predict(project_root + "/uploads/" + request.args.get("image_path"))
# @app.route("/chestxray")
# def chestxray():
#     project_root = os.path.dirname(os.path.abspath(__file__))
#     return xray_model.predict(project_root + "/uploads/" + request.args.get("image_path"))