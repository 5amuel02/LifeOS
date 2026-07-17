import Anthropic from "@anthropic-ai/sdk";

export interface Env {
  ANTHROPIC_API_KEY: string;
  APP_SHARED_SECRET: string;
  RATE_LIMIT_KV: KVNamespace;
}

interface ChatMessage {
  role: "user" | "assistant";
  content: string;
}

const DAILY_LIMIT = 50;
const MAX_HISTORY_MESSAGES = 20;
const MAX_TOKENS = 1024;
const SYSTEM_PROMPT =
  "You are a helpful, friendly personal assistant built into LifeOS, a personal productivity app " +
  "with notes, habit tracking, pomodoro, budgeting, a schedule, and life goals. Keep answers concise " +
  "and practical. Always reply in the same language the user writes in.";

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { "Content-Type": "application/json" },
  });
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    if (request.method !== "POST") {
      return json({ error: "Method not allowed" }, 405);
    }

    if (request.headers.get("X-App-Secret") !== env.APP_SHARED_SECRET) {
      return json({ error: "Unauthorized" }, 401);
    }

    const deviceId = request.headers.get("X-Device-Id");
    if (!deviceId) {
      return json({ error: "Missing X-Device-Id header" }, 400);
    }

    const today = new Date().toISOString().slice(0, 10);
    const usageKey = `usage:${deviceId}:${today}`;
    const currentUsage = parseInt((await env.RATE_LIMIT_KV.get(usageKey)) ?? "0", 10);

    if (currentUsage >= DAILY_LIMIT) {
      return json({ error: "Daily message limit reached. Try again tomorrow." }, 429);
    }

    let body: { messages?: ChatMessage[] };
    try {
      body = await request.json();
    } catch {
      return json({ error: "Invalid JSON body" }, 400);
    }

    const messages = body.messages;
    if (!Array.isArray(messages) || messages.length === 0) {
      return json({ error: "messages must be a non-empty array" }, 400);
    }

    const client = new Anthropic({ apiKey: env.ANTHROPIC_API_KEY });

    try {
      const response = await client.messages.create({
        model: "claude-haiku-4-5",
        max_tokens: MAX_TOKENS,
        system: SYSTEM_PROMPT,
        messages: messages.slice(-MAX_HISTORY_MESSAGES).map((m) => ({
          role: m.role,
          content: m.content,
        })),
      });

      const replyText = response.content
        .filter((block): block is Anthropic.TextBlock => block.type === "text")
        .map((block) => block.text)
        .join("");

      await env.RATE_LIMIT_KV.put(usageKey, String(currentUsage + 1), {
        expirationTtl: 60 * 60 * 24 * 2,
      });

      return json({ reply: replyText || "..." });
    } catch (error) {
      const message = error instanceof Error ? error.message : "Unknown error";
      return json({ error: "Upstream error", detail: message }, 502);
    }
  },
};
