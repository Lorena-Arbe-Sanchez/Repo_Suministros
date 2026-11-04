package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Dao;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExportarDatos {

    // Exportar proveedores a XML
    public static void exportarProveedoresXML() {
        exportarXML("proveedor", "Proveedores.xml");
    }

    // Exportar proveedores a JSON
    public static void exportarProveedoresJSON() {
        exportarJSON("proveedor", "Proveedores.json");
    }

    // Exportar piezas a XML
    public static void exportarPiezasXML() {
        exportarXML("pieza", "Piezas.xml");
    }

    // Exportar piezas a JSON
    public static void exportarPiezasJSON() {
        exportarJSON("pieza", "Piezas.json");
    }

    // Exportar proyectos a XML
    public static void exportarProyectosXML() {
        exportarXML("proyecto", "Proyectos.xml");
    }

    // Exportar proyectos a JSON
    public static void exportarProyectosJSON() {
        exportarJSON("proyecto", "Proyectos.json");
    }

    // Exportar suministros a XML
    public static void exportarSuministrosXML() {
        exportarXML("suministro", "Suministros.xml");
    }

    // Exportar suministros a JSON
    public static void exportarSuministrosJSON() {
        exportarJSON("suministro", "Suministros.json");
    }

    // Exportar todas las entidades a XML
    public static void exportarTodoXML() {
        exportarProveedoresXML();
        exportarPiezasXML();
        exportarProyectosXML();
        exportarSuministrosXML();
    }

    // Exportar todas las entidades a JSON
    public static void exportarTodoJSON() {
        exportarProveedoresJSON();
        exportarPiezasJSON();
        exportarProyectosJSON();
        exportarSuministrosJSON();
    }

    // Exportar todas las entidades a XML y JSON
    public static void exportarTodoXMLyJSON() {
        exportarTodoXML();
        exportarTodoJSON();
    }

    // Obtener los registros de una tabla
    private static List<Map<String, Object>> obtenerRegistros(String tabla)
            throws SQLException {

        EntityManager em = Dao.crearEntityManager();

        try {
            Session session = em.unwrap(Session.class);

            return session.doReturningWork(conexion -> {

                List<Map<String, Object>> registros = new ArrayList<>();

                String consulta = "SELECT * FROM " + tabla;

                try (Statement sentencia = conexion.createStatement();
                     ResultSet resultado = sentencia.executeQuery(consulta)) {

                    ResultSetMetaData metadatos = resultado.getMetaData();
                    int numeroColumnas = metadatos.getColumnCount();

                    while (resultado.next()) {

                        Map<String, Object> registro = new LinkedHashMap<>();

                        for (int i = 1; i <= numeroColumnas; i++) {

                            String columna = metadatos.getColumnName(i);
                            Object valor = resultado.getObject(i);

                            // Convertir los valores de fecha y hora a texto
                            if (valor instanceof Date ||
                                    valor instanceof Time ||
                                    valor instanceof Timestamp) {
                                valor = valor.toString();
                            }

                            registro.put(columna, valor);
                        }

                        registros.add(registro);
                    }
                }

                return registros;
            });

        } finally {
            em.close();
        }
    }

    // Exportar una tabla a XML
    private static void exportarXML(String tabla, String nombreArchivo) {

        try {
            List<Map<String, Object>> registros = obtenerRegistros(tabla);

            // Crear la carpeta si todavía no existe
            Path carpeta = Paths.get("datos", "xml");
            Files.createDirectories(carpeta);

            Path archivo = carpeta.resolve(nombreArchivo);

            // Crear el escritor XML
            XMLOutputFactory fabrica = XMLOutputFactory.newInstance();

            try (OutputStream salida = Files.newOutputStream(archivo)) {

                XMLStreamWriter escritor = fabrica.createXMLStreamWriter(salida, "UTF-8");

                // Inicio del documento XML
                escritor.writeStartDocument("UTF-8", "1.0");
                escritor.writeStartElement("registros");

                // Recorrer los registros de la tabla
                for (Map<String, Object> registro : registros) {

                    escritor.writeStartElement("registro");

                    // Recorrer las columnas de cada registro
                    for (Map.Entry<String, Object> columna : registro.entrySet()) {

                        escritor.writeStartElement(columna.getKey());

                        Object valor = columna.getValue();

                        if (valor != null) {
                            escritor.writeCharacters(valor.toString());
                        }

                        escritor.writeEndElement();
                    }

                    escritor.writeEndElement();
                }

                // Fin del documento XML
                escritor.writeEndElement();
                escritor.writeEndDocument();

                escritor.close();
            }

            System.out.println("✅ Archivo XML exportado: " + archivo);

        } catch (Exception e) {
            System.out.println("❌ Error al exportar XML: " + e.getMessage());
        }
    }

    // Exportar una tabla a JSON
    private static void exportarJSON(String tabla, String nombreArchivo) {

        try {
            List<Map<String, Object>> registros = obtenerRegistros(tabla);

            Path carpeta = Paths.get("datos", "json");
            Files.createDirectories(carpeta);

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            String contenido = gson.toJson(registros);

            Path archivo = carpeta.resolve(nombreArchivo);
            Files.writeString(archivo, contenido);

            System.out.println("✅ Archivo JSON exportado: " + archivo);

        } catch (Exception e) {
            System.out.println("❌ Error al exportar JSON: " + e.getMessage());
        }
    }
}