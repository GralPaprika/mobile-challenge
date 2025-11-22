# Mobile Challenge - Galería de Fotos

Aplicación Android que muestra un álbum de fotos con scroll infinito, descripción y reproducción de videos.

![Demo](Screen_recording_20251122_092827.gif)

## Instalación

1. Abrir el proyecto en Android Studio
2. Esperar a que sincronice las dependencias
3. Configurar `local.properties` (si no está):
   ```properties
   API_BASE_URL="https://jsonplaceholder.typicode.com"
   LOREM_API_BASE_URL="https://lorem-api.com/api"
   ```
4. Conectar un dispositivo o iniciar emulador
5. Click en Run o presionar Shift+F10

## Arquitectura

El proyecto sigue **Clean Architecture**. En `presentation/` están las pantallas, en `domain/` la lógica de negocio, en `data/` los repositorios y la API.

Usamos **MVVM** con `StateFlow`, **Repository Pattern**, **Hilt** para inyección, y **Flow** para operaciones asíncronas.

## Características

- **Paginación servidor**: Infinita scroll con control por álbum
- **Detectar conexión**: Pantalla de error automática sin internet
- **Video en ExoPlayer**: Pantalla completa, soporta rotación
- **Esqueletos de carga**: Shimmer animado
- **Descripción**: Desde API externa con fallback

## Dependencias Principales

- Compose 2025.11.01
- Retrofit 3.0.0, OkHttp 5.3.2
- Hilt 2.57.2
- ExoPlayer 1.8.0
- Mockito 5.20.0, byte-buddy-agent 1.17.7 (testing)

## Testing

Pruebas unitarias en `src/test/`, androidTests en `src/androidTest/`.

```bash
./gradlew test                    # unitarias
./gradlew connectedAndroidTest   # instrumentadas
```

## Notas Técnicas

- ViewModels son `open` para permitir mocking
- byte-buddy-agent para mocking de clases finales
- `StateFlow` con `rememberSaveable` persiste en rotación
- Detecta errores de red verificando excepciones y mensajes
- Coil con lazy loading y crossfade para imágenes

