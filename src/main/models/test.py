from mri import MRI
from chestXray import ChestXRay

mri = MRI()
chest_xray = ChestXRay()

print(mri.predict("images/test.jpg"))

print(chest_xray.predict("images/test.jpg"))