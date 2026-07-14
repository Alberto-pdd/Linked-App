# IMPORTANTE — Pendientes

## Dominio de la aplicación

Actualmente el dominio mostrado en la SplashScreen es **"linked.com"** (placeholder).

**Acción pendiente:** Buscar y registrar un dominio real para la aplicación. Una vez decidido, actualizar el texto en:

- Archivo: `app/src/main/java/pdalbert/apps/linked/ui/screens/splash/SplashScreen.kt`
- Línea: `text = "linked.com"`

---

## Emojis hardcodeados

Los emojis disponibles para links y carpetas están **hardcodeados** actualmente en:

- `ui/screens/home/AddLinkSheet.kt` — `linkEmojis` (15 emojis)
- `ui/screens/home/AddFolderSheet.kt` — `folderEmojis` (15 emojis)

**Acción pendiente:** Conectar a una API o base de datos que proporcione los emojis dinámicamente. Cuando esté listo:

1. Crear un `EmojiRepository` (interface + implementación)
2. Modificar `EmojiPicker` para recibir emojis de forma dinámica
3. Eliminar las listas hardcodeadas de los sheets

---

*Última actualización: 23 de marzo de 2026*
