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
COPY requirements.txt .
RUN pip install -r requirements.txt


FROM base AS development
COPY . .
COPY entrypoint.sh /app/entrypoint.sh
COPY download_models.py .
RUN python download_models.py
RUN chmod +x /app/entrypoint.sh
EXPOSE 4000/tcp
ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh"]


FROM base AS production
COPY . .
ENV PYTHONUNBUFFERED=1
ENV QT_QPA_PLATFORM=offscreen
ENV TF_ENABLE_ONEDNN_OPTS=0
ENV FLASK_ENV=production
RUN python download_models.py
RUN chmod +x entrypoint_production.sh
RUN mkdir -p uploads
EXPOSE 4000/tcp
ENTRYPOINT ["/bin/bash", "/app/entrypoint_production.sh"]
