from flask import Flask
from mri import MRI

app = Flask(__name__)


@app.route("/mri")
def mri():
    mri_model = MRI()
    mri_model.load_model()

    return mri_model.predict("image_path")
