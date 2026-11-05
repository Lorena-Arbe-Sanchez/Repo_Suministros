package idao;

import models.Proveedor;

import java.util.List;

public interface IProveedorDao {

    List<Proveedor> findAll();

    List<Proveedor> buscar(String texto);
}