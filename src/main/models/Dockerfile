FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy model code and download script
COPY . .

# Create weights folder and download weights
RUN python download_models.py

EXPOSE 4000/tcp

CMD ["python","-m","flask", "run", "--host=0.0.0.0", "--port=4000"]
