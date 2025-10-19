package Gestores;

import Clases.*;

import java.io.*;
import java.sql.SQLOutput;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

public class GestionReservas {
    private List<Reserva> listaReservas;
    private List<Cancha> listaCanchas;
    private List<Socio> listaSocios;
    private GestionTarifas gestionTarifas;
    private GestionServiciosExtras gestionExtras;

    private Scanner sc;

    public GestionReservas(List<Socio> listaSocios, GestionTarifas gestionTarifas){
        this.gestionTarifas = gestionTarifas;
        this.gestionExtras = gestionExtras;
        this.listaReservas = new ArrayList<>();
        this.listaSocios = new ArrayList<>();
        this.sc = new Scanner(System.in);
        cargarReservasDesdeArchivo();
    }

    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void mostrarMenuReservas(){
        int opcionReservas = -1;
        while (opcionReservas != 0){
            System.out.println("--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Registrar una reserva");
            System.out.println("2. Listar Reservas");
            System.out.println("3. Modificar una reserva");
            System.out.println("4. Eliminar una reserva");
            System.out.println("5. Consultas");
            System.out.println("0. Volver al menú principal");

            try{
                opcionReservas = sc.nextInt();
                sc.nextLine();
            }catch (java.util.InputMismatchException e){
                System.out.println("Error: Por favor, ingrese un número");
                sc.nextLine();
                opcionReservas = -1;
                continue;
            }

            switch (opcionReservas){
                case 1:
                    registrarReservas();
                    break;
                case 2:
                    listarReservas();
                    break;
                case 3:
                    modificarReservas();
                    break;
                case 4:
                    eliminarReservas();
                    break;
                case 5:
                    mostrarMenuConsultasR();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    public void registrarReservas(){
        System.out.println("--REGISTRAR RESERVAS--");
        System.out.println("-- Socios disponibles --");
        for (Socio s : listaSocios) {
            System.out.println("Cédula: " + s.getNum_documento() + " - " + s.getNombre());
        }
        System.out.print("Ingrese el ID del socio: ");
        int idSocioSeleccionado;
        try {
            idSocioSeleccionado = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }
        Socio socioSeleccionado = null;
        for (Socio s : listaSocios) {
            if (s.getIdSocio() == idSocioSeleccionado) {
                socioSeleccionado = s;
                break;
            }
        }
        if (socioSeleccionado == null) {
            System.out.println("No se encontró un socio con ese ID.");
            return;
        }
        System.out.println("Socio seleccionado:");
        System.out.println("ID: " + socioSeleccionado.getIdSocio() + " - Nombre: " + socioSeleccionado.getNombre());

        System.out.println("--Canchas disponibles--");
        for (Cancha c : listaCanchas){
            System.out.println("ID: " + c.getIdCancha() + " - " + c.getNombre() + " - " + c.getDeporte());
        }
        System.out.println("Ingrese el ID de la cancha:");
        int idCanchaSeleccionada;
        try{
            idCanchaSeleccionada = Integer.parseInt(sc.nextLine());
        }catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }
        Cancha canchaSeleccionada = null;
        for(Cancha c : listaCanchas){
            if(c.getIdCancha() == idCanchaSeleccionada){
                canchaSeleccionada = c;
                break;
            }
        }
        if(canchaSeleccionada == null){
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }
        System.out.println("Cancha seleccionada: ");
        System.out.println("ID: " + canchaSeleccionada.getIdCancha() + " - Nombre: " + canchaSeleccionada.getNombre() + " - Deporte: " + canchaSeleccionada.getDeporte());

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Fecha de la reserva: " + hoy.format(formato));

        System.out.println("Fecha del partido: (dd/MM/aaaa)");
        String fechaTexto = sc.nextLine();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaPartido = LocalDate.parse(fechaTexto, f);

        System.out.println("Hora del partido: (hh:mm)");
        String horaTexto = sc.nextLine();
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaPartido;
        try {
            horaPartido = LocalTime.parse(horaTexto, formatoHora);
        } catch (Exception e) {
            System.out.println("Hora inválida. Use el formato hh:mm (24h).");
            return;
        }

        System.out.print("Duración del partido: (en horas)");
        double duracion;
        try {
            duracion = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Duración inválida.");
            return;
        }

        System.out.print("¿Pago total realizado? (s/n): ");
        String pagoStr = sc.nextLine().trim().toLowerCase();
        boolean pagoTotal = pagoStr.equals("s");

        System.out.print("Observaciones (opcional): ");
        String observaciones = sc.nextLine();

        List<ServicioExtra> extrasSeleccionados = new ArrayList<>();
        System.out.println("-- Servicios extras disponibles --");
        List<ServicioExtra> disponibles = gestionExtras.getListaServiciosExtras();
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println((i + 1) + ". " + disponibles.get(i));
        }
        System.out.println("Ingrese los números de extras separados por coma (o enter para ninguno):");
        String entradaExtras = sc.nextLine().trim();
        if (!entradaExtras.isEmpty()) {
            String[] indices = entradaExtras.split(",");
            for (String idx : indices) {
                try {
                    int i = Integer.parseInt(idx.trim()) - 1;
                    if (i >= 0 && i < disponibles.size()) {
                        extrasSeleccionados.add(disponibles.get(i));
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        Tarifa tarifaAplicada = gestionTarifas.obtenerTarifaVigente(canchaSeleccionada.getDeporte(), fechaPartido);
        if (tarifaAplicada == null) {
            System.out.println("No hay tarifa vigente para ese deporte en esa fecha.");
            return;
        }
        System.out.println("Tarifa aplicada: $" + tarifaAplicada.getMonto());

        final int idCancha = canchaSeleccionada.getIdCancha();
        boolean haySuperposicion = listaReservas.stream().anyMatch(r -> {
            boolean mismaCancha = r.getCancha().getIdCancha() == idCancha;
            boolean mismaFecha = r.getFecha_partido().equals(fechaPartido);

            LocalTime inicioExistente = r.getHora_partido();
            LocalTime finExistente = inicioExistente.plusMinutes((long)(r.getDuracion_partido() * 60));

            LocalTime inicioNueva = horaPartido;
            LocalTime finNueva = horaPartido.plusMinutes((long)(duracion * 60));

            boolean seCruzan = !finNueva.isBefore(inicioExistente) && !inicioNueva.isAfter(finExistente);

            return mismaCancha && mismaFecha && seCruzan;
        });

        if (haySuperposicion) {
            System.out.println("Ya existe una reserva que se superpone con ese horario en la cancha seleccionada.");
            return;
        }

        Reserva nuevaReserva = new Reserva(
                0, // el ID se asigna automáticamente en el constructor
                socioSeleccionado,
                canchaSeleccionada,
                hoy,
                fechaPartido,
                horaPartido,
                duracion,
                pagoTotal,
                observaciones,
                extrasSeleccionados,
                tarifaAplicada
        );

        listaReservas.add(nuevaReserva);
        guardarReservasEnArchivo();

        System.out.println("Reserva registrada con éxito:");
        System.out.println(nuevaReserva);

        guardarReservasEnArchivo();
    }

    public void listarReservas(){
        System.out.println("--- LISTA DE RESERVAS");
        if(listaReservas.isEmpty()){
            System.out.println("No hay reservas registradas.");
            return;
        }
        for(Reserva r : listaReservas){
            System.out.println(r);
        }
    }

    public void modificarReservas() {
        System.out.println("-- MODIFICAR RESERVA --");

        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        // Mostrar reservas disponibles
        for (Reserva r : listaReservas) {
            System.out.println("ID: " + r.getIdReserva() + " - Socio: " + r.getSocio().getNombre() + " - Cancha: " + r.getCancha().getNombre());
        }

        // Seleccionar reserva
        System.out.print("Ingrese el ID de la reserva que desea modificar: ");
        int idReserva;
        try {
            idReserva = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Reserva reserva = null;
        for (Reserva r : listaReservas) {
            if (r.getIdReserva() == idReserva) {
                reserva = r;
                break;
            }
        }

        if (reserva == null) {
            System.out.println("No se encontró una reserva con ese ID.");
            return;
        }

        System.out.println("Reserva actual:");
        System.out.println(reserva);

        // Modificar socio
        System.out.print("¿Desea modificar el socio? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("-- Socios disponibles --");
            for (Socio s : listaSocios) {
                System.out.println("ID: " + s.getIdSocio() + " - " + s.getNombre());
            }
            System.out.print("Ingrese el nuevo ID del socio: ");
            int nuevoId;
            try {
                nuevoId = Integer.parseInt(sc.nextLine());
                for (Socio s : listaSocios) {
                    if (s.getIdSocio() == nuevoId) {
                        reserva.setSocio(s);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. No se modificó el socio.");
            }
        }

        // Modificar cancha
        System.out.print("¿Desea modificar la cancha? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("-- Canchas disponibles --");
            for (Cancha c : listaCanchas) {
                System.out.println("ID: " + c.getIdCancha() + " - " + c.getNombre() + " - " + c.getDeporte());
            }
            System.out.print("Ingrese el nuevo ID de la cancha: ");
            int nuevoId;
            try {
                nuevoId = Integer.parseInt(sc.nextLine());
                for (Cancha c : listaCanchas) {
                    if (c.getIdCancha() == nuevoId) {
                        reserva.setCancha(c);
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. No se modificó la cancha.");
            }
        }

        // Modificar fecha del partido
        System.out.print("¿Desea modificar la fecha del partido? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nueva fecha (dd/MM/yyyy): ");
            String nuevaFecha = sc.nextLine();
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                reserva.setFecha_partido(LocalDate.parse(nuevaFecha, f));
            } catch (Exception e) {
                System.out.println("Fecha inválida. No se modificó.");
            }
        }

        // Modificar hora del partido
        System.out.print("¿Desea modificar la hora del partido? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nueva hora (HH:mm): ");
            String nuevaHora = sc.nextLine();
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
                reserva.setHora_partido(LocalTime.parse(nuevaHora, f));
            } catch (Exception e) {
                System.out.println("Hora inválida. No se modificó.");
            }
        }

        // Modificar duración
        System.out.print("¿Desea modificar la duración del partido? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nueva duración (en horas): ");
            try {
                double nuevaDuracion = Double.parseDouble(sc.nextLine());
                reserva.setDuracion_partido(nuevaDuracion);
            } catch (NumberFormatException e) {
                System.out.println("Duración inválida. No se modificó.");
            }
        }

        // Modificar pago
        System.out.print("¿Desea modificar el estado de pago? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("¿Pago total realizado? (s/n): ");
            String pagoStr = sc.nextLine().trim().toLowerCase();
            reserva.setPagoTotal(pagoStr.equals("s"));
        }

        // Modificar observaciones
        System.out.print("¿Desea modificar las observaciones? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nuevas observaciones: ");
            reserva.setObservaciones(sc.nextLine());
        }

        // Extras y tarifa (opcional)
        System.out.print("¿Desea borrar los servicios extras actuales? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            reserva.setExtras(new ArrayList<>());
        }

        /*System.out.print("¿Desea modificar la tarifa aplicada? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Ingrese el nombre de la nueva tarifa: ");
            String descripcionTarifa = sc.nextLine();
            reserva.setTarifaAplicada(new Tarifa(descripcionTarifa)); // suponiendo que tenés ese constructor
        }*/

        guardarReservasEnArchivo();
        System.out.println("Reserva modificada con éxito.");
    }

    public void eliminarReservas(){

        guardarReservasEnArchivo();
    }

    public void mostrarMenuConsultasR(){
        int opcionReservasConsultas = -1;
        while(opcionReservasConsultas != 0){
            System.out.println("Consultas de reservas");
            System.out.println("1. Reservas en un período de tiempo dado");
            System.out.println("2. Canchas con reserva en una fecha dada");
            System.out.println("3. Canchas sin reserva en una fecha dada");
            System.out.println("0. Salir");

            opcionReservasConsultas = sc.nextInt();
            sc.nextLine();
            switch (opcionReservasConsultas){
                case 1:
                    ReservasEnFechasDadas();
                    break;
                case 2:
                    CanchasConReservaEnFechaDada();
                    break;
                case 3:
                    CanchasSinReservaEnFechaDada();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Intenta de nuevo");
            }
        }
    }

    public void ReservasEnFechasDadas(){

    }

    public void CanchasConReservaEnFechaDada(){

    }

    public void CanchasSinReservaEnFechaDada(){

    }

    public void guardarReservasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reservas.txt"))) {
            for (Reserva r : listaReservas) {
                List<String> extrasStr = new ArrayList<>();
                for (ServicioExtra extra : r.getExtras()) {
                    extrasStr.add(extra.getDescripcion() + "|" + extra.getCosto());
                }
                String extrasTexto = String.join(",", extrasStr);

                String linea = r.getIdReserva() + ";" +
                        r.getSocio().getNombre() + ";" +
                        r.getCancha().getNombre() + ";" +
                        r.getFecha_reserva() + ";" +
                        r.getFecha_partido() + ";" +
                        r.getHora_partido() + ";" +
                        r.getDuracion_partido() + ";" +
                        r.isPagoTotal() + ";" +
                        r.getObservaciones() + ";" +
                        extrasTexto + ";" +
                        r.getTarifaAplicada();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar las reservas: " + e.getMessage());
        }
    }

    public void cargarReservasDesdeArchivo() {
        File archivo = new File("reservas.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                int id = Integer.parseInt(partes[0]);
                String nombreSocio = partes[1];
                String nombreCancha = partes[2];
                LocalDate fechaReserva = LocalDate.parse(partes[3]);
                LocalDate fechaPartido = LocalDate.parse(partes[4]);
                LocalTime horaPartido = LocalTime.parse(partes[5]);
                double duracion = Double.parseDouble(partes[6]);
                boolean pagoTotal = Boolean.parseBoolean(partes[7]);
                String observaciones = partes[8];

                // Buscar socio y cancha por nombre
                Socio socio = buscarSocioPorNombre(nombreSocio);
                Cancha cancha = buscarCanchaPorNombre(nombreCancha);

                Reserva reserva = new Reserva(id, socio, cancha, fechaReserva, fechaPartido, horaPartido, duracion, pagoTotal, observaciones, new ArrayList<>(), null);
                listaReservas.add(reserva);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las reservas: " + e.getMessage());
        }
    }

    private Socio buscarSocioPorNombre(String nombre) {
        for (Socio s : listaSocios) {
            if (s.getNombre().equalsIgnoreCase(nombre)) return s;
        }
        return null;
    }

    private Cancha buscarCanchaPorNombre(String nombre) {
        for (Cancha c : listaCanchas) {
            if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        }
        return null;
    }
}
