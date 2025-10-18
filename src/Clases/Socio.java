package Clases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Socio {
    static int contador = 0;
    private int idSocio;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private int num_documento;
    private LocalDate fecha_nacimiento;
    private int telefono;
    private String pais;

    public int getIdSocio() {
        return idSocio;
    }
    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApaterno() {
        return apaterno;
    }
    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }
    public String getAmaterno() {
        return amaterno;
    }
    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }
    public int getNum_documento() {
        return num_documento;
    }
    public void setNum_documento(int num_documento) {
        this.num_documento = num_documento;
    }
    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }
    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Id Socio: " + idSocio + "\n"
                +"Nombre: " + nombre + " " + apaterno + " " + amaterno + "\n"
                + "Documento: " + num_documento + "\n"
                + "Fecha de nacimiento: " + fecha_nacimiento.format(formato) + "\n"
                + "Teléfono: " + telefono + "\n"
                + "Pais: " + pais;

    }

    public Socio(String nombre, String apaterno, String amaterno, int num_documento, LocalDate fecha_nacimiento, int telefono, String pais) {
        this.idSocio = ++contador;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.num_documento = num_documento;
        this.fecha_nacimiento = fecha_nacimiento;
        this.telefono = telefono;
        this.pais = pais;
    }
}
