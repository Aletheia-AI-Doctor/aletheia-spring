from mri import MRI
# from chest_xray import ChestXRay

mri = MRI()
# chest_xray = ChestXRay()

print(mri.predict("images/Test Cases/MRI/Very Mild.jpg").name)
