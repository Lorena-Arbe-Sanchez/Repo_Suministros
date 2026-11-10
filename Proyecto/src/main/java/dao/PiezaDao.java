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
}