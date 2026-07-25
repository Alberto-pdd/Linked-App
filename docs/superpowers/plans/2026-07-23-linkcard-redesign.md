# LinkCard Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Mejorar el componente LinkCard mostrando el dominio del URL junto al tiempo transcurrido, manteniendo el diseño compacto actual.

**Architecture:** Modificar LinkCard.kt para extraer el dominio de la URL usando Uri.parse(), agregar getTimeAgo() a AllLinksViewModel para consistencia, y actualizar AllLinksScreen.kt para pasar el parámetro timeAgo.

**Tech Stack:** Kotlin, Jetpack Compose, AndroidX, Hilt

---

## File Structure

**Modified Files:**
- `app/src/main/java/pdalbert/apps/linked/ui/components/LinkCard.kt` — Agregar extracción de dominio y mostrar en subtítulo
- `app/src/main/java/pdalbert/apps/linked/viewmodel/AllLinksViewModel.kt` — Agregar función getTimeAgo()
- `app/src/main/java/pdalbert/apps/linked/ui/screens/home/AllLinksScreen.kt` — Pasar timeAgo al LinkCard

---

## Tasks

### Task 1: Actualizar LinkCard.kt con extracción de dominio

**Files:**
- Modify: `app/src/main/java/pdalbert/apps/linked/ui/components/LinkCard.kt:1-149`

- [ ] **Step 1: Agregar import de Uri**

```kotlin
import android.net.Uri
```

Agrega esta línea después del import de `androidx.compose.ui.draw.clip` (línea 21).

- [ ] **Step 2: Agregar extracción de dominio**

Reemplaza las líneas 45-49 (val bgColorParsed) con:

```kotlin
    val bgColorParsed = try {
        Color(android.graphics.Color.parseColor(link.bgColor))
    } catch (_: Exception) {
        Color(0xFFEBF3FB)
    }

    val domain = try {
        Uri.parse(link.url).host?.removePrefix("www.") ?: ""
    } catch (_: Exception) {
        ""
    }
```

- [ ] **Step 3: Actualizar la sección de subtítulo**

Reemplaza las líneas 85-94 (el bloque if de timeAgo) con:

```kotlin
            val subtitle = buildString {
                if (domain.isNotEmpty()) append(domain)
                if (domain.isNotEmpty() && timeAgo.isNotEmpty()) append(" · ")
                if (timeAgo.isNotEmpty()) append(timeAgo)
            }
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = InkDecorations,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
```

- [ ] **Step 4: Verificar que el código compila**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/pdalbert/apps/linked/ui/components/LinkCard.kt
git commit -m "feat: add domain extraction to LinkCard subtitle"
```

---

### Task 2: Agregar getTimeAgo() a AllLinksViewModel

**Files:**
- Modify: `app/src/main/java/pdalbert/apps/linked/viewmodel/AllLinksViewModel.kt:1-129`

- [ ] **Step 1: Agregar import de Instant**

Agrega esta línea en la sección de imports (después de la línea 16):

```kotlin
import kotlinx.datetime.Instant
```

- [ ] **Step 2: Agregar función getTimeAgo**

Agrega esta función al final de la clase AllLinksViewModel (antes de la línea 129):

```kotlin
    fun getTimeAgo(createdAt: Instant): String {
        val now = kotlinx.datetime.Clock.System.now()
        val duration = now - createdAt

        val seconds = duration.inWholeSeconds
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            years > 0 -> if (years == 1L) "hace 1 año" else "hace $years años"
            months > 0 -> if (months == 1L) "hace 1 mes" else "hace $months meses"
            weeks > 0 -> if (weeks == 1L) "hace 1 semana" else "hace $weeks semanas"
            days > 0 -> if (days == 1L) "hace 1 día" else "hace $days días"
            hours > 0 -> if (hours == 1L) "hace 1 hora" else "hace $hours horas"
            minutes > 0 -> if (minutes == 1L) "hace 1 minuto" else "hace $minutes minutos"
            else -> "ahora mismo"
        }
    }
```

- [ ] **Step 3: Verificar que el código compila**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/pdalbert/apps/linked/viewmodel/AllLinksViewModel.kt
git commit -m "feat: add getTimeAgo function to AllLinksViewModel"
```

---

### Task 3: Actualizar AllLinksScreen.kt para pasar timeAgo

**Files:**
- Modify: `app/src/main/java/pdalbert/apps/linked/ui/screens/home/AllLinksScreen.kt:256-265`

- [ ] **Step 1: Actualizar la llamada a LinkCard**

Reemplaza las líneas 256-265:

```kotlin
                            LinkCard(
                                link = link,
                                onClick = {
                                    if (swipedLinkId == link.id.toString()) {
                                        swipedLinkId = null
                                    } else if (swipedLinkId != null) {
                                        swipedLinkId = null
                                    }
                                }
                            )
```

Con:

```kotlin
                            LinkCard(
                                link = link,
                                timeAgo = viewModel.getTimeAgo(link.createdAt),
                                onClick = {
                                    if (swipedLinkId == link.id.toString()) {
                                        swipedLinkId = null
                                    } else if (swipedLinkId != null) {
                                        swipedLinkId = null
                                    }
                                }
                            )
```

- [ ] **Step 2: Verificar que el código compila**

Run: `./gradlew compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/pdalbert/apps/linked/ui/screens/home/AllLinksScreen.kt
git commit -m "feat: pass timeAgo to LinkCard in AllLinksScreen"
```

---

### Task 4: Verificación final

- [ ] **Step 1: Ejecutar build completo**

Run: `./gradlew build`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Ejecutar tests unitarios**

Run: `./gradlew test`
Expected: Todos los tests pasan

- [ ] **Step 3: Verificar que no hay warnings de lint**

Run: `./gradlew lint`
Expected: Sin errores críticos

---

## Self-Review Checklist

- [x] Spec coverage: LinkCard ahora muestra dominio + tiempo
- [x] No placeholders: Todo el código está completo
- [x] Type consistency: Funciones y parámetros consistentes
- [x] Archivos modificados: 3 archivos (LinkCard.kt, AllLinksViewModel.kt, AllLinksScreen.kt)
