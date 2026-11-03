package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Proveedor {

    @Id
    @Column(name = "cod", nullable = false)
    private int cod;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido1;

    @NotNull
    private String apellido2;

    @NotNull
    private String direccion;

    // todo
}