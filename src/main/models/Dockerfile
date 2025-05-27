FROM python:3.12-slim AS base 

WORKDIR /app

RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libsm6 \
    libxext6 \
    libxrender-dev \
    libgomp1 \
    libgcc-s1 \
    && rm -rf /var/lib/apt/lists/*

ENV PYTHONUNBUFFERED=1
ENV QT_QPA_PLATFORM=offscreen

COPY pretrained_model/ ./pretrained_model/

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 4000/tcp

ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh"]