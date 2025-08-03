# Registro de Cambios (Changelog)

Todas las modificaciones notables de este proyecto serán documentadas en este archivo.

El formato se basa en [Keep a Changelog](https://keepachangelog.com/), y este proyecto se adhiere a [Semantic Versioning](https://semver.org/).

---

## [Próxima Versión] - En Desarrollo

### 🚀 Nuevas Funcionalidades Planificadas

- **Módulo de Plataformas de Aterrizaje (Landpads)**: Implementación completa de la funcionalidad para listar y ver detalles de las plataformas de aterrizaje.
- **Módulo de Plataformas de Lanzamiento (Launchpads)**: Implementación completa de la funcionalidad para listar y ver detalles de las plataformas de lanzamiento.
- **Módulo de Cargas Útiles (Payloads)**: Implementación completa de la funcionalidad para listar y ver detalles de las cargas útiles.

### 🔧 Mejoras de Funcionalidad

- **Filtro y Ordenamiento de Lanzamientos**: Se añadirán controles en la UI para filtrar lanzamientos por año, estado de éxito y ordenar por fecha.
- **Búsqueda Global**: Se implementará una funcionalidad de búsqueda en toda la aplicación para encontrar lanzamientos, cohetes, etc.
- **Pantalla de Configuración**: Se creará una pantalla para gestionar opciones de la aplicación, como la limpieza del caché local.

---

## [0.2.1] - 2025-08-03

### 📝 Añadido

#### 📚 Mejoras en la Documentación
- **Changelog.md**: Se añadió un archivo de registro de cambios completo siguiendo el formato [Keep a Changelog](https://keepachangelog.com/) con emojis, categorización clara y estructura profesional.
- **Referencia en README**: Se añadió una sección "Registro de Cambios" en el README.md que enlaza al archivo Changelog.md y explica su propósito.

### 🔄 Cambiado

#### 📖 Estructura de Documentación
- **README.md**: Se mejoró la documentación del proyecto añadiendo una sección dedicada al registro de cambios con enlaces a las mejores prácticas de versionado.
- **Formato del Changelog**: Se implementó un formato profesional con jerarquía visual clara, emojis categorizados y separadores para mejorar la legibilidad.

---

## [0.2.0] - 2025-08-03

### ✨ Añadido

#### 🎯 Nuevos Módulos Completos
- **Módulo de Núcleos (Cores)**: Implementación completa de la funcionalidad para listar y ver detalles de los núcleos de cohetes.
- **Módulo de Tripulación (Crew)**: Implementación completa de la funcionalidad para listar y ver detalles de los miembros de la tripulación.
- **Módulo de Naves (Ships)**: Implementación completa de la funcionalidad para listar y ver detalles de las naves.
- **Módulo de Dragones (Dragons)**: Implementación completa de la funcionalidad para listar y ver detalles de las cápsulas Dragon.

#### 🏠 Mejoras en la Pantalla de Inicio
- **Panel de Control Completo**: Se añadieron carruseles para todas las nuevas secciones (Cápsulas, Núcleos, Tripulación, Naves, Dragones) en la pantalla de inicio.
- **Navegación desde Inicio**: Se habilitó la navegación desde cada tarjeta en los nuevos carruseles hacia su respectiva pantalla de detalle.

### 🔄 Cambiado

#### 🎨 Rediseños de UI/UX
- **Rediseño de la Pantalla de Inicio**: Se unificó el carrusel de lanzamientos en uno solo y se rediseñó la pantalla para funcionar como un panel de control central.
- **Rediseño de la Lista de Lanzamientos**: Se reemplazó la lista de texto simple por un nuevo componente `LaunchCard` que muestra información detallada como el número de vuelo, la fecha y el estado de la misión.
- **Estandarización de Componentes**: Se unificó el diseño de las tarjetas y las pantallas de detalle para Crew y Ships para seguir el estándar visual de Rockets y Dragons, asegurando que las imágenes se muestren de manera consistente.

#### 🧭 Mejoras de Navegación
- **Navegación Mejorada**: Se añadió un botón de retroceso en la barra de navegación superior de las pantallas de detalle para una experiencia de usuario más intuitiva.

### 🐛 Corregido

#### 🚨 Errores Críticos
- **Crash en Detalles del Cohete**: Se solucionó un error fatal (`IllegalFormatConversionException`) que ocurría al ver los detalles de un cohete debido a un formato incorrecto de datos numéricos.
- **Error de Carga en Dragones**: Se resolvió un problema de parseo de JSON (`JsonDataException`) que impedía la carga de datos de la sección "Dragons". También se gestionó la migración de la base de datos resultante.

#### 📅 Mejoras en el Manejo de Datos
- **Lógica de Fechas de Lanzamiento**: Se corrigió el formato de las fechas de lanzamiento para que fueran legibles y se implementó una lógica para determinar correctamente si un lanzamiento es "Próximo" basándose en la fecha actual.

---

## [0.1.1] - 2025-07-28

### 🐛 Corregido

#### 🔧 Errores de Desarrollo
- **Errores de Compilación**: Se resolvieron varios errores de compilación relacionados con el ViewModel y el Gradle Wrapper.
- **Excepción en el Repositorio**: Se corrigió una excepción que ocurría en la capa de repositorio durante la obtención de datos.

---

## [0.1.0] - 2025-07-27

### ✨ Añadido

#### 🏗️ Estructura del Proyecto
- **Estructura y Configuración del Proyecto**: Se estableció la estructura de paquetes inicial siguiendo los principios de Clean Architecture (`data`, `domain`, `presentation`, `di`) y se configuraron todas las dependencias (Hilt, Retrofit, Room, Compose).

#### 🚀 Módulos Principales
- **Módulo de Lanzamientos (Launches)**: Implementación completa de la funcionalidad para listar y ver detalles de los lanzamientos.
- **Módulo de Cohetes (Rockets)**: Implementación completa de la funcionalidad para listar y ver detalles de los cohetes.
- **Módulo de Cápsulas (Capsules)**: Implementación completa de la funcionalidad para listar y ver detalles de las cápsulas.

#### 🎨 Interfaz de Usuario
- **Pantalla de Inicio Inicial**: Se desarrolló la primera versión de la pantalla de inicio con carruseles horizontales.
- **Navegación Principal**: Se estableció el framework de navegación con Jetpack Compose Navigation, incluyendo un NavGraph, un AppShell con menú lateral y TopAppBar.

---

## [0.0.1] - 2025-07-08

### ✨ Añadido

#### 📝 Documentación Inicial
- **Commit Inicial**: Creación del repositorio.
- **Documentación Inicial**: Se añadió el archivo `README.md` con la descripción del proyecto, objetivos, arquitectura y wireframes iniciales.

---

## Tipos de Cambios

- **✨ Añadido**: Nuevas funcionalidades
- **🔄 Cambiado**: Cambios en funcionalidades existentes
- **🐛 Corregido**: Correcciones de errores
- **🚀 Nuevas Funcionalidades Planificadas**: Funcionalidades en desarrollo
- **🔧 Mejoras de Funcionalidad**: Mejoras en funcionalidades existentes
- **🎨 Rediseños de UI/UX**: Cambios en la interfaz de usuario
- **🧭 Mejoras de Navegación**: Mejoras en la navegación
- **🚨 Errores Críticos**: Errores que causaban crashes o problemas graves
- **📅 Mejoras en el Manejo de Datos**: Mejoras en el procesamiento de datos
- **🔧 Errores de Desarrollo**: Errores relacionados con el desarrollo
- **🏗️ Estructura del Proyecto**: Cambios en la arquitectura
- **📝 Documentación Inicial**: Documentación del proyecto