package idao;

import models.Suministro;

import java.util.List;

public interface ISuministroDao {

    List<Suministro> findAll();

    List<Suministro> buscar(String texto);
}
