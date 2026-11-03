package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
public class Proyecto {

    @Id
    @Column(name = "cod", nullable = false)
    private String cod;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    private List<Suministro> suministros;

    public Proyecto() {
    }

    public Proyecto(String cod, String nombre, String ciudad, List<Suministro> suministros) {
        this.cod = cod;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.suministros = suministros;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Suministro> getSuministros() {
        return suministros;
    }

    public void setSuministros(List<Suministro> suministros) {
        this.suministros = suministros;
    }
}