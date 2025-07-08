# Proyecto de Aplicación Móvil: "SpaceX Explorer"

**Informe de Borrador de Proyecto** **Curso:** Desarrollo de Aps Móviles COM-437ES-AVOL1 **Autor:** José David Flórez Navarrete

**Fecha:** 08/07/2025

## Descripción del proyecto

**SpaceX Explorer** será una aplicación móvil nativa para la plataforma **Android** que consumirá la API pública de SpaceX (v4). El objetivo principal es ofrecer a los entusiastas del espacio una interfaz limpia, moderna y fácil de usar para explorar información detallada sobre los lanzamientos, cohetes, cápsulas y la historia de la compañía de Elon Musk.

El desarrollo se centrará en la aplicación de buenas prácticas de ingeniería de software, utilizando **Kotlin** como lenguaje de programación, **Jetpack Compose** para la construcción de la interfaz de usuario y una arquitectura limpia (Clean Architecture) para garantizar que el código sea mantenible, escalable y testeable.

## Exposición del problema

En el ámbito académico, es fundamental que los estudiantes de desarrollo de software no solo aprendan los fundamentos teóricos, sino que también adquieran experiencia práctica con las herramientas y paradigmas modernos del mercado. El "problema" que este proyecto aborda es la necesidad de un ejercicio práctico que integre las siguientes tecnologías y conceptos clave en un solo desarrollo:

1. **Consumo de una API RESTful real:** Conectar una aplicación a una fuente de datos externa es una habilidad esencial.
2. **Desarrollo nativo en Android:** Comprender el ecosistema de Android Studio y el ciclo de vida de una aplicación.
3. **Kotlin y Programación Asíncrona:** Utilizar el lenguaje recomendado por Google y manejar operaciones de red de forma eficiente con corrutinas.
4. **Arquitecturas Limpias (Clean Architecture) y principios SOLID:** Construir software robusto y profesional que sea fácil de mantener y escalar, separando las responsabilidades de manera lógica.
5. **UI Declarativa con Jetpack Compose:** Trabajar con el framework de UI moderno de Android para crear interfaces de manera más eficiente y declarativa.

Este proyecto actúa como una solución integral para practicar y demostrar competencia en estas áreas, resultando en un producto final funcional y un valioso añadido al portafolio profesional.

## Plataforma

- **Sistema Operativo:** Android.
- **Entorno de Desarrollo (IDE):** Android Studio.
- **Lenguaje de Programación:** Kotlin.
- **Kit de Herramientas de UI:** Jetpack Compose.
- **Arquitectura:** Clean Architecture (Capas de Presentación, Dominio y Datos).
- **Librerías Principales:**
  - **Retrofit:** Para las llamadas a la API REST.
  - **Hilt:** Para la inyección de dependencias.
  - **Corrutinas de Kotlin:** Para el manejo de la asincronía.

## Interfaz de usuario e interfaz de administrador

Para este proyecto, no se contempla una "interfaz de administrador" separada. La aplicación está diseñada con una única interfaz de cara al usuario final. El foco está puesto en la experiencia de navegación y descubrimiento de la información de SpaceX.

La **interfaz de usuario (UI)** se compondrá de las siguientes pantallas principales:

1. **Pantalla** de Inicio / Próximos **Lanzamientos:** La pantalla principal que mostrará una lista de los próximos y últimos lanzamientos de SpaceX.
2. **Pantalla** de Historial de **Lanzamientos:** Una lista o cuadrícula con todos los lanzamientos pasados, con opción de búsqueda y filtro.
3. **Pantalla de Detalles del Lanzamiento:** Al seleccionar un lanzamiento, se mostrará toda su información: misión, fecha, cohete utilizado, lugar de lanzamiento, estado (éxito/fallo), galería de imágenes y video del lanzamiento.
4. **Pantalla de Cohetes:** Una lista de los cohetes de SpaceX (Falcon 9, Falcon Heavy, Starship).
5. **Pantalla de Detalles del Cohete:** Al seleccionar un cohete, se mostrarán sus especificaciones técnicas, historial de vuelos y estado actual.

## Funcionalidad

La aplicación contará con las siguientes funcionalidades clave:

- ✅ **Listar lanzamientos:** Obtener y mostrar una lista de todos los lanzamientos pasados, presentes y futuros.
- ✅ **Ver detalles de un lanzamiento:** Acceder a información completa de un lanzamiento específico, incluyendo enlaces a artículos, Reddit, y transmisiones de video.
- ✅ **Filtrar lanzamientos:** Permitir al usuario filtrar los lanzamientos por año o por éxito/fallo.
- ✅ **Buscar lanzamientos:** Implementar una barra de búsqueda para encontrar lanzamientos por nombre de la misión.
- ✅ **Explorar cohetes:** Mostrar una lista de los cohetes desarrollados por SpaceX.
- ✅ **Ver detalles de un cohete:** Mostrar información técnica detallada, imágenes y especificaciones de cada cohete.
- ✅ **Manejo de estados de UI:** La interfaz mostrará indicadores de carga mientras se obtienen los datos y mensajes de error si la conexión a la API falla.

## Diseño (Wireframes / Esquemas de Página)

A continuación se presentan esquemas básicos de las pantallas principales para visualizar la estructura y disposición de los elementos.

### 1. Pantalla de Lista de Lanzamientos

```
+-------------------------------------------------+
|   SpaceX Explorer           [Buscar 🔍]       |
+-------------------------------------------------+
|   [Filtro por Año ▼]  [Filtro Éxito/Fallo ▼]  |
+-------------------------------------------------+
|                                                 |
|   +-----------------------------------------+   |
|   | [Imagen Misión]  Misión: Starlink 5-3   |   |
|   |                  Fecha: 08/07/2025      |   |
|   |                  Cohete: Falcon 9       |   |
|   +-----------------------------------------+   |
|                                                 |
|   +-----------------------------------------+   |
|   | [Imagen Misión]  Misión: CRS-25         |   |
|   |                  Fecha: 15/07/2022      |   |
|   |                  Cohete: Falcon 9       |   |
|   +-----------------------------------------+   |
|                                                 |
|                   [Cargando...]                 |
|                                                 |
+-------------------------------------------------+
```

### 2. Pantalla de Detalles del Lanzamiento

```
+-------------------------------------------------+
|   <-- Volver         Detalles de la Misión      |
+-------------------------------------------------+
|                                                 |
|           [Imagen grande del parche]            |
|                                                 |
|   **Starlink 5-3** |
|   --------------------------------------------  |
|   **Fecha:** 08 de julio de 2025                |
|   **Cohete:** Falcon 9                          |
|   **Sitio:** Kennedy Space Center               |
|   **Estado:** ¡Éxito! ✅                         |
|                                                 |
|   **Descripción:** |
|   Lanzamiento de la nueva tanda de satélites... |
|                                                 |
|   [▶️ Ver Lanzamiento] [📰 Leer Artículo]        |
|                                                 |
+-------------------------------------------------+
```