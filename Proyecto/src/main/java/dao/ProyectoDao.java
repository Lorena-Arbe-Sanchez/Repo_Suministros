package dao;

import idao.IProyectoDao;
import models.Proyecto;

import java.util.List;

public class ProyectoDao extends Dao<Proyecto, String> implements IProyectoDao {

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
        return em.createQuery(
                        "SELECT p FROM Proyecto p WHERE " +
                                "LOWER(p.cod) LIKE :texto OR " +
                                "LOWER(p.nombre) LIKE :texto OR " +
                                "LOWER(p.ciudad) LIKE :texto",
                        Proyecto.class
                ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }
}