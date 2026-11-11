package idao;

import models.Proyecto;

import java.util.List;

public interface IProyectoDao {

    List<Proyecto> findAll();

    List<Proyecto> buscar(String texto);

    List<Proyecto> buscarPorCod(String cod);

    List<Proyecto> buscarPorNombre(String nombre);

    List<Proyecto> buscarPorCiudad(String ciudad);

    List<Proyecto> buscarPorProveedor(String proveedor);

    List<Proyecto> buscarPorPieza(String pieza);
}