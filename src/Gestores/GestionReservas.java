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
    private GestionCanchas gestionCanchas;
    private GestionSocios gestionSocios;
    private GestionTarifas gestionTarifas;
    private GestionServiciosExtras gestionExtras;
    private Scanner sc;

    public GestionReservas(List<Cancha> listaCanchas, GestionSocios gestionSocios, GestionTarifas gestionTarifas, GestionServiciosExtras gestionExtras){
        this.listaCanchas = listaCanchas;
        this.gestionSocios = gestionSocios;
        this.gestionTarifas = gestionTarifas;
        this.gestionExtras = gestionExtras;
        this.listaReservas = new ArrayList<>();

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

        //Mostrar los socios disponibles
        System.out.println("--Socios disponibles--");
        for (Socio s : gestionSocios.getListaSocios()) {
            System.out.println("ID: " + s.getIdSocio() + " | Cédula: " + s.getNum_documento() + " | Nombre: " + s.getNombre() + " " + s.getApaterno());
        }

        //Ingresar el ID del socio para la reserva
        System.out.print("Ingrese el ID del socio: ");
        int idSocioSeleccionado;
        try {
            idSocioSeleccionado = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Socio socioSeleccionado = null;
        for (Socio s : gestionSocios.getListaSocios()) {
            if (s.getIdSocio() == idSocioSeleccionado) {
                socioSeleccionado = s;
                break;
            }
        }
        if (socioSeleccionado == null) {
            System.out.println("No se encontró un socio con ese ID.");
            return;
        }

        //Muestro el socio seleccionado
        System.out.println("Socio seleccionado:");
        System.out.println("ID: " + socioSeleccionado.getIdSocio() + " | Nombre: " + socioSeleccionado.getNombre() + " | Cédula: " + socioSeleccionado.getNum_documento());

        //Muestra la fecha del día por defecto para fecha de reserva
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Fecha de la reserva: " + hoy.format(formato));

        //Registro fecha del partido
        System.out.println("Fecha del partido: (dd/MM/aaaa)");
        String fechaTexto = sc.nextLine();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaPartido = LocalDate.parse(fechaTexto, f);
        if (fechaPartido.isBefore(LocalDate.now())) {
            System.out.println("La fecha del partido debe ser futura.");
            return;
        }

        //Registro hora del partido
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

        //Registro duración del partido
        System.out.print("Duración del partido: (en horas)");
        double duracion;
        try {
            duracion = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Duración inválida.");
            return;
        }

        //Muestro canchas disponibles que no se crucen con otra reserva a la misma hora y fecha
        System.out.println("--Canchas disponibles para ese horario--");
        List<Cancha> disponibles = new ArrayList<>();
        for (Cancha c : listaCanchas) {
            boolean ocupada = false;
            for (Reserva r : listaReservas) {
                boolean mismaCancha = r.getCancha().getIdCancha() == c.getIdCancha();
                boolean mismaFecha = r.getFecha_partido().equals(fechaPartido);
                LocalTime inicioExistente = r.getHora_partido();
                LocalTime finExistente = inicioExistente.plusMinutes((long) (r.getDuracion_partido() * 60));
                LocalTime inicioNueva = horaPartido;
                LocalTime finNueva = horaPartido.plusMinutes((long) (duracion * 60));
                boolean seCruzan = !finNueva.isBefore(inicioExistente) && !inicioNueva.isAfter(finExistente);
                if (mismaCancha && mismaFecha && seCruzan) {
                    ocupada = true;
                    break;
                }
            }
            if (!ocupada) {
                disponibles.add(c);
            }
        }
        if (disponibles.isEmpty()) {
            System.out.println("No hay canchas disponibles en ese horario.");
            return;
        }
        for (Cancha c : disponibles) {
            System.out.println("ID: " + c.getIdCancha() + " | Nombre: " + c.getNombre() + " | Deporte: " + c.getDeporte());
        }

        //Selecciono una cancha de la lista dada anteriormente
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

        //Muestro la información de la cancha seleccionada
        System.out.println("Cancha seleccionada: ");
        System.out.println("ID: " + canchaSeleccionada.getIdCancha() + " | Nombre: " + canchaSeleccionada.getNombre() + " | Deporte: " + canchaSeleccionada.getDeporte());

        //Registro si se hizo el pago total o no
        System.out.print("¿Pago total realizado? (s/n): ");
        String pagoStr = sc.nextLine().trim().toLowerCase();
        boolean pagoTotal = pagoStr.equals("s");

        //Registro observacuiones (si corresponde)
        System.out.print("Observaciones (opcional): ");
        String observaciones = sc.nextLine();

        //Muestro los servicios extras disponibles para seleccionar
        List<ServicioExtra> extrasSeleccionados = new ArrayList<>();
        System.out.println("--Servicios extras disponibles--");
        List<ServicioExtra> extrasDisponibles = gestionExtras.getListaServiciosExtras();
        for (int i = 0; i < extrasDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + extrasDisponibles.get(i));
        }
        //Registro el/los ID del servicio extra que quiero seleccionar
        System.out.println("Ingrese el/los ID de servicios extras separados por coma (o enter para ninguno):");
        String entradaExtras = sc.nextLine().trim();
        if (!entradaExtras.isEmpty()) {
            String[] indices = entradaExtras.split(",");
            for (String idx : indices) {
                try {
                    int i = Integer.parseInt(idx.trim()) - 1;
                    if (i >= 0 && i < extrasDisponibles.size()) {
                        extrasSeleccionados.add(extrasDisponibles.get(i));
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        //Selecciono la tarifa según el deporte y la fecha
        Tarifa tarifaAplicada = gestionTarifas.obtenerTarifaVigente(canchaSeleccionada.getDeporte(), fechaPartido);
        if (tarifaAplicada == null) {
            System.out.println("No hay tarifa vigente para ese deporte en esa fecha.");
            return;
        }
        System.out.println("Tarifa aplicada: $" + tarifaAplicada.getMonto());

        final int idCancha = canchaSeleccionada.getIdCancha();
        boolean haySuperposicion = false;
        for (Reserva r : listaReservas) {
            boolean mismaCancha = r.getCancha().getIdCancha() == idCancha;
            boolean mismaFecha = r.getFecha_partido().equals(fechaPartido);

            LocalTime inicioExistente = r.getHora_partido();
            LocalTime finExistente = inicioExistente.plusMinutes((long)(r.getDuracion_partido() * 60));

            LocalTime inicioNueva = horaPartido;
            LocalTime finNueva = horaPartido.plusMinutes((long)(duracion * 60));

            boolean seCruzan = !finNueva.isBefore(inicioExistente) && !inicioNueva.isAfter(finExistente);

            if (mismaCancha && mismaFecha && seCruzan) {
                haySuperposicion = true;
                break;
            }
        }

        if (haySuperposicion) {
            System.out.println("Ya existe una reserva para esa cancha, día y horario.");
            return;
        }

        //Creo la nueva reserva, la agrego a listaReservas y la muestro
        Reserva nuevaReserva = new Reserva(
                0,
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
        System.out.println("--LISTA DE RESERVAS--");
        if(listaReservas.isEmpty()){
            System.out.println("No hay reservas registradas.");
            return;
        }
        for(Reserva r : listaReservas){
            System.out.println(r);
            System.out.println("----------------");
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
            System.out.println("ID: " + r.getIdReserva() + " | Socio: " + r.getSocio().getNombre() + " " + r.getSocio().getApaterno() + " | Cancha: " + r.getCancha().getNombre());
        }

        // Seleccionar reserva a modificar
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

        //Muestro toda la información de la reserva a modificar
        System.out.println("Reserva actual:");
        System.out.println(reserva);

        // Modificar campo socio
        System.out.print("Nuevo socio: (enter para mantener)");
        System.out.println("-- Socios disponibles --");
        for (Socio s : gestionSocios.getListaSocios()) {
            System.out.println("ID: " + s.getIdSocio() + " | Cédula: " + s.getNum_documento() + " | Nombre: " + s.getNombre());
        }
        System.out.print("Ingrese el nuevo ID del socio: ");
        int nuevoIdSocio;
        try {
            nuevoIdSocio = Integer.parseInt(sc.nextLine());
            for (Socio s : gestionSocios.getListaSocios()) {
                if (s.getIdSocio() == nuevoIdSocio) {
                    reserva.setSocio(s);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Ingrese otro ID.");
        }

        // Modificar cancha
        System.out.print("Nueva cancha: (enter para mantener)");
        System.out.println("--Canchas disponibles--");
        for (Cancha c : listaCanchas) {
            System.out.println("ID: " + c.getIdCancha() + " | Nombre: " + c.getNombre() + " | Deporte: " + c.getDeporte());
        }
        System.out.print("Ingrese el nuevo ID de la cancha: ");
        int nuevoIdCancha;
        try {
            nuevoIdCancha = Integer.parseInt(sc.nextLine());
            for (Cancha c : listaCanchas) {
                if (c.getIdCancha() == nuevoIdCancha) {
                    reserva.setCancha(c);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Ingrese otro ID.");
        }

        //Modificar fecha del partido
        System.out.print("Nueva fecha de partido (dd/mm/aaaa): (enter para mantener)");
        String nuevaFecha = sc.nextLine();
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            reserva.setFecha_partido(LocalDate.parse(nuevaFecha, f));
        } catch (Exception e) {
            System.out.println("Fecha inválida. Ingrese una fecha en formato dd/mm/aaaa.");
        }


        //Modificar hora del partido
        System.out.print("Nueva hora de partido (hh:mm): (enter para mantener) ");
        String nuevaHora = sc.nextLine();
        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
            reserva.setHora_partido(LocalTime.parse(nuevaHora, f));
        } catch (Exception e) {
            System.out.println("Hora inválida. Ingrese la hora en formato hh:mm.");
        }

        //Modificar duración
        System.out.print("Nueva duración de partido (horas): (enter para mantener) ");
        try {
            double nuevaDuracion = Double.parseDouble(sc.nextLine());
            reserva.setDuracion_partido(nuevaDuracion);
        } catch (NumberFormatException e) {
            System.out.println("Duración inválida. Ingrese la duración en cantidad de horas.");
        }

        //Modificar pago
        System.out.print("¿Pago total realizado? (s/n): ");
        String pagoStr = sc.nextLine().trim().toLowerCase();
        reserva.setPagoTotal(pagoStr.equals("s"));

        //Modificar observaciones
        System.out.print("Nuevas observaciones: (enter para mantener)");
        reserva.setObservaciones(sc.nextLine());

        //Mosificar servicios extras
        System.out.print("Nuevos servicios extras: (enter para mantener)");
        reserva.setExtras(new ArrayList<>());

        guardarReservasEnArchivo();

        System.out.println("Reserva modificada con éxito.");
    }

    public void eliminarReservas() {
        System.out.println("-- ELIMINAR RESERVA --");

        if (listaReservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        //Mostrar reservas disponibles para borrar
        for (Reserva r : listaReservas) {
            System.out.println("ID: " + r.getIdReserva() + " | Socio: " + r.getSocio().getNombre() +
                    " | Cancha: " + r.getCancha().getNombre() +
                    " | Fecha partido: " + r.getFecha_partido() +
                    " | Hora: " + r.getHora_partido());
        }

        System.out.print("Ingrese el ID de la reserva a eliminar: ");
        int idEliminar;
        try {
            idEliminar = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Reserva reservaAEliminar = null;
        for (Reserva r : listaReservas) {
            if (r.getIdReserva() == idEliminar) {
                reservaAEliminar = r;
                break;
            }
        }

        if (reservaAEliminar == null) {
            System.out.println("No se encontró una reserva con ese ID.");
            return;
        }

        System.out.print("¿Está seguro que desea eliminar esta reserva? (s/n): ");
        String confirmacion = sc.nextLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            listaReservas.remove(reservaAEliminar);
            System.out.println("Reserva eliminada con éxito.");
        } else {
            System.out.println("Operación cancelada.");
        }

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

    public void ReservasEnFechasDadas() {
        System.out.println("--CONSULTA DE RESERVAS--");

        System.out.print("Fecha inicio (dd/mm/yyyy): ");
        LocalDate inicio = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Fecha final (dd/mm/yyyy): ");
        LocalDate fin = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        boolean encontrado = false;
        for (Reserva r : listaReservas) {
            if (!r.getFecha_partido().isBefore(inicio) && !r.getFecha_partido().isAfter(fin)) {
                System.out.println(r);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay reservas en ese período.");
        }
    }

    public void CanchasConReservaEnFechaDada() {
        System.out.println("--CANCHAS CON RESERVA--");

        System.out.print("Fecha (dd/mm/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<Integer> idsReservadas = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.getFecha_partido().equals(fecha)) {
                idsReservadas.add(r.getCancha().getIdCancha());
            }
        }

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            if (idsReservadas.contains(c.getIdCancha())) {
                System.out.println("Cancha: " + c.getNombre() + " | Deporte: " + c.getDeporte() + " | Estado: Reservada");
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay canchas reservadas en esa fecha.");
        }
    }

    public void CanchasSinReservaEnFechaDada() {
        System.out.println("--CANCHAS SIN RESERVA--");

        System.out.print("Fecha (dd/mm/yyyy): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        List<Integer> idsReservadas = new ArrayList<>();
        for (Reserva r : listaReservas) {
            if (r.getFecha_partido().equals(fecha)) {
                idsReservadas.add(r.getCancha().getIdCancha());
            }
        }

        boolean encontrado = false;
        for (Cancha c : listaCanchas) {
            if (!idsReservadas.contains(c.getIdCancha())) {
                System.out.println("Cancha: " + c.getNombre() + " | Deporte: " + c.getDeporte() + " | Estado: Disponible");
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("Todas las canchas están reservadas en esa fecha.");
        }
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
                        (r.getTarifaAplicada() != null ? r.getTarifaAplicada().getMonto() : "null");
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

                Socio socio = buscarSocioPorNombre(nombreSocio);
                Cancha cancha = buscarCanchaPorNombre(nombreCancha);

                List<ServicioExtra> extras = new ArrayList<>();
                if (!partes[9].isEmpty()) {
                    String[] extrasArray = partes[9].split(",");
                    for (String extraStr : extrasArray) {
                        String[] datos = extraStr.split("\\|");
                        if (datos.length == 2) {
                            String descripcion = datos[0];
                            double costo = Double.parseDouble(datos[1]);
                            extras.add(new ServicioExtra(descripcion, costo));
                        }
                    }
                }

                Tarifa tarifa = null;
                if (partes.length > 10 && partes[10] != null && !partes[10].equals("null")) {
                    try {
                        double monto = Double.parseDouble(partes[10]);
                        tarifa = new Tarifa(cancha.getDeporte(), monto, fechaPartido);
                    } catch (NumberFormatException ignored) {}
                }

                Reserva reserva = new Reserva(id, socio, cancha, fechaReserva, fechaPartido, horaPartido, duracion, pagoTotal, observaciones, extras, tarifa);
                listaReservas.add(reserva);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las reservas: " + e.getMessage());
        }
    }

    private Socio buscarSocioPorNombre(String nombre) {
        for (Socio s : gestionSocios.getListaSocios()) {
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
