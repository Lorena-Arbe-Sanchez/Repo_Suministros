package dao;

import idao.IProveedorDao;
import models.Proveedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProveedorDao extends Dao<Proveedor, String> implements IProveedorDao {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorDao.class);

    @Override
    public Proveedor find(String cod) {
        return em.find(Proveedor.class, cod);
    }

    // Obtener todos los proveedores
    @Override
    public List<Proveedor> findAll() {
        return em.createQuery(
                "SELECT p FROM Proveedor p ORDER BY p.cod", Proveedor.class
        ).getResultList();
    }

    // Buscar proveedores por código, nombre, apellidos o dirección
    @Override
    public List<Proveedor> buscar(String texto) {
        logger.info("Buscando proveedores con el texto: {}", texto);

        return em.createQuery(
                        "SELECT p FROM Proveedor p WHERE " +
                                "LOWER(p.cod) LIKE :texto OR " +
                                "LOWER(p.nombre) LIKE :texto OR " +
                                "LOWER(p.apellido1) LIKE :texto OR " +
                                "LOWER(p.apellido2) LIKE :texto OR " +
                                "LOWER(p.direccion) LIKE :texto",
                        Proveedor.class
                ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }

    // Buscar proveedores por código
    @Override
    public List<Proveedor> buscarPorCod(String cod) {
        logger.info("Buscando proveedores por código: {}", cod);

        return em.createQuery(
                "SELECT p FROM Proveedor p WHERE p.cod = :cod", Proveedor.class
        ).setParameter("cod", cod).getResultList();
    }

    // Buscar proveedores por nombre
    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {
        logger.info("Buscando proveedores por nombre: {}", nombre);

        return em.createQuery(
                "SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE :texto", Proveedor.class
        ).setParameter("texto", "%" + nombre.toLowerCase() + "%").getResultList();
    }

    // Buscar proveedores por primer apellido
    @Override
    public List<Proveedor> buscarPorApellido1(String apellido1) {
        logger.info("Buscando proveedores por primer apellido: {}", apellido1);

        return em.createQuery(
                "SELECT p FROM Proveedor p WHERE LOWER(p.apellido1) LIKE :texto", Proveedor.class
        ).setParameter("texto", "%" + apellido1.toLowerCase() + "%").getResultList();
    }

    // Buscar proveedores por segundo apellido
    @Override
    public List<Proveedor> buscarPorApellido2(String apellido2) {
        logger.info("Buscando proveedores por segundo apellido: {}", apellido2);

        return em.createQuery(
                "SELECT p FROM Proveedor p WHERE LOWER(p.apellido2) LIKE :texto", Proveedor.class
        ).setParameter("texto", "%" + apellido2.toLowerCase() + "%").getResultList();
    }

    // Buscar proveedores por dirección
    @Override
    public List<Proveedor> buscarPorDireccion(String direccion) {
        logger.info("Buscando proveedores por dirección: {}", direccion);

        return em.createQuery(
                "SELECT p FROM Proveedor p WHERE LOWER(p.direccion) LIKE :texto", Proveedor.class
        ).setParameter("texto", "%" + direccion.toLowerCase() + "%").getResultList();
    }

    // Buscar proveedores por pieza suministrada
    @Override
    public List<Proveedor> buscarPorPieza(String pieza) {
        logger.info("Buscando proveedores por pieza: {}", pieza);

        return em.createQuery(
                "SELECT DISTINCT s.proveedor FROM Suministro s WHERE " +
                        "LOWER(s.pieza.cod) LIKE :texto OR " +
                        "LOWER(s.pieza.nombre) LIKE :texto",
                Proveedor.class
        ).setParameter("texto", "%" + pieza.toLowerCase() + "%").getResultList();
    }
}