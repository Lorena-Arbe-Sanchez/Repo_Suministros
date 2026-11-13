# Gestión de Proyectos y Suministros

## Descripción

Aplicación desarrollada en Java para gestionar proyectos, proveedores, piezas y suministros mediante Hibernate ORM y
JPQL, utilizando una base de datos MariaDB.

La aplicación dispone de una interfaz gráfica desarrollada con JavaFX y permite realizar operaciones CRUD, búsquedas,
consultas estadísticas y exportaciones de datos.

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
- Búsquedas avanzadas de suministros mediante diferentes criterios.
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

## Base de datos

El proyecto utiliza una base de datos MariaDB llamada `ad_suministros`.

La configuración de conexión se encuentra en el archivo `persistence.xml`, dentro de los recursos del proyecto.

Es necesario disponer de una instancia de MariaDB y configurar correctamente los datos de conexión antes de ejecutar la
aplicación.

## Requisitos

- JDK 19.
- MariaDB.
- IntelliJ IDEA (recomendado).

## Ejecución

1. Clonar el repositorio.
2. Abrir el proyecto con IntelliJ IDEA.
3. Crear la base de datos `ad_suministros` en MariaDB.
4. Configurar los datos de conexión en `persistence.xml`.
5. Comprobar que Maven dispone de las dependencias necesarias.
6. Ejecutar la aplicación desde la terminal:

```bash
.\mvnw.cmd javafx:run
```

## Pruebas

El proyecto incluye pruebas automatizadas desarrolladas con JUnit 5 para comprobar el funcionamiento de las operaciones
CRUD, las búsquedas, la integridad referencial y las exportaciones XML y JSON.

Para ejecutar las pruebas desde la terminal:

```bash
.\mvnw.cmd test
```