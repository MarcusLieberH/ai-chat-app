package dk.kea.aichat.service;

import dk.kea.aichat.client.JokeClient;
import dk.kea.aichat.client.WeatherClient;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
/**
 * ChatGptService - Intelligent routing mellem forskellige APIs
 * Phase 1: Jokes + Weather (gratis)
 * Phase 2: Tilføj Claude AI senere
 */
@Service
public class ChatGptService {

    private final JokeClient jokeClient;
    private final WeatherClient weatherClient;

    public ChatGptService(JokeClient jokeClient, WeatherClient weatherClient) {
        this.jokeClient = jokeClient;
        this.weatherClient = weatherClient;
    }

    public record ResponseDto(String response) {}

    /**
     * Få en joke response - uanset hvad brugeren skriver!
     */
    public Mono<ResponseDto> getChatGptResponse(String prompt) {
        String lowerPrompt = prompt.toLowerCase();

        // 1. CHECK FOR JOKE REQUEST
        if (isJokeRequest(lowerPrompt)) {
            return handleJokeRequest();
        }

        // 2. CHECK FOR WEATHER REQUEST
        if (isWeatherRequest(lowerPrompt)) {
            String city = extractCity(prompt);
            return handleWeatherRequest(city);
        }

        // 3. FALLBACK - Kan ikke håndtere dette endnu
        return handleFallback(prompt);
    }

    /**
     * Detect om brugeren vil have en joke
     *
     * Keywords: joke, funny, laugh, humor, vittighed
     */
    private boolean isJokeRequest(String prompt) {
        return prompt.contains("joke") ||
                prompt.contains("funny") ||
                prompt.contains("laugh") ||
                prompt.contains("humor") ||
                prompt.contains("vittighed") ||
                prompt.contains("fortæl en joke") ||
                prompt.contains("tell me a joke");
    }

    /**
     * Detect om brugeren spørger om vejret
     *
     * Keywords: weather, vejr, temperature, temperatur
     */
    private boolean isWeatherRequest(String prompt) {
        return prompt.contains("weather") ||
                prompt.contains("vejr") ||
                prompt.contains("temperature") ||
                prompt.contains("temperatur") ||
                prompt.contains("grader") ||
                prompt.contains("how's the weather") ||
                prompt.contains("what's the weather");
    }

    /**
     * Udtræk bynavn fra prompt
     *
     * Leder efter patterns som:
     * - "weather in Copenhagen"
     * - "vejret i London"
     * - "temperature in Paris"
     */
    private String extractCity(String prompt) {
        String lowerPrompt = prompt.toLowerCase();

        // Find " in " eller " i " patterns
        String[] patterns = {" in ", " i "};

        for (String pattern : patterns) {
            int index = lowerPrompt.indexOf(pattern);
            if (index != -1) {
                // Tag tekst efter pattern
                String afterPattern = prompt.substring(index + pattern.length()).trim();
                // Tag første ord (bynavn)
                String[] words = afterPattern.split("\\s+");
                if (words.length > 0) {
                    // Rens for punktum, komma osv.
                    String city = words[0].replaceAll("[^a-zA-ZæøåÆØÅ]", "");
                    if (!city.isEmpty()) {
                        return city;
                    }
                }
            }
        }

        // Default hvis ingen by findes
        return "Copenhagen";
    }

    /**
     * Håndter joke request med JokeAPI
     */
    private Mono<ResponseDto> handleJokeRequest() {
        return jokeClient.getRandomJoke()
                .map(jokeData -> {
                    // Format joken pænt
                    String formattedJoke = "🎭 Here's a joke for you:\n\n" +
                            jokeData.getFullJoke();
                    return new ResponseDto(formattedJoke);
                })
                .onErrorResume(e -> {
                    // Hvis JokeAPI fejler
                    return Mono.just(new ResponseDto(
                            "😔 Sorry, I couldn't fetch a joke right now. Please try again!"
                    ));
                });
    }

    /**
     * Håndter weather request med WeatherAPI
     */
    private Mono<ResponseDto> handleWeatherRequest(String city) {
        return weatherClient.getWeather(city)
                .map(weatherData -> {
                    // Byg et pænt, læsbart svar
                    String weatherInfo = String.format(
                            "🌤️ Weather in %s:\n\n" +
                                    "🌡️ Temperature: %.1f°C (feels like %.1f°C)\n" +
                                    "☁️ Condition: %s\n" +
                                    "💧 Humidity: %d%%",
                            weatherData.name(),
                            weatherData.main().temp(),
                            weatherData.main().feels_like(),
                            weatherData.weather()[0].description(),
                            weatherData.main().humidity()
                    );

                    return new ResponseDto(weatherInfo);
                })
                .onErrorResume(e -> {
                    // Hvis WeatherAPI fejler (f.eks. by ikke fundet)
                    if (e.getMessage().contains("City not found")) {
                        return Mono.just(new ResponseDto(
                                "🗺️ Sorry, I couldn't find weather for '" + city +
                                        "'. Try another city like Copenhagen, London, or New York!"
                        ));
                    }
                    return Mono.just(new ResponseDto(
                            "😔 Sorry, I couldn't fetch weather data right now. Please try again!"
                    ));
                });
    }

    /**
     * Fallback for prompts vi ikke kan håndtere endnu
     *
     * PHASE 2: Dette er hvor vi tilføjer Claude AI!
     */
    private Mono<ResponseDto> handleFallback(String prompt) {
        String fallbackMessage =
                "🤖 I can help you with:\n\n" +
                        "🎭 Jokes - Just ask 'tell me a joke'\n" +
                        "🌤️ Weather - Ask 'what's the weather in [city]'\n\n" +
                        "More features coming soon! 🚀";

        return Mono.just(new ResponseDto(fallbackMessage));
    }
}