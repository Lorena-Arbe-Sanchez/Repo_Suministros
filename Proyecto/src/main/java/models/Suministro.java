package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class Suministro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Double precio;

    @NotNull
    private int cantidad;

    // todo
}