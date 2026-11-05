package idao;

import models.Proyecto;

import java.util.List;

public interface IProyectoDao {

    List<Proyecto> findAll();

    List<Proyecto> buscar(String texto);
}
