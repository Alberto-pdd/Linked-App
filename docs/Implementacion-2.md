# Implementación 2: Modelos de datos

## Fecha
21 de marzo de 2026

## Objetivo
Definir las estructuras de datos que representan los conceptos principales de la aplicación: enlaces, etiquetas, carpetas y usuario.

## Decisiones de diseño

### Identificadores únicos
- Se usa `UUID` en lugar de `Long` para garantizar identificadores irrepetibles
- Cada modelo genera su UUID por defecto al crearse (`UUID.randomUUID()`)

### Relación Link ↔ Folder (N-N)
- Un link puede pertenecer a múltiples carpetas
- Una carpeta puede contener múltiples links
- La relación se almacena en `Link.folderIds: List<UUID>`
- `Folder` no almacena referencias a links — el conteo se calcula filtrando

### Colores de tag
- El color del tag se calcula automáticamente a partir del nombre del tag
- Usa un hash determinista: mismo nombre de tag = mismo color siempre
- El usuario no elige el color — la app lo asigna al crear un tag nuevo
- `tagColor` es una propiedad calculada en `Link`, no se guarda en la base de datos

### Iniciales del usuario
- Se calculan a partir del nombre del usuario (Google Sign-In)
- "Juan Delgado" → "JD"
- No se almacenan como campo separado

## Modelos

### TagColor (enum)
```
DEFAULT  → Azul (#EBF3FB / #3B82C4)
YELLOW   → Amarillo (#FEF3C7 / #92630A)
GREEN    → Verde (#DCFCE7 / #166534)
PURPLE   → Morado (#EDE9FE / #5B21B6)
```

Función `tagColorFor(tagName: String): TagColor`
- Hash del nombre del tag → índice dentro del array de colores
- Garantiza consistencia: "Diseño" siempre será del mismo color

### Link
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | UUID | Identificador único |
| title | String | Título del enlace |
| url | String | URL del enlace |
| description | String | Descripción opcional |
| tag | String | Etiqueta/categoría |
| folderIds | List\<UUID\> | Carpetas donde está contenido |
| createdAt | String | Fecha de creación |
| modifiedAt | String | Fecha de última modificación |
| tagColor | TagColor | Calculado automáticamente del tag |

### Folder
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | UUID | Identificador único |
| name | String | Nombre de la carpeta |

### User
| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | UUID | Identificador único |
| name | String | Nombre de la cuenta Google |
| email | String | Email de la cuenta Google |
| initials | String | Calculado del nombre ("JD") |

## Archivos creados
1. `data/model/TagColor.kt`
2. `data/model/Link.kt`
3. `data/model/Folder.kt`
4. `data/model/User.kt`

## Pendiente para fases futuras
- **Repositorios**: `FakeLinkRepository`, `FakeFolderRepository` con datos mock
- **ViewModels**: consumir repositorios y exponer estado a la UI
- **Retrofit**: `RemoteLinkRepository` conectado al backend SpringBoot
