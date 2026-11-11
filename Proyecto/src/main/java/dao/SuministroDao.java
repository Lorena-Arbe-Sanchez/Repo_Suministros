package dao;

import idao.ISuministroDao;
import models.Suministro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class SuministroDao extends Dao<Suministro, Integer> implements ISuministroDao {

    private static final Logger logger = LoggerFactory.getLogger(SuministroDao.class);

    @Override
    public Suministro find(Integer id) {
        return em.find(Suministro.class, id);
    }

    // Obtener todos los suministros
    @Override
    public List<Suministro> findAll() {
        return em.createQuery(
                "SELECT s FROM Suministro s ORDER BY s.id", Suministro.class
        ).getResultList();
    }

    // Buscar suministros por ID, proveedor, pieza o proyecto
    @Override
    public List<Suministro> buscar(String texto) {
        logger.info("Buscando suministros con el texto: {}", texto);

        String consulta =
                "SELECT s FROM Suministro s WHERE " +
                        "LOWER(s.proveedor.cod) LIKE :texto OR " +
                        "LOWER(s.proveedor.nombre) LIKE :texto OR " +
                        "LOWER(s.pieza.cod) LIKE :texto OR " +
                        "LOWER(s.pieza.nombre) LIKE :texto OR " +
                        "LOWER(s.proyecto.cod) LIKE :texto OR " +
                        "LOWER(s.proyecto.nombre) LIKE :texto";

        try {
            int id = Integer.parseInt(texto);

            return em.createQuery(
                            consulta + " OR s.id = :id", Suministro.class
                    ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                    .setParameter("id", id)
                    .getResultList();

        } catch (NumberFormatException e) {
            return em.createQuery(consulta, Suministro.class)
                    .setParameter("texto", "%" + texto.toLowerCase() + "%")
                    .getResultList();
        }
    }

    // Buscar suministros por ID
    @Override
    public List<Suministro> buscarPorId(int id) {
        logger.info("Buscando suministro por ID: {}", id);

        return em.createQuery(
                "SELECT s FROM Suministro s WHERE s.id = :id", Suministro.class
        ).setParameter("id", id).getResultList();
    }

    // Buscar suministros por proveedor
    @Override
    public List<Suministro> buscarPorProveedor(String proveedor) {
        logger.info("Buscando suministros por proveedor: {}", proveedor);

        return em.createQuery(
                "SELECT s FROM Suministro s WHERE " +
                        "LOWER(s.proveedor.cod) LIKE :texto OR " +
                        "LOWER(s.proveedor.nombre) LIKE :texto",
                Suministro.class
        ).setParameter("texto", "%" + proveedor.toLowerCase() + "%").getResultList();
    }

    // Buscar suministros por pieza
    @Override
    public List<Suministro> buscarPorPieza(String pieza) {
        logger.info("Buscando suministros por pieza: {}", pieza);

        return em.createQuery(
                "SELECT s FROM Suministro s WHERE " +
                        "LOWER(s.pieza.cod) LIKE :texto OR " +
                        "LOWER(s.pieza.nombre) LIKE :texto",
                Suministro.class
        ).setParameter("texto", "%" + pieza.toLowerCase() + "%").getResultList();
    }

    // Buscar suministros por proyecto
    @Override
    public List<Suministro> buscarPorProyecto(String proyecto) {
        logger.info("Buscando suministros por proyecto: {}", proyecto);

        return em.createQuery(
                "SELECT s FROM Suministro s WHERE " +
                        "LOWER(s.proyecto.cod) LIKE :texto OR " +
                        "LOWER(s.proyecto.nombre) LIKE :texto",
                Suministro.class
        ).setParameter("texto", "%" + proyecto.toLowerCase() + "%").getResultList();
    }

    // Consultar el histórico de suministros de un proveedor durante un periodo
    @Override
    public List<Suministro> buscarHistoricoProveedor(String codProveedor, LocalDate desde, LocalDate hasta) {
        logger.info("Consultando histórico del proveedor {} entre {} y {}", codProveedor, desde, hasta);

        return em.createQuery(
                        "SELECT s FROM Suministro s " +
                                "JOIN FETCH s.pieza " +
                                "JOIN FETCH s.proyecto " +
                                "JOIN FETCH s.proveedor " +
                                "WHERE s.proveedor.cod = :codProveedor " +
                                "AND s.fecha BETWEEN :desde AND :hasta " +
                                "ORDER BY s.fecha, s.id",
                        Suministro.class
                ).setParameter("codProveedor", codProveedor)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();
    }

    // Consultar los suministros de un proyecto para realizar su análisis
    @Override
    public List<Suministro> buscarAnalisisProyecto(String codProyecto) {
        logger.info("Consultando análisis del proyecto {}", codProyecto);

        return em.createQuery(
                        "SELECT s FROM Suministro s " +
                                "JOIN FETCH s.pieza " +
                                "JOIN FETCH s.proveedor " +
                                "JOIN FETCH s.proyecto " +
                                "WHERE s.proyecto.cod = :codProyecto " +
                                "ORDER BY s.fecha, s.id",
                        Suministro.class
                ).setParameter("codProyecto", codProyecto)
                .getResultList();
    }
}