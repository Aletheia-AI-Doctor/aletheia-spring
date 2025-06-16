import os
import gdown

File_IDS = {
    "efficientnetb0_alzheimer.h5": "14e08gWe2d-X9KMci0iv2m2Ga7a5zth-A",
    "last.pt": "12aXVwntxfTvEUcl6WE2Noe83Ia0y7S0E",
    "breast_cancer_detection.keras": "18QuheON6UViSjtaz2GrBRQBbuM3hjcnZ",
}

OUTPUT_PATH = "weights"


def download_models():
    """Downloads models from Google Drive"""
    os.makedirs(OUTPUT_PATH, exist_ok=True)

    for file_name, file_id in File_IDS.items():
        output_path = os.path.join(OUTPUT_PATH, file_name)
        if not os.path.exists(output_path):
            print(f"Downloading {file_name}...")
            url = f"https://drive.google.com/uc?id={file_id}"
            gdown.download(url, output_path, quiet=False, fuzzy=True)
            print(f"Downloaded {file_name} to {output_path}")
        else:
            print(f"{file_name} already exists")


if __name__ == "__main__":
    download_models()
