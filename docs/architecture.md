# Arquitectura

## Patrón MVVM

La aplicación sigue el patrón Model-View-ViewModel:

- **Model**: Clases de datos y repositorios (data/)
- **View**: Composables de Jetpack Compose (ui/screens/)
- **ViewModel**: Gestión de estado y lógica de negocio (viewmodel/)

## Flujo de datos

```
UI (Compose) → ViewModel → Repository → Remote/Fake Data Source
```

## Autenticación

- Login exclusivo mediante Google Sign-In
- Credential Manager API para gestión de credenciales
- Token JWT propio del backend (futuro)

## Sistema de diseño

### Colores

**Color de fondo universal** (todas las pantallas):
- Variable: `Background` del tema
- Valor: `#EEEAE4` (beige cálido)
- Definición: `ui/theme/Color.kt`

**Regla**: Todas las pantallas deben importar y usar `Background` desde el tema. No crear colores de fondo locales.

### Tipografías

- **Primaria**: Manrope (texto general, botones, UI)
- **Secundaria**: DM Serif Display (headlines, títulos destacados)

## Decisiones técnicas

- **Compose Navigation** para navegación entre pantallas
- **StateFlow** para exponer estado desde ViewModels
- **Fake Repositories** para desarrollo sin backend
- **Retrofit** preparado para integración futura con SpringBoot
- **Animatable + coroutine** para animaciones fluidas de loops infinitos
