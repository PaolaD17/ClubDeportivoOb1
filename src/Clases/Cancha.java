package Clases;

import java.util.List;

public class Cancha {
    static int contador = 0;
    private int idCancha;
    private String nombre;
    private String deporte;
    private boolean cubierta;
    private int capacidad;
    private String estado;
    private List<String> caracteristicas;

    public int getIdCancha() {
        return idCancha;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDeporte() {
        return deporte;
    }
    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
    public boolean isCubierta() {
        return cubierta;
    }
    public void setCubierta(boolean cubierta) {
        this.cubierta = cubierta;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        if (estado.equalsIgnoreCase("disponible") || estado.equalsIgnoreCase("reservada") || estado.equalsIgnoreCase("ocupada")) {
            this.estado = estado.toLowerCase();
        } else {
            System.out.println("⚠️ Estado inválido. Debe ser disponible, reservada u ocupada.");
        }
    }
    public List<String> getCaracteristicas() {
        return caracteristicas;
    }
    public void setCaracteristicas(List<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    @Override
    public String toString() {
        return "Id Cancha: " + idCancha + "\n" +
                "Nombre: " + nombre + "\n" +
                "Deporte: " + deporte + "\n" +
                "Cubierta: " + (cubierta ? "Sí" : "No") + "\n" +
                "Capacidad: " + capacidad + "\n" +
                "Estado: " + estado + "\n" +
                "Caracteristicas: " + String.join(", ", caracteristicas);
    }

    public Cancha(String nombre, String deporte, boolean cubierta, int capacidad, String estado, List<String> caracteristicas) {
        this.idCancha = ++contador;
        this.nombre = nombre;
        this.deporte = deporte;
        this.cubierta = cubierta;
        this.capacidad = capacidad;
        this.estado = estado;
        this.caracteristicas = caracteristicas;
    }
}
