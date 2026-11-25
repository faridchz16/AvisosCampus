# PROMPTS · Fase 2 · Mejora profesional asistida por IA

**Rama:** `mejora-ia`

## Mejoras realizadas

| # | Mejora | Capas que cambian | Commit |
|---|---|---|---|
| 1 | Dos canales: **Urgentes** (`IMPORTANCE_HIGH`) y **Avisos**; el recordatorio usa el urgente | Notificador + ViewModel | `feat(ia): agrega canal Urgentes para el recordatorio` |
| 2 | Texto largo con `BigTextStyle` al expandir la notificación | Notificador | `feat(ia): muestra texto largo con BigTextStyle` |
| 3 | Historial persistente con DataStore | Repository + ViewModel | `feat(ia): guarda el historial con DataStore` |

## Registro de prompts

| Prompt que escribí | Qué respondió Claude | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un segundo canal de notificación llamado Urgentes con `IMPORTANCE_HIGH` para que el recordatorio aparezca como notificación emergente, manteniendo el canal Avisos para el resto. Respeta MVVM: el ViewModel no puede usar `Context` ni `NotificationManager`.» | Creó `CANAL_URGENTE`, renombró `crearCanal` a `crearCanales` para registrar ambos canales y agregó `EventoAviso(aviso, urgente)` para que el ViewModel indique la urgencia y la pantalla se la pase a `Notificador.mostrar`. | Se descartó que el Notificador decidiera la urgencia según el origen del aviso; la decisión quedó en el ViewModel. | La regla de negocio (qué es urgente) pertenece al ViewModel, que sigue sin `Context`; el Notificador solo traduce esa decisión a canal y prioridad. |
| «Muestra el texto completo de la notificación al expandirla usando `NotificationCompat.BigTextStyle`, sin modificar los datos ficticios del laboratorio.» | Agregó `setStyle(BigTextStyle().bigText(...))` con un texto expandido que incluye el texto del aviso, su origen y su hora. | Se mantuvieron sin cambios el título y el texto de los datos ficticios; el detalle adicional solo aparece al expandir. | Es una mejora de presentación, por eso vive solo en el Notificador; la versión contraída sigue igual a la Fase 1. |
| «Haz que el historial no se pierda al cerrar la app usando DataStore. El Repository no debe recibir `Context` y en `viewmodel/` no debe aparecer `Context`. Separa la factory en `AvisosViewModelFactory.kt` según la estructura del laboratorio.» | Creó `AvisosRepositoryDataStore`, que guarda la lista en JSON y continúa los ids desde el último guardado; cambió la interface a `Flow` y `suspend fun registrar`; creó el DataStore en `AvisosApp` y la factory obtiene el Repository desde la `Application`. Propuso además actualizar el árbol del README. | Se descartó modificar el README. | El documento pide que el README corresponda a la Fase 1; las mejoras de la Fase 2 se documentan en este archivo. |

## Revisión de las reglas

| Regla | Cómo se cumple |
|---|---|
| Sin `Context` ni `NotificationManager` en el ViewModel | El ViewModel solo recibe `Boolean` y `AvisosRepository`; el DataStore se crea en `AvisosApp` |
| Todo receptor se quita en `onDispose` | El receptor del modo avión se registra y se quita dentro de `DisposableEffect` |
| `PendingIntent` sin mutabilidad innecesaria | Se mantiene `PendingIntent.FLAG_IMMUTABLE` |
| Arquitectura MVVM + Repository | `AvisosRepositoryMemoria` y `AvisosRepositoryDataStore` implementan la misma interface; cambiar de una a otra no afecta a la UI |
