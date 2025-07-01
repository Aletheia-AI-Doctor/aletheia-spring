from mri import MRI
from breast_cancer import BreastCancer
# from chest_xray import ChestXRay
import glob

mri = MRI()
breast = BreastCancer()
# chest_xray = ChestXRay()
files = glob.glob("images/Test Cases/Breast Cancer/*.png")

for file in files:
    diagnosis = breast.predict(file)
    print(f"File: {file}, Diagnosis: {diagnosis.name}")
    print()
