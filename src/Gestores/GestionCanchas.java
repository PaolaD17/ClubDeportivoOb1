package Gestores;

import Clases.Cancha;
import Clases.Reserva;

import java.io.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionCanchas {
    private List<Cancha> listaCanchas;
    private GestionReservas gestionReservas;

    private Scanner sc;

    public GestionCanchas() {
        this.listaCanchas = new ArrayList<>();
        this.gestionReservas = gestionReservas;
        this.sc = new Scanner(System.in);

        cargarCanchasDesdeArchivo();
    }

    public List<Cancha> getListaCanchas() {
        return listaCanchas;
    }

    public void setGestorReservas(GestionReservas gestorReservas) {
        this.gestionReservas = gestorReservas;
    }

    public void mostrarMenuCanchas() {
        int opcionCanchas = -1;
        while (opcionCanchas != 0) {
            System.out.println("--GESTIÓN DE CANCHAS--");
            System.out.println("1. Registrar cancha");
            System.out.println("2. Listar canchas");
            System.out.println("3. Modificar cancha");
            System.out.println("4. Eliminar cancha");
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
        System.out.println("--REGISTRO DE CANCHAS--");

        //Registro de nombre
        System.out.println("Nombre: ");
        String nombre = sc.nextLine();

        //Registro de deporte
        System.out.println("Deporte: ");
        String deporte = sc.nextLine();

        //Registro de condición (si es cubierta o descubierta)
        System.out.println("¿Cubierta? S/N");
        String cubiertaStr = sc.nextLine();
        boolean cubierta = cubiertaStr.equalsIgnoreCase("s");

        //Registro de capacidad (cantidad de jugadores)
        System.out.println("Capacidad: ");
        int capacidad = sc.nextInt();
        sc.nextLine();

        //Registro de estado (por defecto será "disponible")
        String estado = "Disponible";

        //Registro de características
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
        System.out.println("--LISTA DE CANCHAS--");
        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }
        for (Cancha c : listaCanchas) {
            System.out.println(c);
            System.out.println("-----------------");
        }
    }

    public void modificarCanchas() {
        System.out.println("--MODIFICAR CANCHAS--");

        System.out.println("Canchas disponibles:");
        for (Cancha c : listaCanchas) {
            System.out.println("ID: " + c.getIdCancha() + " - Nombre: " + c.getNombre());
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
            return;
        }

        System.out.println("Cancha actual:");
        System.out.println(canchaAModificar);

        System.out.print("Nuevo nombre (enter para mantener): ");
        String nuevoNombre = sc.nextLine();
        if (!nuevoNombre.isBlank()){
            canchaAModificar.setNombre(nuevoNombre);
        }

        boolean modificacionRestringida = canchaAModificar.getEstado().equalsIgnoreCase("Ocupada")
                || canchaAModificar.getEstado().equalsIgnoreCase("Reservada");
        if (modificacionRestringida) {
            System.out.println("La cancha está reservada, no se puede modificar ese campo.");
        }

        if (!modificacionRestringida) {
            //Modificar campo de deporte
            System.out.print("Nuevo deporte (enter para mantener): ");
            String nuevoDeporte = sc.nextLine();
            if (!nuevoDeporte.isBlank()) {
                canchaAModificar.setDeporte(nuevoDeporte);
            }

            //Modificar campo de condición (cubierta o descubierta)
            System.out.print("¿Es cubierta? (s/n, enter para mantener): ");
            String cubiertaMod = sc.nextLine();
            if(!cubiertaMod.isBlank()){
                if (cubiertaMod.equalsIgnoreCase("s")) {
                    canchaAModificar.setCubierta(true);
                } else if (cubiertaMod.equalsIgnoreCase("n")) {
                    canchaAModificar.setCubierta(false);
                }
            }

            //Modificar campo de capacidad
            System.out.print("Nueva capacidad (enter para mantener): ");
            String capacidadStr = sc.nextLine();
            if (!capacidadStr.isBlank()) {
                canchaAModificar.setCapacidad(Integer.parseInt(capacidadStr));
            }
        }

        //Modificar campo de características
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

        //Mostrar las canchas disponibles
        System.out.println("Canchas disponibles:");
        for (Cancha c : listaCanchas) {
            System.out.println("ID: " + c.getIdCancha() + " - " + c.getNombre());
        }

        //Seleccionar el id de la cancha que queremos eliminar
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

        //Confirmación para eliminar
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
            System.out.println("3. Estado de cancha por fecha y hora");
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
                    estadoDeCanchasPorFechaYHora();
                    break;
                case 4:
                    canchasPorCondicion();
                    break;
                case 0:
                    System.out.println("Volver al menú principal");
                    break;
                default:
                    System.out.println("Intenta de nuevo");
            }
        }
    }

    //Método para que no tome en cuenta los tildes
    private String normalizarTexto(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }

    public void canchasPorDeporte() {
        System.out.println("-- CONSULTA POR DEPORTE --");

        if (listaCanchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.print("Ingrese el deporte que desea consultar: ");
        String deporteBuscado = normalizarTexto(sc.nextLine());

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            String deporteCancha = normalizarTexto(c.getDeporte());
            if (deporteCancha.equals(deporteBuscado)) {
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
        String nombreBuscado = normalizarTexto(sc.nextLine().trim().toLowerCase());

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            String nombreCancha = normalizarTexto(c.getNombre());
            if (nombreCancha.equals(nombreBuscado)) {
                System.out.println(c);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron canchas con ese nombre.");
        }
    }

    public void estadoDeCanchasPorFechaYHora() {
        System.out.println("-- ESTADO DE CANCHAS POR FECHA Y HORA --");

        System.out.print("Ingrese la fecha (dd/mm/yyyy): ");
        String fechaStr = sc.nextLine();
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Fecha inválida.");
            return;
        }

        System.out.print("Ingrese la hora (HH:mm): ");
        String horaStr = sc.nextLine();
        LocalTime hora;
        try {
            hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Hora inválida.");
            return;
        }

        System.out.println("Estado de canchas para " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a las " + hora);
        System.out.println("------------------");

        for (Cancha cancha : listaCanchas) {
            boolean reservada = false;
            for (Reserva r : gestionReservas.getListaReservas()) {
                boolean mismaCancha = r.getCancha().getIdCancha() == cancha.getIdCancha();
                boolean mismaFecha = r.getFecha_partido().equals(fecha);

                LocalTime inicio = r.getHora_partido();
                LocalTime fin = inicio.plusMinutes((long)(r.getDuracion_partido() * 60));

                boolean dentroDelHorario = !hora.isBefore(inicio) && !hora.isAfter(fin);

                if (mismaCancha && mismaFecha && dentroDelHorario) {
                    reservada = true;
                    break;
                }
            }
            String estado = reservada ? "Reservada" : "Disponible";
            System.out.println("Cancha: " + cancha.getNombre() + " | Deporte: " + cancha.getDeporte() + " | Estado: " + estado);
        }
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
                System.out.println(c);
                System.out.println("------------------");
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
        int maxId = 0;
        for (Cancha c : listaCanchas) {
            if (c.getIdCancha() > maxId) {
                maxId = c.getIdCancha();
            }
        }
        Cancha.setContador(maxId);
    }

}
