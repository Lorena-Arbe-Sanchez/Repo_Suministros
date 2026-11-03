package dao;

import idao.IProyectoDao;
import models.Proyecto;

public class ProyectoDao extends Dao<Proyecto, String> implements IProyectoDao {

    @Override
    public Proyecto find(String cod) {
        return em.find(Proyecto.class, cod);
    }

    // todo
}