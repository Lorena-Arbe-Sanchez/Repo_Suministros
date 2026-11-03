package dao;

import idao.ISuministroDao;
import models.Suministro;

public class SuministroDao extends Dao<Suministro, String> implements ISuministroDao {

    @Override
    public Suministro find(String cod) {
        return em.find(Suministro.class, cod);
    }

    // todo
}