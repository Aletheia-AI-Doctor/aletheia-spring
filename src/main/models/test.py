from mri import MRI

mri = MRI()

mri.load_model()
print(mri.predict("images/test.jpg"))
