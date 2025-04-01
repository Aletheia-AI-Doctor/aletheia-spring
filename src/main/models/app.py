from flask import Flask
from mri import MRI
from flask import request

app = Flask(__name__)

mri_model = MRI()


@app.route("/mri")
def mri():
    return mri_model.predict(request.args.get("image_path"))
