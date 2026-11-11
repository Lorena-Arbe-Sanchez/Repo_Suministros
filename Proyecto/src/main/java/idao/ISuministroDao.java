package idao;

import models.Suministro;

import java.time.LocalDate;
import java.util.List;

public interface ISuministroDao {

    List<Suministro> findAll();

    List<Suministro> buscar(String texto);

    List<Suministro> buscarPorId(int id);

    List<Suministro> buscarPorProveedor(String proveedor);

    List<Suministro> buscarPorPieza(String pieza);

    List<Suministro> buscarPorProyecto(String proyecto);

    List<Suministro> buscarHistoricoProveedor(String codProveedor, LocalDate desde, LocalDate hasta);

    // Consultar los suministros de un proyecto para realizar su análisis
    List<Suministro> buscarAnalisisProyecto(String codProyecto);

    // Obtener todos los suministros con sus relaciones para las estadísticas
    List<Suministro> buscarTodosParaEstadisticas();
}