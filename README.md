# Gestión de Proyectos y Suministros

## Descripción

Aplicación desarrollada en Java para gestionar proveedores, piezas, proyectos y suministros mediante Hibernate ORM, JPQL
y una base de datos MariaDB.

Dispone de dos modalidades de uso:

- **Consola:** gestión mediante menús de texto y búsquedas avanzadas por diferentes parámetros.
- **Interfaz gráfica:** gestión mediante JavaFX.

## Funcionalidades

- Operaciones CRUD de proveedores, piezas, proyectos y suministros.
- Búsquedas y consultas mediante JPQL.
- Validación de datos e integridad referencial.
- Histórico de proveedores, análisis de proyectos y estadísticas globales.
- Exportación de entidades e informes a XML y JSON.
- Consulta de metadatos de la base de datos.
- Sistema de logging y apartado de ayuda.
- Pruebas automatizadas con JUnit 5.

## Tecnologías

Java 19, JavaFX, Maven, Hibernate ORM 6, Jakarta Persistence (JPA), JPQL, MariaDB, JUnit 5, Gson, XMLStreamWriter y
SLF4J.

## Estructura del repositorio

- **Proyecto/**: aplicación Java, código fuente, pruebas y recursos.
- **ad_suministros-todo.sql**: exportación completa de la base de datos (estructura y datos).
- **ad_suministros-inserts.sql**: sentencias INSERT para cargar los datos.
- **LICENSE**: licencia del proyecto.
- **Imágenes del MER**: diagramas del Modelo Entidad-Relación.
- **Planificación.xlsx**: seguimiento del desarrollo por fechas, tareas y prioridades.
- **README.md**: documentación del proyecto.

## Base de datos

Se utiliza MariaDB, con una base de datos llamada `ad_suministros`. Los datos de conexión se configuran en
`persistence.xml`.

Hibernate crea automáticamente las tablas necesarias al ejecutar la aplicación en modo consola si todavía no existen.
También se incluyen dos archivos SQL con la estructura y los datos de la base de datos.

## Ejecución

Requisitos: JDK 19 y MariaDB.

Antes de ejecutar la aplicación, configurar la conexión en `persistence.xml`.

**Modo consola:** ejecutar la clase principal `App.java` desde IntelliJ IDEA.

**Modo interfaz gráfica (JavaFX):**

- Desde la terminal, en la carpeta del proyecto:

```bash
.\mvnw.cmd javafx:run
```

- Desde IntelliJ IDEA: panel Maven → Plugins → javafx → Clicar en `javafx:run`.

## Archivos generados

Dentro de `Proyecto/datos/`, al mismo nivel que `src/`, se almacenan las exportaciones:

- **json/**: archivos JSON.
- **xml/**: archivos XML.
- **metadatos/**: información de la base de datos.

Los logs se guardan en `Proyecto/logs/`, también al nivel de `src/`, organizados en archivos diarios. `aplicacion.log`
corresponde al registro actual o más reciente.

## Pruebas

Las pruebas JUnit 5 se encuentran en `src/test`, en el archivo `AppTest.java`. Para ejecutarlas, abrir el archivo en
IntelliJ IDEA y pulsar el icono verde ▶️ (Run).