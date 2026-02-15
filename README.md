# Sismos Argentina

Aplicación Android que monitorea y muestra en tiempo real los sismos ocurridos en Argentina.

## Características

- 📱 **Android 13** (Target SDK 33) compatible con Android 7.0+ (Min SDK 24)
- 🎨 **Jetpack Compose** con Material Design 3
- 🔄 **Actualización automática** cada 2 minutos mientras la app está abierta
- 🌍 **Datos en tiempo real** desde la API de USGS (United States Geological Survey)
- 📊 **Información detallada** de cada sismo:
  - Magnitud
  - Profundidad
  - Ubicación (provincia/ciudad)
  - Fecha y hora del evento

## Arquitectura

La aplicación utiliza **arquitectura MVVM (Model-View-ViewModel)** con:

- **Data Layer**: Modelos, Retrofit API service, Repository
- **Domain Layer**: Casos de uso para lógica de negocio
- **Presentation Layer**: ViewModels y UI con Jetpack Compose
- **Dependency Injection**: Hilt (Dagger)

## Tecnologías

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Diseño moderno
- **Retrofit + Moshi** - Networking y parsing JSON
- **Coroutines + Flow** - Programación asíncrona reactiva
- **Hilt** - Inyección de dependencias
- **JUnit + MockK** - Testing

## Estructura del Proyecto

```
app/
├── data/
│   ├── model/          # Modelos de datos
│   ├── remote/         # API service
│   └── repository/     # Repositorio de datos
├── domain/
│   └── usecase/        # Casos de uso
├── presentation/
│   ├── viewmodel/      # ViewModels
│   ├── screen/         # Pantallas Compose
│   └── components/     # Componentes reutilizables
├── di/                 # Módulos de Hilt
└── ui/theme/           # Temas y colores
```

## API

La aplicación consume la API de USGS Earthquake:
- **Endpoint**: `https://earthquake.usgs.gov/fdsnws/event/1/query`
- **Filtros**: Coordenadas de Argentina (lat: -55 a -21, lon: -73 a -53)
- **Límite**: Últimos 5 sismos ordenados por fecha

## Requisitos

- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 33
- Gradle 8.2+

## Instalación

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo físico

## Permisos

- `INTERNET` - Para consultar la API de USGS
- `ACCESS_NETWORK_STATE` - Para verificar conectividad

## Licencia

Este proyecto es de código abierto para fines educativos.
