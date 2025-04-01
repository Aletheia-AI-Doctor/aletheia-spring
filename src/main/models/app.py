from flask import Flask
from mri import MRI

app = Flask(__name__)

mri_model = MRI()


@app.route("/mri")
def mri():
    return mri_model.predict("image_path")
