import dao.*;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;
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

            System.out.println("🔌 Conexión con la base de datos establecida.");
            System.out.println("✅ Entidades inicializadas correctamente.");

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
                        menuSuministros(sc, suministroDao, proveedorDao, piezaDao, proyectoDao);
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
            System.out.println("5. Buscar proveedores (por código, nombre, apellidos o dirección)");
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

    // Listar todos los proveedores
    private static void listarProveedores(ProveedorDao dao) {

        List<Proveedor> proveedores = dao.findAll();

        // TODO : Probar esta opción y la de "isEmpty" de los demás modelos
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
        String nombre = sc.nextLine();
        if (!nombre.trim().isEmpty()) {
            proveedor.setNombre(nombre);
        }

        System.out.print("Nuevo primer apellido (" + proveedor.getApellido1() + "): ");
        String apellido1 = sc.nextLine();
        if (!apellido1.trim().isEmpty()) {
            proveedor.setApellido1(apellido1);
        }

        System.out.print("Nuevo segundo apellido (" + proveedor.getApellido2() + "): ");
        String apellido2 = sc.nextLine();
        if (!apellido2.trim().isEmpty()) {
            proveedor.setApellido2(apellido2);
        }

        System.out.print("Nueva dirección (" + proveedor.getDireccion() + "): ");
        String direccion = sc.nextLine();
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
            dao.delete(proveedor);
            System.out.println("✅ Proveedor eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Buscar proveedores
    private static void buscarProveedores(Scanner sc, ProveedorDao dao) {

        String texto = leerTexto(sc, "Introduce el texto (código, nombre, apellidos o dirección) que quieres buscar: ");

        List<Proveedor> proveedores = dao.buscar(texto);

        if (proveedores.isEmpty()) {
            System.out.println("No se han encontrado proveedores.");
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
        String nombre = sc.nextLine();
        if (!nombre.trim().isEmpty()) pieza.setNombre(nombre.trim());

        System.out.print("Nuevo precio (" + pieza.getPrecio() + "): ");
        String precio = sc.nextLine();

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
            precio = sc.nextLine();
        }

        System.out.print("Nueva descripción (" + pieza.getDescripcion() + "): ");
        String descripcion = sc.nextLine();
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
            dao.delete(pieza);
            System.out.println("✅ Pieza eliminada correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Buscar piezas
    private static void buscarPiezas(Scanner sc, PiezaDao dao) {
        String texto = leerTexto(sc, "Introduce el texto (código, nombre o descripción) que quieres buscar: ");
        List<Pieza> piezas = dao.buscar(texto);

        if (piezas.isEmpty()) {
            System.out.println("No se han encontrado piezas.");
            return;
        }

        for (Pieza p : piezas) {
            System.out.println("\nCódigo: " + p.getCod());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Precio: " + p.getPrecio());
            System.out.println("Descripción: " + p.getDescripcion());
        }
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
        String nombre = sc.nextLine();
        if (!nombre.trim().isEmpty()) proyecto.setNombre(nombre.trim());

        System.out.print("Nueva ciudad (" + proyecto.getCiudad() + "): ");
        String ciudad = sc.nextLine();
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
            dao.delete(proyecto);
            System.out.println("✅ Proyecto eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Buscar proyectos
    private static void buscarProyectos(Scanner sc, ProyectoDao dao) {
        String texto = leerTexto(sc, "Introduce el texto (código, nombre o ciudad) que quieres buscar: ");
        List<Proyecto> proyectos = dao.buscar(texto);

        if (proyectos.isEmpty()) {
            System.out.println("No se han encontrado proyectos.");
            return;
        }

        for (Proyecto p : proyectos) {
            System.out.println("\nCódigo: " + p.getCod());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Ciudad: " + p.getCiudad());
        }
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
        String fecha = sc.nextLine();

        while (!fecha.trim().isEmpty()) {
            try {
                suministro.setFecha(LocalDate.parse(fecha));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Introduce una fecha válida (AAAA-MM-DD).");
                System.out.print("Vuelve a introducir la fecha: ");
                fecha = sc.nextLine();
            }
        }

        System.out.print("Nueva cantidad (" + suministro.getCantidad() + "): ");
        String cantidad = sc.nextLine();

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
            cantidad = sc.nextLine();
        }

        System.out.print("Nuevo código de proveedor (" + suministro.getProveedor().getCod() + "): ");
        String codProveedor = sc.nextLine();

        while (!codProveedor.trim().isEmpty()) {
            Proveedor proveedor = proveedorDao.find(codProveedor.trim());

            if (proveedor != null) {
                suministro.setProveedor(proveedor);
                break;
            } else {
                System.out.println("⚠️ No existe ningún proveedor con ese código.");
            }

            System.out.print("Vuelve a introducir el código del proveedor: ");
            codProveedor = sc.nextLine();
        }

        System.out.print("Nuevo código de pieza (" + suministro.getPieza().getCod() + "): ");
        String codPieza = sc.nextLine();

        while (!codPieza.trim().isEmpty()) {
            Pieza pieza = piezaDao.find(codPieza.trim());

            if (pieza != null) {
                suministro.setPieza(pieza);
                break;
            } else {
                System.out.println("⚠️ No existe ninguna pieza con ese código.");
            }

            System.out.print("Vuelve a introducir el código de la pieza: ");
            codPieza = sc.nextLine();
        }

        System.out.print("Nuevo código de proyecto (" + suministro.getProyecto().getCod() + "): ");
        String codProyecto = sc.nextLine();

        while (!codProyecto.trim().isEmpty()) {
            Proyecto proyecto = proyectoDao.find(codProyecto.trim());

            if (proyecto != null) {
                suministro.setProyecto(proyecto);
                break;
            } else {
                System.out.println("⚠️ No existe ningún proyecto con ese código.");
            }

            System.out.print("Vuelve a introducir el código del proyecto: ");
            codProyecto = sc.nextLine();
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
            dao.delete(suministro);
            System.out.println("✅ Suministro eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Buscar suministros
    private static void buscarSuministros(Scanner sc, SuministroDao dao) {
        String texto = leerTexto(sc, "Introduce el texto (ID, proveedor, pieza o proyecto) que quieres buscar: ");
        List<Suministro> suministros = dao.buscar(texto);

        if (suministros.isEmpty()) {
            System.out.println("No se han encontrado suministros.");
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

        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();

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
            texto = sc.nextLine();

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
                return LocalDate.parse(sc.nextLine());

            } catch (DateTimeParseException e) {
                System.out.println("❌ Error: introduce una fecha válida (AAAA-MM-DD).");
            }
        }
    }
}