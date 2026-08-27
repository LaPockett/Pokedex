
<h1 align="center"><img src="https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExN3E4N3RzcjRxc3VvOWlzeG5qOHlqNHlqMXBkeGIwcjB4YnZmdzZiOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/fxTDiduVAhCgnNLaG2/giphy.gif" width="60"/>Pokédex<img src="https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExN3E4N3RzcjRxc3VvOWlzeG5qOHlqNHlqMXBkeGIwcjB4YnZmdzZiOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/fxTDiduVAhCgnNLaG2/giphy.gif" width="60"></h1>

Este es un mini-proyecto de una **Pokeédex** desarrollado íntegramente como un hobby y ejercicio de aprendizaje. El objetivo principal es explorar y poner en práctica las tecnologías más modernas del ecosistema Android mientras se consume la popular **PokeAPI**.

## ✨ Funcionalidades Principales

### 1. Exploración de Pokémon

- **Lista Infinita:** Visualización de Pokémon en un grid con scroll infinito (pagination) para una navegación fluida.
- **Skeleton Loading:** Animaciones de carga (shimmer effect) mientras se obtienen los datos de la red.
- **Búsqueda Rápida:** Acceso instantáneo a la información de tus Pokémon favoritos.

### 2. Detalles del Pokémon

- **Ficha Técnica:** Visualización de altura, peso y experiencia base con formatos personalizados.
- **Estadísticas Base:** Gráficos visuales de los stats (HP, Attack, Defense, etc.) adaptados al color del tipo principal del Pokémon.
- **Habilidades:** Sección detallada con las habilidades (abilities) de cada criatura.
- **Gritos (Cries):** ¡Escucha al Pokémon! Al tocar la imagen en la pantalla de detalle, se reproduce su sonido característico mediante `MediaPlayer`.

### 3. Personalización y Estética

- **Modo Shiny:** Interruptor global en la barra superior que permite alternar entre los sprites normales y sus versiones Shiny.
- **Tema Claro/Oscuro:** Soporte completo para modo oscuro y claro, cambiando dinámicamente la paleta de colores.
- **Colores Dinámicos:** La interfaz de detalle se adapta visualmente según el tipo elemental del Pokémon (Fuego, Agua, Planta, etc.).

### 4. Sistema de Favoritos

- **Persistencia Local:** Guardado de Pokémon favoritos utilizando Room Database para que no se pierdan al cerrar la app.
- **Contador en Tiempo Real:** La barra superior incluye un sistema de notificaciones (Badge) que muestra cuántos Pokémon tienes marcados como favoritos.

### 5. Navegación y UX

- **Navegación Intuitiva:** Implementación de Jetpack Compose Navigation para transiciones suaves entre pantallas.
- **Botón de Retorno:** Botón flotante (FAB) inteligente que aparece al hacer scroll para volver rápidamente al principio de la lista.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Diseño 100% declarativo).
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Networking:** Retrofit + GSON para el consumo de la PokeAPI.
- **Carga de Imágenes:** Coil (con soporte para red y OkHttp).
- **Base de Datos:** Room para el almacenamiento de favoritos.
- **Gestión de Estado:** Corrutinas de Kotlin y StateFlow.
- **Dependency Injection:** Inyección manual / Factory pattern.

## 📸 Screenshoots
<img src= "docs/screens.png" />

---

Desarrollado con ❤️ por **LaPockett**