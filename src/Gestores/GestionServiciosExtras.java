package Gestores;

import Clases.ServicioExtra;

import java.io.*;
import java.util.*;

public class GestionServiciosExtras {
    private List<ServicioExtra> listaServiciosExtras = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public GestionServiciosExtras() {
        cargarServiciosExtrasDesdeArchivo();
    }

    public List<ServicioExtra> getListaServiciosExtras() {
        return listaServiciosExtras;
    }

    public void mostrarMenuServiciosExtras() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("--GESTIÓN DE SERVICIOS EXTRAS--");
            System.out.println("1. Registrar servicio extra");
            System.out.println("2. Listar servicios extras");
            System.out.println("0. Volver");

            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrarServicioExtra();
                    break;
                case 2:
                    listarServiciosExtras();
                    break;
                case 0:
                    System.out.println("Volver al menú principal...");
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    public void registrarServicioExtra() {
        //Registrar descripción
        System.out.print("Descripción del servicio extra: ");
        String descripcion = sc.nextLine();

        //Registrar costo
        System.out.print("Costo: ");
        double costo = Double.parseDouble(sc.nextLine());

        //Creo el nuevo servicio extra y lo agrego a la listaServiciosExtras
        ServicioExtra nuevo = new ServicioExtra(descripcion, costo);
        listaServiciosExtras.add(nuevo);

        guardarServiciosExtrasEnArchivo();

        System.out.println("Servicio extra registrado.");
    }

    public void listarServiciosExtras() {
        if (listaServiciosExtras.isEmpty()) {
            System.out.println("No hay servicios extras registrados.");
            return;
        }
        for (ServicioExtra se : listaServiciosExtras) {
            System.out.println(se);
            System.out.println("-----------------");
        }
    }

    public void guardarServiciosExtrasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("extras.txt"))) {
            for (ServicioExtra e : listaServiciosExtras) {
                writer.write(e.getDescripcion() + ";" + e.getCosto());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar servicios extras: " + e.getMessage());
        }
    }

    public void cargarServiciosExtrasDesdeArchivo() {
        File archivo = new File("extras.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                String descripcion = partes[0];
                double costo = Double.parseDouble(partes[1]);
                listaServiciosExtras.add(new ServicioExtra(descripcion, costo));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar servicios extras: " + e.getMessage());
        }
    }
}