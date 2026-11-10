package idao;

import models.Suministro;

import java.util.List;

public interface ISuministroDao {

    List<Suministro> findAll();

    List<Suministro> buscar(String texto);

    List<Suministro> buscarPorId(int id);

    List<Suministro> buscarPorProveedor(String proveedor);

    List<Suministro> buscarPorPieza(String pieza);

    List<Suministro> buscarPorProyecto(String proyecto);
}
