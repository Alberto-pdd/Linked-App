# Home Screen - Funcionalidades Pendientes

## Resumen

Este documento describe las funcionalidades que quedan por implementar en la pantalla Home de Linked.

---

## 1. Funcionalidades de Filtros

### Filtros de Etiqueta (TagFilterChips)
- **Estado actual**: Los chips se muestran visualmente pero no filtran
- **Funcionalidad pendiente**:
  - Filtrar links por etiqueta seleccionada
  - Mantener el chip activo seleccionado
  - Combinar con otros filtros (búsqueda, tiempo)

### Filtros Temporales (TimeFilterDropdown)
- **Estado actual**: El dropdown se muestra visualmente pero no filtra
- **Funcionalidad pendiente**:
  - Filtrar links por período: "Hoy", "Esta semana", "Este mes"
  - Calcular fechas automáticamente
  - Combinar con otros filtros

### Filtros de Búsqueda (SearchBar)
- **Estado actual**: La búsqueda filtra por título, URL y etiqueta
- **Funcionalidad pendiente**:
  - Agregar botón de limpiar (✅ Implementado)
  - Filtrar en tiempo real mientras se escribe

---

## 2. Funcionalidades de Ordenamiento

### Orden de Links
- **Estado actual**: Los links se muestran en orden de creación
- **Funcionalidad pendiente**:
  - Ordenar por fecha de creación (ascendente/descendente)
  - Botón de dirección (↓/↑) para cambiar orden
  - Mantener orden seleccionado

### Orden de Carpetas
- **Estado actual**: Las carpetas se muestran en orden alfabético
- **Funcionalidad pendiente**:
  - Ordenar por: "Más nuevo", "Más antiguo", "Más enlaces", "Menos enlaces"
  - Dropdown de selección de orden
  - Mantener orden seleccionado

---

## 3. Funcionalidades de Más Opciones

### LinkCard
- **Estado actual**: No hay botón de más opciones
- **Funcionalidad pendiente**:
  - Botón "⋯" en cada tarjeta de link
  - Menú contextual con opciones: Editar, Eliminar, Compartir, Copiar URL
  - Acciones rápidas sin navegar

### FolderCard
- **Estado actual**: No hay botón de más opciones
- **Funcionalidad pendiente**:
  - Botón "⋯" en cada tarjeta de carpeta
  - Menú contextual con opciones: Editar, Eliminar, Compartir, Abrir
  - Acciones rápidas sin navegar

---

## 4. Funcionalidades de Navegación

### FAB (Floating Action Button)
- **Estado actual**: No existe FAB
- **Funcionalidad pendiente**:
  - Botón flotante "+" en esquina inferior derecha
  - Al hacer click: abrir AddLinkSheet para crear nuevo enlace
  - Animación de apertura/cierre

### Home Indicator
- **Estado actual**: No existe indicador
- **Funcionalidad pendiente**:
  - Barra inferior que indica zona de gestos
  - Solo visual, sin funcionalidad

### Navegación de Secciones
- **Estado actual**: "Ver todos →" navega a AllLinks/AllFolders
- **Funcionalidad pendiente**:
  - Animación de transición entre pantallas
  - Mantener estado de filtros al volver

---

## 5. Funcionalidades de UI

### Elevation de Carpetas
- **Estado actual**: La sección de carpetas no tiene elevación
- **Funcionalidad pendiente**:
  - Sombra superior en la sección de carpetas
  - Efecto de elevación sobre la sección de links

### Avatar de Usuario
- **Estado actual**: Muestra iniciales del usuario
- **Funcionalidad pendiente**:
  - Foto de perfil si está disponible
  - Menú de opciones al hacer click

### Empty States
- **Estado actual**: Se muestran cuando no hay items
- **Funcionalidad pendiente**:
  - Animaciones de entrada
  - Botones de acción rápida

---

## 6. Funcionalidades de Datos

### Persistencia de Filtros
- **Estado actual**: Los filtros se resetean al cerrar la app
- **Funcionalidad pendiente**:
  - Guardar filtros seleccionados
  - Restaurar filtros al abrir la app
  - Preferencias de usuario

### Sincronización
- **Estado actual**: Los datos se cargan localmente
- **Funcionalidad pendiente**:
  - Sincronización con servidor
  - Resolución de conflictos
  - Modo offline

---

## 7. Funcionalidades de Accesibilidad

### Navegación por Teclado
- **Estado actual**: No soportada
- **Funcionalidad pendiente**:
  - Navegación con Tab/Shift+Tab
  - Activación con Enter/Espacio
  - Atajos de teclado

### Lectores de Pantalla
- **Estado actual**: Básico
- **Funcionalidad pendiente**:
  - Descripciones completas
  - Estados actualizados
  - Navegación lógica

---

## 8. Funcionalidades de Rendimiento

### Lazy Loading
- **Estado actual**: Se cargan todos los items
- **Funcionalidad pendiente**:
  - Carga bajo demanda
  - Paginación
  - Cache de imágenes

### Optimización de Renders
- **Estado actual**: Re-renders completos
- **Funcionalidad pendiente**:
  - keys estables en LazyColumn
  - DerivedStateOf para cálculos
  - Remember para valores computados

---

## Prioridad de Implementación

### Alta (Próxima iteración)
1. Funcionalidad de filtros (temporal y por etiqueta)
2. FAB con navegación a AddLinkSheet
3. Botón de limpiar en SearchBar

### Media (Iteración siguiente)
1. Funcionalidad de ordenamiento
2. Botones de más opciones
3. Elevation de carpetas

### Baja (Futuro)
1. Persistencia de filtros
2. Sincronización
3. Accesibilidad avanzada
4. Optimización de rendimiento
