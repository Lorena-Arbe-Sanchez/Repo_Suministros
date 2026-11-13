import com.google.gson.JsonParser;
import dao.*;
import jakarta.persistence.EntityManager;
import models.Pieza;
import models.Proveedor;
import models.Proyecto;
import models.Suministro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ExportarDatos;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    // --------------------------------------------------
    // PRUEBAS DE CONEXIÓN Y ENTIDADES
    // --------------------------------------------------

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

    // --------------------------------------------------
    // PRUEBAS DE BÚSQUEDAS AVANZADAS
    // --------------------------------------------------

    /**
     * Comprueba las búsquedas avanzadas de proveedores
     */
    @Test
    public void testBusquedasProveedor() {

        // Por código
        assertEquals(Set.of("PR001"),
                proveedorDao.buscarPorCod("PR001").stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        // Por nombre
        assertEquals(Set.of("PR001"),
                proveedorDao.buscarPorNombre("Comercial").stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        // Por primer apellido
        assertEquals(Set.of("PR001"),
                proveedorDao.buscarPorApellido1("García").stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        // Por segundo apellido
        assertEquals(Set.of("PR002"),
                proveedorDao.buscarPorApellido2("Sánchez").stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        // Por dirección
        List<Proveedor> resultados = proveedorDao.buscarPorDireccion("Vitoria");

        assertEquals(Set.of("PR001", "PR002", "PR003"),
                resultados.stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(3, resultados.size());

        // Por pieza suministrada
        resultados = proveedorDao.buscarPorPieza("P001");

        assertEquals(Set.of("PR001", "PR002"),
                resultados.stream()
                        .map(Proveedor::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(2, resultados.size());

        // Búsqueda sin resultados
        assertTrue(proveedorDao.buscarPorCod("PR999").isEmpty());
    }

    /**
     * Comprueba las búsquedas avanzadas de piezas
     */
    @Test
    public void testBusquedasPieza() {

        // Por código
        assertEquals(Set.of("P001"),
                piezaDao.buscarPorCod("P001").stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        // Por nombre
        assertEquals(Set.of("P001"),
                piezaDao.buscarPorNombre("Tornillo").stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        // Por descripción
        List<Pieza> resultados = piezaDao.buscarPorDescripcion("acero");

        assertEquals(Set.of("P001", "P002", "P003", "P004"),
                resultados.stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(4, resultados.size());

        // Por intervalo de precios
        resultados = piezaDao.buscarPorPrecio(0.01, 0.10);

        assertEquals(Set.of("P002", "P003"),
                resultados.stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(2, resultados.size());

        // Por proveedor
        resultados = piezaDao.buscarPorProveedor("PR001");

        assertEquals(Set.of("P001", "P002", "P009"),
                resultados.stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(3, resultados.size());

        // Por proyecto
        resultados = piezaDao.buscarPorProyecto("PRY002");

        assertEquals(Set.of("P004", "P005", "P006"),
                resultados.stream()
                        .map(Pieza::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(3, resultados.size());

        // Búsqueda sin resultados
        assertTrue(piezaDao.buscarPorCod("P999").isEmpty());
    }

    /**
     * Comprueba las búsquedas avanzadas de proyectos
     */
    @Test
    public void testBusquedasProyecto() {

        // Por código
        assertEquals(Set.of("PRY001"),
                proyectoDao.buscarPorCod("PRY001").stream()
                        .map(Proyecto::getCod)
                        .collect(Collectors.toSet()));

        // Por nombre
        assertEquals(Set.of("PRY001"),
                proyectoDao.buscarPorNombre("nave").stream()
                        .map(Proyecto::getCod)
                        .collect(Collectors.toSet()));

        // Por ciudad
        assertEquals(Set.of("PRY002"),
                proyectoDao.buscarPorCiudad("Bilbao").stream()
                        .map(Proyecto::getCod)
                        .collect(Collectors.toSet()));

        // Por proveedor participante
        List<Proyecto> resultados = proyectoDao.buscarPorProveedor("PR001");

        assertEquals(Set.of("PRY001", "PRY006"),
                resultados.stream()
                        .map(Proyecto::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(2, resultados.size());

        // Por pieza utilizada
        resultados = proyectoDao.buscarPorPieza("P001");

        assertEquals(Set.of("PRY001", "PRY005"),
                resultados.stream()
                        .map(Proyecto::getCod)
                        .collect(Collectors.toSet()));

        assertEquals(2, resultados.size());

        // Búsqueda sin resultados
        assertTrue(proyectoDao.buscarPorCod("PRY999").isEmpty());
    }

    /**
     * Comprueba las búsquedas avanzadas de suministros
     */
    @Test
    public void testBusquedasSuministro() {

        // Por ID
        assertEquals(Set.of(1),
                suministroDao.buscarPorId(1).stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet()));

        // Por proveedor
        List<Suministro> resultados = suministroDao.buscarPorProveedor("PR001");

        assertEquals(Set.of(1, 2, 13),
                resultados.stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet()));

        assertEquals(3, resultados.size());

        // Por pieza
        resultados = suministroDao.buscarPorPieza("P001");

        assertEquals(Set.of(1, 11),
                resultados.stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet()));

        assertEquals(2, resultados.size());

        // Por proyecto
        resultados = suministroDao.buscarPorProyecto("PRY002");

        assertEquals(Set.of(4, 5, 6),
                resultados.stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet()));

        assertEquals(3, resultados.size());

        // Búsqueda sin resultados
        assertTrue(suministroDao.buscarPorId(999).isEmpty());
    }

    // --------------------------------------------------
    // PRUEBAS DE ANÁLISIS Y ESTADÍSTICAS
    // --------------------------------------------------

    /**
     * Comprueba el histórico de suministros de un proveedor durante un periodo
     */
    @Test
    public void testHistoricoProveedor() {

        // Histórico de PR001 durante el 2026
        List<Suministro> resultados = suministroDao.buscarHistoricoProveedor(
                "PR001",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31)
        );

        assertEquals(3, resultados.size());

        assertEquals(
                Set.of(1, 2, 13),
                resultados.stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet())
        );

        // Comprobar que las relaciones están disponibles
        for (Suministro s : resultados) {
            assertNotNull(s.getPieza());
            assertNotNull(s.getProyecto());
            assertNotNull(s.getProveedor());
        }

        // Comprobar que no devuelve resultados para un periodo sin suministros
        assertTrue(suministroDao.buscarHistoricoProveedor(
                "PR001",
                LocalDate.of(2099, 1, 1),
                LocalDate.of(2099, 12, 31)
        ).isEmpty());

        // Comprobar que no devuelve resultados para un proveedor inexistente
        assertTrue(suministroDao.buscarHistoricoProveedor(
                "PR999",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31)
        ).isEmpty());
    }

    /**
     * Comprueba la consulta de suministros para analizar un proyecto
     */
    @Test
    public void testAnalisisProyecto() {

        // Consultar los suministros de PRY001
        List<Suministro> resultados = suministroDao.buscarAnalisisProyecto("PRY001");

        assertEquals(3, resultados.size());

        assertEquals(
                Set.of(1, 2, 3),
                resultados.stream()
                        .map(Suministro::getId)
                        .collect(Collectors.toSet())
        );

        // Comprobar que las relaciones están disponibles
        for (Suministro s : resultados) {
            assertNotNull(s.getPieza());
            assertNotNull(s.getProveedor());
            assertNotNull(s.getProyecto());
            assertEquals("PRY001", s.getProyecto().getCod());
        }

        // Comprobar que no devuelve resultados para un proyecto sin suministros
        assertTrue(suministroDao.buscarAnalisisProyecto("PRY999").isEmpty());
    }

    /**
     * Comprueba la consulta de todos los suministros para las estadísticas globales
     */
    @Test
    public void testBuscarTodosParaEstadisticas() {

        List<Suministro> resultados = suministroDao.buscarTodosParaEstadisticas();

        assertFalse(resultados.isEmpty());

        // Comprobar que están incluidos los suministros originales
        assertTrue(resultados.stream().anyMatch(s -> s.getId() == 1));
        assertTrue(resultados.stream().anyMatch(s -> s.getId() == 15));

        // Comprobar que las relaciones están disponibles
        for (Suministro s : resultados) {
            assertNotNull(s.getPieza());
            assertNotNull(s.getProveedor());
            assertNotNull(s.getProyecto());
        }

        // Comprobar que los resultados están ordenados por ID
        for (int i = 1; i < resultados.size(); i++) {
            assertTrue(resultados.get(i - 1).getId() < resultados.get(i).getId());
        }
    }

    @Test
    public void testExportacionesXML() throws Exception {

        Path carpeta = Paths.get("datos", "xml");

        String[] archivos = {
                "Proveedores.xml",
                "Piezas.xml",
                "Proyectos.xml",
                "Suministros.xml"
        };

        // Evitar que archivos antiguos hagan pasar la prueba
        for (String nombre : archivos) {
            Files.deleteIfExists(carpeta.resolve(nombre));
        }

        // Exportar todas las entidades
        ExportarDatos.exportarTodoXML();

        // Comprobar que los cuatro XML se han generado correctamente
        for (String nombre : archivos) {

            Path archivo = carpeta.resolve(nombre);

            assertTrue(Files.exists(archivo), "No se ha generado " + nombre);
            assertTrue(Files.size(archivo) > 0, nombre + " está vacío");

            var documento = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(archivo.toFile());

            assertEquals("registros", documento.getDocumentElement().getTagName());

            assertTrue(
                    documento.getElementsByTagName("registro").getLength() > 0,
                    nombre + " no contiene registros"
            );
        }

        // Comprobar también la exportación de un informe XML
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("codigoPrueba", "TEST_EXPORT");
        datos.put("totalPrueba", 3);

        Path informe = carpeta.resolve("InformeJUnit.xml");
        Files.deleteIfExists(informe);

        ExportarDatos.exportarInformeXML("InformeJUnit.xml", datos);

        assertTrue(Files.exists(informe));
        assertTrue(Files.size(informe) > 0);

        var documento = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(informe.toFile());

        assertEquals("informe", documento.getDocumentElement().getTagName());
        assertEquals(
                "TEST_EXPORT",
                documento.getElementsByTagName("codigoPrueba").item(0).getTextContent()
        );

        Files.deleteIfExists(informe);
    }

    @Test
    public void testExportacionesJSON() throws Exception {

        Path carpeta = Paths.get("datos", "json");

        String[] archivos = {
                "Proveedores.json",
                "Piezas.json",
                "Proyectos.json",
                "Suministros.json"
        };

        // Evitar que archivos antiguos hagan pasar la prueba
        for (String nombre : archivos) {
            Files.deleteIfExists(carpeta.resolve(nombre));
        }

        // Exportar todas las entidades
        ExportarDatos.exportarTodoJSON();

        // Comprobar que los cuatro JSON se han generado correctamente
        for (String nombre : archivos) {

            Path archivo = carpeta.resolve(nombre);

            assertTrue(Files.exists(archivo), "No se ha generado " + nombre);
            assertTrue(Files.size(archivo) > 0, nombre + " está vacío");

            var contenido = JsonParser.parseString(Files.readString(archivo));

            assertTrue(contenido.isJsonArray());
            assertFalse(contenido.getAsJsonArray().isEmpty(), nombre + " no contiene registros");
        }

        // Comprobar también la exportación de un informe JSON
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("codigoPrueba", "TEST_EXPORT");
        datos.put("totalPrueba", 3);

        Path informe = carpeta.resolve("InformeJUnit.json");
        Files.deleteIfExists(informe);

        ExportarDatos.exportarInformeJSON("InformeJUnit.json", datos);

        assertTrue(Files.exists(informe));
        assertTrue(Files.size(informe) > 0);

        var contenido = JsonParser.parseString(Files.readString(informe));

        assertTrue(contenido.isJsonObject());
        assertEquals(
                "TEST_EXPORT",
                contenido.getAsJsonObject().get("codigoPrueba").getAsString()
        );

        Files.deleteIfExists(informe);
    }
}