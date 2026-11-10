package dao;

import idao.IPiezaDao;
import models.Pieza;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PiezaDao extends Dao<Pieza, String> implements IPiezaDao {

    private static final Logger logger = LoggerFactory.getLogger(PiezaDao.class);

    @Override
    public Pieza find(String cod) {
        return em.find(Pieza.class, cod);
    }

    // Obtener todas las piezas
    @Override
    public List<Pieza> findAll() {
        return em.createQuery(
                "SELECT p FROM Pieza p ORDER BY p.cod", Pieza.class
        ).getResultList();
    }

    // Buscar piezas por código, nombre o descripción
    @Override
    public List<Pieza> buscar(String texto) {
        logger.info("Buscando piezas con el texto: {}", texto);

        return em.createQuery(
                        "SELECT p FROM Pieza p WHERE " +
                                "LOWER(p.cod) LIKE :texto OR " +
                                "LOWER(p.nombre) LIKE :texto OR " +
                                "LOWER(p.descripcion) LIKE :texto",
                        Pieza.class
                ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }

    // Buscar piezas por código
    @Override
    public List<Pieza> buscarPorCod(String cod) {
        logger.info("Buscando piezas por código: {}", cod);

        return em.createQuery(
                "SELECT p FROM Pieza p WHERE p.cod = :cod", Pieza.class
        ).setParameter("cod", cod).getResultList();
    }

    // Buscar piezas por nombre
    @Override
    public List<Pieza> buscarPorNombre(String nombre) {
        logger.info("Buscando piezas por nombre: {}", nombre);

        return em.createQuery(
                "SELECT p FROM Pieza p WHERE LOWER(p.nombre) LIKE :texto", Pieza.class
        ).setParameter("texto", "%" + nombre.toLowerCase() + "%").getResultList();
    }

    // Buscar piezas por descripción
    @Override
    public List<Pieza> buscarPorDescripcion(String descripcion) {
        logger.info("Buscando piezas por descripción: {}", descripcion);

        return em.createQuery(
                "SELECT p FROM Pieza p WHERE LOWER(p.descripcion) LIKE :texto", Pieza.class
        ).setParameter("texto", "%" + descripcion.toLowerCase() + "%").getResultList();
    }

    // Buscar piezas por intervalo de precios
    @Override
    public List<Pieza> buscarPorPrecio(double precioMin, double precioMax) {
        logger.info("Buscando piezas entre {} y {} euros", precioMin, precioMax);

        return em.createQuery(
                        "SELECT p FROM Pieza p WHERE p.precio BETWEEN :min AND :max ORDER BY p.precio",
                        Pieza.class
                ).setParameter("min", precioMin)
                .setParameter("max", precioMax)
                .getResultList();
    }

    // Buscar piezas por proveedor
    @Override
    public List<Pieza> buscarPorProveedor(String proveedor) {
        logger.info("Buscando piezas por proveedor: {}", proveedor);

        return em.createQuery(
                "SELECT DISTINCT s.pieza FROM Suministro s WHERE " +
                        "LOWER(s.proveedor.cod) LIKE :texto OR " +
                        "LOWER(s.proveedor.nombre) LIKE :texto",
                Pieza.class
        ).setParameter("texto", "%" + proveedor.toLowerCase() + "%").getResultList();
    }

    // Buscar piezas por proyecto
    @Override
    public List<Pieza> buscarPorProyecto(String proyecto) {
        logger.info("Buscando piezas por proyecto: {}", proyecto);

        return em.createQuery(
                "SELECT DISTINCT s.pieza FROM Suministro s WHERE " +
                        "LOWER(s.proyecto.cod) LIKE :texto OR " +
                        "LOWER(s.proyecto.nombre) LIKE :texto",
                Pieza.class
        ).setParameter("texto", "%" + proyecto.toLowerCase() + "%").getResultList();
    }
}