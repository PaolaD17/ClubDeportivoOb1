package Gestores;

import Clases.Cancha;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionCanchas {
    private List<Cancha> listaCanchas;
    private Scanner sc;

    public GestionCanchas() {
        this.listaCanchas = new ArrayList<>();
        this.sc = new Scanner(System.in);
        cargarCanchasDesdeArchivo();
    }

    public void mostrarMenuCanchas() {
        int opcionCanchas = -1;
        while (opcionCanchas != 0) {
            System.out.println("--- GESTIÓN DE CANCHAS ---");
            System.out.println("1. Registrar una cancha");
            System.out.println("2. Listar canchas");
            System.out.println("3. Modificar una cancha");
            System.out.println("4. Eliminar una cancha");
            System.out.println("5. Consultas");
            System.out.println("0. Volver al Menú Principal");

            try {
                opcionCanchas = sc.nextInt();
                sc.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Por favor, ingrese un número.");
                sc.nextLine();
                opcionCanchas = -1;
                continue;
            }

            switch (opcionCanchas) {
                case 1:
                    registrarCanchas();
                    break;
                case 2:
                    listarCanchas();
                    break;
                case 3:
                    modificarCanchas();
                    break;
                case 4:
                    eliminarCanchas();
                    break;
                case 5:
                    mostrarMenuConsultas();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    public void registrarCanchas() {
        System.out.println("--REGISTRAR CANCHAS--");
        System.out.println("Nombre: ");
        String nombre = sc.nextLine();

        System.out.println("Deporte: ");
        String deporte = sc.nextLine();

        System.out.println("¿Cubierta? S/N");
        String cubiertaStr = sc.nextLine();
        boolean cubierta = cubiertaStr.equalsIgnoreCase("s");

        System.out.println("Capacidad: ");
        int capacidad = sc.nextInt();
        sc.nextLine();

        System.out.println("Estado: (disponible/reservada/ocupada");
        String estado = sc.nextLine();

        System.out.println("Características: (separadas por coma)");
        String[] caracteristicasArray = sc.nextLine().split(",");
        List<String> caracteristicas = new ArrayList<>();
        for (String c : caracteristicasArray) {
            caracteristicas.add(c.trim());
        }

        Cancha nuevaCancha = new Cancha(nombre, deporte, cubierta, capacidad, estado, caracteristicas);
        listaCanchas.add(nuevaCancha);

        System.out.println("Cancha registrada con éxito!");
        System.out.println(nuevaCancha);

        guardarCanchasEnArchivo();
    }

    public void listarCanchas(){
        System.out.println("--- LISTA DE CANCHAS ---");
        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        for (Cancha c : listaCanchas) {
            System.out.println(c);
        }
    }

    public void modificarCanchas() {
        System.out.println("--MODIFICAR CANCHAS--");
        System.out.println("Canchas disponibles:");
        for (Cancha c : listaCanchas) {
            System.out.println("ID: " + c.getIdCancha() + " - " + c.getNombre());
        }

        System.out.print("Ingrese el ID de la cancha a modificar: ");
        int idModificar = sc.nextInt();
        sc.nextLine();

        Cancha canchaAModificar = null;
        for (Cancha c : listaCanchas) {
            if (c.getIdCancha() == idModificar) {
                canchaAModificar = c;
                break;
            }
        }

        if (canchaAModificar == null) {
            System.out.println("No hay cancha registrada con ese ID.");
        }

        System.out.println("Cancha actual:");
        System.out.println(canchaAModificar);

        System.out.print("Nuevo nombre (enter para mantener): ");
        String nuevoNombre = sc.nextLine();
        if (!nuevoNombre.isBlank()){
            canchaAModificar.setNombre(nuevoNombre);
        }

        System.out.print("Nuevo deporte (enter para mantener): ");
        String nuevoDeporte = sc.nextLine();
        if (!nuevoDeporte.isBlank()){
            canchaAModificar.setDeporte(nuevoDeporte);
        }

        System.out.print("¿Es cubierta? (s/n, enter para mantener): ");
        String cubiertaMod = sc.nextLine();
        if (cubiertaMod.equalsIgnoreCase("s")){
            canchaAModificar.setCubierta(true);
        }else if (cubiertaMod.equalsIgnoreCase("n")){
            canchaAModificar.setCubierta(false);
        }

        System.out.print("Nueva capacidad (enter para mantener): ");
        String capacidadStr = sc.nextLine();
        if (!capacidadStr.isBlank()){
            canchaAModificar.setCapacidad(Integer.parseInt(capacidadStr));
        }

        System.out.print("Nuevo estado (disponible/reservada/ocupada, enter para mantener): ");
        String nuevoEstado = sc.nextLine();
        if (!nuevoEstado.isBlank()){
            canchaAModificar.setEstado(nuevoEstado);
        }

        System.out.print("Nuevas características (separadas por coma, enter para mantener): ");
        String nuevasCaracteristicas = sc.nextLine();
        if (!nuevasCaracteristicas.isBlank()) {
            String[] caractArray = nuevasCaracteristicas.split(",");
            List<String> caractList = new ArrayList<>();
            for (String c : caractArray) {
                caractList.add(c.trim());
            }
            canchaAModificar.setCaracteristicas(caractList);
        }

        System.out.println("Cancha modificada con éxito.");

        guardarCanchasEnArchivo();
    }

    public void eliminarCanchas() {
        System.out.println("--ELIMINAR CANCHAS--");
        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.println("Canchas disponibles:");
        for (Cancha c : listaCanchas) {
            System.out.println("ID: " + c.getIdCancha() + " - " + c.getNombre());
        }

        System.out.print("Ingrese el ID de la cancha a eliminar: ");
        int idEliminar;
        try {
            idEliminar = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número válido.");
            sc.nextLine();
            return;
        }

        Cancha canchaAEliminar = null;
        for (Cancha c : listaCanchas) {
            if (c.getIdCancha() == idEliminar) {
                canchaAEliminar = c;
                break;
            }
        }

        if (canchaAEliminar == null) {
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }

        System.out.print("¿Está seguro que desea eliminar esta cancha? (s/n): ");
        String confirmacion = sc.nextLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            listaCanchas.remove(canchaAEliminar);
            System.out.println("Cancha eliminada con éxito.");
        } else {
            System.out.println("Operación cancelada.");
        }

        guardarCanchasEnArchivo();
    }

    public void mostrarMenuConsultas() {
        int opcionCanchasConsultas = -1;
        while (opcionCanchasConsultas != 0) {
            System.out.println("Consultas de canchas");
            System.out.println("1. Canchas por deporte");
            System.out.println("2. Canchas por nombre");
            System.out.println("3. Canchas por fecha");
            System.out.println("4. Canchas por condición (cubiertas o descubiertas)");
            System.out.println("0. Salir");

            opcionCanchasConsultas = sc.nextInt();
            sc.nextLine();
            switch (opcionCanchasConsultas) {
                case 1:
                    canchasPorDeporte();
                    break;
                case 2:
                    canchasPorNombre();
                    break;
                case 3:
                    canchasPorFecha();
                    break;
                case 4:
                    canchasPorCondicion();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Intenta de nuevo");
            }
        }
    }

    public void canchasPorDeporte() {
        System.out.println("-- CONSULTA POR DEPORTE --");

        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.print("Ingrese el deporte que desea consultar: ");
        String deporteBuscado = sc.nextLine().trim().toLowerCase();

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            if (c.getDeporte().toLowerCase().equals(deporteBuscado)) {
                System.out.println(c);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron canchas para el deporte " + deporteBuscado);
        }
    }

    public void canchasPorNombre() {
        System.out.println("-- CONSULTA POR NOMBRE --");

        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.print("Ingrese el nombre o parte del nombre a buscar: ");
        String nombreBuscado = sc.nextLine().trim().toLowerCase();

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            if (c.getNombre().toLowerCase().contains(nombreBuscado)) {
                System.out.println(c);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron canchas con ese nombre.");
        }
    }

    public void canchasPorFecha() {
        System.out.println("Canchas por fecha");
    }

    public void canchasPorCondicion() {
        System.out.println("-- CONSULTA POR CONDICIÓN --");

        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.print("¿Desea ver canchas cubiertas? (s/n): ");
        String respuesta = sc.nextLine().trim().toLowerCase();

        boolean buscarCubiertas;
        if (respuesta.equals("s")) {
            buscarCubiertas = true;
        } else if (respuesta.equals("n")) {
            buscarCubiertas = false;
        } else {
            System.out.println("Respuesta inválida. Use 's' para sí o 'n' para no.");
            return;
        }

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            if (c.isCubierta() == buscarCubiertas) {
                System.out.println("----------------------------");
                System.out.println(c);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron canchas " + (buscarCubiertas ? "cubiertas." : "descubiertas."));
        }
    }

    public void guardarCanchasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("canchas.txt"))) {
            for (Cancha c : listaCanchas) {
                String linea = c.getIdCancha() + ";" +
                        c.getNombre() + ";" +
                        c.getDeporte() + ";" +
                        c.isCubierta() + ";" +
                        c.getCapacidad() + ";" +
                        c.getEstado() + ";" +
                        String.join(",", c.getCaracteristicas());
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar las canchas: " + e.getMessage());
        }
    }

    public void cargarCanchasDesdeArchivo() {
        File archivo = new File("canchas.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                String deporte = partes[2];
                boolean cubierta = Boolean.parseBoolean(partes[3]);
                int capacidad = Integer.parseInt(partes[4]);
                String estado = partes[5];
                List<String> caracteristicas = new ArrayList<>();
                if (partes.length > 6) {
                    String[] caractArray = partes[6].split(",");
                    for (String c : caractArray) {
                        caracteristicas.add(c.trim());
                    }
                }

                Cancha cancha = new Cancha(nombre, deporte, cubierta, capacidad, estado, caracteristicas);
                cancha.setIdCancha(id);
                listaCanchas.add(cancha);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las canchas: " + e.getMessage());
        }
    }
}
