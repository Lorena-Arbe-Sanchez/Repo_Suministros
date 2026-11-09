import dao.*;
import jakarta.persistence.EntityManager;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas automatizadas para comprobar el funcionamiento de la conexión,
 * las entidades y las operaciones CRUD de los DAO
 */
public class AppTest {

    private static final String PREFIJO = "TEST_";

    private ProveedorDao proveedorDao;
    private PiezaDao piezaDao;
    private ProyectoDao proyectoDao;
    private SuministroDao suministroDao;

    /**
     * Inicializa los DAO antes de cada prueba
     */
    @BeforeEach
    public void setUp() {
        proveedorDao = new ProveedorDao();
        piezaDao = new PiezaDao();
        proyectoDao = new ProyectoDao();
        suministroDao = new SuministroDao();
    }

    /**
     * Elimina los registros de prueba y cierra los DAO
     */
    @AfterEach
    public void tearDown() {

        // Eliminar primero los suministros de prueba
        for (Suministro s : suministroDao.findAll()) {
            if (esRegistroPrueba(s.getProveedor().getCod())
                    || esRegistroPrueba(s.getPieza().getCod())
                    || esRegistroPrueba(s.getProyecto().getCod())) {
                suministroDao.delete(s);
            }
        }

        // Eliminar los proveedores de prueba
        for (Proveedor p : proveedorDao.findAll()) {
            if (esRegistroPrueba(p.getCod())) {
                proveedorDao.delete(p);
            }
        }

        // Eliminar las piezas de prueba
        for (Pieza p : piezaDao.findAll()) {
            if (esRegistroPrueba(p.getCod())) {
                piezaDao.delete(p);
            }
        }

        // Eliminar los proyectos de prueba
        for (Proyecto p : proyectoDao.findAll()) {
            if (esRegistroPrueba(p.getCod())) {
                proyectoDao.delete(p);
            }
        }

        proveedorDao.cerrar();
        piezaDao.cerrar();
        proyectoDao.cerrar();
        suministroDao.cerrar();
    }

    /**
     * Comprueba si un código pertenece a un registro de prueba
     */
    private boolean esRegistroPrueba(String cod) {
        return cod != null && cod.startsWith(PREFIJO);
    }

    /**
     * Comprueba que los resultados están ordenados
     */
    private <T> void comprobarOrden(List<T> lista, Function<T, String> obtenerCodigo) {
        for (int i = 1; i < lista.size(); i++) {
            assertTrue(
                    obtenerCodigo.apply(lista.get(i - 1))
                            .compareTo(obtenerCodigo.apply(lista.get(i))) <= 0
            );
        }
    }

    /**
     * Comprueba la conexión con la base de datos
     */
    @Test
    public void testConexion() {

        EntityManager em = Dao.crearEntityManager();

        try {
            Object resultado = em.createNativeQuery("SELECT 1").getSingleResult();

            assertNotNull(resultado, "No se ha podido conectar con la base de datos");
            assertTrue(em.isOpen(), "La conexión no está abierta");

        } finally {
            em.close();
        }
    }

    /**
     * Comprueba que Hibernate reconoce las cuatro entidades
     */
    @Test
    public void testEntidades() {

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

    // --------------------------------------------------
    // PRUEBAS DE PROVEEDOR
    // --------------------------------------------------

    /**
     * Comprueba las operaciones CRUD del proveedor
     */
    @Test
    public void testCRUDProveedor() {

        Proveedor proveedor = new Proveedor(
                PREFIJO + "PROV_01",
                "Proveedor JUnit",
                "Apellido1",
                "Apellido2",
                "Direccion de prueba",
                null
        );

        // CREATE
        Proveedor creado = proveedorDao.create(proveedor);

        assertNotNull(creado);
        assertEquals(PREFIJO + "PROV_01", creado.getCod());

        // READ
        proveedorDao.getEntityManager().clear();

        Proveedor encontrado = proveedorDao.find(PREFIJO + "PROV_01");

        assertNotNull(encontrado);
        assertEquals("Proveedor JUnit", encontrado.getNombre());
        assertEquals("Apellido1", encontrado.getApellido1());

        assertNull(proveedorDao.find(PREFIJO + "NO_EXISTE"));

        // Comprobar el listado y su orden
        List<Proveedor> proveedores = proveedorDao.findAll();

        assertFalse(proveedores.isEmpty());
        assertTrue(proveedores.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PROV_01")
        ));

        comprobarOrden(proveedores, Proveedor::getCod);

        // UPDATE
        encontrado.setNombre("Proveedor JUnit Modificado");
        encontrado.setDireccion("Nueva direccion");

        Proveedor actualizado = proveedorDao.update(encontrado);

        assertNotNull(actualizado);
        assertEquals("Proveedor JUnit Modificado", actualizado.getNombre());
        assertEquals("Nueva direccion", actualizado.getDireccion());

        // Comprobar la modificación desde la base de datos
        proveedorDao.getEntityManager().clear();

        encontrado = proveedorDao.find(PREFIJO + "PROV_01");

        assertEquals("Proveedor JUnit Modificado", encontrado.getNombre());
        assertEquals("Nueva direccion", encontrado.getDireccion());

        // SEARCH
        List<Proveedor> resultados = proveedorDao.buscar("jUnIt");

        assertTrue(resultados.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PROV_01")
        ));

        assertTrue(proveedorDao.buscar("ZZZ_TEST_SIN_RESULTADOS").isEmpty());

        // DELETE
        assertTrue(proveedorDao.delete(encontrado));
        assertNull(proveedorDao.find(PREFIJO + "PROV_01"));
    }

    // --------------------------------------------------
    // PRUEBAS DE PIEZA
    // --------------------------------------------------

    /**
     * Comprueba las operaciones CRUD de la pieza
     */
    @Test
    public void testCRUDPieza() {

        Pieza pieza = new Pieza(
                PREFIJO + "PIEZA_01",
                "Pieza JUnit",
                10.50,
                "Descripcion de prueba",
                null
        );

        // CREATE
        Pieza creada = piezaDao.create(pieza);

        assertNotNull(creada);
        assertEquals(PREFIJO + "PIEZA_01", creada.getCod());

        // READ
        piezaDao.getEntityManager().clear();

        Pieza encontrada = piezaDao.find(PREFIJO + "PIEZA_01");

        assertNotNull(encontrada);
        assertEquals("Pieza JUnit", encontrada.getNombre());
        assertEquals(10.50, encontrada.getPrecio(), 0.001);

        assertNull(piezaDao.find(PREFIJO + "NO_EXISTE"));

        // Comprobar el listado y su orden
        List<Pieza> piezas = piezaDao.findAll();

        assertFalse(piezas.isEmpty());
        assertTrue(piezas.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PIEZA_01")
        ));

        comprobarOrden(piezas, Pieza::getCod);

        // UPDATE
        encontrada.setNombre("Pieza JUnit Modificada");
        encontrada.setPrecio(25.75);
        encontrada.setDescripcion("Nueva descripcion");

        Pieza actualizada = piezaDao.update(encontrada);

        assertNotNull(actualizada);
        assertEquals("Pieza JUnit Modificada", actualizada.getNombre());
        assertEquals(25.75, actualizada.getPrecio(), 0.001);
        assertEquals("Nueva descripcion", actualizada.getDescripcion());

        // Comprobar la modificación desde la base de datos
        piezaDao.getEntityManager().clear();

        encontrada = piezaDao.find(PREFIJO + "PIEZA_01");

        assertEquals("Pieza JUnit Modificada", encontrada.getNombre());
        assertEquals(25.75, encontrada.getPrecio(), 0.001);

        // SEARCH
        List<Pieza> resultados = piezaDao.buscar("jUnIt");

        assertTrue(resultados.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PIEZA_01")
        ));

        assertTrue(piezaDao.buscar("ZZZ_TEST_SIN_RESULTADOS").isEmpty());

        // DELETE
        assertTrue(piezaDao.delete(encontrada));
        assertNull(piezaDao.find(PREFIJO + "PIEZA_01"));
    }

    // --------------------------------------------------
    // PRUEBAS DE PROYECTO
    // --------------------------------------------------

    /**
     * Comprueba las operaciones CRUD del proyecto
     */
    @Test
    public void testCRUDProyecto() {

        Proyecto proyecto = new Proyecto(
                PREFIJO + "PROY_01",
                "Proyecto JUnit",
                "Vitoria-Gasteiz",
                null
        );

        // CREATE
        Proyecto creado = proyectoDao.create(proyecto);

        assertNotNull(creado);
        assertEquals(PREFIJO + "PROY_01", creado.getCod());

        // READ
        proyectoDao.getEntityManager().clear();

        Proyecto encontrado = proyectoDao.find(PREFIJO + "PROY_01");

        assertNotNull(encontrado);
        assertEquals("Proyecto JUnit", encontrado.getNombre());
        assertEquals("Vitoria-Gasteiz", encontrado.getCiudad());

        assertNull(proyectoDao.find(PREFIJO + "NO_EXISTE"));

        // Comprobar el listado y su orden
        List<Proyecto> proyectos = proyectoDao.findAll();

        assertFalse(proyectos.isEmpty());
        assertTrue(proyectos.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PROY_01")
        ));

        comprobarOrden(proyectos, Proyecto::getCod);

        // UPDATE
        encontrado.setNombre("Proyecto JUnit Modificado");
        encontrado.setCiudad("Bilbao");

        Proyecto actualizado = proyectoDao.update(encontrado);

        assertNotNull(actualizado);
        assertEquals("Proyecto JUnit Modificado", actualizado.getNombre());
        assertEquals("Bilbao", actualizado.getCiudad());

        // Comprobar la modificación desde la base de datos
        proyectoDao.getEntityManager().clear();

        encontrado = proyectoDao.find(PREFIJO + "PROY_01");

        assertEquals("Proyecto JUnit Modificado", encontrado.getNombre());
        assertEquals("Bilbao", encontrado.getCiudad());

        // SEARCH
        List<Proyecto> resultados = proyectoDao.buscar("jUnIt");

        assertTrue(resultados.stream().anyMatch(
                p -> p.getCod().equals(PREFIJO + "PROY_01")
        ));

        assertTrue(proyectoDao.buscar("ZZZ_TEST_SIN_RESULTADOS").isEmpty());

        // DELETE
        assertTrue(proyectoDao.delete(encontrado));
        assertNull(proyectoDao.find(PREFIJO + "PROY_01"));
    }

    // --------------------------------------------------
    // PRUEBAS DE SUMINISTRO
    // --------------------------------------------------

    /**
     * Comprueba las operaciones CRUD del suministro y sus relaciones
     */
    @Test
    public void testCRUDSuministro() {

        // Crear las entidades relacionadas
        Proveedor proveedor = proveedorDao.create(new Proveedor(
                PREFIJO + "PROV_02",
                "Proveedor JUnit",
                "Apellido1",
                "Apellido2",
                "Direccion de prueba",
                null
        ));

        Pieza pieza = piezaDao.create(new Pieza(
                PREFIJO + "PIEZA_02",
                "Pieza JUnit",
                15.50,
                "Descripcion de prueba",
                null
        ));

        Proyecto proyecto = proyectoDao.create(new Proyecto(
                PREFIJO + "PROY_02",
                "Proyecto JUnit",
                "Vitoria-Gasteiz",
                null
        ));

        Pieza segundaPieza = piezaDao.create(new Pieza(
                PREFIJO + "PIEZA_03",
                "Segunda pieza JUnit",
                20.00,
                "Segunda descripcion",
                null
        ));

        // Recuperar las entidades desde el EntityManager del suministro
        Proveedor proveedorRelacionado = suministroDao.getEntityManager()
                .find(Proveedor.class, proveedor.getCod());

        Pieza piezaRelacionada = suministroDao.getEntityManager()
                .find(Pieza.class, pieza.getCod());

        Proyecto proyectoRelacionado = suministroDao.getEntityManager()
                .find(Proyecto.class, proyecto.getCod());

        Pieza segundaPiezaRelacionada = suministroDao.getEntityManager()
                .find(Pieza.class, segundaPieza.getCod());

        Suministro suministro = new Suministro(
                0,
                LocalDate.of(2026, 11, 9),
                10,
                proveedorRelacionado,
                piezaRelacionada,
                proyectoRelacionado
        );

        // CREATE
        Suministro creado = suministroDao.create(suministro);

        assertNotNull(creado);
        assertTrue(creado.getId() > 0);

        int id = creado.getId();

        // READ
        suministroDao.getEntityManager().clear();

        Suministro encontrado = suministroDao.find(id);

        assertNotNull(encontrado);
        assertEquals(10, encontrado.getCantidad());
        assertEquals(LocalDate.of(2026, 11, 9), encontrado.getFecha());

        assertEquals(PREFIJO + "PROV_02", encontrado.getProveedor().getCod());
        assertEquals(PREFIJO + "PIEZA_02", encontrado.getPieza().getCod());
        assertEquals(PREFIJO + "PROY_02", encontrado.getProyecto().getCod());

        assertNull(suministroDao.find(-999));

        // Comprobar el listado y su orden
        List<Suministro> suministros = suministroDao.findAll();

        assertFalse(suministros.isEmpty());
        assertTrue(suministros.stream().anyMatch(s -> s.getId() == id));

        comprobarOrden(
                suministros,
                s -> String.format("%010d", s.getId())
        );

        // UPDATE
        encontrado.setFecha(LocalDate.of(2026, 12, 1));
        encontrado.setCantidad(25);
        encontrado.setPieza(segundaPiezaRelacionada);

        Suministro actualizado = suministroDao.update(encontrado);

        assertNotNull(actualizado);
        assertEquals(25, actualizado.getCantidad());
        assertEquals(LocalDate.of(2026, 12, 1), actualizado.getFecha());
        assertEquals(PREFIJO + "PIEZA_03", actualizado.getPieza().getCod());

        // Comprobar la modificación desde la base de datos
        suministroDao.getEntityManager().clear();

        encontrado = suministroDao.find(id);

        assertEquals(25, encontrado.getCantidad());
        assertEquals(LocalDate.of(2026, 12, 1), encontrado.getFecha());
        assertEquals(PREFIJO + "PIEZA_03", encontrado.getPieza().getCod());

        // SEARCH por ID
        List<Suministro> resultados = suministroDao.buscar(String.valueOf(id));

        assertTrue(resultados.stream().anyMatch(s -> s.getId() == id));

        // SEARCH por proveedor, pieza y proyecto
        assertTrue(suministroDao.buscar("JUnit").stream()
                .anyMatch(s -> s.getId() == id));

        assertTrue(suministroDao.buscar("ZZZ_TEST_SIN_RESULTADOS").isEmpty());

        // DELETE
        assertTrue(suministroDao.delete(encontrado));
        assertNull(suministroDao.find(id));
    }

    // --------------------------------------------------
    // PRUEBAS DE CLAVES FORÁNEAS
    // --------------------------------------------------

    /**
     * Comprueba que no se pueden eliminar entidades relacionadas con suministros
     */
    @Test
    public void testIntegridadReferencial() {

        // Crear las entidades relacionadas
        Proveedor proveedor = proveedorDao.create(new Proveedor(
                PREFIJO + "PROV_03",
                "Proveedor JUnit",
                "Apellido1",
                "Apellido2",
                "Direccion de prueba",
                null
        ));

        Pieza pieza = piezaDao.create(new Pieza(
                PREFIJO + "PIEZA_04",
                "Pieza JUnit",
                12.50,
                "Descripcion de prueba",
                null
        ));

        Proyecto proyecto = proyectoDao.create(new Proyecto(
                PREFIJO + "PROY_03",
                "Proyecto JUnit",
                "Vitoria-Gasteiz",
                null
        ));

        // Recuperar las entidades desde el EntityManager del suministro
        Proveedor proveedorRelacionado = suministroDao.getEntityManager()
                .find(Proveedor.class, proveedor.getCod());

        Pieza piezaRelacionada = suministroDao.getEntityManager()
                .find(Pieza.class, pieza.getCod());

        Proyecto proyectoRelacionado = suministroDao.getEntityManager()
                .find(Proyecto.class, proyecto.getCod());

        // Crear el suministro que relaciona las tres entidades
        Suministro suministro = suministroDao.create(new Suministro(
                0,
                LocalDate.now(),
                5,
                proveedorRelacionado,
                piezaRelacionada,
                proyectoRelacionado
        ));

        // Comprobar que las eliminaciones están bloqueadas
        assertFalse(proveedorDao.delete(proveedor));
        assertFalse(piezaDao.delete(pieza));
        assertFalse(proyectoDao.delete(proyecto));

        // Comprobar que las entidades siguen existiendo
        assertNotNull(proveedorDao.find(PREFIJO + "PROV_03"));
        assertNotNull(piezaDao.find(PREFIJO + "PIEZA_04"));
        assertNotNull(proyectoDao.find(PREFIJO + "PROY_03"));

        // Eliminar primero el suministro
        suministro = suministroDao.find(suministro.getId());

        assertTrue(suministroDao.delete(suministro));

        // Ahora sí se pueden eliminar las entidades
        proveedor = proveedorDao.find(PREFIJO + "PROV_03");
        pieza = piezaDao.find(PREFIJO + "PIEZA_04");
        proyecto = proyectoDao.find(PREFIJO + "PROY_03");

        assertTrue(proveedorDao.delete(proveedor));
        assertTrue(piezaDao.delete(pieza));
        assertTrue(proyectoDao.delete(proyecto));
    }
}