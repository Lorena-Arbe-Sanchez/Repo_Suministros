# Gestión de Proyectos y Suministros

## Descripción

Aplicación desarrollada en Java para gestionar proyectos, proveedores, piezas y suministros mediante Hibernate y JPQL,
utilizando una base de datos MariaDB.

## Funcionalidades

- Gestión de proveedores (CRUD).
- Gestión de piezas (CRUD).
- Gestión de proyectos (CRUD).
- Gestión de suministros (CRUD).
- Consultas y búsquedas mediante JPQL.
- Control de relaciones e integridad referencial.
- Consulta de metadatos de la base de datos.
- Exportación de datos a XML y JSON.
- Pruebas automatizadas con JUnit.

## Tecnologías utilizadas

- Java 19
- Maven
- Hibernate ORM 6
- Jakarta Persistence (JPA)
- JPQL
- MariaDB
- JUnit 5
- Gson

## Base de datos

El proyecto utiliza MariaDB. La configuración de conexión se encuentra en `persistence.xml`.

## Ejecución

1. Clonar el repositorio.
2. Abrir el proyecto con IntelliJ IDEA.
3. Configurar la conexión a MariaDB.
4. Comprobar las dependencias Maven.
5. Ejecutar la aplicación desde la clase principal.