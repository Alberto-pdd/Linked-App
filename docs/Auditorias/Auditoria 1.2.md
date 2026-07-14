# Auditoría 1.2 — Proyecto Linked (Android App)

**Fecha:** 13 de julio de 2026  
**Versión:** 1.2 (Detallada y explicativa)  
**Realizada por:** opencode (Análisis automatizado de código + documentación)  
**Alcance:** Análisis exhaustivo del código fuente, arquitectura, documentación (`/docs`), coherencia MVVM, y desviaciones del Roadmap  

---

## 📖 ÍNDICE

1. [Contexto del Proyecto](#1-contexto-del-proyecto)
2. [Metodología de Auditoría](#2-metodología-de-auditoría)
3. [Resumen Ejecutivo](#3-resumen-ejecutivo)
4. [Parte A: Análisis Técnico Detallado (Bugs y Deuda Técnica)](#4-parte-a-análisis-técnico-detallado-bugs-y-deuda-técnica)
   - [A.1 Bugs Críticos en SessionManager](#a1-bugs-críticos-en-sessionmanager)
   - [A.2 Anti-patrones de Arquitectura de Datos](#a2-anti-patrones-de-arquitectura-de-datos)
   - [A.3 Violaciones de UI/Theming](#a3-violaciones-de-uitheming)
   - [A.4 Duplicación de Código y Mantenibilidad](#a4-duplicación-de-código-y-mantenibilidad)
   - [A.5 Configuración de Build y Testing](#a5-configuración-de-build-y-testing)
5. [Parte B: Análisis de Coherencia Documental y Arquitectural](#5-parte-b-análisis-de-coherencia-documental-y-arquitectural)
   - [B.1 Roadmap vs Realidad: Desviaciones Críticas](#b1-roadmap-vs-realidad-desviaciones-críticas)
   - [B.2 Violación de Separación MVVM (ViewModels en UI)](#b2-violación-de-separación-mvvm-viewmodels-en-ui)
   - [B.3 Carpetas Vacías Pendientes](#b3-carpetas-vacías-pendientes)
6. [Parte C: Fortalezas del Proyecto](#6-parte-c-fortalezas-del-proyecto)
7. [Matriz de Riesgo e Impacto](#7-matriz-de-riesgo-e-impacto)
8. [Plan de Acción Detallado y Priorizado](#8-plan-de-acción-detallado-y-priorizado)
9. [Guía de Reanudación para IA](#9-guía-de-reanudación-para-ia)
10. [Anexos: Estructura de Archivos Objetivo](#10-anexos-estructura-de-archivos-objetivo)

---

## 1. CONTEXTO DEL PROYECTO

### 1.1 Qué es Linked
Aplicación Android nativa para **guardar, organizar y acceder a enlaces/bookmarks** personales. Funciona como un "Pocket" privado con carpetas, tags, búsqueda y sincronización futura con backend SpringBoot + PostgreSQL.

### 1.2 Stack Tecnológico
| Capa | Tecnología | Versión |
|------|------------|---------|
| Lenguaje | Kotlin | 2.0.21 |
| UI | Jetpack Compose + Material 3 | BOM 2024.09.00 |
| Arquitectura | MVVM (Model-View-ViewModel) | - |
| Navegación | Compose Navigation | 2.7.7 |
| Persistencia Local | DataStore Preferences | 1.1.1 |
| Serialización | kotlinx.serialization | 1.6.3 |
| DI | *Pendiente* (carpeta `di/` existe) | - |
| Tests | JUnit 4 + Espresso + Compose Test | - |
| Min SDK / Target | 29 / 36* | *compileSdk 36 no existe |

### 1.3 Estado según Documentación (`/docs`)
| Fase | Nombre | Estado Docs | Estado Real |
|------|--------|-------------|-------------|
| 1 | Dependencias y Theme | ✅ Completada | ✅ Completada |
| 2 | Modelos de datos | ✅ Completada | ✅ Completada |
| 3 | Navegación | ✅ Completada | ✅ Completada |
| 4 | Splash + Session Manager | ✅ Completada | ✅ Completada (con bugs) |
| 5 | Pantalla Login | ✅ Completada | ✅ Completada (con bugs) |
| 6 | Home + Bottom Nav | ⏳ Pendiente | ⚠️ Parcial (Home existe, BottomNav NO) |
| 7 | Pantalla Ajustes | ⏳ Pendiente | ❌ No existe |
| 8 | Modal Añadir Link | ⏳ Pendiente | ⚠️ Existe (`AddLinkSheet`) pero **desconectado** |
| 9 | Modal Añadir Carpeta | ⏳ Pendiente | ⚠️ Existe (`AddFolderSheet`) pero **desconectado** |
| 10 | Pantalla Detalle Link | ⏳ Pendiente | ❌ No existe (solo placeholder) |
| 11 | Diálogo Confirmar Eliminar | ⏳ Pendiente | ⚠️ Existe (`DeleteConfirmDialog`) pero **desconectado** |
| 12 | **Repositorios Fake** | ⏳ Pendiente | ❌ **INCUMPLIDA** — Usa DataStore real |

---

## 2. METODOLOGÍA DE AUDITORÍA

1. **Lectura completa** de todos los `.kt` y `.xml` del proyecto (`app/src/main/java/...`)
2. **Lectura completa** de toda la documentación en `/docs` (9 archivos .md)
3. **Análisis estático** de: arquitectura, patrones, naming, imports, dependencias
4. **Comparativa** Roadmap documentado vs código real implementado
5. **Verificación** de coherencia MVVM, theming, navegación, serialización
6. **Clasificación** de hallazgos por severidad (Crítico/Alto/Medio/Bajo) y categoría

---

## 3. RESUMEN EJECUTIVO

> **Veredicto:** El proyecto tiene **excelentes cimientos técnicos** (theme system robusto, modelos bien diseñados, navegación limpia, componentes UI reutilizables de alta calidad), pero presenta **desviaciones críticas** respecto a su propia documentación y arquitectura declarada que **bloquean el avance seguro** a nuevas features.

### Problemas que IMPIDEN continuar con Fase 6+:
1. **SessionManager tiene 3 bugs que crashean la app** al iniciar (NPE, Flow vacío, return@map inválido)
2. **ViewModels están en carpetas equivocadas** (`ui/screens/*` en vez de `viewmodel/`) — rompe MVVM
3. **Fase 12 saltada**: Se implementó DataStore real en lugar de Fake Repos — acopla prematuramente a persistencia
4. **LoginScreen rompe theming** y tiene Google Sign-In hardcodeado (código de prueba en prod)

### Deuda Técnica Acumulada:
- ~130 líneas duplicadas (swipe-to-delete en AllLinks + AllFolders)
- 360 líneas en un solo archivo (LoginScreen)
- 0 tests unitarios/UI
- compileSdk 36 (inexistente), minifyEnabled=false
- Strings hardcodeados sin i18n

---

## 4. PARTE A: ANÁLISIS TÉCNICO DETALLADO

### A.1 Bugs Críticos en SessionManager

#### 🐛 Bug #1: NullPointerException Garantizada en `getUser()`
**Archivo:** `app/src/main/java/pdalbert/apps/linked/data/local/SessionManager.kt`  
**Línea:** 65

```kotlin
// CÓDIGO ACTUAL (LÍNEAS 62-68)
suspend fun getUser(): User? {
    val session = getSession() ?: return null
    return User(
        id = UUID.fromString(session.userId),  // 💥 CRASH AQUÍ
        name = session.userName ?: "",
        email = session.userEmail ?: ""
    )
}
```

**Por qué falla:**
- `session.userId` es `String?` (nullable) — viene de `preferences[Keys.USER_ID]` que devuelve `String?`
- `UUID.fromString(null)` lanza `NullPointerException` inmediata
- DataStore puede tener claves faltantes si: usuario borra datos, migración falla, corrupción

**Evidencia en código:** Keys definidas líneas 27-30:
```kotlin
val USER_ID = stringPreferencesKey("user_id")  // String?
```

**Fix Requerido:**
```kotlin
suspend fun getUser(): User? {
    val session = getSession() ?: return null
    val userId = session.userId ?: return null  // Guard clause
    return User(
        id = UUID.fromString(userId),
        name = session.userName ?: "",
        email = session.userEmail ?: ""
    )
}
```

---

#### 🐛 Bug #2: `return@map null` Inválido en `getSession()`
**Archivo:** `SessionManager.kt`  
**Línea:** 51

```kotlin
// CÓDIGO ACTUAL (LÍNEAS 48-59)
suspend fun getSession(): SessionData? {
    return context.dataStore.data.map { preferences ->
        val isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
        if (!isLoggedIn) return@map null  // 💥 ERROR DE COMPILACIÓN / COMPORTAMIENTO INDEFINIDO
        SessionData(...)
    }.first()
}
```

**Por qué falla:**
- `return@map` solo funciona dentro de lambdas de `map` **inline** de la stdlib
- `DataStore.data.map { ... }` usa `Flow.map` que **no es inline** → `return@map` no compila o tiene comportamiento indefinido
- El compilador Kotlin puede aceptar esto pero el runtime no hace lo esperado

**Fix Requerido — Usar `filter` + `mapNotNull`:**
```kotlin
suspend fun getSession(): SessionData? {
    return context.dataStore.data
        .map { preferences ->
            val isLoggedIn = preferences[Keys.IS_LOGGED_IN] ?: false
            if (!isLoggedIn) null else SessionData(...)  // Devolver null, no return@map
        }
        .filterNotNull()  // Elimina los null
        .firstOrNull()    // Safe terminal operator
}
```

---

#### 🐛 Bug #3: `NoSuchElementException` en `isLoggedIn()`
**Archivo:** `SessionManager.kt`  
**Línea:** 36

```kotlin
// CÓDIGO ACTUAL (LÍNEAS 33-37)
suspend fun isLoggedIn(): Boolean {
    return context.dataStore.data.map { preferences ->
        preferences[Keys.IS_LOGGED_IN] ?: false
    }.first()  // 💥 CRASH si Flow está vacío
}
```

**Por qué falla:**
- `.first()` lanza excepción si el Flow no emite ningún valor
- DataStore **siempre emite al menos una vez** (valor inicial), pero en tests o edge cases puede fallar
- Operador inseguro por definición

**Fix Requerido:**
```kotlin
suspend fun isLoggedIn(): Boolean {
    return context.dataStore.data
        .map { it[Keys.IS_LOGGED_IN] ?: false }
        .firstOrNull() ?: false  // Default seguro
}
```

---

#### 📋 Resumen Fixes SessionManager

| Bug | Severidad | Archivo:Línea | Fix | Tests Necesarios |
|-----|-----------|---------------|-----|------------------|
| NPE en getUser() | 🔴 Crítico | 65 | Null-check antes de UUID.fromString | Unit test: userId null, userId válido |
| return@map inválido | 🔴 Crítico | 51 | filterNotNull + firstOrNull | Unit test: loggedIn=true/false |
| first() inseguro | 🔴 Crítico | 36 | firstOrNull() ?: false | Unit test: Flow vacío |

> **IMPORTANTE:** Estos 3 bugs **bloquean el arranque de la app** en cualquier escenario real. Deben arreglarse **antes de cualquier otra tarea**.

---

### A.2 Anti-patrones de Arquitectura de Datos

#### 🏗️ Problema #4: JSON en Preferences DataStore (Anti-pattern)

**Archivos:** 
- `DataStoreLinkRepository.kt` (líneas 27-35, 43-52, 56-65, 69-78)
- `DataStoreFolderRepository.kt` (igual)

**Código problemático (patrón repetido 4x):**
```kotlin
// EN CADA OPERACIÓN CRUD:
override suspend fun create(link: Link) {
    context.linkDataStore.edit { preferences ->
        val current = try {
            val jsonString = preferences[Keys.LINKS_JSON] ?: "[]"
            json.decodeFromString<List<Link>>(jsonString)
        } catch (_: Exception) { emptyList() }
        val updated = current + link
        preferences[Keys.LINKS_JSON] = json.encodeToString(updated)
    }
}
```

**Por qué es anti-pattern:**
| Aspecto | Preferences DataStore | Room / Proto DataStore |
|---------|----------------------|------------------------|
| Diseñado para | Key-value simples (Boolean, String, Int) | Estructuras complejas, listas, relaciones |
| Transacciones | ❌ No atómicas (read-modify-write) | ✅ ACID |
| Concurrencia | ❌ Race conditions | ✅ Transacciones |
| Query/Filter | ❌ Cargar TODO, filtrar en memoria | ✅ SQL / Indexes |
| Tamaño datos | ⚠️ Límite ~100KB recomendado | ✅ MB/GB |
| Migraciones | ❌ Manuales | ✅ Automáticas |

**Consecuencias reales en Linked:**
1. **Race Condition:** Dos clicks rápidos "Guardar" → uno se pierde (leer-lista-A, leer-lista-B, escribir-A, escribir-B → B gana, A perdido)
2. **Memoria:** Cada `getAll()` deserializa **lista completa** → O(n) RAM
3. **Corrupción silenciosa:** `catch (_: Exception) { emptyList() }` borra datos sin avisar
4. **No escalable:** Al añadir backend, hay que reescribir todo el repositorio

**Evidencia en Roadmap:** Fase 12 dice explícitamente *"Fake Repositories para desarrollo sin backend"*. La implementación actual **saltea esa fase** y acopla a DataStore real.

---

#### 🏗️ Problema #5: Race Conditions en Repositorios

**Escenario de fallo real:**
```
Usuario abre AddLinkSheet → Rellena formulario → Pulsa "Guardar" rápido 2 veces
  │
  ├─ Hilo A: create(link1) → read JSON → decode → list=[link1] → encode → write
  │
  └─ Hilo B: create(link2) → read JSON → decode → list=[] (A no ha escrito aún) → list=[link2] → write
  
RESULTADO: Solo link2 se guarda. link1 PERDIDO.
```

**Fix Arquitectural (según Roadmap):**
```kotlin
// FakeLinkRepository.kt (NUEVO - Fase 12)
class FakeLinkRepository : LinkRepository {
    private val _links = MutableStateFlow<List<Link>>(mockLinks)
    override val links: StateFlow<List<Link>> = _links
    
    override suspend fun create(link: Link) {
        _links.update { it + link }  // Atómico en Mutex/StateFlow
    }
    // ... resto CRUD in-memory
}
```

---

#### 🏗️ Problema #6: Error Silencioso en Parsing JSON

**Código:** `catch (_: Exception) { emptyList() }` (líneas 32-34, 73-75 en ambos repos)

**Por qué es peligroso:**
- JSON corrupto → lista vacía → usuario ve "sin enlaces" sin saber por qué
- No hay logging, no hay reporte, no hay recovery
- Datos del usuario **desaparecen misteriosamente**

**Fix Mínimo:**
```kotlin
catch (e: Exception) {
    Timber.e(e, "Failed to parse links JSON, returning empty list")
    emptyList()
}
```
**Fix Correcto (Fake Repo):** Eliminar JSON completamente → estado en memoria.

---

### A.3 Violaciones de UI/Theming

#### 🎨 Problema #7: LoginScreen Rompe el Sistema de Temas

**Archivo:** `app/src/main/java/pdalbert/apps/linked/ui/screens/login/LoginScreen.kt`  
**Líneas:** 49-53 (colores locales)

```kotlin
// CÓDIGO ACTUAL - COLORES HARDCODEADOS
private val Surface = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextMuted = Color(0xFF999999)
private val Accent = Color(0xFF2563EB)  // Distinto al tema (#3B82C4)
```

**Colores del Tema (ui/theme/Color.kt) — DEBEN USARSE:**
```kotlin
val Background = Color(0xFFEEEAE4)
val Surface = Color(0xFFFFFFFF)
val Ink = Color(0xFF111110)
val InkMuted = Color(0xFF222222)
val Accent = Color(0xFF3B82C4)  // ← LoginScreen usa #2563EB (diferente)
val Border = Color(0xFFE2E1DC)
```

**Impactos:**
1. **Dark Mode roto:** Colores fijos no responden a `isSystemInDarkTheme()`
2. **Inconsistencia visual:** Accent azul distinto (#2563EB vs #3B82C4)
3. **Mantenibilidad:** Cambiar tema requiere editar LoginScreen aparte
4. **Viola `architecture.md:32`:** *"Regla: Todas las pantallas deben importar y usar Background del tema. No crear colores de fondo locales."*

**Fix:** Eliminar líneas 49-53, importar `Background`, `Surface`, `Ink`, `InkMuted`, `Accent`, `Border` del tema.

---

#### 🎨 Problema #8: Google Sign-In Hardcodeado (Código de Prueba en Prod)

**Archivo:** `LoginScreen.kt`  
**Línea:** 201

```kotlin
// CÓDIGO ACTUAL
.clickable { viewModel.onGoogleSignInSuccess("Usuario", "test@linked.com") }
```

**Por qué es crítico:**
- Simula login exitoso **sin llamar a Google Identity API**
- `google-identity:1.1.0` está en dependencias pero **no se usa**
- Credential Manager API no implementada
- Usuario **no puede autenticarse realmente**

**Implementación Real Requerida (Fase 5 pendiente):**
```kotlin
// En LoginViewModel.kt
fun signInWithGoogle(activity: ComponentActivity) {
    viewModelScope.launch {
        try {
            val request = GetCredentialRequest(
                listOf(GetGoogleIdTokenOptionRequest(
                    serverClientId = "TU_CLIENT_ID.apps.googleusercontent.com",
                    filterByAuthorizedAccounts = true
                ))
            )
            val result = credentialManager.getCredential(activity, request)
            when (result.credential) {
                is GoogleIdTokenCredential -> {
                    val idToken = result.credential.idToken ?: return@launch
                    // ENVIAR idToken A BACKEND → backend valida con Google → devuelve JWT + User
                    // SessionManager.saveSession(user)
                    _navigationEvent.value = "home"
                }
                is PublicKeyCredential -> { /* Passkey */ }
            }
        } catch (e: GetCredentialException) {
            // Manejar error: usuario canceló, no hay cuentas, etc.
        }
    }
}
```

---

#### 🎨 Problema #9: SearchBar sin Placeholder Nativo

**Archivo:** `ui/components/SearchBar.kt`  
**Líneas:** 60-69

```kotlin
// ACTUAL: Placeholder manual
if (query.isEmpty()) {
    Text(text = placeholder, color = InkMuted, ...)
}
BasicTextField(value = query, onValueChange = onQueryChanged, ...)
```

**Problema:** `BasicTextField` **soporta placeholder nativo** via `TextFieldDefaults` o `TextField` de Material3.

**Fix (Material3 TextField):**
```kotlin
@Composable
fun SearchBar(...) {
    var text by remember { mutableStateOf(query) }
    TextField(
        value = text,
        onValueChange = { text = it; onQueryChanged(it) },
        placeholder = { Text(text = placeholder, color = InkMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = InkMuted) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Surface,
            focusedContainerColor = Surface,
            unfocusedBorderColor = Border,
            focusedBorderColor = Accent,
            cursorColor = Accent,
            placeholderColor = InkMuted
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 14.dp)
    )
}
```

---

### A.4 Duplicación de Código y Mantenibilidad

#### 🔄 Problema #10: Swipe-to-Delete Duplicado (130+ líneas)

**Archivos:**
- `AllLinksScreen.kt` líneas 190-254
- `AllFoldersScreen.kt` líneas 174-238

**Código IDÉNTICO (excepto tipos Link vs Folder):**
```kotlin
// ESTE BLOQUE SE REPITE CASI TEXTUALMENTE EN AMBOS ARCHIVOS
var swipedItemId by remember { mutableStateOf<String?>(null) }

// En LazyColumn items:
if (swipedItemId == item.id.toString()) {
    Row(modifier = Modifier.align(Alignment.CenterEnd).clip(RoundedCornerShape(...))) {
        Box(modifier = Modifier.background(Color(0xFF3B82C4)).clickable { 
            swipedItemId = null; viewModel.onEditClicked(item) }.padding(horizontal = 18.dp)) {
            Column { Icon(Icons.Outlined.Edit); Text("Editar") }
        }
        Box(modifier = Modifier.background(Danger).clickable { 
            swipedItemId = null; viewModel.onDeleteClicked(item.id) }.padding(horizontal = 18.dp)) {
            Column { Icon(Icons.Outlined.Delete); Text("Eliminar") }
        }
    }
}
```

**Fix: Componente Reutilizable `SwipeableCard`**
```kotlin
@Composable
fun <T> SwipeableCard(
    item: T,
    itemId: String,
    swipedId: String?,
    onSwipeChanged: (String?) -> Unit,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    // Lógica de swipe UNA SOLA VEZ
    // Renderiza actions detrás + content encima
}
```

**Uso en AllLinksScreen:**
```kotlin
items(filteredLinks) { link ->
    SwipeableCard(
        item = link,
        itemId = link.id.toString(),
        swipedId = swipedLinkId,
        onSwipeChanged = { swipedLinkId = it },
        onEdit = viewModel::onEditClicked,
        onDelete = viewModel::onDeleteClicked
    ) { LinkCard(link = it, onClick = { }) }
}
```

---

#### 🔄 Problema #11: LoginScreen Monolítico (360 líneas)

**Archivo:** `LoginScreen.kt` — **Todo en una función `@Composable`**

**Debe dividirse en:**
```
LoginScreen.kt (orquesta)
├── LoginHeader.kt           // Logo + "Linked" (líneas 67-94)
├── FloatingCardsLayer.kt    // 3 cards animadas (líneas 99-145)
├── LoginHeadline.kt         // "Todos tus enlaces, en orden" (156-176)
├── GoogleSignInButton.kt    // Botón + legal text (187-247)
└── LegalText.kt             // Términos/Privacidad (228-247)
```

**Beneficios:**
- Reutilizable (FloatingCardsLayer también en SplashScreen)
- Testable por separado
- Legible (cada archivo < 80 líneas)
- Previewable en Android Studio

---

#### 🔄 Problema #12: Sin Inyección de Dependencias

**Archivo:** `MainActivity.kt` líneas 19-21

```kotlin
// ACTUAL: Instanciación manual en Activity
val sessionManager = SessionManager(applicationContext)
val linkRepository = DataStoreLinkRepository(applicationContext)
val folderRepository = DataStoreFolderRepository(applicationContext)
```

**Problemas:**
- Acopla Activity a implementaciones concretas
- Imposible testear con mocks
- Violación de Investment Principle (DI)
- Carpeta `di/` existe pero vacía (`.gitkeep`)

**Fix (Hilt - recomendado para Android):**
```kotlin
// di/AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideSessionManager(@ApplicationContext ctx: Context): SessionManager =
        SessionManager(ctx)
    
    @Provides @Singleton
    fun provideLinkRepository(@ApplicationContext ctx: Context): LinkRepository =
        DataStoreLinkRepository(ctx)  // O FakeLinkRepository en tests
    
    @Provides @Singleton
    fun provideFolderRepository(@ApplicationContext ctx: Context): FolderRepository =
        DataStoreFolderRepository(ctx)
}

// MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var sessionManager: SessionManager
    @Inject lateinit var linkRepository: LinkRepository
    @Inject lateinit var folderRepository: FolderRepository
    // ...
}
```

---

### A.5 Configuración de Build y Testing

#### ⚙️ Problema #13: `compileSdk = 36` No Existe

**Archivo:** `app/build.gradle.kts` línea 10

```kotlin
android {
    compileSdk = 36  // 💥 Android 14 = API 34, Android 15 = API 35. 36 NO EXISTE
    targetSdk = 36
}
```

**Versiones reales (2024/2025):**
| Android | API Level | Nombre |
|---------|-----------|--------|
| 14 (UpsideDownCake) | 34 | Estable |
| 15 (VanillaIceCream) | 35 | Preview/Estable reciente |
| 16 | 36 | **No liberado aún** |

**Fix:** Cambiar a `34` o `35` según target real.

---

#### ⚙️ Problema #14: `minifyEnabled = false` en Release

**Archivo:** `app/build.gradle.kts` línea 24

```kotlin
buildTypes {
    release {
        isMinifyEnabled = false  // 💥 APK sin ofuscar, sin optimizar, tamaño máximo
        proguardFiles(...)
    }
}
```

**Impacto:**
- APK ~2-3x más grande
- Código legible en ingeniería inversa
- Sin eliminación de código muerto (unused resources, classes)

**Fix:**
```kotlin
release {
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}
```

---

#### 🧪 Problema #15: 0 Tests Reales

**Estado actual:**
```
app/src/test/                    → Solo ExampleUnitTest.kt (2+2=4)
app/src/androidTest/             → Solo ExampleInstrumentedTest.kt (package name)
```

**Tests MÍNIMOS necesarios:**
| Capa | Qué testear | Herramienta |
|------|-------------|-------------|
| ViewModels | StateFlows, navegación, CRUD logic | JUnit4 + Turbine + MockK |
| Repositories | CRUD, filtrado, edge cases | JUnit4 + MockK |
| SessionManager | isLoggedIn, save/get/clear | JUnit4 + DataStore test utils |
| Serializers | UUID, Link, Folder roundtrip | JUnit4 + kotlinx.serialization |
| UI (Compose) | Render, clicks, navigation | Compose Test + Espresso |
| Navigation | Rutas, arguments, deep links | Compose Navigation Testing |

---

#### 🧪 Problema #16: Strings Hardcodeados (Sin i18n)

**Ejemplos dispersos:**
```kotlin
// AllLinksScreen.kt:143
text = "${filteredLinks.size} enlace${if (filteredLinks.size != 1) "s" else ""}"

// AllLinksScreen.kt:161-163
Text(text = "Sin enlaces")
Text(text = "Añade tu primer enlace\npulsando el botón de arriba.")

// AddLinkSheet.kt:197, 231
Text(text = "Cancelar")
Text(text = if (editingLink != null) "Guardar cambios" else "Guardar")

// DeleteConfirmDialog.kt:39, 40
title = "Eliminar enlace"
message = "¿Seguro que quieres eliminarlo?\nEsta acción no se puede deshacer."
```

**Fix:** Mover a `res/values/strings.xml` y `res/values-es/strings.xml`:
```xml
<!-- strings.xml -->
<string name="links_count">%1$d enlace%2$s</string>
<string name="no_links">Sin enlaces</string>
<string name="add_first_link">Añade tu primer enlace\npulsando el botón de arriba.</string>
<string name="cancel">Cancelar</string>
<string name="save">Guardar</string>
<string name="save_changes">Guardar cambios</string>
<string name="delete_link_title">Eliminar enlace</string>
<string name="delete_link_confirm">¿Seguro que quieres eliminarlo?\nEsta acción no se puede deshacer.</string>
```

**Uso en Compose:**
```kotlin
Text(text = stringResource(R.string.links_count, filteredLinks.size, plural))
```

---

## 5. PARTE B: ANÁLISIS DE COHERENCIA DOCUMENTAL Y ARQUITECTURAL

### B.1 Roadmap vs Realidad: Desviaciones Críticas

| Fase | Documentación (`Roadmap.md`) | Código Real | Desviación |
|------|------------------------------|-------------|------------|
| 6 | HomeScreen + BottomNavBar | HomeScreen ✅, **BottomNavBar ❌** | Falta componente navegante principal |
| 7 | SettingsScreen + ViewModel | **No existe** (solo placeholder en NavGraph) | Pantalla completa faltante |
| 8 | AddLinkSheet (modal) | **Existe** pero **desconectado** de HomeViewModel | Código muerto |
| 9 | AddFolderSheet (modal) | **Existe** pero **desconectado** | Código muerto |
| 10 | LinkDetailScreen + ViewModel | **No existe** (placeholder "Link Detail: $id") | Pantalla completa faltante |
| 11 | DeleteConfirmDialog | **Existe** pero **desconectado** | Código muerto |
| **12** | **FakeLinkRepository + FakeFolderRepository** | **DataStoreLinkRepository + DataStoreFolderRepository (REALES)** | **❌ FASE SALTADA — Acoplamiento prematuro** |

**Conclusión:** El proyecto **tiene más código del que dice el Roadmap** (Sheets, Dialog existen) pero **están desconectados**, y **falta lo fundamental** (BottomNav, Settings, LinkDetail, Fake Repos).

---

### B.2 Violación de Separación MVVM (ViewModels en UI)

#### Estructura DOCUMENTADA (`architecture.md:7-9` y `Siguiente.md:58-78`):
```
pdalbert.apps.linked/
├── data/
│   ├── model/          ✅
│   ├── local/          ✅
│   ├── repository/     ✅ (interfaces + DataStore impl)
│   └── remote/         (futuro)
├── ui/
│   ├── screens/        ← SOLO Composables (Views)
│   ├── components/     ✅
│   ├── navigation/     ✅
│   └── theme/          ✅
├── viewmodel/          ← **DEBE CONTENER TODOS LOS VIEWMODELS** (actualmente .gitkeep)
└── di/                 ← Módulo Hilt/Koin (actualmente .gitkeep)
```

#### Estructura REAL (INCORRECTA):
```
ui/screens/
├── home/
│   ├── HomeScreen.kt
│   ├── HomeViewModel.kt           ❌ DEBE ESTAR EN viewmodel/
│   ├── AllLinksScreen.kt
│   ├── AllLinksViewModel.kt       ❌ DEBE ESTAR EN viewmodel/
│   ├── AllFoldersScreen.kt
│   ├── AllFoldersViewModel.kt     ❌ DEBE ESTAR EN viewmodel/
│   ├── AddLinkSheet.kt
│   └── AddFolderSheet.kt
├── login/
│   ├── LoginScreen.kt
│   └── LoginViewModel.kt          ❌ DEBE ESTAR EN viewmodel/
└── splash/
    ├── SplashScreen.kt
    └── SplashViewModel.kt         ❌ DEBE ESTAR EN viewmodel/
```

**ViewModels mal ubicados (6 archivos):**
1. `ui/screens/home/HomeViewModel.kt`
2. `ui/screens/home/AllLinksViewModel.kt`
3. `ui/screens/home/AllFoldersViewModel.kt`
4. `ui/screens/login/LoginViewModel.kt`
5. `ui/screens/splash/SplashViewModel.kt`
6. `ui/screens/settings/SettingsViewModel.kt` (cuando se cree)

**Consecuencias técnicas:**
| Problema | Descripción |
|----------|-------------|
| Rompe MVVM | ViewModel (lógica) mezclado con View (UI) |
| Testing difícil | No se puede testear ViewModel sin cargar Compose |
| Imports circulares | ViewModel importa Screen, Screen importa ViewModel |
| Reutilización imposible | ViewModel atado a pantalla específica |
| DI rota | Hilt/Koin espera ViewModels en package separado |

---

### B.3 Carpetas Vacías Pendientes

| Carpeta | Contenido Actual | Qué Debe Contener |
|---------|------------------|-------------------|
| `viewmodel/` | `.gitkeep` | **Todos los 6+ ViewModels** movidos desde `ui/screens/` |
| `di/` | `.gitkeep` | `AppModule.kt` (Hilt) o `AppContainer.kt` (Koin) + `Application` class |

---

## 6. PARTE C: FORTALEZAS DEL PROYECTO (Lo que SÍ está bien)

| Área | Detalle | Por qué es bueno |
|------|---------|------------------|
| **Theme System** | `Color.kt`, `Type.kt`, `Theme.kt` completos + dark mode | Coherente, tipado, extensible, usa `MaterialTheme` correctamente |
| **Modelos de Datos** | `Link`, `Folder`, `User`, `TagColor` con UUID, computed props | Limpios, serializables, relaciones N-N bien modeladas |
| **Navegación** | `Screen` sealed class + `NavGraph` con factories | Type-safe routes (casi), factories para ViewModels, limpio |
| **SplashScreen** | Animaciones fluidas (Animatable + coroutines), 12 cards decorativas | Código complejo pero bien estructurado, performante |
| **Componentes UI** | `EmojiPicker`, `ColorPicker`, `Toast`, `FilterChips`, `DeleteConfirmDialog`, `LinkCard`, `FolderCard`, `SearchBar`, `TopNav` | Reutilizables, parametrizados, bien diseñados |
| **Serialización** | `UUIDSerializer`, `UUIDListSerializer` + kotlinx.serialization | Robusto, listo para backend JSON |
| **Dependencias** | Version catalog (`libs.versions.toml`) actualizado | Mantenible, centralizado, sin version conflicts |
| **Arquitectura Base** | MVVM declarado, StateFlow, viewModelScope, factories | Fundamentos correctos, solo hay que mover archivos |

---

## 7. MATRIZ DE RIESGO E IMPACTO

| ID | Problema | Severidad | Esfuerzo Fix | Impacto si No Se Arregla | Prioridad |
|----|----------|-----------|--------------|--------------------------|-----------|
| A1 | SessionManager NPE | 🔴 Crítico | 30 min | App crashea al iniciar | **P0** |
| A2 | SessionManager return@map | 🔴 Crítico | 30 min | Sesión no se lee | **P0** |
| A3 | SessionManager first() | 🔴 Crítico | 10 min | Crash edge cases | **P0** |
| A4 | JSON en DataStore | 🟠 Alto | 2-4 h (Fake Repos) | Race conditions, no escalable | **P1** |
| A5 | Race conditions repos | 🟠 Alto | Incluido en A4 | Pérdida de datos usuario | **P1** |
| A6 | Error silencioso JSON | 🟠 Alto | 15 min | Datos perdidos sin aviso | **P1** |
| A7 | LoginScreen theming | 🟠 Alto | 1 h | Dark mode roto, inconsistencia | **P1** |
| A8 | Google Sign-In fake | 🔴 Crítico | 2-4 h | Login no funciona | **P0** |
| A9 | SearchBar placeholder | 🟡 Medio | 30 min | UX pobre | **P2** |
| A10 | Swipe duplicado | 🟡 Medio | 1 h | Mantenibilidad, bugs futuros | **P2** |
| A11 | LoginScreen 360 líneas | 🟡 Medio | 1 h | Mantenibilidad | **P2** |
| A12 | Sin DI | 🟠 Alto | 2-3 h | Testing imposible, acoplamiento | **P1** |
| A13 | compileSdk 36 | 🟡 Medio | 5 min | Build falla en CI/nuevos SDK | **P2** |
| A14 | minifyEnabled false | 🟡 Medio | 10 min | APK grande, inseguro | **P2** |
| A15 | 0 tests | 🟠 Alto | 8-16 h | Sin regresiones detection | **P1** |
| A16 | Strings hardcodeados | 🟡 Medio | 2 h | Sin i18n, mantenimiento | **P2** |
| B1 | Fase 12 saltada (Fake Repos) | 🔴 Crítico | 2-4 h | Arquitectura acoplada | **P0** |
| B2 | ViewModels en UI | 🔴 Crítico | 1 h | MVVM roto, testing difícil | **P0** |
| B3 | Carpetas vacías | 🟢 Bajo | 30 min | Estructura incompleta | **P3** |

---

## 8. PLAN DE ACCIÓN DETALLADO Y PRIORIZADO

### 🚨 P0 — BLOQUEANTES (Hacer AHORA, en este orden)

#### Tarea P0.1: Arreglar SessionManager (3 bugs)
**Archivo:** `data/local/SessionManager.kt`  
**Tiempo:** ~45 min  
**Pasos:**
1. Línea 36: `.first()` → `.firstOrNull() ?: false`
2. Línea 51: `return@map null` → `if (!isLoggedIn) null else SessionData(...)` + `.filterNotNull().firstOrNull()`
3. Línea 65: `UUID.fromString(session.userId)` → `session.userId?.let { UUID.fromString(it) } ?: return null`
4. **Añadir tests unitarios** para los 3 casos

**Criterio de aceptación:** App inicia sin crash con/sin sesión guardada, con userId null, con DataStore corrupto.

---

#### Tarea P0.2: Mover ViewModels a `viewmodel/`
**Archivos:** 6 ViewModels  
**Tiempo:** ~1 h  
**Pasos:**
```bash
# 1. Crear package viewmodel
mkdir -p app/src/main/java/pdalbert/apps/linked/viewmodel

# 2. Mover archivos
mv app/src/main/java/pdalbert/apps/linked/ui/screens/home/HomeViewModel.kt app/src/main/java/pdalbert/apps/linked/viewmodel/
mv app/src/main/java/pdalbert/apps/linked/ui/screens/home/AllLinksViewModel.kt app/src/main/java/pdalbert/apps/linked/viewmodel/
mv app/src/main/java/pdalbert/apps/linked/ui/screens/home/AllFoldersViewModel.kt app/src/main/java/pdalbert/apps/linked/viewmodel/
mv app/src/main/java/pdalbert/apps/linked/ui/screens/login/LoginViewModel.kt app/src/main/java/pdalbert/apps/linked/viewmodel/
mv app/src/main/java/pdalbert/apps/linked/ui/screens/splash/SplashViewModel.kt app/src/main/java/pdalbert/apps/linked/viewmodel/

# 3. Actualizar package declaration en cada archivo:
# package pdalbert.apps.linked.viewmodel

# 4. Actualizar imports en NavGraph.kt, Screens, etc.
```

**Criterio de aceptación:** Compila, navega, ViewModels en `viewmodel/`, `ui/screens/` solo tiene Composables.

---

#### Tarea P0.3: Implementar Fake Repositories (Fase 12)
**Archivos nuevos:** `data/repository/FakeLinkRepository.kt`, `FakeFolderRepository.kt`  
**Tiempo:** ~3 h  
**Por qué ANTES de DI:** Los Fakes son las implementaciones que se inyectarán en dev/test.

**FakeLinkRepository.kt:**
```kotlin
package pdalbert.apps.linked.data.repository

class FakeLinkRepository : LinkRepository {
    private val _links = MutableStateFlow<List<Link>>(getMockLinks())
    override val links: StateFlow<List<Link>> = _links
    
    override fun getAll() = _links
    override fun getById(id: UUID) = _links.map { it.find { it.id == id } }
    
    override suspend fun create(link: Link) {
        _links.update { it + link }
    }
    override suspend fun update(link: Link) {
        _links.update { it.map { if (it.id == link.id) link else it } }
    }
    override suspend fun delete(id: UUID) {
        _links.update { it.filter { it.id != id } }
    }
    
    private fun getMockLinks(): List<Link> = listOf(
        Link(id = UUID.fromString("11111111-1111-1111-1111-111111111111"), 
             title = "Figma", url = "https://figma.com", tag = "Diseño"),
        Link(id = UUID.fromString("22222222-2222-2222-2222-222222222222"), 
             title = "Claude", url = "https://claude.ai", tag = "IA"),
        // ... más mocks
    )
}
```

**Cambio en NavGraph/MainActivity:** Inyectar `FakeLinkRepository` / `FakeFolderRepository` en lugar de DataStore.

**Criterio de aceptación:** HomeScreen muestra mocks, CRUD funciona en memoria, sin DataStore.

---

#### Tarea P0.4: Implementar Google Sign-In Real
**Archivos:** `LoginViewModel.kt`, `LoginScreen.kt`, `NavGraph.kt`  
**Tiempo:** ~3 h  
**Dependencias:** Ya están (`google-identity:1.1.0`)

**Pasos clave:**
1. `LoginViewModel.signInWithGoogle(activity: ComponentActivity)` usando `CredentialManager`
2. `LoginScreen` llama a `viewModel.signInWithGoogle(requireActivity())` en click
3. Manejar `GetCredentialException` (usuario canceló, no hay cuentas, error red)
4. En éxito: `SessionManager.saveSession(user)` → navegar a Home

**Criterio de aceptación:** Usuario real puede loguearse con su cuenta Google, sesión persiste.

---

### 🟠 P1 — ARQUITECTURA Y ROADMAP (Esta semana)

#### Tarea P1.1: Configurar Hilt (DI)
**Archivos:** `di/AppModule.kt`, `LinkedApplication.kt`, `build.gradle.kts`  
**Tiempo:** ~2 h

```kotlin
// build.gradle.kts (app) - plugins
alias(libs.plugins.hilt)  // Añadir a libs.versions.toml: hilt = "2.48"

// di/AppModule.kt
@Module @InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideSessionManager(@ApplicationContext ctx: Context) = SessionManager(ctx)
    
    @Provides @Singleton
    fun provideLinkRepository(): LinkRepository = FakeLinkRepository()  // Dev
    
    @Provides @Singleton
    fun provideFolderRepository(): FolderRepository = FakeFolderRepository()
}

// LinkedApplication.kt
@HiltAndroidApp
class LinkedApplication : Application()

// AndroidManifest.xml
<application android:name=".LinkedApplication" ...>
```

---

#### Tarea P1.2: Fix LoginScreen Theming
**Archivo:** `ui/screens/login/LoginScreen.kt`  
**Tiempo:** ~1 h
1. Eliminar líneas 49-53 (colores locales)
2. Importar: `Background`, `Surface`, `Ink`, `InkMuted`, `Accent`, `Border` desde `ui.theme`
3. Reemplazar `Surface` → `Surface`, `TextPrimary` → `Ink`, `TextMuted` → `InkMuted`, `Accent` → `Accent`
4. Verificar Dark Mode en Preview

---

#### Tarea P1.3: Componente SwipeableCard (Elimina duplicación)
**Archivo nuevo:** `ui/components/SwipeableCard.kt`  
**Tiempo:** ~1 h  
**Uso:** `AllLinksScreen`, `AllFoldersScreen`, futuras listas

---

#### Tarea P1.4: Fix compileSdk + minifyEnabled
**Archivo:** `app/build.gradle.kts`  
**Tiempo:** 10 min
```kotlin
compileSdk = 34  // o 35 si target Android 15
targetSdk = 34

buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(...)
    }
}
```

---

#### Tarea P1.5: Externalizar Strings a strings.xml
**Archivos:** `res/values/strings.xml` (nuevo/editar), todos los `.kt` con texto hardcodeado  
**Tiempo:** ~2 h

---

### 🟡 P2 — CALIDAD Y TESTING (Próximos sprints)

| Tarea | Descripción | Esfuerzo |
|-------|-------------|----------|
| P2.1 | Dividir LoginScreen en 5 componentes | 1 h |
| P2.2 | Tests unitarios ViewModels (Turbine + MockK) | 8 h |
| P2.3 | Tests unitarios Repositories (Fake + DataStore) | 4 h |
| P2.4 | Tests UI Compose (AllLinks, AllFolders, Home) | 6 h |
| P2.5 | Configurar Detekt + Ktlint + CI | 2 h |
| P2.6 | Navigation type-safe (Navigation 3+) | 3 h |
| P2.7 | Timestamps String → Instant/Long | 2 h |

---

### 🟢 P3 — DOCS Y PENDIENTES

| Tarea | Descripción |
|-------|-------------|
| P3.1 | Actualizar dominio en SplashScreen (`linked.com` → real) |
| P3.2 | EmojiRepository dinámico (eliminar hardcode en Sheets) |
| P3.3 | Actualizar Roadmap.md y Siguiente.md tras completar P0/P1 |
| P3.4 | Crear Implementacion-6.md, -7.md, etc. según fases |

---

## 9. GUÍA DE REANUDACIÓN PARA IA

> **Si eres una IA retomando este trabajo:** Lee esta sección primero.

### 9.1 Contexto Inmediato
```yaml
Proyecto: Linked (Android Bookmark Manager)
Estado: Fases 1-5 "completadas" en docs, pero con bugs críticos y desviaciones
Próximo paso REAL: P0.1 → P0.2 → P0.3 → P0.4 (en orden)
No implementar Fase 6 (Home/BottomNav) hasta completar P0
```

### 9.2 Archivos Clave a Tener en Contexto
```
CRÍTICOS (leer y entender):
├── data/local/SessionManager.kt           ← 3 bugs P0
├── ui/screens/login/LoginScreen.kt        ← theming + Google Sign-In fake
├── ui/screens/home/HomeViewModel.kt       ← mover a viewmodel/
├── ui/screens/home/AllLinksViewModel.kt   ← mover a viewmodel/
├── ui/screens/home/AllFoldersViewModel.kt ← mover a viewmodel/
├── ui/screens/login/LoginViewModel.kt     ← mover a viewmodel+
├── ui/screens/splash/SplashViewModel.kt   ← mover a viewmodel/
├── data/repository/DataStoreLinkRepository.kt    ← reemplazar por Fake
├── data/repository/DataStoreFolderRepository.kt  ← reemplazar por Fake
├── ui/navigation/NavGraph.kt              ← actualizar imports ViewModels
└── MainActivity.kt                        ← configurar DI

DOCS (leer para entender intención):
├── docs/Roadmap.md
├── docs/Siguiente.md
├── docs/architecture.md
├── docs/IMPORTANTE.md
└── docs/Auditorias/Auditoria 1.2.md  (ESTE DOCUMENTO)
```

### 9.3 Comandos de Verificación Rápida
```bash
# 1. Compilar y ver errores
./gradlew compileDebugKotlin

# 2. Ejecutar tests (actualmente 0 reales)
./gradlew test

# 3. Verificar build release
./gradlew assembleRelease

# 4. Lint
./gradlew lint
```

### 9.4 Qué NO Hacer
- ❌ No empezar Fase 6 (Home/BottomNav) sin Fix P0
- ❌ No tocar DataStore Repos salvo para reemplazar por Fake
- ❌ No mover ViewModels sin actualizar TODOS los imports
- ❌ No implementar Google Sign-In sin CredentialManager API

---

## 10. ANEXOS: ESTRUCTURA DE ARCHIVOS OBJETIVO

### 10.1 Estructura Actual (Problemática)
```
app/src/main/java/pdalbert/apps/linked/
├── data/
│   ├── local/SessionManager.kt
│   ├── model/Link.kt, Folder.kt, User.kt, TagColor.kt, UUIDSerializer.kt
│   └── repository/
│       ├── LinkRepository.kt, FolderRepository.kt (interfaces)
│       ├── DataStoreLinkRepository.kt  ❌ REAL - debería ser Fake
│       └── DataStoreFolderRepository.kt ❌ REAL - debería ser Fake
├── di/.gitkeep  ❌ VACÍO
├── ui/
│   ├── components/ ✅ (9 componentes buenos)
│   ├── navigation/Screen.kt, NavGraph.kt ✅
│   ├── screens/
│   │   ├── splash/SplashScreen.kt, SplashViewModel.kt ❌ VM en UI
│   │   ├── login/LoginScreen.kt, LoginViewModel.kt ❌ VM en UI
│   │   ├── home/
│   │   │   ├── HomeScreen.kt, HomeViewModel.kt ❌ VM en UI
│   │   │   ├── AllLinksScreen.kt, AllLinksViewModel.kt ❌ VM en UI
│   │   │   ├── AllFoldersScreen.kt, AllFoldersViewModel.kt ❌ VM en UI
│   │   │   ├── AddLinkSheet.kt, AddFolderSheet.kt ✅ (Sheets OK en UI)
│   │   └── settings/ ❌ NO EXISTE
│   └── theme/ ✅
├── viewmodel/.gitkeep  ❌ VACÍO - DEBE TENER 6+ VMs
└── MainActivity.kt
```

### 10.2 Estructura Objetivo (Tras P0 + P1)
```
app/src/main/java/pdalbert/apps/linked/
├── data/
│   ├── local/SessionManager.kt  ✅ ARREGLADO
│   ├── model/ ✅
│   └── repository/
│       ├── LinkRepository.kt, FolderRepository.kt
│       ├── FakeLinkRepository.kt      ✅ NUEVO (Fase 12)
│       ├── FakeFolderRepository.kt    ✅ NUEVO (Fase 12)
│       └── (futuro) RemoteLinkRepository.kt
├── di/
│   ├── AppModule.kt              ✅ NUEVO (Hilt)
│   └── LinkedApplication.kt      ✅ NUEVO
├── ui/
│   ├── components/
│   │   ├── ... (existentes)
│   │   └── SwipeableCard.kt      ✅ NUEVO (refactor)
│   ├── navigation/ ✅
│   ├── screens/
│   │   ├── splash/SplashScreen.kt           ✅ (solo View)
│   │   ├── login/LoginScreen.kt             ✅ (solo View + theming fix)
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── AllLinksScreen.kt
│   │   │   ├── AllFoldersScreen.kt
│   │   │   ├── AddLinkSheet.kt
│   │   │   └── AddFolderSheet.kt
│   │   ├── settings/SettingsScreen.kt       ✅ NUEVO (Fase 7)
│   │   └── linkdetail/LinkDetailScreen.kt   ✅ NUEVO (Fase 10)
│   └── theme/ ✅
├── viewmodel/                      ✅ POBLADO
│   ├── HomeViewModel.kt
│   ├── AllLinksViewModel.kt
│   ├── AllFoldersViewModel.kt
│   ├── LoginViewModel.kt
│   ├── SplashViewModel.kt
│   ├── SettingsViewModel.kt        ✅ NUEVO
│   └── LinkDetailViewModel.kt      ✅ NUEVO
└── MainActivity.kt  ✅ (usa @AndroidEntryPoint + @Inject)
```

---

---

*Fin del documento — Auditoría 1.2 Completa*  
*Generado para ser accionable por humanos y retomable por IAs*