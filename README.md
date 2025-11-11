#  AI Chat Application

KEA 3. Semester - AI Integration Project

##  Om Projektet

En intelligent chatbot der kombinerer:
-  **JokeAPI** - Random jokes
- 🌤 **WeatherAPI** - Real-time vejr data
-  **Smart routing** - Intelligent keyword detection

## 🛠 Teknologier

- **Backend:** Spring Boot 3.5.7 + WebFlux
- **Frontend:** HTML, CSS, JavaScript (ES6 modules)
- **APIs:** JokeAPI, OpenWeatherMap
- **Build:** Maven
- **Java:** 17

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Setup

1. **Clone repository:**
```bash
git clone https://github.com/YOUR_USERNAME/ai-chat-app.git
cd ai-chat-app
```

2. **Setup API keys:**
```bash
# Copy template
cp src/main/resources/application.properties.template src/main/resources/application.properties

# Edit with your keys
# Get Weather API key from: https://openweathermap.org/api
```

3. **Run application:**
```bash
mvn spring-boot:run
```

4. **Open browser:**
```
http://localhost:8080
```

##  Brug

### Test Prompts:

**Jokes:**
- "tell me a joke"
- "fortæl en vittighed"
- "make me laugh"

**Weather:**
- "what's the weather in Copenhagen"
- "vejret i London"
- "temperature in Paris"

##  Projekt Struktur
```
src/
├── main/
│   ├── java/dk/kea/aichat/
│   │   ├── client/         # API clients
│   │   ├── config/         # Configuration
│   │   ├── controller/     # REST endpoints
│   │   └── service/        # Business logic
│   └── resources/
│       ├── static/         # Frontend files
│       └── application.properties
```

##  Team

KEA Datamatiker - 3. Semester
- [Marcus]
- [Julius]
