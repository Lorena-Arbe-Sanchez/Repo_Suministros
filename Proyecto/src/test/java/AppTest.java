import dao.Dao;
import jakarta.persistence.EntityManager;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para comprobar el funcionamiento de la conexión y de
 * las entidades de la aplicación de gestión de proyectos y suministros.
 */
public class AppTest {

    /**
     * Comprueba la conexión con la base de datos.
     */
    @Test
    public void testConexion() throws Exception {

        EntityManager em = Dao.crearEntityManager();

        try {
            // Comprobar que se puede ejecutar una consulta en la base de datos
            Object resultado = em.createNativeQuery("SELECT 1").getSingleResult();

            assertNotNull(resultado, "No se ha podido conectar con la base de datos");
            assertTrue(em.isOpen(), "La conexión no está abierta");

        } finally {
            em.close();
        }
    }

    /**
     * Comprueba que las entidades están registradas correctamente en Hibernate.
     */
    @Test
    public void testEntidades() throws Exception {

        EntityManager em = Dao.crearEntityManager();

        try {
            assertNotNull(em.getMetamodel().entity(Proveedor.class));
            assertNotNull(em.getMetamodel().entity(Pieza.class));
            assertNotNull(em.getMetamodel().entity(Proyecto.class));
            assertNotNull(em.getMetamodel().entity(Suministro.class));

        } finally {
            em.close();
        }
    }
}