import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// TODO : Hacer

/**
 * Pruebas unitarias para comprobar el funcionamiento de la conexión
 * y de los DAO de la aplicación de gestión de la asociación de juegos.
 */
public class AppTest extends TestCase {
    /**
     * Constructor de la clase de pruebas.
     *
     * @param testName nombre del test
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * Crea la suite con todos los métodos de prueba de esta clase.
     *
     * @return conjunto de pruebas
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }
}