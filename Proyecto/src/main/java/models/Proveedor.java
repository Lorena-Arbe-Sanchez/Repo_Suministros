package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Proveedor {

    @Id
    @Column(name = "cod", nullable = false)
    private int cod;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "apellido1", nullable = false)
    private String apellido1;

    @NotNull
    @Column(name = "apellido2", nullable = false)
    private String apellido2;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<Suministro> suministros;

    public Proveedor() {
    }

    public Proveedor(int cod, String nombre, String apellido1, String apellido2, String direccion,
                     List<Suministro> suministros) {
        this.cod = cod;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.direccion = direccion;
        this.suministros = suministros;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Suministro> getSuministros() {
        return suministros;
    }

    public void setSuministros(List<Suministro> suministros) {
        this.suministros = suministros;
    }
}