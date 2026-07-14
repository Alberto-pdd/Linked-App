# Implementación 3: Navegación

## Fecha
21 de marzo de 2026

## Última actualización
22 de marzo de 2026 — Marcada como completada

## Estado
**Completada**

## Objetivo
Configurar el grafo de navegación con Compose Navigation. Definir las rutas de la app y reemplazar el contenido de MainActivity por el grafo de navegación.

## Rutas definidas

| Ruta | Destino | Argumentos |
|------|---------|------------|
| `splash` | Pantalla de inicio (startDestination) | Ninguno |
| `login` | Pantalla de login Google | Ninguno |
| `home` | Pantalla principal | Ninguno |
| `settings` | Pantalla de ajustes | Ninguno |
| `link_detail/{linkId}` | Detalle de link | `linkId: UUID (String)` |

## Flujo de navegación

```
App inicia
    │
    ▼
SplashScreen (startDestination)
    ├── NO autenticado → LoginScreen → HomeScreen
    └── SÍ autenticado → HomeScreen
                            ├── [avatar] → SettingsScreen
                            │       └── Cerrar sesión → LoginScreen
                            ├── [+Link] → AddLinkSheet (no es ruta)
                            ├── [+Carpeta] → AddFolderSheet (no es ruta)
                            └── [tap link] → LinkDetailScreen
                                    └── [⋮] → DeleteConfirmDialog (no es ruta)
```

## Archivos creados

### Screen.kt
Sealed class con 5 objetos de ruta. Cada objeto define su `route` string.
- `LinkDetail` incluye método helper `createRoute(linkId: UUID)` para construir la ruta con el argumento.

### NavGraph.kt
Composable que configura `NavHost` con todas las rutas.
- Usa `rememberNavController()` para el controlador de navegación
- Define `startDestination = Screen.Splash.route`
- Recibe `SessionManager` como parámetro para crear ViewModels
- Cada pantalla realiza navegación condicional basada en estado

### MainActivity.kt
Reemplazado el contenido del template:
- Antes: `Scaffold` con `Greeting("Android")`
- Ahora: `LinkedTheme` + `NavGraph` con `rememberNavController()` + `SessionManager`

## Archivos creados
1. `ui/navigation/Screen.kt`
2. `ui/navigation/NavGraph.kt`

## Archivos modificados
1. `MainActivity.kt`

## Dependencias
- `navigation-compose` (ya añadida en Fase 1)
- `lifecycle-viewmodel-compose` (ya añadida en Fase 1)
