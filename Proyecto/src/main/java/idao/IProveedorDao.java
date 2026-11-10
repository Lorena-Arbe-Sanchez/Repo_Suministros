package idao;

import models.Proveedor;

import java.util.List;

public interface IProveedorDao {

    List<Proveedor> findAll();

    List<Proveedor> buscar(String texto);

    List<Proveedor> buscarPorCod(String cod);

    List<Proveedor> buscarPorNombre(String nombre);

    List<Proveedor> buscarPorApellido1(String apellido1);

    List<Proveedor> buscarPorApellido2(String apellido2);

    List<Proveedor> buscarPorDireccion(String direccion);

    List<Proveedor> buscarPorPieza(String pieza);
}