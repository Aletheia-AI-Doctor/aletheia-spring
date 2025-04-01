from mri import MRI

mri = MRI()

mri.load_model()
print(mri.predict("images/OAS1_0003_MR1_mpr-1_123.jpg"))
print(mri.predict("images/OAS1_0001_MR1_mpr-1_105.jpg"))
