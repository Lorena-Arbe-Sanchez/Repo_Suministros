package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class Proyecto {

    @Id
    @Column(name = "cod", nullable = false)
    private int cod;

    @NotNull
    private String nombre;

    @NotNull
    private String ciudad;

    // todo
}