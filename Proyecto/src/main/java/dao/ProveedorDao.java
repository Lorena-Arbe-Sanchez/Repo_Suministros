package dao;

import idao.IProveedorDao;
import models.Proveedor;

public class ProveedorDao extends Dao<Proveedor, String> implements IProveedorDao {

    @Override
    public Proveedor find(String cod) {
        return em.find(Proveedor.class, cod);
    }

    // todo
}