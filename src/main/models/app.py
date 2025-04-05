from flask import Flask
from mri import MRI
from flask import request
import os

app = Flask(__name__)

mri_model = MRI()


@app.route("/mri")
def mri():
    project_root = os.path.dirname(os.path.abspath(__file__))

    return mri_model.predict(project_root + "/uploads/" + request.args.get("image_path"))
