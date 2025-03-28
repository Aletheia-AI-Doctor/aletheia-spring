from flask import Flask

app = Flask(__name__)

@app.route("/mri")
def mri():
    return "<p>mri model is here!!</p>"
