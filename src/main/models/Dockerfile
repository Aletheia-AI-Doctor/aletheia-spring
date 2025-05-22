FROM python:3.12-slim AS base 

WORKDIR /app

COPY pretrained_model/ ./pretrained_model/

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy model code and download script
COPY . .

# RUN chmod +x wait_for_it.sh
# Create weights folder and download weights
RUN python download_models.py

EXPOSE 4000/tcp

ENTRYPOINT ["python","-m","flask", "run", "--host=0.0.0.0", "--port=4000"]
