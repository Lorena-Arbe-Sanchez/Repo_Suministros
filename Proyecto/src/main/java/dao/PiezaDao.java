package dao;

import idao.IPiezaDao;
import models.Pieza;

public class PiezaDao extends Dao<Pieza, String> implements IPiezaDao {

    @Override
    public Pieza find(String cod) {
        return em.find(Pieza.class, cod);
    }

    // todo
}