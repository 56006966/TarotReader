const express = require("express");
const cors = require("cors");

const app = express();
const port = Number(process.env.PORT || 8080);
const apiNinjasApiKey = process.env.API_NINJAS_API_KEY || "";
const mysticalApiKey = process.env.MYSTICAL_API_KEY || "";

app.use(cors({ origin: process.env.CORS_ORIGIN || "*" }));
app.use(express.json());

const horoscopeSigns = [
  "Aries",
  "Taurus",
  "Gemini",
  "Cancer",
  "Leo",
  "Virgo",
  "Libra",
  "Scorpio",
  "Sagittarius",
  "Capricorn",
  "Aquarius",
  "Pisces",
];

const overviewOpeners = [
  "A clear signal is coming through today.",
  "Your instincts are sharper than usual.",
  "A softer approach gets better results right now.",
  "Momentum builds once you commit to one priority.",
];

const loveMessages = [
  "Say the honest thing kindly and let the conversation breathe.",
  "Warmth matters more than perfection today.",
  "Pay attention to who feels easy to be yourself around.",
  "A small gesture can reset the tone of a relationship.",
];

const careerMessages = [
  "Finish one meaningful task before scattering your focus.",
  "A practical adjustment will make the rest of the week easier.",
  "Trust the skill you have already built.",
  "A direct message or follow-up could open the next door.",
];

const energyMessages = [
  "Protect your pace and leave space between commitments.",
  "Movement helps clear mental static today.",
  "Recharge before you reach for more stimulation.",
  "Your energy improves when your environment is less cluttered.",
];

const luckyVibes = [
  "gold tones and a bold first step",
  "fresh air and an uncluttered plan",
  "music, color, and a playful attitude",
  "quiet focus and one meaningful text",
];

const tarotSpreads = {
  DAILY: {
    displayName: "Daily Card",
    cardCount: 1,
    positions: ["Guidance"],
    prompt: "A simple one-card pull for the energy around you today.",
  },
  PAST_PRESENT_FUTURE: {
    displayName: "Past, Present, Future",
    cardCount: 3,
    positions: ["Past", "Present", "Future"],
    prompt: "See what shaped this moment, where you stand now, and what is unfolding next.",
  },
  LOVE: {
    displayName: "Love Spread",
    cardCount: 3,
    positions: ["Your Heart", "Their Energy", "Next Step"],
    prompt: "A relationship-focused spread for emotional clarity and gentle direction.",
  },
  CAREER: {
    displayName: "Career Spread",
    cardCount: 3,
    positions: ["Current Path", "Challenge", "Opportunity"],
    prompt: "Use this spread to check your momentum, obstacles, and practical next move.",
  },
  CELTIC_CROSS_LITE: {
    displayName: "Celtic Cross Lite",
    cardCount: 5,
    positions: ["Present", "Challenge", "Foundation", "Guidance", "Outcome"],
    prompt: "A deeper five-card spread for complex situations without the full ten-card intensity.",
  },
  CELTIC_CROSS: {
    displayName: "Celtic Cross",
    cardCount: 10,
    positions: [
      "Present",
      "Challenge",
      "Foundation",
      "Recent Past",
      "Conscious Goal",
      "Near Future",
      "Self",
      "Environment",
      "Hopes and Fears",
      "Outcome",
    ],
    prompt: "A full ten-card spread for layered situations, showing the core dynamic, surrounding influences, and likely direction.",
  },
};

const tarotDeck = [
  "The Fool", "The Magician", "The High Priestess", "The Empress", "The Emperor",
  "The Hierophant", "The Lovers", "The Chariot", "Strength", "The Hermit",
  "Wheel of Fortune", "Justice", "The Hanged Man", "Death", "Temperance",
  "The Devil", "The Tower", "The Star", "The Moon", "The Sun", "Judgement",
  "The World", "Ace of Wands", "Two of Wands", "Three of Wands", "Four of Wands",
  "Five of Wands", "Six of Wands", "Seven of Wands", "Eight of Wands", "Nine of Wands",
  "Ten of Wands", "Page of Wands", "Knight of Wands", "Queen of Wands", "King of Wands",
  "Ace of Cups", "Two of Cups", "Three of Cups", "Four of Cups", "Five of Cups",
  "Six of Cups", "Seven of Cups", "Eight of Cups", "Nine of Cups", "Ten of Cups",
  "Page of Cups", "Knight of Cups", "Queen of Cups", "King of Cups", "Ace of Swords",
  "Two of Swords", "Three of Swords", "Four of Swords", "Five of Swords", "Six of Swords",
  "Seven of Swords", "Eight of Swords", "Nine of Swords", "Ten of Swords", "Page of Swords",
  "Knight of Swords", "Queen of Swords", "King of Swords", "Ace of Pentacles",
  "Two of Pentacles", "Three of Pentacles", "Four of Pentacles", "Five of Pentacles",
  "Six of Pentacles", "Seven of Pentacles", "Eight of Pentacles", "Nine of Pentacles",
  "Ten of Pentacles", "Page of Pentacles", "Knight of Pentacles", "Queen of Pentacles",
  "King of Pentacles",
];

app.get("/health", (_req, res) => {
  res.json({
    ok: true,
    service: "tarotreader-backend",
    providers: {
      horoscope: apiNinjasApiKey ? "api-ninjas" : "local-fallback",
      tarot: mysticalApiKey ? "mystical-api" : "local-fallback",
    },
  });
});

app.get("/tarotreader/horoscope", async (req, res) => {
  const sign = normalizeSign(String(req.query.sign || ""));
  if (!sign) {
    return res.status(400).json({ error: "A valid zodiac sign is required." });
  }

  const remoteReading = await fetchApiNinjasHoroscope(sign);
  if (remoteReading) {
    return res.json(remoteReading);
  }

  const daySeed = dayOfYear(new Date());
  const signSeed = horoscopeSigns.indexOf(sign);
  res.json({
    sign,
    source: "local-fallback",
    overview: `${overviewOpeners[(daySeed + signSeed) % overviewOpeners.length]} ${sign}, stay present and let your next move be deliberate.`,
    love: loveMessages[(daySeed + signSeed * 2) % loveMessages.length],
    career: careerMessages[(daySeed + signSeed * 3) % careerMessages.length],
    energy: energyMessages[(daySeed + signSeed * 4) % energyMessages.length],
    luckyVibe: luckyVibes[(daySeed + signSeed * 5) % luckyVibes.length],
  });
});

app.get("/tarotreader/tarot/cards", (_req, res) => {
  res.json({
    source: "phunkypixels-backend",
    count: tarotDeck.length,
    cards: tarotDeck.map((name) => ({ name })),
  });
});

app.get("/tarotreader/tarot/spreads", (_req, res) => {
  res.json({
    source: mysticalApiKey ? "mystical-api" : "local-fallback",
    spreads: Object.entries(tarotSpreads).map(([id, spread]) => ({ id, ...spread })),
  });
});

app.post("/tarotreader/tarot/spread", async (req, res) => {
  const spreadId = String(req.body?.spread || "DAILY");
  const spread = tarotSpreads[spreadId];
  if (!spread) {
    return res.status(400).json({ error: "Unknown spread." });
  }

  const remoteReading = await fetchMysticalTarotReading(spreadId, spread);
  if (remoteReading) {
    return res.json(remoteReading);
  }

  const deck = shuffle([...tarotDeck]).slice(0, spread.cardCount);
  const cards = deck.map((name, index) => {
    const isReversed = Math.random() >= 0.5;
    return {
      position: spread.positions[index],
      name,
      isReversed,
      meaning: buildMeaning(name, isReversed),
    };
  });

  return res.json({
    spread: spreadId,
    displayName: spread.displayName,
    headline: spread.prompt,
    determination: buildDetermination(spreadId, cards),
    source: "local-fallback",
    cards,
  });
});

app.listen(port, () => {
  console.log(`TarotReader backend listening on port ${port}`);
});

function normalizeSign(rawValue) {
  return horoscopeSigns.find((sign) => sign.toLowerCase() === rawValue.trim().toLowerCase()) || null;
}

function dayOfYear(date) {
  const start = new Date(date.getFullYear(), 0, 0);
  const diff = date - start;
  return Math.floor(diff / 86400000);
}

function shuffle(items) {
  for (let index = items.length - 1; index > 0; index -= 1) {
    const swapIndex = Math.floor(Math.random() * (index + 1));
    [items[index], items[swapIndex]] = [items[swapIndex], items[index]];
  }
  return items;
}

function buildMeaning(name, isReversed) {
  if (!isReversed) {
    return `${name} suggests visible movement, insight, and a meaningful next step. Work with its energy directly instead of postponing the lesson.`;
  }
  return `${name} reversed suggests delay, inner tension, or a lesson that needs reflection before action. Slow down enough to understand what is being redirected.`;
}

function buildDetermination(spreadId, cards) {
  if (spreadId === "DAILY") {
    return `${cards[0].name} sets the tone for today. Let the card guide one clear decision instead of trying to solve everything at once.`;
  }

  if (spreadId === "PAST_PRESENT_FUTURE" && cards.length === 3) {
    return `${cards[0].name} explains what shaped the moment, ${cards[1].name} shows what is active now, and ${cards[2].name} points toward what is forming next. Move from awareness into one grounded next step.`;
  }

  if (spreadId === "LOVE" && cards.length === 3) {
    return `${cards[0].name}, ${cards[1].name}, and ${cards[2].name} together point toward emotional clarity through honesty, pacing, and direct communication.`;
  }

  if (spreadId === "CAREER" && cards.length === 3) {
    return `${cards[0].name}, ${cards[1].name}, and ${cards[2].name} together suggest progress through focus, practical adjustment, and a direct response to the real blocker.`;
  }

  if (spreadId.startsWith("CELTIC_CROSS")) {
    const first = cards[0]?.name || "The first card";
    const second = cards[1]?.name || "the crossing card";
    const last = cards[cards.length - 1]?.name || "the outcome";
    return `${first} defines the center of the matter, ${second} shows the tension around it, and ${last} points toward the likely direction if the current pattern continues.`;
  }

  return cards.map((card) => card.name).join(", ");
}

async function fetchApiNinjasHoroscope(sign) {
  if (!apiNinjasApiKey) return null;

  try {
    const url = new URL("https://api.api-ninjas.com/v1/horoscope");
    url.searchParams.set("zodiac", sign.toLowerCase());
    const response = await fetch(url, {
      headers: {
        "X-Api-Key": apiNinjasApiKey,
        Accept: "application/json",
      },
    });
    if (!response.ok) return null;

    const data = await response.json();
    const horoscope = String(data.horoscope || "").trim();
    if (!horoscope) return null;

    return {
      sign,
      source: "api-ninjas",
      overview: horoscope,
      love: `Love note for ${sign}: ${horoscope}`,
      career: `Career note for ${sign}: ${horoscope}`,
      energy: `Energy note for ${sign}: ${horoscope}`,
      luckyVibe: "synced with your daily horoscope",
      date: data.date || null,
    };
  } catch (_error) {
    return null;
  }
}

async function fetchMysticalTarotReading(spreadId, spread) {
  if (!mysticalApiKey) return null;

  try {
    const request = buildMysticalRequest(spreadId);
    if (!request) return null;

    const response = await fetch(`https://api.mysticalapi.com/v1${request.path}`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${mysticalApiKey}`,
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(request.body),
    });

    if (!response.ok) return null;
    const data = await response.json();
    const cards = mapMysticalCards(spreadId, spread, data);
    if (!cards.length) return null;

    return {
      spread: spreadId,
      displayName: spread.displayName,
      headline: data.spread?.description || spread.prompt,
      determination: buildDetermination(spreadId, cards),
      source: "mystical-api",
      cards,
      theme: data.theme || null,
    };
  } catch (_error) {
    return null;
  }
}

function buildMysticalRequest(spreadId) {
  const todaySeed = new Date().toISOString().slice(0, 10);
  switch (spreadId) {
    case "DAILY":
      return {
        path: "/tarot/daily-card",
        body: {
          theme: "general",
          seed: todaySeed,
        },
      };
    case "PAST_PRESENT_FUTURE":
      return {
        path: "/tarot/three-card",
        body: {
          spread_type: "past_present_future",
          theme: "general",
          seed: todaySeed,
        },
      };
    case "LOVE":
      return {
        path: "/tarot/three-card",
        body: {
          spread_type: "past_present_future",
          theme: "love",
          seed: todaySeed,
        },
      };
    case "CAREER":
      return {
        path: "/tarot/three-card",
        body: {
          spread_type: "situation_action_outcome",
          theme: "career",
          seed: todaySeed,
        },
      };
    case "CELTIC_CROSS":
      return {
        path: "/tarot/celtic-cross",
        body: {
          theme: "general",
          seed: todaySeed,
        },
      };
    default:
      return null;
  }
}

function mapMysticalCards(spreadId, spread, data) {
  if (spreadId === "DAILY" && data.card) {
    const isReversed = data.card.orientation === "reversed";
    return [
      {
        position: spread.positions[0],
        name: data.card.name,
        isReversed,
        meaning: data.meaning || buildMeaning(data.card.name, isReversed),
      },
    ];
  }

  const incomingCards = Array.isArray(data.cards) ? data.cards : [];
  return incomingCards.slice(0, spread.cardCount).map((card, index) => {
    const isReversed = card.orientation === "reversed";
    return {
      position: card.position?.name || spread.positions[index] || `Card ${index + 1}`,
      name: card.name,
      isReversed,
      meaning: buildMeaning(card.name, isReversed),
    };
  });
}
