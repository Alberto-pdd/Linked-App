# Auditoría 1 — Proyecto Linked (Android App)

**Fecha:** 13 de julio de 2026  
**Realizada por:** opencode (Análisis automatizado de código)  
**Alcance:** Análisis completo del código fuente, arquitectura, documentación y coherencia del proyecto

---

## 📋 Resumen Ejecutivo

El proyecto **Linked** es una aplicación Android para gestión de enlaces/bookmarks, desarrollada en **Kotlin 2.0.21** con **Jetpack Compose** y arquitectura **MVVM**. 

**Estado general:** ✅ Base sólida con buenas prácticas en theme, modelos, navegación y componentes UI, pero con **bugs críticos en SessionManager**, **violaciones de arquitectura MVVM** (ViewModels en carpetas UI), **desviación del Roadmap** (repositorios DataStore reales en lugar de Fake), y **deuda técnica significativa** (duplicación, hardcodeos, 0 tests).

---

## 🔴 PARTE 1: Primera Auditoría — Bugs y Problemas Técnicos

### 🚨 Críticos (Bloqueantes)

| # | Archivo/Línea | Problema | Impacto |
|---|---------------|----------|---------|
| 1 | `SessionManager.kt:65` | `UUID.fromString(session.userId)` sin null-check → **NPE garantizada** si userId es null | Crash al iniciar app si DataStore corrupto |
| 2 | `SessionManager.kt:51` | `return@map null` dentro de `.map()` → **No compila / comportamiento indefinido** | Sesión no se lee correctamente |
| 3 | `SessionManager.kt:36` | `.first()` en Flow vacío → **NoSuchElementException** | Crash si no hay preferencias guardadas |

### ⚠️ Arquitectura y Datos

| # | Problema | Detalle |
|---|----------|---------|
| 4 | **Anti-pattern: JSON en Preferences DataStore** | Preferences DataStore para key-value simples, no listas JSON completas. O(n) en cada operación, sin transacciones atómicas |
| 5 | **Race Conditions en Repositorios** | Patrón leer-modificar-escribir no atómico → pérdida de datos en escrituras concurrentes |
| 6 | **Error Silencioso en Parsing JSON** | `catch (_: Exception) { emptyList() }` oculta corrupción de datos sin log ni aviso al usuario |

### 🎨 UI / Compose / Temas

| # | Archivo | Problema |
|---|---------|----------|
| 7 | `LoginScreen.kt:49-53` | **Colores hardcodeados** (Surface, TextPrimary, Accent distinto al tema) → **No soporta dark mode**, inconsistencia visual |
| 8 | `LoginScreen.kt:201` | **Google Sign-In hardcodeado** → `onGoogleSignInSuccess("Usuario", "test@linked.com")` código de prueba en producción |
| 9 | `SearchBar.kt:60-69` | `BasicTextField` sin placeholder nativo → placeholder manual con `if (query.isEmpty())` |
| 10 | `AllLinksScreen` + `AllFoldersScreen` | **Swipe-to-delete duplicado** → ~130 líneas idénticas en ambos archivos |

### 🏗️ Calidad de Código / Mantenibilidad

| # | Problema | Detalle |
|---|----------|---------|
| 11 | `LoginScreen.kt` = 360 líneas | Debe dividirse en componentes: `LoginHeader`, `FloatingCards`, `LoginCTA`, `LegalText` |
| 12 | **Sin Inyección de Dependencias** | `MainActivity` instancia repositorios manualmente → anti-pattern |
| 13 | **Navegación no type-safe** | `Screen.kt` usa String routes; UUID en ruta como String → parsing manual propenso a errores |
| 14 | **Timestamps como Strings** | `Link.kt:18-19` `createdAt: String`, `modifiedAt: String` → deberían ser `Instant`/`Long` |
| 15 | **Strings hardcodeados en UI** | Textos en código en lugar de `strings.xml` → sin i18n, mantenimiento difícil |

### 🧪 Testing

| # | Problema |
|---|----------|
| 16 | **0 Tests reales** | Solo boilerplate `ExampleUnitTest` y `ExampleInstrumentedTest`. Sin tests de ViewModels, Repos, UI |

### ⚙️ Configuración / Build

| # | Archivo | Problema |
|---|---------|----------|
| 17 | `app/build.gradle.kts:10` | `compileSdk = 36` **no existe** (Android 14=34, 15=35) |
| 18 | `app/build.gradle.kts:24` | `isMinifyEnabled = false` en release → APK sin ofuscar/optimizar |
| 19 | Proyecto | Falta **lint / detekt / ktlint** configurado |

---

## 🔵 PARTE 2: Segunda Auditoría — Análisis de Documentación y Coherencia

### 📚 Documentación en `/docs` (Roadmap vs Realidad)

| Fase | Nombre | Estado Docs | Estado REAL | Desviación |
|------|--------|-------------|-------------|------------|
| 1 | Dependencias y Theme | ✅ | ✅ | — |
| 2 | Modelos de datos | ✅ | ✅ | — |
| 3 | Navegación | ✅ | ✅ | — |
| 4 | Splash + Session Manager | ✅ | ✅ | — |
| 5 | Pantalla Login | ✅ | ✅ | — |
| 6 | Home + Bottom Nav | ⏳ Pendiente | ⚠️ Parcial | HomeScreen existe, **BottomNav NO** |
| 7 | Pantalla Ajustes | ⏳ Pendiente | ❌ No existe | Placeholder en NavGraph |
| 8 | Modal Añadir Link | ⏳ Pendiente | ⚠️ Existe | `AddLinkSheet.kt` creado pero **no conectado** |
| 9 | Modal Añadir Carpeta | ⏳ Pendiente | ⚠️ Existe | `AddFolderSheet.kt` creado pero **no conectado** |
| 10 | Pantalla Detalle Link | ⏳ Pendiente | ❌ No existe | Solo placeholder en NavGraph |
| 11 | Diálogo Confirmación Eliminar | ⏳ Pendiente | ⚠️ Existe | `DeleteConfirmDialog.kt` creado pero **no conectado** |
| 12 | Repositorios Fake | ⏳ Pendiente | ❌ **NO** | **Usa DataStore real** en lugar de Fake |

> **Hallazgo crítico:** La **Fase 12 (Fake Repos)** está marcada como pendiente en Roadmap, pero el código ya implementa repositorios **DataStore reales** (`DataStoreLinkRepository`, `DataStoreFolderRepository`). Esto contradice el plan explícito: *"Fake Repositories para desarrollo sin backend"*.

---

### ❌ Violaciones de Arquitectura MVVM (Coherencia de Archivos)

#### Estructura ACTUAL (INCORRECTA)
```
ui/screens/home/
├── HomeScreen.kt
├── HomeViewModel.kt        ❌ DEBE ESTAR EN viewmodel/
├── AllLinksScreen.kt
├── AllLinksViewModel.kt    ❌ DEBE ESTAR EN viewmodel/
├── AllFoldersScreen.kt
├── AllFoldersViewModel.kt  ❌ DEBE ESTAR EN viewmodel/
├── AddLinkSheet.kt
├── AddFolderSheet.kt
ui/screens/login/
├── LoginScreen.kt
├── LoginViewModel.kt       ❌ DEBE ESTAR EN viewmodel/
ui/screens/splash/
├── SplashScreen.kt
├── SplashViewModel.kt      ❌ DEBE ESTAR EN viewmodel/
```

#### Estructura CORRECTA (según `architecture.md:7-9` y MVVM)
```
viewmodel/
├── HomeViewModel.kt
├── AllLinksViewModel.kt
├── AllFoldersViewModel.kt
├── LoginViewModel.kt
├── SplashViewModel.kt
├── SettingsViewModel.kt
├── LinkDetailViewModel.kt
ui/screens/
├── home/
│   ├── HomeScreen.kt
│   ├── AllLinksScreen.kt
│   ├── AllFoldersScreen.kt
│   ├── AddLinkSheet.kt
│   ├── AddFolderSheet.kt
├── login/
│   ├── LoginScreen.kt
├── splash/
│   ├── SplashScreen.kt
├── settings/
│   ├── SettingsScreen.kt
├── linkdetail/
│   ├── LinkDetailScreen.kt
```

**Consecuencias:**
- Rompe separación de responsabilidades (ViewModel ≠ View)
- Dificulta testing unitario de ViewModels
- Viola arquitectura documentada
- Imports circulares potenciales

---

### ❌ Carpetas Vacías (Solo .gitkeep)
```
viewmodel/.gitkeep    → Debería contener TODOS los ViewModels
di/.gitkeep           → Debería contener módulo Hilt/Koin (planificado en Roadmap)
```

---

### ✅ Lo Que Está BIEN (Fortalezas del Proyecto)

1. **Theme system completo y coherente** (`Color.kt`, `Type.kt`, `Theme.kt`)
2. **Modelos de datos bien diseñados** (UUID, relación N-N, `tagColor` calculado)
3. **Navegación bien estructurada** (`Screen` sealed class, `NavGraph`)
4. **SplashScreen** con animaciones fluidas y bien implementadas
5. **Componentes UI reutilizables** y de calidad:
   - `EmojiPicker`, `ColorPicker`, `Toast`, `FilterChips`
   - `DeleteConfirmDialog`, `LinkCard`, `FolderCard`
   - `SearchBar`, `TopNav`
6. **Arquitectura MVVM en ViewModels** (StateFlow, viewModelScope, factories)
7. **Serialización JSON** robusta con kotlinx.serialization + UUID serializers
8. **Dependencias actualizadas** y bien organizadas en version catalog (`libs.versions.toml`)

---

## 📋 Plan de Acción Priorizado

### P0 — Críticos (Bloquean funcionamiento correcto)
1. **Fixear SessionManager** (NPE, compilación, Flow vacío)
2. **Mover ViewModels a `viewmodel/`** y corregir imports
3. **Reemplazar DataStore Repos por Fake Repos** (cumplir Fase 12 del Roadmap)
4. **Fixear LoginScreen theming** (usar colores del tema: `Background`, `Ink`, `Accent`, etc.)
5. **Implementar Google Sign-In real** (Credential Manager API con `google-identity`)

### P1 — Arquitectura / Roadmap
6. **Crear package `viewmodel/`** y mover todos los ViewModels
7. **Crear módulo DI en `di/`** (Hilt o Koin)
8. **Implementar `BottomNavBar`** (Fase 6)
9. **Implementar `SettingsScreen`** (Fase 7)
10. **Conectar Sheets/Dialogs** a ViewModels (`AddLinkSheet`, `AddFolderSheet`, `DeleteConfirmDialog`)
11. **Implementar `LinkDetailScreen`** (Fase 10)

### P2 — Calidad / Refactor
12. **Extraer `SwipeableCard`** componente (eliminar duplicación AllLinks/AllFolders)
13. **Dividir `LoginScreen`** en componentes menores
14. **Migrar timestamps a `Instant`/`Long`** (epoch millis)
15. **Externalizar strings a `strings.xml`** (i18n)
16. **Navigation type-safe** (Navigation 3+ routes)
17. **Fix `compileSdk` (34/35) + `minifyEnabled=true`**
18. **Añadir tests** (ViewModel, Repository, Compose UI)

### P3 — Docs / Pendientes (`IMPORTANTE.md`)
19. **Actualizar dominio en SplashScreen** (placeholder "linked.com")
20. **Crear `EmojiRepository` dinámico** (eliminar listas hardcodeadas en Sheets)

---

## 📁 Archivos Clave a Revisar/Modificar

### Prioridad Inmediata
- `app/src/main/java/pdalbert/apps/linked/data/local/SessionManager.kt` — **Bugs críticos**
- `app/src/main/java/pdalbert/apps/linked/ui/screens/login/LoginScreen.kt` — **Theming + Google Sign-In**
- `app/src/main/java/pdalbert/apps/linked/ui/screens/home/` — **Mover ViewModels**
- `app/src/main/java/pdalbert/apps/linked/data/repository/` — **Fake vs DataStore**

### Estructura Target
```
app/src/main/java/pdalbert/apps/linked/
├── data/
│   ├── model/           ✅ OK
│   ├── local/           ✅ SessionManager (fix bugs)
│   ├── repository/      🔄 FakeLinkRepository, FakeFolderRepository (NUEVOS)
│   └── remote/          (futuro)
├── di/                  🔄 Hilt/Koin module (NUEVO)
├── ui/
│   ├── components/      ✅ OK (reutilizables)
│   ├── navigation/      ✅ OK
│   ├── screens/
│   │   ├── home/        🔄 Solo Screens + Sheets (ViewModels fuera)
│   │   ├── login/       🔄 Solo LoginScreen (ViewModel fuera)
│   │   ├── splash/      🔄 Solo SplashScreen (ViewModel fuera)
│   │   ├── settings/    (NUEVO)
│   │   └── linkdetail/  (NUEVO)
│   └── theme/           ✅ OK
└── viewmodel/           🔄 TODOS los ViewModels AQUÍ (NUEVA UBICACIÓN)
```

---

## 🎯 Conclusión

El proyecto tiene **excelentes cimientos** (arquitectura MVVM clara, theme system robusto, navegación bien estructurada, componentes UI de calidad), pero presenta **desviaciones críticas** respecto a su propia documentación y arquitectura declarada:

1. **SessionManager tiene bugs que crashean la app** (P0)
2. **ViewModels violan la separación MVVM** estando en carpetas UI (P0)
3. **Se saltó la Fase 12 (Fake Repos)** implementando DataStore real prematuramente (P1)
4. **LoginScreen rompe el theming** y tiene Google Sign-In fake (P0)
5. **Falta testing, lint, y configuración de release** (P2)

**Recomendación:** Abordar P0 inmediatamente antes de continuar con nuevas features. El costo de arreglar SessionManager y mover ViewModels ahora es bajo; dejarlo para después multiplicará la deuda técnica.

---

*Documento generado automáticamente tras análisis completo del código fuente y documentación del proyecto Linked.*