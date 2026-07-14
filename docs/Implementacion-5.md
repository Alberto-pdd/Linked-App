# Implementación 5: Pantalla Login

## Fecha
21 de marzo de 2026

## Última actualización
22 de marzo de 2026 — Unificación de color de fondo

## Objetivo
Crear la pantalla de login con autenticación mediante Google Sign-In.

## Sistema de colores

### Color de fondo universal
- **Color**: `Background` (#EEEAE4) del tema
- **Uso**: Todas las pantallas (Splash, Login, y futuras) usan este color de fondo
- **Definición**: `ui/theme/Color.kt` → `val Background = Color(0xFFEEEAE4)`

**Importante**: No crear colores de fondo locales. Siempre importar `Background` desde el tema.

## Componentes de la pantalla

Basada en el diseño HTML `linked-login.html` (versión actualizada).

### Layout
- Fondo: `Background` (#EEEAE4) — unificado con Splash Screen
- Contenido distribuido verticalmente
- Padding horizontal: 33dp
- Padding bottom: 46dp

### Estructura

1. **Logo centrado** (parte alta)
   - Icono: `linked_logo.png` (34dp, borderRadius 10dp)
   - Texto: "Linked" (30sp, Manrope ExtraBold)
   - Espacio entre icono y texto: 9dp

2. **Spacer flexible** (0.15f weight)

3. **Cards flotantes** (zona media)
   - 3 cards animadas con balanceo sinusoidal
   - Rotación: ±4 grados
   - Movimiento: ±8dp vertical

4. **Spacer flexible** (0.25f weight)

5. **Contenido inferior**
   - Headline: "Todos tus enlaces, *en orden.*" (42sp, DM Serif Display)
   - "en orden." en italic (DM Serif Display Italic, color Accent)
   - Subtext: 14sp, color #777
   - Spacer: 22dp
   - Botón Google: 60dp, borderRadius 14dp, Medium 15sp
   - Legal: 10.5sp, color #BBB

### Cards flotantes
| Card | Icono | Dominio | Rotación | Velocidad | Delay |
|------|-------|---------|----------|-----------|-------|
| 1 | dribbble_icon | dribbble.com | -4deg | 3000ms | 0ms |
| 2 | ic_scroll | techcrunch.com | +4deg | 3400ms | 1500ms |
| 3 | youtube_icon | youtube.com | -4deg | 3200ms | 750ms |

### Animación
- Tipo: `Animatable` + coroutine loop
- Movimiento lineal: -8dp a +8dp
- Velocidad constante (LinearEasing)
- Delay inicial por `kotlinx.coroutines.delay()`

## Flujo de autenticación

1. Usuario pulsa "Continuar con Google"
2. Se lanza Credential Manager API
3. Usuario selecciona cuenta Google
4. App recibe nombre + email
5. SessionManager.saveSession() guarda los datos
6. Navega a HomeScreen

## Archivos creados
1. `ui/screens/login/LoginScreen.kt`
2. `ui/screens/login/LoginViewModel.kt`
3. `res/drawable/ic_scroll.xml` — Icono scroll para Tech News
4. `res/font/dm_serif_display_regular.ttf` — Fuente DM Serif Display
5. `res/font/dm_serif_display_italic.ttf` — Fuente DM Serif Display Italic

## Archivos modificados
1. `ui/navigation/NavGraph.kt` — Reemplaza placeholder Login por LoginScreen real
2. `ui/theme/Type.kt` — Añade fuente DM Serif Display

## Dependencias utilizadas
- `google-identity` (ya añadida en Fase 1)
- `lifecycle-viewmodel-compose` (ya añadida en Fase 1)
