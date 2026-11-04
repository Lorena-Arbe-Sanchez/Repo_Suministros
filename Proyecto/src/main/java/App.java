import dao.*;
import models.Proveedor;
import utils.ExportarDatos;
import utils.MetadatosBD;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

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

            System.out.println("🔌 Conexión con la base de datos establecida");
            System.out.println("✅ Entidades inicializadas correctamente");

            // Menú principal
            int opcion;

            do {

                System.out.println("\n\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestión de proveedores");
                System.out.println("2. Gestión de piezas");
                System.out.println("3. Gestión de proyectos");
                System.out.println("4. Gestión de suministros");
                System.out.println("5. Exportar datos XML y JSON");
                System.out.println("6. Metadatos de la BBDD");
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
                        menuSuministros(sc, suministroDao);
                        break;
                    case 5:
                        menuExportaciones(sc);
                        break;
                    case 6:
                        MetadatosBD.mostrarMetadatos();
                        MetadatosBD.exportarMetadatos();
                        break;
                    case 0:
                        System.out.println("👋 ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("⚠️ Opción no válida.");
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();

        } finally {

            // Cerrar los EntityManager
            if (proveedorDao != null) proveedorDao.cerrar();
            if (piezaDao != null) piezaDao.cerrar();
            if (proyectoDao != null) proyectoDao.cerrar();
            if (suministroDao != null) suministroDao.cerrar();

            // Cerrar la fábrica de EntityManager
            Dao.cerrarFabrica();
        }
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
                    // TODO: Listar piezas
                    break;
                case 2:
                    // TODO: Dar de alta una pieza
                    break;
                case 3:
                    // TODO: Modificar una pieza
                    break;
                case 4:
                    // TODO: Dar de baja una pieza
                    break;
                case 5:
                    // TODO: Buscar piezas
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
                    // TODO: Listar proyectos
                    break;
                case 2:
                    // TODO: Dar de alta un proyecto
                    break;
                case 3:
                    // TODO: Modificar un proyecto
                    break;
                case 4:
                    // TODO: Dar de baja un proyecto
                    break;
                case 5:
                    // TODO: Buscar proyectos
                    break;
                case 0:
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de suministros
    private static void menuSuministros(Scanner sc, SuministroDao dao) {

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
                    // TODO: Listar suministros
                    break;
                case 2:
                    // TODO: Registrar un suministro
                    break;
                case 3:
                    // TODO: Modificar un suministro
                    break;
                case 4:
                    // TODO: Eliminar un suministro
                    break;
                case 5:
                    // TODO: Buscar suministros
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

    // Listar todos los proveedores
    private static void listarProveedores(ProveedorDao dao) {

        List<Proveedor> proveedores = dao.findAll();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados");
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

        System.out.print("Código: ");
        String cod = sc.nextLine();

        if (dao.find(cod) != null) {
            System.out.println("⚠️ Ya existe un proveedor con ese código");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Primer apellido: ");
        String apellido1 = sc.nextLine();

        System.out.print("Segundo apellido: ");
        String apellido2 = sc.nextLine();

        System.out.print("Dirección: ");
        String direccion = sc.nextLine();

        Proveedor proveedor = new Proveedor(
                cod, nombre, apellido1, apellido2, direccion, null
        );

        dao.create(proveedor);

        System.out.println("Proveedor creado correctamente");
    }

    // Modificar un proveedor
    private static void modificarProveedor(Scanner sc, ProveedorDao dao) {

        System.out.print("Introduce el código del proveedor: ");
        String cod = sc.nextLine();

        Proveedor proveedor = dao.find(cod);

        if (proveedor == null) {
            System.out.println("⚠️ No existe ningún proveedor con ese código");
            return;
        }

        System.out.print("Nuevo nombre: ");
        proveedor.setNombre(sc.nextLine());

        System.out.print("Nuevo primer apellido: ");
        proveedor.setApellido1(sc.nextLine());

        System.out.print("Nuevo segundo apellido: ");
        proveedor.setApellido2(sc.nextLine());

        System.out.print("Nueva dirección: ");
        proveedor.setDireccion(sc.nextLine());

        dao.update(proveedor);

        System.out.println("Proveedor modificado correctamente");
    }

    // Eliminar un proveedor
    private static void eliminarProveedor(Scanner sc, ProveedorDao dao) {

        System.out.print("Introduce el código del proveedor: ");
        String cod = sc.nextLine();

        Proveedor proveedor = dao.find(cod);

        if (proveedor == null) {
            System.out.println("⚠️ No existe ningún proveedor con ese código");
            return;
        }

        System.out.println("¿Seguro que quieres eliminar este proveedor? (s/n)");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            dao.delete(proveedor);
            System.out.println("Proveedor eliminado correctamente");
        } else {
            System.out.println("Operación cancelada");
        }
    }

    // Buscar proveedores
    private static void buscarProveedores(Scanner sc, ProveedorDao dao) {

        System.out.print("Introduce el texto que quieres buscar: ");
        String texto = sc.nextLine();

        List<Proveedor> proveedores = dao.buscar(texto);

        if (proveedores.isEmpty()) {
            System.out.println("No se han encontrado proveedores");
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

    /* ----- Funciones auxiliares ----- */

    // Comprobar que el dato es numérico
    public static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("❌ Error: debes introducir un número.");
            sc.nextLine();
            System.out.print("Vuelve a intentarlo: ");
        }

        int numero = sc.nextInt();
        sc.nextLine();

        return numero;
    }

    // Leer un número entero mayor que 0
    public static int leerPositivo(Scanner sc, String mensaje) {
        int numero;

        do {
            System.out.print(mensaje);
            numero = leerInt(sc);

            if (numero <= 0) {
                System.out.println("❌ Error: el número debe ser mayor que 0.");
            }

        } while (numero <= 0);

        return numero;
    }

    // Leer un precio válido mayor que 0
    public static double leerPrecio(Scanner sc, String mensaje) {
        double precio;

        do {
            System.out.print(mensaje);

            while (!sc.hasNextDouble()) {
                System.out.println("❌ Error: debes introducir un número.");
                sc.nextLine();
                System.out.print("Vuelve a intentarlo: ");
            }

            precio = sc.nextDouble();
            sc.nextLine();

            if (precio <= 0) {
                System.out.println("❌ Error: el precio debe ser mayor que 0.");
            }

        } while (precio <= 0);

        return precio;
    }

    // Comprobar que se ha proporcionado un texto
    public static String leerTexto(Scanner sc, String mensaje) {

        String texto;

        do {
            System.out.print(mensaje);
            texto = sc.nextLine();

            if (texto.trim().isEmpty()) {
                System.out.println("❌ Error: el campo no puede estar vacío.");
            }

        } while (texto.trim().isEmpty());

        return texto;
    }

    // Leer una fecha válida
    public static LocalDate leerFecha(Scanner sc, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(sc.nextLine());

            } catch (DateTimeParseException e) {
                System.out.println("❌ Error: introduce una fecha válida (AAAA-MM-DD).");
            }
        }
    }
}