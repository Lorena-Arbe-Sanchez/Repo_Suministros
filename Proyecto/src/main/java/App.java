import dao.*;

public class App {

    public static void main(String[] args) {

        ProveedorDao proveedorDao = null;
        PiezaDao piezaDao = null;
        ProyectoDao proyectoDao = null;
        SuministroDao suministroDao = null;

        try {
            proveedorDao = new ProveedorDao();
            piezaDao = new PiezaDao();
            proyectoDao = new ProyectoDao();
            suministroDao = new SuministroDao();

            System.out.println("Conexión con la base de datos establecida");
            System.out.println("Entidades inicializadas correctamente");

        } catch (Exception e) {
            System.out.println("Error al inicializar la aplicación");
            e.printStackTrace();

        } finally {
            if (proveedorDao != null) proveedorDao.cerrar();
            if (piezaDao != null) piezaDao.cerrar();
            if (proyectoDao != null) proyectoDao.cerrar();
            if (suministroDao != null) suministroDao.cerrar();

            Dao.cerrarFabrica();
        }

        // todo : continuar
    }
}