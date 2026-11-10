package dao;

import idao.IProyectoDao;
import models.Proyecto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProyectoDao extends Dao<Proyecto, String> implements IProyectoDao {

    private static final Logger logger = LoggerFactory.getLogger(ProyectoDao.class);

    @Override
    public Proyecto find(String cod) {
        return em.find(Proyecto.class, cod);
    }

    // Obtener todos los proyectos
    @Override
    public List<Proyecto> findAll() {
        return em.createQuery(
                "SELECT p FROM Proyecto p ORDER BY p.cod", Proyecto.class
        ).getResultList();
    }

    // Buscar proyectos por código, nombre o ciudad
    @Override
    public List<Proyecto> buscar(String texto) {
        logger.info("Buscando proyectos con el texto: {}", texto);

        return em.createQuery(
                        "SELECT p FROM Proyecto p WHERE " +
                                "LOWER(p.cod) LIKE :texto OR " +
                                "LOWER(p.nombre) LIKE :texto OR " +
                                "LOWER(p.ciudad) LIKE :texto",
                        Proyecto.class
                ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }

    // Buscar proyectos por código
    @Override
    public List<Proyecto> buscarPorCod(String cod) {
        logger.info("Buscando proyectos por código: {}", cod);

        return em.createQuery(
                "SELECT p FROM Proyecto p WHERE p.cod = :cod", Proyecto.class
        ).setParameter("cod", cod).getResultList();
    }

    // Buscar proyectos por nombre
    @Override
    public List<Proyecto> buscarPorNombre(String nombre) {
        logger.info("Buscando proyectos por nombre: {}", nombre);

        return em.createQuery(
                "SELECT p FROM Proyecto p WHERE LOWER(p.nombre) LIKE :texto", Proyecto.class
        ).setParameter("texto", "%" + nombre.toLowerCase() + "%").getResultList();
    }

    // Buscar proyectos por ciudad
    @Override
    public List<Proyecto> buscarPorCiudad(String ciudad) {
        logger.info("Buscando proyectos por ciudad: {}", ciudad);

        return em.createQuery(
                "SELECT p FROM Proyecto p WHERE LOWER(p.ciudad) LIKE :texto", Proyecto.class
        ).setParameter("texto", "%" + ciudad.toLowerCase() + "%").getResultList();
    }

    // Buscar proyectos por proveedor participante
    @Override
    public List<Proyecto> buscarPorProveedor(String proveedor) {
        logger.info("Buscando proyectos por proveedor: {}", proveedor);

        return em.createQuery(
                "SELECT DISTINCT s.proyecto FROM Suministro s WHERE " +
                        "LOWER(s.proveedor.cod) LIKE :texto OR " +
                        "LOWER(s.proveedor.nombre) LIKE :texto",
                Proyecto.class
        ).setParameter("texto", "%" + proveedor.toLowerCase() + "%").getResultList();
    }

    // Buscar proyectos por pieza utilizada
    @Override
    public List<Proyecto> buscarPorPieza(String pieza) {
        logger.info("Buscando proyectos por pieza: {}", pieza);

        return em.createQuery(
                "SELECT DISTINCT s.proyecto FROM Suministro s WHERE " +
                        "LOWER(s.pieza.cod) LIKE :texto OR " +
                        "LOWER(s.pieza.nombre) LIKE :texto",
                Proyecto.class
        ).setParameter("texto", "%" + pieza.toLowerCase() + "%").getResultList();
    }
}