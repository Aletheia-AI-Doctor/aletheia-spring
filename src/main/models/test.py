# from mri import MRI
from chest_xray import ChestXRay

# mri = MRI()
chest_xray = ChestXRay()

print(chest_xray.predict("images/test.jpg"))
