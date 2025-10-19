package Clases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarifa {
    private String deporte;
    private double monto;
    private LocalDate fechaVigencia;

    public String getDeporte() {
        return deporte;
    }
    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
            this.monto = monto;
    }
    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }
    public void setFechaVigencia(LocalDate fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Deporte: " + deporte +
                " | Monto: $" + monto +
                " | Vigente desde: " + fechaVigencia.format(formato);
    }

    public Tarifa(String deporte, double monto, LocalDate fechaVigencia) {
        this.deporte = deporte;
        this.monto = monto;
        this.fechaVigencia = fechaVigencia;
    }
}
