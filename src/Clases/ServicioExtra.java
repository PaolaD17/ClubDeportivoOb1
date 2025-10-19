package Clases;

public class ServicioExtra {
    private String descripcion;
    private double costo;

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getCosto() {
        return costo;
    }
    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Descripcion: " + descripcion + "\n" +
                " | Costo: $" + costo;
    }

    public ServicioExtra(String descripcion, double costo) {
        this.descripcion = descripcion;
        this.costo = costo;
    }
}
