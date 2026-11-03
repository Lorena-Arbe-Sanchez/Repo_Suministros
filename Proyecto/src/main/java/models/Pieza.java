package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class Pieza {

    @Id
    @Column(name = "cod", nullable = false)
    private int cod;

    @NotNull
    private String nombre;

    @NotNull
    private Double precio;

    @NotNull
    private String descripcion;

    // todo
}