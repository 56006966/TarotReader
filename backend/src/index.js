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

const majorMeanings = new Map([
  ["The Fool", [
    "A fresh start is opening up. Trust curiosity, stay light on your feet, and let a new chapter teach you as you go.",
    "Impulsiveness or naivety could scatter your energy. Slow down just enough to notice what you are overlooking.",
  ]],
  ["The Magician", [
    "You already have the tools you need. Focus your intention and turn ideas into visible action.",
    "Your focus may be split or your confidence may be performative. Reconnect with substance before you try to persuade anyone.",
  ]],
  ["The High Priestess", [
    "Slow down and listen inward. The right answer is emerging through intuition rather than force.",
    "Inner wisdom is being drowned out by noise, secrecy, or avoidance. Sit with the truth you already suspect.",
  ]],
  ["The Empress", [
    "Nurture what is growing in your life. Creativity, comfort, and care are your strengths today.",
    "Overgiving, creative block, or neglect of your own needs is throwing things off balance. Care for yourself as intentionally as you care for others.",
  ]],
  ["The Emperor", [
    "Create structure around what matters. Clear boundaries and a steady plan will help things move.",
    "Control, rigidity, or fear of vulnerability may be limiting progress. Lead with steadiness, not domination.",
  ]],
  ["The Hierophant", [
    "Tradition, mentorship, and trusted wisdom can steady this moment. Learn from what has endured.",
    "Outdated rules or pressure to conform may be muting your own judgment. Keep what is wise and release what is rigid.",
  ]],
  ["The Lovers", [
    "Lead with honesty in relationships and choices. Alignment matters more than instant comfort.",
    "Misalignment or avoidance is clouding an important choice. Come back to your values before you commit.",
  ]],
  ["The Chariot", [
    "Momentum is on your side. Commit to one direction and move with confidence.",
    "You may be pushing too hard or losing control of competing priorities. Regain direction before forcing another step.",
  ]],
  ["Strength", [
    "Gentle persistence wins here. Meet stress with patience rather than pressure.",
    "Self-doubt or pent-up frustration needs softer handling. Courage right now looks like emotional steadiness.",
  ]],
  ["The Hermit", [
    "A little solitude can bring clarity. Give yourself room to think before reacting.",
    "Isolation or overthinking may be turning insight into distance. Seek wisdom, but do not disappear from life.",
  ]],
  ["Wheel of Fortune", [
    "A shift is underway. Stay adaptable and look for the opportunity inside change.",
    "Resistance to change or bad timing is making things feel stuck. Focus on what you can influence while the cycle turns.",
  ]],
  ["Justice", [
    "Truth wants to be seen. Be fair, direct, and willing to own your part.",
    "Imbalance, denial, or consequences delayed are in play. Get honest now so the correction is cleaner later.",
  ]],
  ["The Hanged Man", [
    "Pause before pushing ahead. A new perspective will save you wasted effort.",
    "Stagnation may be replacing surrender. If the pause has stopped teaching you, it may be time to reengage.",
  ]],
  ["Death", [
    "Something is ending so something healthier can begin. Release what has run its course.",
    "Clinging to the old is stretching out a necessary ending. Letting go is the only way this transforms.",
  ]],
  ["Temperance", [
    "Balance is the message. Blend rest, action, feeling, and logic instead of overdoing one side.",
    "Excess, impatience, or emotional swings are disrupting harmony. Return to moderation and integration.",
  ]],
  ["The Devil", [
    "Notice habits that drain your power. Awareness is the first step toward freedom.",
    "You are starting to see the grip of fear, attachment, or self-sabotage. Name it clearly so it loses strength.",
  ]],
  ["The Tower", [
    "Unexpected change can clear false foundations. Let the truth reorganize your next move.",
    "Tension is building because something unstable has not yet been addressed. A conscious reset may prevent a harsher break.",
  ]],
  ["The Star", [
    "Hope is returning. Stay open to healing, inspiration, and gentler possibilities.",
    "Discouragement or disconnection may be dimming your sense of possibility. Rebuild trust in small, steady ways.",
  ]],
  ["The Moon", [
    "Not everything is visible yet. Move carefully and trust your instincts while the fog lifts.",
    "Fear, confusion, or projection could be distorting what you see. Check your assumptions before reacting.",
  ]],
  ["The Sun", [
    "Joy, confidence, and momentum are available. Let yourself be seen in a fuller way.",
    "Temporary doubt or burnout may be dimming your clarity. Light returns faster when you stop hiding what is true.",
  ]],
  ["Judgement", [
    "You are being called to rise into a clearer version of yourself. Answer honestly.",
    "Avoiding accountability or the next calling can keep you in an outdated identity. Respond instead of postponing.",
  ]],
  ["The World", [
    "A cycle is completing. Acknowledge how far you have come and step into what is next.",
    "Closure is available, but unfinished emotional threads may still need attention. Complete the lesson before rushing onward.",
  ]],
]);

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
  const [uprightMeaning, reversedMeaning] = getCardMeanings(name);
  return isReversed ? reversedMeaning : uprightMeaning;
}

function buildDetermination(spreadId, cards) {
  const reversedCount = cards.filter((card) => card.isReversed).length;
  const majorCount = cards.filter((card) => majorMeanings.has(card.name)).length;
  const dominantSuit = findDominantSuit(cards);
  const patternSummary = buildPatternSummary(reversedCount, majorCount, dominantSuit);
  const pairingSummary = buildPairingSummary(cards, spreadId);

  if (spreadId === "DAILY") {
    return `${cards[0].name} sets the tone for today. ${cards[0].meaning} Let the card guide one clear decision instead of trying to solve everything at once.${patternSummary}${pairingSummary}`;
  }

  if (spreadId === "PAST_PRESENT_FUTURE" && cards.length === 3) {
    return `${cards[0].name} explains what shaped the moment, ${cards[1].name} shows what is active now, and ${cards[2].name} points toward what is forming next. The determination is to learn from the past without repeating it, respond honestly to the present, and let the future unfold through one grounded next step.${patternSummary}${pairingSummary}`;
  }

  if (spreadId === "LOVE" && cards.length === 3) {
    return `${cards[0].name}, ${cards[1].name}, and ${cards[2].name} together point toward emotional clarity through honesty, pacing, and direct communication. The determination here is to stop guessing, say the true thing with care, and move in the spirit of the final card rather than in fear.${patternSummary}${pairingSummary}`;
  }

  if (spreadId === "CAREER" && cards.length === 3) {
    return `${cards[0].name}, ${cards[1].name}, and ${cards[2].name} together suggest progress through focus, practical adjustment, and a direct response to the real blocker. The determination is to work with what is already developing, address the friction directly, and move toward the opportunity with practical discipline.${patternSummary}${pairingSummary}`;
  }

  if (spreadId.startsWith("CELTIC_CROSS")) {
    const first = cards[0]?.name || "The first card";
    const second = cards[1]?.name || "the crossing card";
    const last = cards[cards.length - 1]?.name || "the outcome";
    return `${first} defines the center of the matter, ${second} shows the tension around it, and ${last} points toward the likely direction if the current pattern continues. The determination is to work from the root pattern first, treat the challenge as useful information, and let the surrounding cards guide your pacing instead of forcing a fast result.${patternSummary}${pairingSummary}`;
  }

  return `${cards.map((card) => card.name).join(", ")}.${patternSummary}${pairingSummary}`;
}

function getCardMeanings(name) {
  if (majorMeanings.has(name)) {
    return majorMeanings.get(name);
  }

  const [rank, suit] = name.split(" of ");
  const suitTheme = {
    Wands: "energy, desire, and momentum",
    Cups: "emotion, intuition, and connection",
    Swords: "clarity, tension, and truth",
    Pentacles: "work, resources, and steady growth",
  }[suit] || "change and growth";

  const uprightLead = {
    Ace: `A new opening appears in ${suitTheme}.`,
    Two: `A balancing act is taking shape in ${suitTheme}.`,
    Three: `Growth, exchange, or momentum is building in ${suitTheme}.`,
    Four: `Stability and pause matter more than speed in ${suitTheme}.`,
    Five: `Conflict or strain is exposing what needs adjustment in ${suitTheme}.`,
    Six: `Movement, support, or harmony is returning to ${suitTheme}.`,
    Seven: `Assessment and conviction are being tested in ${suitTheme}.`,
    Eight: `Momentum or pressure is accelerating in ${suitTheme}.`,
    Nine: `The situation is maturing, and its true weight is visible in ${suitTheme}.`,
    Ten: `A cycle is reaching completion in ${suitTheme}.`,
    Page: `Curiosity and a new lesson are arriving through ${suitTheme}.`,
    Knight: `Strong motion and pursuit are pushing ${suitTheme} forward.`,
    Queen: `The mature, embodied side of ${suitTheme} is your advantage now.`,
    King: `Mastery, leadership, and responsibility define this moment in ${suitTheme}.`,
  }[rank] || `${name} is asking for awareness, honesty, and a thoughtful next step.`;

  const reversedLead = {
    Ace: "The opening is real, but hesitation or blockage is slowing it down.",
    Two: "Imbalance or indecision is making the situation harder than it needs to be.",
    Three: "Misalignment is interrupting natural progress.",
    Four: "Stagnation or overprotection may be replacing healthy stability.",
    Five: "The friction is lingering because the deeper issue has not been addressed.",
    Six: "Progress is possible, but it may be delayed, uneven, or difficult to trust.",
    Seven: "Doubt, scattered effort, or weak boundaries are reducing your leverage.",
    Eight: "The pace may be chaotic, blocked, or turned inward.",
    Nine: "Anxiety, fatigue, or over-identification with the pressure is clouding judgment.",
    Ten: "Completion is near, but the load may be too heavy or the ending resisted.",
    Page: "Inexperience, distraction, or emotional immaturity is asking for more grounding.",
    Knight: "The drive is present, but it risks becoming reckless, stalled, or misdirected.",
    Queen: "Your inner steadiness is being interrupted by self-doubt or emotional imbalance.",
    King: "Authority is slipping into rigidity, passivity, or overcontrol.",
  }[rank] || `${name} reversed suggests the lesson is delayed or turned inward.`;

  return [
    `${uprightLead} Let the lesson of the ${name} guide your next move with awareness.`,
    `${reversedLead} The reversed ${name} asks for reflection before action.`,
  ];
}

function findDominantSuit(cards) {
  const suitCounts = cards.reduce((counts, card) => {
    const suit = card.name.includes(" of ") ? card.name.split(" of ")[1] : null;
    if (!suit) return counts;
    counts[suit] = (counts[suit] || 0) + 1;
    return counts;
  }, {});

  return Object.entries(suitCounts)
    .sort((left, right) => right[1] - left[1])[0]?.[0] || null;
}

function buildPatternSummary(reversedCount, majorCount, dominantSuit) {
  const parts = [];

  if (majorCount >= 2) {
    parts.push("Major Arcana energy suggests this reading touches a bigger life lesson rather than a passing mood.");
  }

  if (reversedCount >= 2) {
    parts.push("Multiple reversed cards suggest the situation is more internal, delayed, or emotionally tangled than it first appears.");
  }

  if (dominantSuit) {
    const suitThemes = {
      Wands: "The pattern is action-heavy and asks for courage, pacing, and clean momentum.",
      Cups: "The pattern is emotional and relational, so honesty and care matter more than force.",
      Swords: "The pattern is mental and communicative, asking for clarity, boundaries, and truth.",
      Pentacles: "The pattern is practical and grounded, asking for patience, consistency, and tangible follow-through.",
    };
    parts.push(suitThemes[dominantSuit]);
  }

  return parts.length ? ` ${parts.join(" ")}` : "";
}

function buildPairingSummary(cards, spreadId) {
  const names = cards.map((card) => card.name);
  const pairings = [];

  if (hasAll(names, ["The Lovers", "Two of Cups"])) {
    pairings.push("The Lovers with Two of Cups amplifies partnership, emotional reciprocity, and a choice that should be made from alignment instead of fear.");
  }
  if (hasAll(names, ["The Tower", "Death"])) {
    pairings.push("The Tower with Death signals deep irreversible change, suggesting the old structure is not meant to be restored in its previous form.");
  }
  if (hasAll(names, ["The Star", "Temperance"])) {
    pairings.push("The Star with Temperance points toward healing, integration, and a steadier recovery than the situation first suggested.");
  }
  if (hasAll(names, ["The Devil", "Eight of Swords"])) {
    pairings.push("The Devil with Eight of Swords highlights a self-reinforcing trap of fear, attachment, or over-identification with the problem.");
  }
  if (hasAll(names, ["The Sun", "Ace of Wands"])) {
    pairings.push("The Sun with Ace of Wands gives this reading a bright initiating spark, favoring confident action and clean momentum.");
  }
  if (hasAll(names, ["Justice", "King of Swords"])) {
    pairings.push("Justice with King of Swords emphasizes truth, fairness, and a decision that needs precision more than sentimentality.");
  }
  if (hasAll(names, ["Three of Swords", "Five of Cups"])) {
    pairings.push("Three of Swords with Five of Cups suggests grief or disappointment that needs acknowledgment before genuine movement can begin.");
  }
  if (hasAll(names, ["Six of Swords", "Wheel of Fortune"])) {
    pairings.push("Six of Swords with Wheel of Fortune suggests transition is not only possible but already underway beneath the surface.");
  }
  if (hasAll(names, ["Ace of Pentacles", "Ten of Pentacles"])) {
    pairings.push("Ace of Pentacles with Ten of Pentacles strongly favors long-term material growth, stability, or a practical foundation that can compound over time.");
  }
  if (hasAll(names, ["Page of Cups", "The Moon"])) {
    pairings.push("Page of Cups with The Moon suggests sensitive intuition, dreamlike insight, and emotions that need gentle interpretation instead of immediate certainty.");
  }

  if (spreadId === "PAST_PRESENT_FUTURE" || spreadId === "LOVE" || spreadId === "CAREER") {
    const sameSuit = allSameSuit(cards);
    if (sameSuit) {
      pairings.push(`All three cards lean into ${sameSuit}, making this spread unusually coherent and concentrated around one theme rather than divided priorities.`);
    }
  }

  if (spreadId.startsWith("CELTIC_CROSS")) {
    const outcome = cards[cards.length - 1];
    const center = cards[0];
    if (center && outcome && center.name === outcome.name) {
      pairings.push("The center and outcome mirroring each other suggests the core issue remains the central lesson until it is consciously integrated.");
    }
  }

  return pairings.length ? ` ${pairings.join(" ")}` : "";
}

function hasAll(names, requiredNames) {
  return requiredNames.every((name) => names.includes(name));
}

function allSameSuit(cards) {
  const suits = cards
    .map((card) => (card.name.includes(" of ") ? card.name.split(" of ")[1] : null))
    .filter(Boolean);

  if (suits.length !== cards.length || suits.length === 0) {
    return null;
  }

  return suits.every((suit) => suit === suits[0]) ? suits[0] : null;
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
      love: horoscope,
      career: horoscope,
      energy: horoscope,
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
