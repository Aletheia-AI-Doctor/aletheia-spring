FROM python:3.12-slim-bookworm AS base
WORKDIR /app

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libsm6 \
    libxext6 \
    libxrender-dev \
    libgomp1 \
    libgcc-s1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

ENV PYTHONUNBUFFERED=1 \
    QT_QPA_PLATFORM=offscreen

COPY requirements.txt .

FROM base AS development
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
RUN chmod +x entrypoint.sh
EXPOSE 4000
ENTRYPOINT ["./entrypoint.sh"]
CMD ["--debug"]

FROM base AS builder
RUN pip install --user -r requirements.txt

FROM base AS production
RUN useradd -m appuser && \
    chown appuser:appuser /app

ENV PYTHONDONTWRITEBYTECODE=1 \
    PATH="/home/appuser/.local/bin:${PATH}"

COPY --from=builder --chown=appuser:appuser /home/appuser/.local /home/appuser/.local
COPY --chown=appuser:appuser pretrained_model/ ./pretrained_model/
COPY --chown=appuser:appuser . .
RUN chmod +x entrypoint.sh

USER appuser

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:4000/health || exit 1

EXPOSE 4000
ENTRYPOINT ["./entrypoint.sh"]