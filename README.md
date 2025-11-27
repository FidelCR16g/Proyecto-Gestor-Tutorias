Sistema de Gestión de Tutorías (SGT)

Universidad Veracruzana | Facultad de Estadística e Informática | Licenciatura en Ingeniería de Software

Descripción del Proyecto

El Sistema de Gestión de Tutorías (SGT) es una aplicación de escritorio desarrollada en JavaFX diseñada para optimizar y digitalizar el proceso de tutorías académicas. 

El sistema permite a los Tutores Académicos gestionar sus sesiones, registrar asistencia y generar reportes, mientras que permite a el Coordinador supervisar el cumplimiento y configurar los periodos escolares.

🎯 Objetivos Principales
* Automatizar la agenda de sesiones de tutoría.
* Digitalizar el pase de lista y el registro de problemáticas.
* Centralizar la información para la generación de reportes institucionales.

---

⚙️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura MVC (Modelo-Vista-Controlador) estricta, complementada con patrones de diseño para garantizar la escalabilidad y mantenibilidad.

* Lenguaje:** Java 8 (JDK 1.8.0_202).
* Interfaz Gráfica: JavaFX con FXML.
* Base de Datos: MySQL 8.0.
* Patrones de Diseño:
    * DAO (Data Access Object): Para la persistencia de datos.
    * Singleton: Para el manejo de Sesión y Conexión a BD.
    * RBAC: Control de Acceso Basado en Roles (Seguridad a nivel de aplicación).

---

🚀 Funcionalidades (Casos de Uso)

👤 Rol: Tutor Académico
- CU-03 Registrar Horario de Tutoría:** Generación automática de bloques de horarios (intervalos de 20 min) con validación de fechas y periodos.
- CU-04 Registrar Asistencia: (En desarrollo) Listado dinámico para marcar asistencia.
- CU-06 Gestionar Evidencia: (En desarrollo) Carga de archivos probatorios.
- CU-08 Enviar Reporte de Tutoría: (En desarrollo).

👔 Rol: Coordinador
- CU-15 Gestionar Planeación: Configuración de periodos y fechas límite.
- CU-17 Revisar Reporte de Tutoría: Validación de entregas de tutores.

---

🛠️ Configuración e Instalación

1. Requisitos Previos
* Java JDK 8.
* MySQL Server.
* IDE recomendado: NetBeans 8.2 / 12+ o IntelliJ IDEA.

2. Base de Datos
Ejecuta el script SQL ubicado en `database/script_gestion_tutorias.sql` para crear la estructura y cargar los datos iniciales (Roles, Periodos, Usuarios de prueba).

3. Configuración de Conexión
El sistema utiliza un archivo de propiedades para la conexión. Asegúrate de configurar tus credenciales locales en:
`src/gestortutoriasfx/recurso/database.properties`

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/gestion_tutorias_final?useSSL=false...
db.user=root
db.password=TU_CONTRASEÑA