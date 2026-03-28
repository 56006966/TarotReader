# TarotReader Backend

Starter API for `https://api.phunkypixels.com` that gives the Android app one stable backend surface.

## Endpoints

- `GET /health`
- `GET /tarotreader/horoscope?sign=Leo`
- `GET /tarotreader/tarot/cards`
- `GET /tarotreader/tarot/spreads`
- `POST /tarotreader/tarot/spread`

Example request:

```bash
curl -X POST https://api.phunkypixels.com/tarotreader/tarot/spread \
  -H "Content-Type: application/json" \
  -d "{\"spread\":\"LOVE\"}"
```

## Local run

```bash
cd backend
npm install
npm run dev
```

Default port is `8080`.

## Deploy shape

Point your host or reverse proxy so:

- `https://api.phunkypixels.com/health`
- `https://api.phunkypixels.com/tarotreader/...`

all route to this Node service.

## Android config

The Android app reads its backend base URL from `local.properties`:

```properties
backend.baseUrl=https://api.phunkypixels.com/
```

If the backend is unreachable, the app falls back to the current local tarot and horoscope logic.
