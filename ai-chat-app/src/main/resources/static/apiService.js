const BASE_URL = "http://localhost:8080";

export default async function askAI(prompt) {
    const response = await fetch(BASE_URL + "/api/prompt", {
        method: "POST",
        credentials: 'include',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({prompt: prompt}),
    });
    return await response.json();
}
