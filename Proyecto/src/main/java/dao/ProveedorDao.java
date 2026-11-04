package dao;

import idao.IProveedorDao;
import models.Proveedor;

import java.util.List;

public class ProveedorDao extends Dao<Proveedor, String> implements IProveedorDao {

    @Override
    public Proveedor find(String cod) {
        return em.find(Proveedor.class, cod);
    }

    // Obtener todos los proveedores
    @Override
    public List<Proveedor> findAll() {
        return em.createQuery(
                "SELECT p FROM Proveedor p ORDER BY p.cod", Proveedor.class
        ).getResultList();
    }

    // Buscar proveedores por código, nombre, apellidos o dirección
    @Override
    public List<Proveedor> buscar(String texto) {
        return em.createQuery(
                        "SELECT p FROM Proveedor p WHERE " +
                                "LOWER(p.cod) LIKE :texto OR " +
                                "LOWER(p.nombre) LIKE :texto OR " +
                                "LOWER(p.apellido1) LIKE :texto OR " +
                                "LOWER(p.apellido2) LIKE :texto OR " +
                                "LOWER(p.direccion) LIKE :texto",
                        Proveedor.class
                ).setParameter("texto", "%" + texto.toLowerCase() + "%")
                .getResultList();
    }

    // todo
}