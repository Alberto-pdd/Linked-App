# Implementación 4: Splash Screen + Session Manager

## Fecha
21 de marzo de 2026

## Última actualización
22 de marzo de 2026 — Actualización con implementación final

## Objetivo
Implementar la pantalla de inicio con animación y el sistema de persistencia de sesión del usuario.

## Sistema de colores

### Color de fondo universal
- **Color**: `Background` (#EEEAE4) del tema
- **Uso**: Todas las pantallas usan este color de fondo
- **Definición**: `ui/theme/Color.kt` → `val Background = Color(0xFFEEEAE4)`

---

## Splash Screen

### Layout
- Fondo: `Background` (#EEEAE4)
- Logo centrado horizontalmente en la parte alta
- Cards decorativas flotando en la zona media
- Progress indicator en la parte inferior

### Componentes visuales (de fondo a frente)

**Capa 1: Cards decorativas**
- 12 cards con iconos de servicios (Dribbble, Claude, Instagram, YouTube, etc.)
- Posiciones escaladas proporcionalmente desde diseño HTML original
- Animación sinusoidal fluida (±8dp vertical)
- Rotación: ±4 grados por card

**Capa 2: Logo**
- Icono: `linked_logo.png` (34dp, borderRadius 10dp)
- Texto: "Linked" (30sp, Manrope ExtraBold)
- Centrado horizontalmente
- Espacio entre icono y texto: 9dp

**Capa 3: Progress indicator**
- `LinearProgressIndicator` en modo indeterminado
- Ancho: 56dp, Alto: 3dp
- Color: Accent (#2563EB)
- Track color: Border (#E2E1DC)

### Timeline de animación
```
0.0s  → Cards empiezan a flotar
0.0s  → Logo aparece
0.0s  → Progress indicator inicia
2.5s  → Navega a Login o Home
```

### Animación de cards
- Tipo: `Animatable` + `LaunchedEffect` coroutine
- Movimiento: sinusoidal continuo (-8dp a +8dp)
- Velocidad: LinearEasing
- Desfase por delay inicial (`kotlinx.coroutines.delay()`)

**Parámetros por card:**

| Card | Icono | Velocidad | Delay |
|------|-------|-----------|-------|
| 1 | dribbble_icon | 3000ms | 0ms |
| 2 | claude_icon | 3400ms | 1500ms |
| 3 | instagram_icon | 3200ms | 750ms |
| 4 | youtube_icon | 3100ms | 1200ms |
| 5 | figma_icon | 3300ms | 800ms |
| 6 | github_icon | 3500ms | 1800ms |
| 7 | pinterest_icon | 3000ms | 500ms |
| 8 | airbnb_icon | 3400ms | 1300ms |
| 9 | linkedin_icon | 3200ms | 900ms |
| 10 | notion_icon | 3100ms | 1600ms |
| 11 | telegram_icon | 3300ms | 400ms |
| 12 | amazon_icon | 3500ms | 1100ms |

---

## Session Manager

Gestor de sesión que persiste el estado de autenticación usando DataStore.

### Operaciones
| Método | Descripción |
|--------|-------------|
| `isLoggedIn(): Boolean` | Comprueba si hay sesión activa |
| `saveSession(user: User)` | Guarda datos del usuario |
| `getSession(): SessionData?` | Lee datos del usuario guardado |
| `getUser(): User?` | Obtiene el objeto User de la sesión |
| `clearSession()` | Borra la sesión (logout) |

### Estructura guardada en DataStore
```
is_logged_in: true
user_id: "550e8400-e29b-41d4-a716-446655440000"
user_name: "Juan Díaz"
user_email: "juan.diaz@gmail.com"
```

---

## SplashViewModel

ViewModel que verifica el estado de la sesión y emite la ruta de destino.

### Flujo
1. Al inicializar, lee `SessionManager.isLoggedIn()`
2. Espera `SPLASH_DURATION_MS` (2500ms)
3. Emite la ruta destino: `"login"` o `"home"`
4. NavGraph navega a la pantalla correspondiente

### Estado expuesto
```kotlin
val destination: StateFlow<String?>  // "login", "home", o null
```

---

## Estrategia: Simulación vs Backend

### Ahora — Simulación sin backend
- Google Sign-In devuelve nombre + email
- App guarda datos directamente en DataStore
- No hay verificación real de seguridad

### Futuro — Backend SpringBoot + PostgreSQL
- App envía ID token al backend
- Backend verifica con Google y crea registro en PostgreSQL
- Backend devuelve JWT + datos usuario
- App guarda JWT para llamadas futuras

**¿Qué cambia?** Solo `LoginViewModel.onGoogleSignInSuccess()`
**¿Qué NO cambia?** SessionManager, UI, navegación, flujos

---

## Archivos creados
1. `data/local/SessionManager.kt`
2. `ui/screens/splash/SplashScreen.kt`
3. `ui/screens/splash/SplashViewModel.kt`
4. `res/drawable/figma_icon.xml`
5. `res/drawable/claude_icon.xml`
6. `res/drawable/instagram_icon.xml`
7. `res/drawable/youtube_icon.xml`
8. `res/drawable/dribbble_icon.xml`
9. `res/drawable/github_icon.xml`
10. `res/drawable/pinterest_icon.xml`
11. `res/drawable/airbnb_icon.xml`
12. `res/drawable/linkedin_icon.xml`
13. `res/drawable/notion_icon.xml`
14. `res/drawable/telegram_icon.xml`
15. `res/drawable/amazon_icon.xml`

## Archivos modificados
1. `ui/navigation/NavGraph.kt` — Reemplaza placeholder Splash por SplashScreen real
2. `gradle/libs.versions.toml` — Añadida dependencia DataStore
3. `app/build.gradle.kts` — Añadida dependencia DataStore

## Dependencias utilizadas
- `lifecycle-viewmodel-compose` (ya añadida en Fase 1)
- `androidx.datastore:datastore-preferences` (añadida en esta fase)
