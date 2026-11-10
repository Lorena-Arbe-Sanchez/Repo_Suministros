package dao;

import idao.ISuministroDao;
import models.Suministro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}