# Fases de implementación — Linked

## Visión general

Este documento describe cada fase del desarrollo de la aplicación Linked, desde la configuración inicial hasta la integración con el backend.

| Fase | Nombre | Estado |
|------|--------|--------|
| 1 | Dependencias y Theme | Completada |
| 2 | Modelos de datos | Completada |
| 3 | Navegación | Completada |
| 4 | Splash Screen + Session Manager | Completada |
| 5 | Pantalla Login | Completada |
| 6 | Pantalla Home + Bottom Nav | Pendiente |
| 7 | Pantalla Ajustes | Pendiente |
| 8 | Modal Añadir Link | Pendiente |
| 9 | Modal Añadir Carpeta | Pendiente |
| 10 | Pantalla Detalle Link | Pendiente |
| 11 | Diálogo Confirmación Eliminar | Pendiente |
| 12 | Repositorios Fake | Pendiente |

---

## Flujo de navegación completo

```
App inicia
    │
    ▼
SplashScreen (startDestination)
    │ Animación 2.5s + checkSession()
    │ Cards decorativas + logo centrado + progress indicator
    │
    ├── NO autenticado → LoginScreen
    │       └── Google Sign-In → guarda sesión → HomeScreen
    │
    └── SÍ autenticado → HomeScreen
            ├── [avatar] → SettingsScreen
            │       ├── Perfil
            │       ├── Tema
            │       ├── Idioma
            │       ├── Exportar/Importar
            │       ├── Soporte
            │       └── Cerrar sesión → borra sesión → LoginScreen
            ├── [+Link] → AddLinkSheet (bottom sheet)
            ├── [+Carpeta] → AddFolderSheet (bottom sheet)
            └── [tap link] → LinkDetailScreen
                    ├── Edición inline (título, URL, descripción)
                    ├── [⋮] → DeleteConfirmDialog
                    └── "Abrir enlace" → navegador externo
```

---

## Fase 1: Dependencias y Theme — Completada

Configurar las dependencias del proyecto y establecer el sistema de diseño visual.

**Archivos modificados/creados:**
- `gradle/libs.versions.toml` — Añadidas versiones y libraries (navigation, viewmodel, google auth, serialization)
- `app/build.gradle.kts` — Añadido plugin serialization + 4 dependencias
- `ui/theme/Color.kt` — Paleta de 14 colores Linked
- `ui/theme/Type.kt` — Tipografía Manrope (6 estilos)
- `ui/theme/Theme.kt` — Tema light/dark con colores Linked
- `res/font/` — 5 archivos Manrope (.ttf)

---

## Fase 2: Modelos de datos — Completada

Definir las estructuras de datos de la aplicación.

**Archivos creados:**
- `data/model/TagColor.kt` — Enum de colores + función `tagColorFor()`
- `data/model/Link.kt` — Data class con 8 campos + tagColor calculado
- `data/model/Folder.kt` — Data class con id y name
- `data/model/User.kt` — Data class con id, name, email + initials calculado

**Decisiones clave:**
- UUID para identificadores únicos irrepetibles
- Relación N-N Link↔Folder via `Link.folderIds: List<UUID>`
- Colores de tag deterministas por hash del nombre

---

## Fase 3: Navegación — Completada

Configurar el grafo de navegación con Compose Navigation.

**Archivos creados/modificados:**
- `ui/navigation/Screen.kt` — Rutas definidas como sealed class
- `ui/navigation/NavGraph.kt` — Grafo de navegación con todas las rutas
- `MainActivity.kt` — Reemplazado contenido por NavGraph

**Rutas definidas:**
- `Splash` → Pantalla de inicio (startDestination)
- `Login` → Pantalla de inicio de sesión
- `Home` → Pantalla principal con links y carpetas
- `Settings` → Pantalla de ajustes
- `LinkDetail/{linkId}` → Detalle de un link específico

**Nota:** Las bottom sheets (añadir link, añadir carpeta) no son rutas de navegación, se abren dentro de HomeScreen.

---

## Fase 4: Splash Screen + Session Manager — Completada

Crear la pantalla de inicio con animación y el gestor de sesión persistente.

**Archivos creados:**
- `ui/screens/splash/SplashScreen.kt` — UI de la splash screen
- `ui/screens/splash/SplashViewModel.kt` — Verifica sesión y redirige
- `data/local/SessionManager.kt` — Guarda/lee/borra sesión con DataStore

**Splash Screen — Componentes:**
- 12 cards decorativas flotando con animación sinusoidal
- Logo centrado horizontalmente en la parte alta
- Progress indicator lineal
- Fondo: `#EEEAE4` (beige cálido)
- Duración total de animación: ~2.5 segundos

**Animación de cards:**
- Movimiento sinusoidal fluido (Animatable + coroutine)
- Rotación: ±4 grados
- Velocidades diferentes por card
- Delays diferentes para desincronizar

**Session Manager — Funcionalidad:**
- `isLoggedIn(): Boolean` — Comprueba si hay sesión activa
- `saveSession(user: User)` — Guarda datos del usuario en DataStore
- `getSession(): SessionData?` — Lee datos del usuario guardado
- `clearSession()` — Borra la sesión (logout)

**Flujo:**
```
SplashScreen (startDestination)
    │ Animación 2.5s + checkSession()
    │
    ├── isLoggedIn = false → LoginScreen
    └── isLoggedIn = true → HomeScreen
```

---

## Fase 5: Pantalla Login — Completada

Crear la pantalla de login con Google Sign-In.

**Archivos creados:**
- `ui/screens/login/LoginScreen.kt` — UI de la pantalla de login
- `ui/screens/login/LoginViewModel.kt` — ViewModel para gestión de autenticación
- `res/drawable/ic_scroll.xml` — Icono scroll para Tech News
- `res/drawable/ic_card_design.xml` — Icono para card de diseño
- `res/drawable/ic_card_tech.xml` — Icono para card de tech
- `res/drawable/ic_card_video.xml` — Icono para card de video
- `res/drawable/ic_google.xml` — Icono de Google
- `res/font/dm_serif_display_regular.ttf` — Fuente DM Serif Display
- `res/font/dm_serif_display_italic.ttf` — Fuente DM Serif Display Italic

**Archivos modificados:**
- `ui/navigation/NavGraph.kt` — Reemplaza placeholder Login por LoginScreen real
- `ui/theme/Type.kt` — Añade fuente DM Serif Display

**Componentes de la pantalla:**
- Logo centrado en la parte alta (linked_logo.png + "Linked")
- Cards flotantes con animación (Dribbble, Scroll, YouTube)
- Headline: "Todos tus enlaces, *en orden.*" (DM Serif Display)
- "en orden." en italic (DM Serif Display Italic)
- Botón "Continuar con Google"
- Texto legal

**Diseño:**
- Fondo: `#EEEAE4` (unificado con Splash)
- Layout vertical con spacers flexibles
- Logo arriba, cards en medio, contenido abajo

**Flujo:**
- Google Sign-In exitoso → guarda sesión en SessionManager → navega a HomeScreen

**Dependencias:**
- Google Identity (ya añadida en Fase 1)
- Credential Manager API para el flujo de autenticación

---

## Fase 6: Pantalla Home + Bottom Nav — Pendiente

Crear la pantalla principal con la lista de links, carpetas y navegación inferior.

**Archivos a crear:**
- `ui/screens/home/HomeScreen.kt` — UI de la pantalla principal
- `ui/screens/home/HomeViewModel.kt` — ViewModel para links y carpetas
- `ui/components/BottomNavBar.kt` — Barra de navegación inferior
- `ui/components/LinkCard.kt` — Card de link en la lista
- `ui/components/FolderCard.kt` — Card de carpeta en la lista
- `ui/components/SearchBar.kt` — Barra de búsqueda

**Componentes de la pantalla:**
- Header: título "Linked" + avatar con iniciales del usuario (JD)
- Barra de búsqueda
- Sección "Links": contador + botón añadir + lista de LinkCards
- Sección "Carpetas": contador + botón añadir + lista de FolderCards
- Bottom Nav: Inicio, Buscar, Carpetas, Perfil

**Funcionalidad:**
- Al tocar avatar → navega a SettingsScreen
- Al tocar un link → navega a LinkDetailScreen
- Al tocar botón añadir link → abre AddLinkSheet (Fase 8)
- Al tocar botón añadir carpeta → abre AddFolderSheet (Fase 9)
- Búsqueda por título/tag/URL

---

## Fase 7: Pantalla Ajustes — Pendiente

Crear la pantalla de configuración de la aplicación.

**Archivos a crear:**
- `ui/screens/settings/SettingsScreen.kt` — UI de la pantalla de ajustes
- `ui/screens/settings/SettingsViewModel.kt` — Gestión de cierre de sesión

**Componentes de la pantalla:**
- Profile card: avatar gradient + nombre + email + badge "Google"
- Sección Cuenta: Perfil
- Sección Apariencia: Tema, Idioma
- Sección Privacidad y datos: Exportar datos, Importar enlaces
- Sección Soporte: Valorar, Centro de ayuda, Reportar, Política de privacidad
- Sección Zona de peligro: Cerrar sesión (rojo), Eliminar cuenta (rojo)

**Funcionalidad:**
- Accesible desde el avatar (JD) en el header del HomeScreen
- Cerrar sesión → llama `SessionManager.clearSession()` → navega a LoginScreen (limpia backstack)

---

## Fase 8: Modal Añadir Link — Pendiente

Crear el bottom sheet para añadir nuevos enlaces.

**Archivos a crear:**
- `ui/screens/home/AddLinkSheet.kt` — UI del modal bottom sheet

**Componentes del modal:**
- Handle de arrastre
- Título "Nuevo enlace"
- Campo: URL (obligatorio)
- Campo: Título
- Campo: Etiqueta (tag)
- Botones: Cancelar + Guardar enlace

**Funcionalidad:**
- Se abre desde HomeScreen al pulsar botón "+"
- Valida que la URL no esté vacía
- Genera UUID para el nuevo link
- Cierra el modal tras guardar
- Actualiza la lista en HomeScreen

---

## Fase 9: Modal Añadir Carpeta — Pendiente

Crear el bottom sheet para añadir nuevas carpetas.

**Archivos a crear:**
- `ui/screens/home/AddFolderSheet.kt` — UI del modal bottom sheet

**Componentes del modal:**
- Handle de arrastre
- Título "Nueva carpeta"
- Campo: Nombre (obligatorio)
- Botones: Cancelar + Crear carpeta

**Funcionalidad:**
- Se abre desde HomeScreen al pulsar botón "+ carpetas"
- Valida que el nombre no esté vacío
- Genera UUID para la nueva carpeta
- Cierra el modal tras crear
- Actualiza la lista en HomeScreen

---

## Fase 10: Pantalla Detalle Link — Pendiente

Crear la pantalla de detalle con edición inline.

**Archivos a crear:**
- `ui/screens/linkdetail/LinkDetailScreen.kt` — UI de la pantalla de detalle
- `ui/screens/linkdetail/LinkDetailViewModel.kt` — ViewModel para edición

**Componentes de la pantalla:**
- Botón volver (flecha) + menú 3 puntos (con opción "Eliminar")
- Favicon/color del tag + badge de tag + título editable
- URL editable + botón copiar
- Info card:
  - Descripción editable (placeholder "Añade una descripción...")
  - Fecha de creación
  - Fecha de última modificación
  - Carpetas (chips)
- Botón "Abrir enlace" (abre URL en navegador)
- Hint: "Toca el nombre, URL o descripción para editar"
- Toast de notificación ("✓ Guardado", "✓ URL copiada")

**Funcionalidad de edición inline:**
- Título, URL y descripción son editables directamente
- Al perder foco → guarda automáticamente
- Muestra toast "✓ Guardado"
- Actualiza "Modificado" a "Ahora mismo"

---

## Fase 11: Diálogo Confirmación Eliminar — Pendiente

Crear el modal de confirmación para eliminar enlaces.

**Archivos a crear:**
- `ui/components/DeleteConfirmDialog.kt` — Diálogo de confirmación

**Componentes:**
- Icono de papelera
- Título: "Eliminar enlace"
- Subtítulo: "¿Seguro que quieres eliminarlo? Esta acción no se puede deshacer."
- Botones: Cancelar + Eliminar (rojo)

**Funcionalidad:**
- Se abre desde el menú 3 puntos en LinkDetailScreen
- Confirmar → elimina el link y vuelve a HomeScreen
- Cancelar → cierra el diálogo
- Toast "🗑️ Enlace eliminado" tras eliminar

---

## Fase 12: Repositorios Fake — Pendiente

Crear repositorios con datos mock para desarrollo sin backend.

**Archivos a crear:**
- `data/repository/LinkRepository.kt` — Interface del repositorio de links
- `data/repository/FakeLinkRepository.kt` — Implementación fake con datos hardcoded
- `data/repository/FolderRepository.kt` — Interface del repositorio de carpetas
- `data/repository/FakeFolderRepository.kt` — Implementación fake con datos hardcoded

**Datos mock de links:**
- Figma — Collaborative Design (tag: "Diseño")
- Claude by Anthropic (tag: "IA")
- TechCrunch — Latest News (tag: "Noticias")
- YouTube — Ver después (tag: "Video")

**Datos mock de carpetas:**
- Trabajo (2 links)
- Recursos Dev (1 link)

**Funcionalidad:**
- CRUD completo: getAll, getById, create, update, delete
- Filtrado por carpeta
- Búsqueda por título/tag/URL
- Conteo de links por carpeta

---

## Fase futura: Backend Integration

Cuando el backend SpringBoot + PostgreSQL esté listo:

**Archivos a crear:**
- `data/remote/LinkApi.kt` — Interfaz Retrofit para endpoints de links
- `data/remote/FolderApi.kt` — Interfaz Retrofit para endpoints de carpetas
- `data/remote/AuthApi.kt` — Interfaz Retrofit para autenticación
- `data/repository/RemoteLinkRepository.kt` — Implementación real del repositorio
- `data/repository/RemoteFolderRepository.kt` — Implementación real del repositorio
- `di/AppModule.kt` — Inyección de dependencias (Hilt/Koin)

**Endpoints previstos:**
```
GET    /api/links          → List<Link>
GET    /api/links/{id}     → Link
POST   /api/links          → Link
PUT    /api/links/{id}     → Link
DELETE /api/links/{id}     → void

GET    /api/folders        → List<Folder>
POST   /api/folders        → Folder
DELETE /api/folders/{id}   → void

POST   /api/auth/google    → User + token
```

---

## Pendiente: Google Sign-In

**Estado:** No implementado

**Descripción:** El login actual es fake (hardcoded). Hay que implementar Google Sign-In real cuando se configure Firebase.

**Archivos a modificar:**
- `ui/screens/login/LoginScreen.kt` — Reemplazar `.clickable { viewModel.onGoogleSignInSuccess("Usuario", "test@linked.com") }` con llamada real a Google
- `ui/screens/login/LoginViewModel.kt` — Añadir lógica de Google Sign-In
- `app/build.gradle.kts` — Añadir dependencias de Credential Manager

**Dependencias:**
- Firebase project configurado
- Google Cloud Console con OAuth 2.0 credentials
- SHA-1 fingerprint registrado

**Referencia:** Roadmap Phase 5 (Login) — actualmente incompleto
---
