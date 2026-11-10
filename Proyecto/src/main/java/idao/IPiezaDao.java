package idao;

import models.Pieza;

import java.util.List;

public interface IPiezaDao {

    List<Pieza> findAll();

    List<Pieza> buscar(String texto);

    List<Pieza> buscarPorCod(String cod);

    List<Pieza> buscarPorNombre(String nombre);

    List<Pieza> buscarPorDescripcion(String descripcion);

    List<Pieza> buscarPorPrecio(double precioMin, double precioMax);

    List<Pieza> buscarPorProveedor(String proveedor);

    List<Pieza> buscarPorProyecto(String proyecto);
}