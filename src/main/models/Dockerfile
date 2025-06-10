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

ENV TF_ENABLE_ONEDNN_OPTS=0
ENV FLASK_ENV=production

COPY pretrained_model/ ./pretrained_model/

COPY requirements.txt .

FROM base AS development
RUN pip install -r requirements.txt

COPY . .

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

EXPOSE 4000/tcp

ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh"]

# Development stage
# Production stage
FROM base AS production

# Install production dependencies including Gunicorn
RUN pip install --no-cache-dir --prefer-binary -r requirements.txt

# Copy application code
COPY . .

# Create necessary directories
RUN mkdir -p /app/uploads && \
    chmod -R 777 /app/uploads

# Create non-root user
RUN useradd -m myuser && \
    chown -R myuser /app
USER myuser

# Expose port
EXPOSE 8000

# Gunicorn configuration
CMD ["gunicorn", "--bind", "0.0.0.0:8000", \
     "--workers", "4", \
     "--threads", "2", \
     "--timeout", "120", \
     "--worker-class", "gevent", \
     "--access-logfile", "-", \
     "--error-logfile", "-", \
     "app:app"]
