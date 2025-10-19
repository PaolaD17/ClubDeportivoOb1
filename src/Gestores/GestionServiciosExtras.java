package Gestores;

import Clases.Cancha;
import Clases.ServicioExtra;

import java.io.*;
import java.util.*;

public class GestionServiciosExtras {
    private List<ServicioExtra> listaExtras = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public GestionServiciosExtras() {
        cargarExtrasDesdeArchivo();
    }

    public void mostrarMenuExtras() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("--- GESTIÓN DE SERVICIOS EXTRAS ---");
            System.out.println("1. Registrar servicio extra");
            System.out.println("2. Listar servicios extras");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    registrarExtra();
                    break;
                case 2:
                    listarExtras();
                    break;
                case 0:
                    System.out.println("Volviendo...");
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    public void registrarExtra() {
        System.out.print("Descripción del extra: ");
        String descripcion = sc.nextLine();

        System.out.print("Costo: ");
        double costo = Double.parseDouble(sc.nextLine());

        ServicioExtra nuevo = new ServicioExtra(descripcion, costo);
        listaExtras.add(nuevo);

        guardarExtrasEnArchivo();

        System.out.println("Extra registrado.");
    }

    public void listarExtras() {
        if (listaExtras.isEmpty()) {
            System.out.println("No hay extras registrados.");
            return;
        }
        for (ServicioExtra se : listaExtras) {
            System.out.println(se);
            System.out.println("-----------------");
        }
    }

    public List<ServicioExtra> getListaExtras() {
        return listaExtras;
    }

    public void guardarExtrasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("extras.txt"))) {
            for (ServicioExtra e : listaExtras) {
                writer.write(e.getDescripcion() + ";" + e.getCosto());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar extras: " + e.getMessage());
        }
    }

    public void cargarExtrasDesdeArchivo() {
        File archivo = new File("extras.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                String descripcion = partes[0];
                double costo = Double.parseDouble(partes[1]);
                listaExtras.add(new ServicioExtra(descripcion, costo));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar extras: " + e.getMessage());
        }
    }
}