# Implementación 1: Dependencias y Theme

## Fecha
21 de marzo de 2026

## Objetivo
Configurar las dependencias del proyecto y establecer el sistema de diseño visual (colores, tipografía, tema) basado en las interfaces HTML proporcionadas.

## Dependencias añadidas

### `gradle/libs.versions.toml`
- **navigationCompose** `2.7.7` — Navegación entre pantallas con Compose
- **lifecycleViewModelCompose** `2.7.0` — Integración de ViewModel con Compose
- **googleIdentity** `1.1.0` — Autenticación con Google Sign-In
- **kotlinxSerialization** `1.6.3` — Serialización JSON para futura comunicación con backend

### `app/build.gradle.kts`
- Plugin `kotlin-serialization` añadido
- 4 nuevas dependencias en `dependencies {}`

## Sistema de diseño

### Paleta de colores (Color.kt)
Basada en los CSS de las interfaces HTML.

| Color | Hex | Uso |
|-------|-----|-----|
| Background | `#F5F4F0` | Fondo general de la app |
| Surface | `#FFFFFF` | Cards, inputs, superficies |
| Ink | `#111110` | Texto principal |
| InkMuted | `#7A7A72` | Texto secundario/placeholder |
| Accent | `#3B82C4` | Botones primarios, links, énfasis |
| AccentBg | `#EBF3FB` | Fondo de tag azul |
| Border | `#E2E1DC` | Bordes de cards e inputs |
| Danger | `#EF4444` | Eliminar, errores |
| DangerBg | `#FEF2F2` | Fondo de confirmación eliminar |
| Yellow | `#F5A623` | Tag amarillo |
| YellowBg | `#FEF3C7` | Fondo de tag amarillo |
| Green | `#166534` | Tag verde |
| GreenBg | `#DCFCE7` | Fondo de tag verde |
| Purple | `#5B21B6` | Tag morado |
| PurpleBg | `#EDE9FE` | Fondo de tag morado |

### Tipografía (Type.kt)
Fuente: **Manrope** en toda la aplicación.

| Estilo | Peso | Tamaño | Uso |
|--------|------|--------|-----|
| headlineLarge | ExtraBold | 28sp | Título principal ("Linked") |
| headlineMedium | ExtraBold | 22sp | Títulos de sección |
| titleMedium | SemiBold | 15sp | Subtítulos, nombres de link |
| bodyLarge | Medium | 14sp | Texto de cuerpo, inputs |
| bodyMedium | Normal | 13sp | Texto secundario, URL |
| labelSmall | SemiBold | 10.5sp | Labels, badges de tag |

Fuentes descargadas en `res/font/`:
- `manrope_regular.ttf`
- `manrope_medium.ttf`
- `manrope_semibold.ttf`
- `manrope_bold.ttf`
- `manrope_extrabold.ttf`

### Tema (Theme.kt)
- Modo claro: colores Linked (fondo crema, superficies blancas)
- Modo dark: esquema invertido (fondo oscuro, texto claro)
- `dynamicColor` desactivado por defecto para mantener la identidad visual

## Archivos modificados
1. `gradle/libs.versions.toml`
2. `app/build.gradle.kts`
3. `ui/theme/Color.kt`
4. `ui/theme/Type.kt`
5. `ui/theme/Theme.kt`

## Archivos creados
1. `res/font/manrope_regular.ttf`
2. `res/font/manrope_medium.ttf`
3. `res/font/manrope_semibold.ttf`
4. `res/font/manrope_bold.ttf`
5. `res/font/manrope_extrabold.ttf`
