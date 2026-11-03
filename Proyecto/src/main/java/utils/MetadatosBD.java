package utils;

import dao.Dao;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.mariadb.jdbc.DatabaseMetaData;

import java.io.FileWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class MetadatosBD {

    public static void exportarMetadatos() {

        EntityManager em = Dao.crearEntityManager();

        try {
            em.unwrap(Session.class).doWork(conexion -> {

                DatabaseMetaData metadata = conexion.getMetaData();

                try (FileWriter fichero = new FileWriter(
                        "datos/metadatos/metadatos_bbdd.txt")) {

                    fichero.write("INFORMACIÓN DE LA BASE DE DATOS\n");
                    fichero.write("--------------------------------\n");

                    fichero.write("Base de datos: "
                            + metadata.getDatabaseProductName() + "\n");

                    fichero.write("Versión: "
                            + metadata.getDatabaseProductVersion() + "\n");

                    fichero.write("Driver: "
                            + metadata.getDriverName() + "\n");

                    fichero.write("URL: "
                            + metadata.getURL() + "\n");

                    fichero.write("\nTABLAS\n");

                    try (ResultSet tablas = metadata.getTables(
                            conexion.getCatalog(), null, "%",
                            new String[]{"TABLE"})) {

                        while (tablas.next()) {

                            String tabla = tablas.getString("TABLE_NAME");

                            fichero.write("\nTabla: " + tabla + "\n");

                            // Claves primarias.
                            try (ResultSet claves = metadata.getPrimaryKeys(
                                    conexion.getCatalog(), null, tabla)) {

                                while (claves.next()) {
                                    fichero.write("  PK: "
                                            + claves.getString("COLUMN_NAME")
                                            + "\n");
                                }
                            }

                            // Columnas.
                            try (ResultSet columnas = metadata.getColumns(
                                    conexion.getCatalog(), null, tabla, "%")) {

                                while (columnas.next()) {
                                    fichero.write("  Columna: "
                                            + columnas.getString("COLUMN_NAME")
                                            + " | Tipo: "
                                            + columnas.getString("TYPE_NAME")
                                            + "\n");
                                }
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("Error al exportar los metadatos: "
                    + e.getMessage());

        } finally {
            em.close();
        }
    }
}