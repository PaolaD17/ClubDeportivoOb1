package Gestores;

import Clases.Tarifa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class GestionTarifas {
    private List<Tarifa> listaTarifas = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public GestionTarifas() {
        cargarTarifasDesdeArchivo();
    }

    public List<Tarifa> getListaTarifas() {
        return listaTarifas;
    }

    public void mostrarMenuTarifas() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("--GESTIÓN DE TARIFAS--");
            System.out.println("1. Registrar tarifa");
            System.out.println("2. Listar tarifas");
            System.out.println("3. Buscar tarifa vigente por deporte");
            System.out.println("0. Volver");

            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrarTarifa();
                    break;
                case 2:
                    listarTarifas();
                    break;
                case 3:
                    buscarTarifaPorDeporte();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo");
            }
        }
    }

    public void registrarTarifa() {
        //Registrar nombre del deporte de dicha tarifa
        System.out.print("Deporte: ");
        String deporte = sc.nextLine().trim().toLowerCase();

        //Registrar monto de la tarifa
        System.out.print("Monto: ");
        double monto = Double.parseDouble(sc.nextLine());

        //Registrar fecha de entrada en viencia de la tarifa
        System.out.print("Fecha de vigencia (dd/mm/yyyy): ");
        String fechaTexto = sc.nextLine();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaVigencia = LocalDate.parse(fechaTexto, f);

        Tarifa nueva = new Tarifa(deporte, monto, fechaVigencia);
        listaTarifas.add(nueva);

        guardarTarifasEnArchivo();

        System.out.println("Tarifa registrada.");
    }

    public void listarTarifas() {
        if (listaTarifas.isEmpty()) {
            System.out.println("No hay tarifas registradas.");
            return;
        }
        for (Tarifa t : listaTarifas) {
            System.out.println(t);
            System.out.println("-----------------");
        }
    }

    public Tarifa obtenerTarifaVigente(String deporte, LocalDate fechaPartido) {
        Tarifa tarifaVigente = null;
        for (Tarifa t : listaTarifas) {
            boolean mismoDeporte = t.getDeporte().equalsIgnoreCase(deporte);
            boolean fechaValida = !t.getFechaVigencia().isAfter(fechaPartido);

            if (mismoDeporte && fechaValida) {
                if (tarifaVigente == null || t.getFechaVigencia().isAfter(tarifaVigente.getFechaVigencia())) {
                    tarifaVigente = t;
                }
            }
        }
        return tarifaVigente;
    }

    public void buscarTarifaPorDeporte() {
        System.out.print("Ingrese el deporte: ");
        String deporte = sc.nextLine().trim().toLowerCase();

        System.out.print("Fecha del partido (dd/MM/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Tarifa tarifa = obtenerTarifaVigente(deporte, fecha);
        if (tarifa != null) {
            System.out.println("Tarifa vigente: " + tarifa);
        } else {
            System.out.println("No hay tarifa vigente para ese deporte en esa fecha.");
        }
    }

    public void guardarTarifasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tarifas.txt"))) {
            for (Tarifa t : listaTarifas) {
                writer.write(t.getDeporte() + ";" + t.getMonto() + ";" + t.getFechaVigencia());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar tarifas: " + e.getMessage());
        }
    }

    public void cargarTarifasDesdeArchivo() {
        File archivo = new File("tarifas.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                String deporte = partes[0];
                double monto = Double.parseDouble(partes[1]);
                LocalDate fecha = LocalDate.parse(partes[2]);
                listaTarifas.add(new Tarifa(deporte, monto, fecha));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar tarifas: " + e.getMessage());
        }
    }
}