# Hyper Ultra Launcher (v0.1.0)

Launcher Android ligero (Kotlin + Jetpack Compose) con estética inspirada en HyperOS. Proyecto independiente, no es software oficial de Xiaomi.

- Paquete: `io.github.hyperultra.launcher` · minSdk 26 · targetSdk 35 · sin código nativo (no depende de la arquitectura de la CPU).
- Permisos: solo `WRITE_SETTINGS` (opcional, para el brillo).
- **ESTADO: el código NO ha sido compilado ni probado.** El entorno donde se generó no tenía Android SDK, Gradle ni red.

## Compilar el APK

### Opción A: GitHub Actions (sin instalar nada)
1. Crea un repositorio privado en GitHub y sube el contenido de esta carpeta.
2. Pestaña **Actions → Build APK → Run workflow**.
3. Al terminar, descarga el artefacto `HyperUltraLauncher-apk`:
   `app-debug.apk` y `app-release.apk` (ambos firmados con la clave de depuración).

### Opción B: Android Studio
Abre la carpeta, deja que sincronice Gradle y usa **Build → Build APK(s)**.
Salida: `app/build/outputs/apk/debug/app-debug.apk`.

### Opción C: línea de comandos
Requiere JDK 17, Android SDK (plataforma 35) y Gradle 8.9: `gradle assembleDebug`.

## Instalar en el REDMI A5
1. Copia el APK al móvil y ábrelo; permite "instalar apps de esta fuente" si lo pide.
2. Ajustes → Apps → Apps predeterminadas → App de inicio → Hyper Ultra Launcher
   (o pulsa "Launcher predeterminado" dentro de los ajustes del launcher).

## Uso
- Deslizar arriba: cajón de apps (búsqueda arriba). Deslizar abajo: Centro de control o búsqueda (configurable).
- Mantener pulsado en inicio: ajustes. Mantener pulsado un icono del cajón: info, dock, ocultar.

## Implementado
Inicio con reloj, fecha, batería, calendario y nota; dock (por defecto con teléfono/mensajes/navegador/cámara predeterminados); cajón con búsqueda; columnas y tamaño de icono; ocultar apps (protegido con el bloqueo del sistema si existe); temas y colores; claro/oscuro; Modo Lite (por defecto); desenfoque real del fondo en Android 12+; Centro de control (linterna, volumen, brillo con permiso, multimedia, atajos a ajustes); exportar/importar configuración por portapapeles; fondo de pantalla del sistema.

## Limitaciones (algunas por Android, otras por alcance de la v0.1)
- Wi-Fi, Bluetooth, modo avión y ahorro de batería: Android no permite activarlos desde una app normal; abren el panel oficial y no muestran estado.
- Pantalla de bloqueo: una app normal no puede reemplazarla. No incluida.
- Notificaciones y título de la música en curso: requieren acceso a notificaciones. No incluido.
- No incluido todavía: carpetas, widgets de terceros redimensionables, paquetes de iconos, relojes/widgets de música y fotos, cuadrícula libre en inicio.
