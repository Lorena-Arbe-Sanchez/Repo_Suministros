# Gestión de Proyectos y Suministros

## Descripción

Aplicación desarrollada en Java para gestionar proyectos, proveedores, piezas y suministros mediante Hibernate ORM y
JPQL, utilizando una base de datos MariaDB.

El proyecto dispone de dos modalidades de ejecución:

- **Modo consola:** permite gestionar los datos mediante menús de texto y ofrece algunas funcionalidades adicionales,
  como búsquedas avanzadas de entidades mediante diferentes parámetros.
- **Modo interfaz gráfica:** permite utilizar las funcionalidades principales de la aplicación mediante una interfaz
  desarrollada con JavaFX.

## Funcionalidades

### Gestión de datos

- Gestión de proveedores (CRUD).
- Gestión de piezas (CRUD).
- Gestión de proyectos (CRUD).
- Gestión de suministros (CRUD).
- Validación de datos introducidos.
- Control de relaciones entre entidades e integridad referencial.

### Búsquedas y consultas

- Búsqueda de proveedores por distintos campos.
- Búsqueda de piezas por distintos campos.
- Búsqueda de proyectos.
- Búsquedas avanzadas de entidades mediante diferentes parámetros, especialmente en el modo consola.
- Consultas mediante JPQL.

### Análisis y estadísticas

- Consulta del histórico de suministros de un proveedor.
- Análisis de suministros asociados a un proyecto.
- Cálculo del coste total de los suministros de un proyecto.
- Estadísticas globales de proveedores, piezas, proyectos y suministros.
- Identificación de proveedores, piezas y proyectos con mayor actividad.

### Otras funcionalidades

- Exportación de datos a XML y JSON.
- Exportación de informes de análisis y estadísticas.
- Consulta de metadatos de la base de datos.
- Sistema de logging para registrar operaciones y errores.
- Apartado de ayuda integrado en la aplicación.

## Tecnologías utilizadas

- **Java 19:** lenguaje de programación.
- **JavaFX:** desarrollo de la interfaz gráfica.
- **Maven:** gestión del proyecto y sus dependencias.
- **Hibernate ORM 6:** mapeo objeto-relacional.
- **Jakarta Persistence (JPA):** persistencia de datos.
- **JPQL:** consultas a la base de datos.
- **MariaDB:** sistema gestor de base de datos.
- **JUnit 5:** pruebas automatizadas.
- **Gson:** generación de archivos JSON.
- **XMLStreamWriter:** generación de archivos XML.
- **SLF4J:** registro de eventos mediante logging.

## Estructura del repositorio

El repositorio contiene los siguientes elementos:

- **Proyecto/**: aplicación Java, incluyendo el código fuente, las pruebas y los recursos necesarios para su ejecución.
- **ad_suministros-todo.sql**: exportación completa de la base de datos, incluyendo su estructura y sus datos.
- **ad_suministros-inserts.sql**: archivo SQL que contiene únicamente las sentencias INSERT para introducir los datos.
- **LICENSE**: archivo con la licencia del proyecto.
- **README.md**: documentación e instrucciones de uso.
- **Imágenes del MER**: diagramas del Modelo Entidad-Relación de la base de datos.
- **Planificación.xlsx**: planificación y seguimiento del desarrollo del proyecto, organizada por fechas y con
  información detallada de las tareas, prioridades y otros datos.

## Base de datos

El proyecto utiliza una base de datos MariaDB llamada `ad_suministros`.

La configuración de conexión se encuentra en el archivo `persistence.xml`, dentro de los recursos del proyecto.

Se incluyen dos archivos SQL:

- **ad_suministros-todo.sql:** contiene la estructura completa de la base de datos y sus datos.
- **ad_suministros-inserts.sql:** contiene únicamente los datos mediante sentencias INSERT, para incorporarlos a una
  base de datos cuya estructura ya exista.

Al ejecutar la aplicación en modo consola, Hibernate crea automáticamente las tablas necesarias en la base de datos si
todavía no existen, según la configuración de persistencia.

## Requisitos

- JDK 19.
- MariaDB.
- IntelliJ IDEA (recomendado).

## Ejecución de la aplicación

La aplicación se puede ejecutar de dos formas:

### 1. Modo consola

Permite utilizar la aplicación mediante menús de texto y acceder a algunas funcionalidades adicionales, como las
búsquedas avanzadas de entidades por diferentes parámetros.

Para ejecutarla, iniciar la clase principal de la aplicación desde IntelliJ IDEA.

### 2. Modo interfaz gráfica (JavaFX)

Permite gestionar la aplicación mediante una interfaz gráfica.

Existen dos formas de iniciarla:

- **Desde la terminal:** ejecutar el siguiente comando desde la carpeta del proyecto:

```bash
.\mvnw.cmd javafx:run
```

- **Desde IntelliJ IDEA:** abrir el panel Maven, desplegar `Plugins` → `javafx` y ejecutar la opción `javafx:run`.

### Configuración previa

Antes de ejecutar cualquiera de las modalidades:

1. Disponer de una instancia de MariaDB.
2. Configurar los datos de conexión en `persistence.xml`.
3. Comprobar que Maven dispone de las dependencias necesarias.

## Exportaciones y archivos generados

Dentro de la carpeta `Proyecto/datos/`, situada al mismo nivel que `src/`, se almacenan los archivos generados por la
aplicación.

Esta carpeta contiene tres subcarpetas:

- **json/**: exportaciones de entidades e informes en formato JSON.
- **xml/**: exportaciones de entidades e informes en formato XML.
- **metadatos/**: información sobre la estructura y los metadatos de la base de datos.

## Sistema de logging

Los registros de actividad y errores de la aplicación se almacenan en la carpeta `Proyecto/logs/`, situada al mismo
nivel que `src/`.

Los logs se organizan en diferentes archivos, generándose un archivo por cada día de actividad.

El archivo `aplicacion.log` corresponde al registro actual o más reciente de la aplicación.

## Pruebas

El proyecto incluye pruebas automatizadas desarrolladas con JUnit 5 para comprobar el funcionamiento de las operaciones
CRUD, las búsquedas, la integridad referencial y las exportaciones XML y JSON.

Para ejecutar las pruebas:

1. Abrir el proyecto en IntelliJ IDEA.
2. Localizar la carpeta `test`, situada al mismo nivel que `main` dentro de `src`.
3. Abrir el archivo `AppTest`.
4. Ejecutar las pruebas desde IntelliJ IDEA utilizando la opción de ejecución del propio archivo.