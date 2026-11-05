package idao;

import models.Pieza;

import java.util.List;

public interface IPiezaDao {

    List<Pieza> findAll();

    List<Pieza> buscar(String texto);
}