# Siguiente — Estado del proyecto

## Fecha de última actualización
22 de marzo de 2026

## Estado actual

La aplicación Linked está en desarrollo activo. Las fases 1-5 están completadas.

### Fases completadas
1. ✅ Dependencias y Theme
2. ✅ Modelos de datos
3. ✅ Navegación
4. ✅ Splash Screen + Session Manager
5. ✅ Pantalla Login

### Próxima fase
**Fase 6: Pantalla Home + Bottom Nav**

---

## Qué se debe hacer ahora

Crear la pantalla principal (Home) con la lista de links, carpetas y navegación inferior.

### Archivos a crear
- `ui/screens/home/HomeScreen.kt` — UI de la pantalla principal
- `ui/screens/home/HomeViewModel.kt` — ViewModel para links y carpetas
- `ui/components/BottomNavBar.kt` — Barra de navegación inferior
- `ui/components/LinkCard.kt` — Card de link en la lista
- `ui/components/FolderCard.kt` — Card de carpeta en la lista
- `ui/components/SearchBar.kt` — Barra de búsqueda

### Componentes de la pantalla
- Header: título "Linked" + avatar con iniciales del usuario (JD)
- Barra de búsqueda
- Sección "Links": contador + botón añadir + lista de LinkCards
- Sección "Carpetas": contador + botón añadir + lista de FolderCards
- Bottom Nav: Inicio, Buscar, Carpetas, Perfil

### Funcionalidad
- Al tocar avatar → navega a SettingsScreen
- Al tocar un link → navega a LinkDetailScreen
- Al tocar botón añadir link → abre AddLinkSheet (bottom sheet)
- Al tocar botón añadir carpeta → abre AddFolderSheet (bottom sheet)
- Búsqueda por título/tag/URL

### Diseño
- Fondo: `Background` (#EEEAE4)
- Tipografías: Manrope (general), DM Serif Display (headlines)
- Botón "Continuar con Google" para autenticación

---

## Archivos clave del proyecto

### Estructura de paquetes
```
pdalbert.apps.linked/
├── data/
│   ├── model/          ← Link, Folder, User, TagColor
│   ├── local/          ← SessionManager
│   ├── repository/     ← Pendiente (fake repos)
│   └── remote/         ← Pendiente (Retrofit)
├── ui/
│   ├── screens/
│   │   ├── splash/     ← SplashScreen, SplashViewModel
│   │   ├── login/      ← LoginScreen, LoginViewModel
│   │   ├── home/       ← Pendiente
│   │   ├── linkdetail/ ← Pendiente
│   │   ├── linkform/   ← Pendiente
│   │   └── settings/   ← Pendiente
│   ├── components/     ← Pendiente
│   ├── navigation/     ← Screen, NavGraph
│   └── theme/          ← Colors, Typography, Theme
├── viewmodel/          ← Pendiente
└── di/                 ← Pendiente
```

### Documentación
- `docs/Roadmap.md` — Fases del proyecto
- `docs/architecture.md` — Arquitectura y sistema de diseño
- `docs/Implementacion-X.md` — Detalles de cada fase completada
- `docs/IMPORTANTE.md` — Notas importantes (dominio pendiente)

### Assets
- `assets/icons/` — Iconos SVG originales
- `res/drawable/` — Iconos convertidos a Android Vector Drawable
- `res/font/` — Fuentes Manrope + DM Serif Display

---

## Instrucciones para Opencode

1. **Leer este archivo** para entender el estado actual
2. **Leer `docs/Roadmap.md`** para ver todas las fases
3. **Leer `docs/architecture.md`** para entender el sistema de diseño
4. **Leer `docs/README.md`** para entender decisiones importantes del proyecto
5. **Implementar la Fase 6** según la descripción anterior

### Notas importantes
- No crear colores de fondo locales, usar `Background` del tema
- Usar Manrope como fuente principal, DM Serif Display para headlines
- Las animaciones deben usar `Animatable` + `LaunchedEffect` (no `rememberInfiniteTransition`)
- Los iconos deben estar en `res/drawable/` como Android Vector Drawable
- El color de fondo es `#EEEAE4` para todas las pantallas

---

## Cómo actualizar este archivo

Este archivo debe actualizarse después de cada fase completada. El proceso es:

### 1. Al completar una fase

1. **Marcar la fase como completada** en la sección "Fases completadas"
2. **Actualizar la "Próxima fase"** con el siguiente número de fase
3. **Actualizar "Qué se debe hacer ahora"** con la descripción de la nueva fase
4. **Actualizar la "Fecha de última actualización"**
5. **Crear el archivo `docs/Implementacion-X.md`** documentando la fase completada

### 2. Ejemplo de actualización

Si se acaba de completar la Fase 6 (Home Screen):

```markdown
## Fecha de última actualización
23 de marzo de 2026

### Fases completadas
1. ✅ Dependencias y Theme
2. ✅ Modelos de datos
3. ✅ Navegación
4. ✅ Splash Screen + Session Manager
5. ✅ Pantalla Login
6. ✅ Pantalla Home + Bottom Nav  ← AÑADIR

### Próxima fase
**Fase 7: Pantalla Ajustes**  ← CAMBIAR

---

## Qué se debe hacer ahora

Crear la pantalla de configuración de la aplicación.  ← CAMBIAR
...
```

### 3. Archivos a actualizar siempre

| Archivo | Qué actualizar |
|---------|----------------|
| `docs/Siguiente.md` | Estado, próxima fase, descripción |
| `docs/Roadmap.md` | Marcar fase como "Completada" |
| `docs/Implementacion-X.md` | Crear nuevo documento de la fase |
| `docs/architecture.md` | Solo si hay cambios en arquitectura/diseño |

### 4. Reglas de actualización

- **Siempre** actualizar antes de empezar una nueva fase
- **Nunca** modificar fases anteriores (crear nuevo Implementacion-X.md)
- **Documentar** decisiones importantes tomadas durante el desarrollo
- **Incluir** problemas encontrados y soluciones aplicadas
