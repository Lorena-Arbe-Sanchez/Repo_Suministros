import dao.*;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExportarDatos;
import utils.MetadatosBD;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.info("Iniciando la aplicación");

        ProveedorDao proveedorDao = null;
        PiezaDao piezaDao = null;
        ProyectoDao proyectoDao = null;
        SuministroDao suministroDao = null;

        try (Scanner sc = new Scanner(System.in)) {

            // Inicializar los DAO
            proveedorDao = new ProveedorDao();
            piezaDao = new PiezaDao();
            proyectoDao = new ProyectoDao();
            suministroDao = new SuministroDao();

            System.out.println("🔌 Conexión con la base de datos establecida.");
            System.out.println("✅ Entidades inicializadas correctamente.");
            System.out.println("🟡 Los mensajes de color amarillo corresponden al registro de " +
                    "eventos (logs) de la aplicación.");
            System.out.println("ℹ️ Puedes escribir 'FIN' en cualquier momento para finalizar la aplicación.");

            // Menú principal
            int opcion;

            do {

                System.out.println("\n\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestión de proveedores");
                System.out.println("2. Gestión de piezas");
                System.out.println("3. Gestión de proyectos");
                System.out.println("4. Gestión de suministros");
                System.out.println("5. Exportar datos a XML y JSON");
                System.out.println("6. Metadatos de la BBDD");
                System.out.println("7. Análisis y estadísticas");
                System.out.println("8. Ayuda");
                System.out.println("0. Salir");
                System.out.print("\nElige una opción: ");

                opcion = leerInt(sc);

                switch (opcion) {
                    case 1:
                        menuProveedores(sc, proveedorDao);
                        break;
                    case 2:
                        menuPiezas(sc, piezaDao);
                        break;
                    case 3:
                        menuProyectos(sc, proyectoDao);
                        break;
                    case 4:
                        menuSuministros(sc, suministroDao, proveedorDao, piezaDao, proyectoDao);
                        break;
                    case 5:
                        menuExportaciones(sc);
                        break;
                    case 6:
                        MetadatosBD.mostrarMetadatos();
                        MetadatosBD.exportarMetadatos();
                        break;
                    case 7:
                        menuAnalisis(sc, suministroDao, proveedorDao, proyectoDao, piezaDao);
                        break;
                    case 8:
                        mostrarAyuda();
                        break;
                    case 0:
                        System.out.println("👋 ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("⚠️ Opción no válida.");
                }

            } while (opcion != 0);

        } catch (FinAplicacionException e) {
            logger.info("Aplicación finalizada por el usuario");
            System.out.println("\n👋 Aplicación finalizada por el usuario.");

        } catch (Exception e) {
            logger.error("Error inesperado en la aplicación", e);
            System.out.println("❌ Error: " + e.getMessage());

        } finally {

            // Cerrar los EntityManager
            if (proveedorDao != null) proveedorDao.cerrar();
            if (piezaDao != null) piezaDao.cerrar();
            if (proyectoDao != null) proyectoDao.cerrar();
            if (suministroDao != null) suministroDao.cerrar();

            // Cerrar la fábrica de EntityManager
            Dao.cerrarFabrica();
        }

        logger.info("Aplicación finalizada");
    }

    // Menú de proveedores
    private static void menuProveedores(Scanner sc, ProveedorDao dao) {

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE PROVEEDORES =====");
            System.out.println("1. Listar proveedores");
            System.out.println("2. Dar de alta un proveedor");
            System.out.println("3. Modificar un proveedor");
            System.out.println("4. Dar de baja un proveedor");
            System.out.println("5. Buscar proveedores");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    listarProveedores(dao);
                    break;
                case 2:
                    crearProveedor(sc, dao);
                    break;
                case 3:
                    modificarProveedor(sc, dao);
                    break;
                case 4:
                    eliminarProveedor(sc, dao);
                    break;
                case 5:
                    buscarProveedores(sc, dao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de piezas
    private static void menuPiezas(Scanner sc, PiezaDao dao) {

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE PIEZAS =====");
            System.out.println("1. Listar piezas");
            System.out.println("2. Dar de alta una pieza");
            System.out.println("3. Modificar una pieza");
            System.out.println("4. Dar de baja una pieza");
            System.out.println("5. Buscar piezas");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    listarPiezas(dao);
                    break;
                case 2:
                    crearPieza(sc, dao);
                    break;
                case 3:
                    modificarPieza(sc, dao);
                    break;
                case 4:
                    eliminarPieza(sc, dao);
                    break;
                case 5:
                    buscarPiezas(sc, dao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de proyectos
    private static void menuProyectos(Scanner sc, ProyectoDao dao) {

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE PROYECTOS =====");
            System.out.println("1. Listar proyectos");
            System.out.println("2. Dar de alta un proyecto");
            System.out.println("3. Modificar un proyecto");
            System.out.println("4. Dar de baja un proyecto");
            System.out.println("5. Buscar proyectos");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    listarProyectos(dao);
                    break;
                case 2:
                    crearProyecto(sc, dao);
                    break;
                case 3:
                    modificarProyecto(sc, dao);
                    break;
                case 4:
                    eliminarProyecto(sc, dao);
                    break;
                case 5:
                    buscarProyectos(sc, dao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de suministros
    private static void menuSuministros(Scanner sc, SuministroDao dao, ProveedorDao proveedorDao,
                                        PiezaDao piezaDao, ProyectoDao proyectoDao) {

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE SUMINISTROS =====");
            System.out.println("1. Listar suministros");
            System.out.println("2. Registrar un suministro");
            System.out.println("3. Modificar un suministro");
            System.out.println("4. Eliminar un suministro");
            System.out.println("5. Buscar suministros");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    listarSuministros(dao);
                    break;
                case 2:
                    crearSuministro(sc, dao, proveedorDao, piezaDao, proyectoDao);
                    break;
                case 3:
                    modificarSuministro(sc, dao, proveedorDao, piezaDao, proyectoDao);
                    break;
                case 4:
                    eliminarSuministro(sc, dao);
                    break;
                case 5:
                    buscarSuministros(sc, dao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de exportaciones
    private static void menuExportaciones(Scanner sc) {

        int opcion;

        do {

            System.out.println("\n\n===== EXPORTAR DATOS =====");
            System.out.println("1. Exportar proveedores a XML");
            System.out.println("2. Exportar proveedores a JSON");
            System.out.println("3. Exportar piezas a XML");
            System.out.println("4. Exportar piezas a JSON");
            System.out.println("5. Exportar proyectos a XML");
            System.out.println("6. Exportar proyectos a JSON");
            System.out.println("7. Exportar suministros a XML");
            System.out.println("8. Exportar suministros a JSON");
            System.out.println("9. Exportar todo a XML");
            System.out.println("10. Exportar todo a JSON");
            System.out.println("11. Exportar todo a XML y JSON");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    ExportarDatos.exportarProveedoresXML();
                    break;
                case 2:
                    ExportarDatos.exportarProveedoresJSON();
                    break;
                case 3:
                    ExportarDatos.exportarPiezasXML();
                    break;
                case 4:
                    ExportarDatos.exportarPiezasJSON();
                    break;
                case 5:
                    ExportarDatos.exportarProyectosXML();
                    break;
                case 6:
                    ExportarDatos.exportarProyectosJSON();
                    break;
                case 7:
                    ExportarDatos.exportarSuministrosXML();
                    break;
                case 8:
                    ExportarDatos.exportarSuministrosJSON();
                    break;
                case 9:
                    ExportarDatos.exportarTodoXML();
                    break;
                case 10:
                    ExportarDatos.exportarTodoJSON();
                    break;
                case 11:
                    ExportarDatos.exportarTodoXMLyJSON();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de análisis y estadísticas
    private static void menuAnalisis(Scanner sc, SuministroDao suministroDao, ProveedorDao proveedorDao,
                                     ProyectoDao proyectoDao, PiezaDao piezaDao) {

        int opcion;

        do {

            System.out.println("\n\n===== ANÁLISIS Y ESTADÍSTICAS =====");
            System.out.println("1. Histórico de proveedor");
            System.out.println("2. Análisis de proyectos");
            System.out.println("3. Estadísticas globales");
            System.out.println("0. ↩️ Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    historicoProveedor(sc, suministroDao, proveedorDao);
                    break;
                case 2:
                    analisisProyecto(sc, suministroDao, proyectoDao);
                    break;
                case 3:
                    estadisticasGlobales(suministroDao, proveedorDao, piezaDao, proyectoDao);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Consultar el histórico de suministros de un proveedor
    private static void historicoProveedor(Scanner sc, SuministroDao suministroDao, ProveedorDao proveedorDao) {

        System.out.println("\n===== HISTÓRICO DE PROVEEDOR =====");

        // Comprobar que existe el proveedor
        Proveedor proveedor;

        do {
            String codProveedor = leerTexto(sc, "Código del proveedor: ");
            proveedor = proveedorDao.find(codProveedor);

            if (proveedor == null) {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }

        } while (proveedor == null);

        // Introducir el periodo
        LocalDate desde = leerFecha(sc, "Fecha inicial (AAAA-MM-DD): ");
        LocalDate hasta;

        do {
            hasta = leerFecha(sc, "Fecha final (AAAA-MM-DD): ");

            if (hasta.isBefore(desde)) {
                System.out.println("⚠️ La fecha final no puede ser anterior a la inicial.");
            }

        } while (hasta.isBefore(desde));

        // Consultar el histórico
        List<Suministro> suministros = suministroDao.buscarHistoricoProveedor(proveedor.getCod(), desde, hasta);

        System.out.println("\n===== HISTÓRICO DE " + proveedor.getNombre().toUpperCase() + " =====");

        System.out.println("Periodo: " + desde + " - " + hasta);

        if (suministros.isEmpty()) {
            System.out.println("\nNo se han encontrado suministros durante este periodo.");
            return;
        }

        // Mostrar los suministros y calcular las cantidades
        long cantidadTotal = 0;

        System.out.printf("%n%-12s %-20s %-45s %10s%n",
                "Fecha", "Pieza", "Proyecto", "Cantidad");

        System.out.println("-------------------------------------------------------------------------------------------");

        for (Suministro s : suministros) {

            System.out.printf("%-12s %-20s %-45s %10d%n",
                    s.getFecha(),
                    s.getPieza().getNombre(),
                    s.getProyecto().getNombre(),
                    s.getCantidad()
            );

            cantidadTotal += s.getCantidad();
        }

        System.out.println("\nTotal de suministros: " + suministros.size());
        System.out.println("Cantidad total suministrada: " + cantidadTotal);
    }

    // Consultar el análisis de recursos utilizados en un proyecto
    private static void analisisProyecto(Scanner sc, SuministroDao suministroDao, ProyectoDao proyectoDao) {

        System.out.println("\n===== ANÁLISIS DE PROYECTO =====");

        // Comprobar que existe el proyecto
        Proyecto proyecto;

        do {
            String codProyecto = leerTexto(sc, "Código del proyecto: ");
            proyecto = proyectoDao.find(codProyecto);

            if (proyecto == null) {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }

        } while (proyecto == null);

        // Consultar los suministros del proyecto
        List<Suministro> suministros = suministroDao.buscarAnalisisProyecto(proyecto.getCod());

        System.out.println("\n===== ANÁLISIS DE " + proyecto.getNombre().toUpperCase() + " =====");
        System.out.println("Ciudad: " + proyecto.getCiudad());

        if (suministros.isEmpty()) {
            System.out.println("\nEste proyecto todavía no tiene suministros.");
            return;
        }

        // Mostrar los suministros y calcular los recursos utilizados
        java.util.Set<String> proveedores = new java.util.HashSet<>();
        java.util.Set<String> piezas = new java.util.HashSet<>();

        long cantidadTotal = 0;
        double costeTotal = 0;

        System.out.printf("%n%-12s %-20s %-25s %-45s %10s %12s%n",
                "Fecha", "Pieza", "Proveedor", "Proyecto", "Cantidad", "Coste");

        System.out.println("----------------------------------------------------------------------------------------" +
                "--------------------------------------------");

        for (Suministro s : suministros) {

            double coste = s.getPieza().getPrecio() * s.getCantidad();

            System.out.printf("%-12s %-20s %-25s %-45s %10d %12.2f €%n",
                    s.getFecha(),
                    s.getPieza().getNombre(),
                    s.getProveedor().getNombre(),
                    s.getProyecto().getNombre(),
                    s.getCantidad(),
                    coste
            );

            proveedores.add(s.getProveedor().getCod());
            piezas.add(s.getPieza().getCod());

            cantidadTotal += s.getCantidad();
            costeTotal += coste;
        }

        System.out.println("\n===== RESUMEN DEL PROYECTO =====");
        System.out.println("Número de proveedores: " + proveedores.size());
        System.out.println("Número de piezas utilizadas: " + piezas.size());
        System.out.println("Número de suministros: " + suministros.size());
        System.out.println("Cantidad total suministrada: " + cantidadTotal);
        System.out.printf("Coste total estimado: %.2f €%n", costeTotal);
    }

    // Consultar las estadísticas globales
    private static void estadisticasGlobales(SuministroDao suministroDao, ProveedorDao proveedorDao,
                                             PiezaDao piezaDao, ProyectoDao proyectoDao) {

        System.out.println("\n===== ESTADÍSTICAS GLOBALES =====");

        // Obtener todos los registros
        List<Proveedor> proveedores = proveedorDao.findAll();
        List<Pieza> piezas = piezaDao.findAll();
        List<Proyecto> proyectos = proyectoDao.findAll();
        List<Suministro> suministros = suministroDao.buscarTodosParaEstadisticas();

        // Mostrar totales generales
        System.out.println("\n===== RESUMEN GENERAL =====");
        System.out.println("Total de proveedores: " + proveedores.size());
        System.out.println("Total de piezas: " + piezas.size());
        System.out.println("Total de proyectos: " + proyectos.size());
        System.out.println("Total de suministros: " + suministros.size());

        // Almacenar los suministros realizados por proveedor, pieza y proyecto
        Map<String, Integer> suministrosPorProveedor = new HashMap<>();
        Map<String, Integer> cantidadesPorPieza = new HashMap<>();
        Map<String, Integer> suministrosPorProyecto = new HashMap<>();

        long cantidadTotal = 0;

        for (Suministro s : suministros) {

            suministrosPorProveedor.merge(s.getProveedor().getCod(), 1, Integer::sum);
            cantidadesPorPieza.merge(s.getPieza().getCod(), s.getCantidad(), Integer::sum);
            suministrosPorProyecto.merge(s.getProyecto().getCod(), 1, Integer::sum);

            cantidadTotal += s.getCantidad();
        }

        System.out.println("Cantidad total suministrada: " + cantidadTotal);

        if (suministros.isEmpty()) {
            System.out.println("\nNo hay suministros registrados para generar estadísticas.");
            return;
        }

        // Calcular los valores máximos
        int maxProveedor = 0;
        int maxPieza = 0;
        int maxProyecto = 0;

        for (int cantidad : suministrosPorProveedor.values()) {
            if (cantidad > maxProveedor) {
                maxProveedor = cantidad;
            }
        }

        for (int cantidad : cantidadesPorPieza.values()) {
            if (cantidad > maxPieza) {
                maxPieza = cantidad;
            }
        }

        for (int cantidad : suministrosPorProyecto.values()) {
            if (cantidad > maxProyecto) {
                maxProyecto = cantidad;
            }
        }

        // Mostrar los proveedores más activos
        System.out.println("\n===== PROVEEDORES MÁS ACTIVOS =====");

        for (Proveedor p : proveedores) {

            int cantidad = suministrosPorProveedor.getOrDefault(p.getCod(), 0);

            if (cantidad == maxProveedor) {
                System.out.println(p.getCod() + " - " + p.getNombre() + " " +
                        p.getApellido1() + ": " + cantidad + " suministros");
            }
        }

        // Mostrar las piezas más utilizadas
        System.out.println("\n===== PIEZAS MÁS UTILIZADAS =====");

        for (Pieza p : piezas) {

            int cantidad = cantidadesPorPieza.getOrDefault(p.getCod(), 0);

            if (cantidad == maxPieza) {
                System.out.println(p.getCod() + " - " + p.getNombre() + ": " +
                        cantidad + " unidades");
            }
        }

        // Mostrar los proyectos con más suministros
        System.out.println("\n===== PROYECTOS CON MÁS SUMINISTROS =====");

        for (Proyecto p : proyectos) {

            int cantidad = suministrosPorProyecto.getOrDefault(p.getCod(), 0);

            if (cantidad == maxProyecto) {
                System.out.println(p.getCod() + " - " + p.getNombre() + ": " +
                        cantidad + " suministros");
            }
        }
    }

    // Mostrar información de ayuda
    private static void mostrarAyuda() {

        System.out.println("\n\n===== AYUDA =====");

        System.out.println("\n¿QUÉ ES ESTA APLICACIÓN?");
        System.out.println("Aplicación para gestionar proveedores, piezas, proyectos y suministros.");
        System.out.println("Permite realizar operaciones CRUD, búsquedas y exportaciones de datos.");

        System.out.println("\n1. GESTIÓN DE PROVEEDORES");
        System.out.println("Permite registrar, consultar, modificar, eliminar y buscar proveedores.");
        System.out.println("Los proveedores no se pueden eliminar si tienen suministros asociados.");

        System.out.println("\n2. GESTIÓN DE PIEZAS");
        System.out.println("Permite registrar, consultar, modificar, eliminar y buscar piezas.");
        System.out.println("Las piezas no se pueden eliminar si tienen suministros asociados.");

        System.out.println("\n3. GESTIÓN DE PROYECTOS");
        System.out.println("Permite registrar, consultar, modificar, eliminar y buscar proyectos.");
        System.out.println("Los proyectos no se pueden eliminar si tienen suministros asociados.");

        System.out.println("\n4. GESTIÓN DE SUMINISTROS");
        System.out.println("Permite registrar, consultar, modificar, eliminar y buscar suministros.");
        System.out.println("Cada suministro debe estar asociado a un proveedor, una pieza y un proyecto.");
        System.out.println("El ID de cada suministro se genera automáticamente.");

        System.out.println("\n5. EXPORTAR DATOS XML Y JSON");
        System.out.println("Permite exportar los datos de cada entidad por separado o todos juntos.");
        System.out.println("Los archivos generados se guardan en los directorios correspondientes.");

        System.out.println("\n6. METADATOS DE LA BBDD");
        System.out.println("Permite consultar y exportar información sobre la estructura de la base de datos.");

        System.out.println("\n0. SALIR");
        System.out.println("Cierra la aplicación y libera los recursos utilizados.");

        System.out.println("\nNOTA: Para volver al menú principal, utiliza la opción 0 de cada submenú.");

        System.out.println("\nFINALIZAR LA APLICACIÓN");
        System.out.println("Puedes escribir 'FIN' en cualquier momento que se solicite un dato.");
        System.out.println("La aplicación finalizará inmediatamente y cerrará los recursos utilizados.");
    }

    // Listar todos los proveedores
    private static void listarProveedores(ProveedorDao dao) {

        List<Proveedor> proveedores = dao.findAll();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
            return;
        }

        for (Proveedor p : proveedores) {
            System.out.println("\nCódigo: " + p.getCod());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Primer apellido: " + p.getApellido1());
            System.out.println("Segundo apellido: " + p.getApellido2());
            System.out.println("Dirección: " + p.getDireccion());
        }
    }

    // Crear un proveedor
    private static void crearProveedor(Scanner sc, ProveedorDao dao) {

        String cod;

        do {
            cod = leerTexto(sc, "Código: ");

            if (dao.find(cod) != null) {
                System.out.println("⚠️ Ya existe un proveedor con ese código.");
            }
        } while (dao.find(cod) != null);

        String nombre = leerTexto(sc, "Nombre: ");
        String apellido1 = leerTexto(sc, "Primer apellido: ");
        String apellido2 = leerTexto(sc, "Segundo apellido: ");
        String direccion = leerTexto(sc, "Dirección: ");

        Proveedor proveedor = new Proveedor(cod, nombre, apellido1, apellido2, direccion, null);

        dao.create(proveedor);

        System.out.println("✅ Proveedor creado correctamente.");
    }

    // Modificar un proveedor
    private static void modificarProveedor(Scanner sc, ProveedorDao dao) {

        String cod;
        Proveedor proveedor;

        do {
            cod = leerTexto(sc, "Introduce el código del proveedor: ");
            proveedor = dao.find(cod);

            if (proveedor == null) {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }

        } while (proveedor == null);

        System.out.print("Nuevo nombre (" + proveedor.getNombre() + "): ");
        String nombre = leerEntrada(sc);
        if (!nombre.trim().isEmpty()) {
            proveedor.setNombre(nombre);
        }

        System.out.print("Nuevo primer apellido (" + proveedor.getApellido1() + "): ");
        String apellido1 = leerEntrada(sc);
        if (!apellido1.trim().isEmpty()) {
            proveedor.setApellido1(apellido1);
        }

        System.out.print("Nuevo segundo apellido (" + proveedor.getApellido2() + "): ");
        String apellido2 = leerEntrada(sc);
        if (!apellido2.trim().isEmpty()) {
            proveedor.setApellido2(apellido2);
        }

        System.out.print("Nueva dirección (" + proveedor.getDireccion() + "): ");
        String direccion = leerEntrada(sc);
        if (!direccion.trim().isEmpty()) {
            proveedor.setDireccion(direccion);
        }

        dao.update(proveedor);

        System.out.println("✅ Proveedor modificado correctamente.");
    }

    // Eliminar un proveedor
    private static void eliminarProveedor(Scanner sc, ProveedorDao dao) {

        String cod;
        Proveedor proveedor;

        do {
            cod = leerTexto(sc, "Introduce el código del proveedor: ");
            proveedor = dao.find(cod);

            if (proveedor == null) {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }

        } while (proveedor == null);

        String respuesta = leerTexto(sc, "¿Seguro que quieres eliminarlo? (s/n): ");

        if (respuesta.equalsIgnoreCase("s")) {
            if (dao.delete(proveedor)) {
                System.out.println("✅ Proveedor eliminado correctamente.");
            } else {
                System.out.println("❌ No se puede eliminar el proveedor porque tiene suministros asociados.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Menú de búsquedas de proveedores
    private static void buscarProveedores(Scanner sc, ProveedorDao dao) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE PROVEEDORES =====");
            System.out.println("1. Por código");
            System.out.println("2. Por nombre");
            System.out.println("3. Por primer apellido");
            System.out.println("4. Por segundo apellido");
            System.out.println("5. Por dirección");
            System.out.println("6. Por pieza suministrada");
            System.out.println("0. ↩️ Volver");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            List<Proveedor> proveedores = null;

            switch (opcion) {
                case 1:
                    proveedores = dao.buscarPorCod(leerTexto(sc, "Código: "));
                    break;
                case 2:
                    proveedores = dao.buscarPorNombre(leerTexto(sc, "Nombre: "));
                    break;
                case 3:
                    proveedores = dao.buscarPorApellido1(leerTexto(sc, "Primer apellido: "));
                    break;
                case 4:
                    proveedores = dao.buscarPorApellido2(leerTexto(sc, "Segundo apellido: "));
                    break;
                case 5:
                    proveedores = dao.buscarPorDireccion(leerTexto(sc, "Dirección: "));
                    break;
                case 6:
                    proveedores = dao.buscarPorPieza(leerTexto(sc, "Código o nombre de la pieza: "));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

            if (proveedores != null) {
                if (proveedores.isEmpty()) {
                    System.out.println("No se han encontrado proveedores.");
                } else {
                    for (Proveedor p : proveedores) {
                        System.out.println("\nCódigo: " + p.getCod());
                        System.out.println("Nombre: " + p.getNombre());
                        System.out.println("Primer apellido: " + p.getApellido1());
                        System.out.println("Segundo apellido: " + p.getApellido2());
                        System.out.println("Dirección: " + p.getDireccion());
                    }
                }
            }

        } while (opcion != 0);
    }

    // Listar todas las piezas
    private static void listarPiezas(PiezaDao dao) {
        List<Pieza> piezas = dao.findAll();

        if (piezas.isEmpty()) {
            System.out.println("No hay piezas registradas.");
            return;
        }

        for (Pieza p : piezas) {
            System.out.println("\nCódigo: " + p.getCod());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Precio: " + p.getPrecio());
            System.out.println("Descripción: " + p.getDescripcion());
        }
    }

    // Crear una pieza
    private static void crearPieza(Scanner sc, PiezaDao dao) {
        String cod;
        Pieza piezaExistente;

        do {
            cod = leerTexto(sc, "Código: ");
            piezaExistente = dao.find(cod);

            if (piezaExistente != null) {
                System.out.println("⚠️ Ya existe una pieza con ese código.");
            }
        } while (piezaExistente != null);

        String nombre = leerTexto(sc, "Nombre: ");
        double precio = leerPrecio(sc, "Precio: ");
        String descripcion = leerTexto(sc, "Descripción: ");

        Pieza pieza = new Pieza(cod, nombre, precio, descripcion, null);

        dao.create(pieza);
        System.out.println("✅ Pieza creada correctamente.");
    }

    // Modificar una pieza
    private static void modificarPieza(Scanner sc, PiezaDao dao) {
        String cod;
        Pieza pieza;

        do {
            cod = leerTexto(sc, "Introduce el código de la pieza: ");
            pieza = dao.find(cod);

            if (pieza == null) {
                System.out.println("⚠️ No existe ninguna pieza con ese código.");
            }
        } while (pieza == null);

        System.out.print("Nuevo nombre (" + pieza.getNombre() + "): ");
        String nombre = leerEntrada(sc);
        if (!nombre.trim().isEmpty()) pieza.setNombre(nombre.trim());

        System.out.print("Nuevo precio (" + pieza.getPrecio() + "): ");
        String precio = leerEntrada(sc);

        while (!precio.trim().isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(precio);

                if (nuevoPrecio > 0) {
                    pieza.setPrecio(nuevoPrecio);
                    break;
                } else {
                    System.out.println("⚠️ El precio debe ser mayor que 0.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debes introducir un número válido.");
            }

            System.out.print("Vuelve a introducir el precio: ");
            precio = leerEntrada(sc);
        }

        System.out.print("Nueva descripción (" + pieza.getDescripcion() + "): ");
        String descripcion = leerEntrada(sc);
        if (!descripcion.trim().isEmpty()) pieza.setDescripcion(descripcion.trim());

        dao.update(pieza);
        System.out.println("✅ Pieza modificada correctamente.");
    }

    // Eliminar una pieza
    private static void eliminarPieza(Scanner sc, PiezaDao dao) {
        String cod;
        Pieza pieza;

        do {
            cod = leerTexto(sc, "Introduce el código de la pieza: ");
            pieza = dao.find(cod);

            if (pieza == null) {
                System.out.println("⚠️ No existe ninguna pieza con ese código.");
            }
        } while (pieza == null);

        String respuesta = leerTexto(sc, "¿Seguro que quieres eliminarla? (s/n): ");

        if (respuesta.equalsIgnoreCase("s")) {
            if (dao.delete(pieza)) {
                System.out.println("✅ Pieza eliminada correctamente.");
            } else {
                System.out.println("❌ No se puede eliminar la pieza porque tiene suministros asociados.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Menú de búsquedas de piezas
    private static void buscarPiezas(Scanner sc, PiezaDao dao) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE PIEZAS =====");
            System.out.println("1. Por código");
            System.out.println("2. Por nombre");
            System.out.println("3. Por descripción");
            System.out.println("4. Por intervalo de precios");
            System.out.println("5. Por proveedor");
            System.out.println("6. Por proyecto al que se destinan");
            System.out.println("0. ↩️ Volver");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            List<Pieza> piezas = null;

            switch (opcion) {
                case 1:
                    piezas = dao.buscarPorCod(leerTexto(sc, "Código: "));
                    break;
                case 2:
                    piezas = dao.buscarPorNombre(leerTexto(sc, "Nombre: "));
                    break;
                case 3:
                    piezas = dao.buscarPorDescripcion(leerTexto(sc, "Descripción: "));
                    break;
                case 4:
                    double precioMin = leerPrecio(sc, "Precio mínimo: ");
                    double precioMax;

                    do {
                        precioMax = leerPrecio(sc, "Precio máximo: ");

                        if (precioMax < precioMin) {
                            System.out.println("⚠️ El precio máximo no puede ser inferior al mínimo.");
                        }
                    } while (precioMax < precioMin);

                    piezas = dao.buscarPorPrecio(precioMin, precioMax);
                    break;
                case 5:
                    piezas = dao.buscarPorProveedor(leerTexto(sc, "Código o nombre del proveedor: "));
                    break;
                case 6:
                    piezas = dao.buscarPorProyecto(leerTexto(sc, "Código o nombre del proyecto: "));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

            if (piezas != null) {
                if (piezas.isEmpty()) {
                    System.out.println("No se han encontrado piezas.");
                } else {
                    for (Pieza p : piezas) {
                        System.out.println("\nCódigo: " + p.getCod());
                        System.out.println("Nombre: " + p.getNombre());
                        System.out.println("Precio: " + p.getPrecio());
                        System.out.println("Descripción: " + p.getDescripcion());
                    }
                }
            }

        } while (opcion != 0);
    }

    // Listar todos los proyectos
    private static void listarProyectos(ProyectoDao dao) {
        List<Proyecto> proyectos = dao.findAll();

        if (proyectos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
            return;
        }

        for (Proyecto p : proyectos) {
            System.out.println("\nCódigo: " + p.getCod());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Ciudad: " + p.getCiudad());
        }
    }

    // Crear un proyecto
    private static void crearProyecto(Scanner sc, ProyectoDao dao) {
        String cod;
        Proyecto proyectoExistente;

        do {
            cod = leerTexto(sc, "Código: ");
            proyectoExistente = dao.find(cod);

            if (proyectoExistente != null) {
                System.out.println("⚠️ Ya existe un proyecto con ese código.");
            }
        } while (proyectoExistente != null);

        String nombre = leerTexto(sc, "Nombre: ");
        String ciudad = leerTexto(sc, "Ciudad: ");

        Proyecto proyecto = new Proyecto(cod, nombre, ciudad, null);

        dao.create(proyecto);
        System.out.println("✅ Proyecto creado correctamente.");
    }

    // Modificar un proyecto
    private static void modificarProyecto(Scanner sc, ProyectoDao dao) {
        String cod;
        Proyecto proyecto;

        do {
            cod = leerTexto(sc, "Introduce el código del proyecto: ");
            proyecto = dao.find(cod);

            if (proyecto == null) {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }
        } while (proyecto == null);

        System.out.print("Nuevo nombre (" + proyecto.getNombre() + "): ");
        String nombre = leerEntrada(sc);
        if (!nombre.trim().isEmpty()) proyecto.setNombre(nombre.trim());

        System.out.print("Nueva ciudad (" + proyecto.getCiudad() + "): ");
        String ciudad = leerEntrada(sc);
        if (!ciudad.trim().isEmpty()) proyecto.setCiudad(ciudad.trim());

        dao.update(proyecto);
        System.out.println("✅ Proyecto modificado correctamente.");
    }

    // Eliminar un proyecto
    private static void eliminarProyecto(Scanner sc, ProyectoDao dao) {
        String cod;
        Proyecto proyecto;

        do {
            cod = leerTexto(sc, "Introduce el código del proyecto: ");
            proyecto = dao.find(cod);

            if (proyecto == null) {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }
        } while (proyecto == null);

        String respuesta = leerTexto(sc, "¿Seguro que quieres eliminarlo? (s/n): ");

        if (respuesta.equalsIgnoreCase("s")) {
            if (dao.delete(proyecto)) {
                System.out.println("✅ Proyecto eliminado correctamente.");
            } else {
                System.out.println("❌ No se puede eliminar el proyecto porque tiene suministros asociados.");
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Menú de búsquedas de proyectos
    private static void buscarProyectos(Scanner sc, ProyectoDao dao) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE PROYECTOS =====");
            System.out.println("1. Por código");
            System.out.println("2. Por nombre");
            System.out.println("3. Por ciudad");
            System.out.println("4. Por proveedor participante");
            System.out.println("5. Por pieza utilizada");
            System.out.println("0. ↩️ Volver");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            List<Proyecto> proyectos = null;

            switch (opcion) {
                case 1:
                    proyectos = dao.buscarPorCod(leerTexto(sc, "Código: "));
                    break;
                case 2:
                    proyectos = dao.buscarPorNombre(leerTexto(sc, "Nombre: "));
                    break;
                case 3:
                    proyectos = dao.buscarPorCiudad(leerTexto(sc, "Ciudad: "));
                    break;
                case 4:
                    proyectos = dao.buscarPorProveedor(leerTexto(sc, "Código o nombre del proveedor: "));
                    break;
                case 5:
                    proyectos = dao.buscarPorPieza(leerTexto(sc, "Código o nombre de la pieza: "));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

            if (proyectos != null) {
                if (proyectos.isEmpty()) {
                    System.out.println("No se han encontrado proyectos.");
                } else {
                    for (Proyecto p : proyectos) {
                        System.out.println("\nCódigo: " + p.getCod());
                        System.out.println("Nombre: " + p.getNombre());
                        System.out.println("Ciudad: " + p.getCiudad());
                    }
                }
            }

        } while (opcion != 0);
    }

    // Listar todos los suministros
    private static void listarSuministros(SuministroDao dao) {
        List<Suministro> suministros = dao.findAll();

        if (suministros.isEmpty()) {
            System.out.println("No hay suministros registrados.");
            return;
        }

        for (Suministro s : suministros) {
            System.out.println("\nID: " + s.getId());
            System.out.println("Fecha: " + s.getFecha());
            System.out.println("Cantidad: " + s.getCantidad());
            System.out.println("Proveedor: " + s.getProveedor().getCod() + " - " + s.getProveedor().getNombre());
            System.out.println("Pieza: " + s.getPieza().getCod() + " - " + s.getPieza().getNombre());
            System.out.println("Proyecto: " + s.getProyecto().getCod() + " - " + s.getProyecto().getNombre());
        }
    }

    // Crear un suministro
    private static void crearSuministro(Scanner sc, SuministroDao dao, ProveedorDao proveedorDao,
                                        PiezaDao piezaDao, ProyectoDao proyectoDao) {

        LocalDate fecha = leerFecha(sc, "Fecha (AAAA-MM-DD): ");
        int cantidad = leerPositivo(sc, "Cantidad: ");

        Proveedor proveedor;
        do {
            String codProveedor = leerTexto(sc, "Código del proveedor: ");
            proveedor = proveedorDao.find(codProveedor);

            if (proveedor == null) {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }
        } while (proveedor == null);

        Pieza pieza;
        do {
            String codPieza = leerTexto(sc, "Código de la pieza: ");
            pieza = piezaDao.find(codPieza);

            if (pieza == null) {
                System.out.println("⚠️ No existe ninguna pieza con ese código.");
            }
        } while (pieza == null);

        Proyecto proyecto;
        do {
            String codProyecto = leerTexto(sc, "Código del proyecto: ");
            proyecto = proyectoDao.find(codProyecto);

            if (proyecto == null) {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }
        } while (proyecto == null);

        Suministro suministro = new Suministro(0, fecha, cantidad, proveedor, pieza, proyecto);

        dao.create(suministro);
        System.out.println("✅ Suministro registrado correctamente.");
    }

    // Modificar un suministro
    private static void modificarSuministro(Scanner sc, SuministroDao dao, ProveedorDao proveedorDao,
                                            PiezaDao piezaDao, ProyectoDao proyectoDao) {

        int id;
        Suministro suministro;

        do {
            id = leerPositivo(sc, "Introduce el ID del suministro: ");
            suministro = dao.find(id);

            if (suministro == null) {
                System.out.println("⚠️ No existe ningún suministro con ese ID.");
            }
        } while (suministro == null);

        System.out.print("Nueva fecha (" + suministro.getFecha() + "): ");
        String fecha = leerEntrada(sc);

        while (!fecha.trim().isEmpty()) {
            try {
                suministro.setFecha(LocalDate.parse(fecha));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Introduce una fecha válida (AAAA-MM-DD).");
                System.out.print("Vuelve a introducir la fecha: ");
                fecha = leerEntrada(sc);
            }
        }

        System.out.print("Nueva cantidad (" + suministro.getCantidad() + "): ");
        String cantidad = leerEntrada(sc);

        while (!cantidad.trim().isEmpty()) {
            try {
                int nuevaCantidad = Integer.parseInt(cantidad);

                if (nuevaCantidad > 0) {
                    suministro.setCantidad(nuevaCantidad);
                    break;
                } else {
                    System.out.println("⚠️ La cantidad debe ser mayor que 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debes introducir un número entero válido.");
            }

            System.out.print("Vuelve a introducir la cantidad: ");
            cantidad = leerEntrada(sc);
        }

        System.out.print("Nuevo código de proveedor (" + suministro.getProveedor().getCod() + "): ");
        String codProveedor = leerEntrada(sc);

        while (!codProveedor.trim().isEmpty()) {
            Proveedor proveedor = proveedorDao.find(codProveedor.trim());

            if (proveedor != null) {
                suministro.setProveedor(proveedor);
                break;
            } else {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }

            System.out.print("Vuelve a introducir el código del proveedor: ");
            codProveedor = leerEntrada(sc);
        }

        System.out.print("Nuevo código de pieza (" + suministro.getPieza().getCod() + "): ");
        String codPieza = leerEntrada(sc);

        while (!codPieza.trim().isEmpty()) {
            Pieza pieza = piezaDao.find(codPieza.trim());

            if (pieza != null) {
                suministro.setPieza(pieza);
                break;
            } else {
                System.out.println("⚠️ No existe ninguna pieza con ese código.");
            }

            System.out.print("Vuelve a introducir el código de la pieza: ");
            codPieza = leerEntrada(sc);
        }

        System.out.print("Nuevo código de proyecto (" + suministro.getProyecto().getCod() + "): ");
        String codProyecto = leerEntrada(sc);

        while (!codProyecto.trim().isEmpty()) {
            Proyecto proyecto = proyectoDao.find(codProyecto.trim());

            if (proyecto != null) {
                suministro.setProyecto(proyecto);
                break;
            } else {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }

            System.out.print("Vuelve a introducir el código del proyecto: ");
            codProyecto = leerEntrada(sc);
        }

        dao.update(suministro);
        System.out.println("✅ Suministro modificado correctamente.");
    }

    // Eliminar un suministro
    private static void eliminarSuministro(Scanner sc, SuministroDao dao) {
        int id;
        Suministro suministro;

        do {
            id = leerPositivo(sc, "Introduce el ID del suministro: ");
            suministro = dao.find(id);

            if (suministro == null) {
                System.out.println("⚠️ No existe ningún suministro con ese ID.");
            }
        } while (suministro == null);

        String respuesta = leerTexto(sc, "¿Seguro que quieres eliminarlo? (s/n): ");

        if (respuesta.equalsIgnoreCase("s")) {
            if (dao.delete(suministro)) {
                System.out.println("✅ Suministro eliminado correctamente.");
            } else {
                System.out.println("❌ No se ha podido eliminar el suministro.");
            }
        }
    }

    // Menú de búsquedas de suministros
    private static void buscarSuministros(Scanner sc, SuministroDao dao) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE SUMINISTROS =====");
            System.out.println("1. Por ID");
            System.out.println("2. Por proveedor");
            System.out.println("3. Por pieza");
            System.out.println("4. Por proyecto");
            System.out.println("0. ↩️ Volver");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            List<Suministro> suministros = null;

            switch (opcion) {
                case 1:
                    suministros = dao.buscarPorId(leerPositivo(sc, "ID: "));
                    break;
                case 2:
                    suministros = dao.buscarPorProveedor(leerTexto(sc, "Código o nombre del proveedor: "));
                    break;
                case 3:
                    suministros = dao.buscarPorPieza(leerTexto(sc, "Código o nombre de la pieza: "));
                    break;
                case 4:
                    suministros = dao.buscarPorProyecto(leerTexto(sc, "Código o nombre del proyecto: "));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

            if (suministros != null) {
                if (suministros.isEmpty()) {
                    System.out.println("No se han encontrado suministros.");
                } else {
                    for (Suministro s : suministros) {
                        System.out.println("\nID: " + s.getId());
                        System.out.println("Fecha: " + s.getFecha());
                        System.out.println("Cantidad: " + s.getCantidad());
                        System.out.println("Proveedor: " + s.getProveedor().getCod() +
                                " - " + s.getProveedor().getNombre());
                        System.out.println("Pieza: " + s.getPieza().getCod() +
                                " - " + s.getPieza().getNombre());
                        System.out.println("Proyecto: " + s.getProyecto().getCod() +
                                " - " + s.getProyecto().getNombre());
                    }
                }
            }

        } while (opcion != 0);
    }

    /* ----- Funciones auxiliares ----- */

    // Leer un dato y comprobar si se quiere finalizar la aplicación
    private static String leerEntrada(Scanner sc) {
        String texto = sc.nextLine();

        if (texto.trim().equalsIgnoreCase("FIN")) {
            throw new FinAplicacionException();
        }

        return texto;
    }

    // Comprobar que el dato es numérico
    public static int leerInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(leerEntrada(sc));

            } catch (NumberFormatException e) {
                System.out.println("❌ Error: debes introducir un número.");
                System.out.print("Vuelve a intentarlo: ");
            }
        }
    }

    // Leer un número entero mayor que 0
    public static int leerPositivo(Scanner sc, String mensaje) {
        int numero;

        while (true) {
            System.out.print(mensaje);
            String texto = leerEntrada(sc).trim();

            if (texto.isEmpty()) {
                System.out.println("❌ Error: debes introducir un número");
                continue;
            }

            try {
                numero = Integer.parseInt(texto);

                if (numero > 0) {
                    return numero;
                }

                System.out.println("❌ Error: el número debe ser mayor que 0");

            } catch (NumberFormatException e) {
                System.out.println("❌ Error: debes introducir un número entero válido");
            }
        }
    }

    // Leer un precio válido mayor que 0
    public static double leerPrecio(Scanner sc, String mensaje) {
        double precio;

        while (true) {
            System.out.print(mensaje);
            String texto = leerEntrada(sc).trim();

            try {
                precio = Double.parseDouble(texto);

                if (precio > 0) {
                    return precio;
                }

                System.out.println("❌ Error: el precio debe ser mayor que 0.");

            } catch (NumberFormatException e) {
                System.out.println("❌ Error: debes introducir un número válido.");
            }
        }
    }

    // Comprobar que se ha proporcionado un texto
    public static String leerTexto(Scanner sc, String mensaje) {

        String texto;

        do {
            System.out.print(mensaje);
            texto = leerEntrada(sc);

            if (texto.trim().isEmpty()) {
                System.out.println("❌ Error: el campo no puede estar vacío.");
            }

        } while (texto.trim().isEmpty());

        return texto.trim();
    }

    // Leer una fecha válida
    public static LocalDate leerFecha(Scanner sc, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(leerEntrada(sc));

            } catch (DateTimeParseException e) {
                System.out.println("❌ Error: introduce una fecha válida (AAAA-MM-DD).");
            }
        }
    }

    // Excepción para finalizar la aplicación
    private static class FinAplicacionException extends RuntimeException {
    }
}