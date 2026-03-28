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

## Provider keys

Optional environment variables:

```bash
export API_NINJAS_API_KEY=your_api_ninjas_key
export MYSTICAL_API_KEY=your_mystical_key
```

When present:

- horoscope requests use API Ninjas
- tarot spread requests use Mystical API for `Daily`, `Past, Present, Future`, `Love`, `Career`, and `Celtic Cross`

When keys are missing or a provider call fails, the backend falls back to the local reading logic.

Note: Tarotap publicly advertises capabilities, but I could not verify a stable public endpoint/auth contract from their public docs page, so it is not wired here yet.

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

## Sources

- [API Ninjas Horoscope docs](https://www.api-ninjas.com/api/horoscope)
- [Mystical Tarot API docs](https://mysticalapi.com/api/tarot.html)
- [Tarotap API overview](https://tarotap.com/en/tarot-api)
