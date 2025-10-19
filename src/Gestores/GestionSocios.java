package Gestores;

import Clases.Socio;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GestionSocios {
    private List<Socio> listaSocios = new ArrayList<>();
    private Scanner sc;

    public GestionSocios() {
        this.listaSocios = new ArrayList<>();
        this.sc = new Scanner(System.in);
        cargarSociosDesdeArchivo();
    }

    public void mostrarMenuSocios(){
        int opcionSocios = -1;
        while (opcionSocios != 0) {
            System.out.println("--- GESTIÓN DE SOCIOS ---");
            System.out.println("1. Registrar socios");
            System.out.println("2. Listar socios");
            System.out.println("3. Modificar socios");
            System.out.println("4. Eliminar socios");
            System.out.println("0. Volver al menú principal");

            try{
                opcionSocios = sc.nextInt();
                sc.nextLine();
            }catch(java.util.InputMismatchException e){
                System.out.println("Error: Por favor, ingrese un número");
                sc.nextLine();
                opcionSocios = -1;
                continue;
            }

            switch (opcionSocios) {
                case 1:
                    registrarSocios();
                    break;
                case 2:
                    listarSocios();
                    break;
                case 3:
                    modificarSocios();
                    break;
                case 4:
                    eliminarSocios();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    public void registrarSocios() {
        System.out.println("--REGISTRAR SOCIOS--");

        System.out.println("Nombre: ");
        String nombre = sc.nextLine();

        System.out.println("Apellido paterno: ");
        String apellido_paterno = sc.nextLine();

        System.out.println("Apellido materno: ");
        String apellido_materno = sc.nextLine();

        System.out.println("Cédula: ");
        int num_documento = sc.nextInt();
        sc.nextLine();

        System.out.println("Fecha de nacimiento: (dd/MM/aaaa)");
        String fechaNacTexto = sc.nextLine();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha_nacimiento = LocalDate.parse(fechaNacTexto, f);

        System.out.println("Teléfono: ");
        int telefono = sc.nextInt();
        sc.nextLine();

        System.out.println("País: ");
        String pais = sc.nextLine();

        Socio nuevoSocio = new Socio(nombre, apellido_paterno, apellido_materno, num_documento, fecha_nacimiento, telefono, pais);
        listaSocios.add(nuevoSocio);

        System.out.println("Socio registrado con éxito!");
        System.out.println(nuevoSocio);

        guardarSociosEnArchivo();
    }

    public void listarSocios() {
        System.out.println("--LISTA DE SOCIOS--");
        if(listaSocios.isEmpty()) {
            System.out.println("No hay socios registrados");
            return;
        }
        for (Socio s : listaSocios) {
            System.out.println(s);
            System.out.println("------------------");
        }
    }

    public void modificarSocios() {
        System.out.println("--MDIFICAR SOCIOS--");

        System.out.println("Socios disponibles:");
        for(Socio s : listaSocios) {
            System.out.println("Nombre: " + s.getNombre() + " - Apellidos: " + s.getApaterno() + " " + s.getAmaterno() + " - Cédula: " + s.getNum_documento());
        }

        System.out.println("Ingrese la cédula del socio a modificar: ");
        int num_documentoModificar = sc.nextInt();
        sc.nextLine();

        Socio socio = null;
        for (Socio s : listaSocios) {
            if (s.getNum_documento() == num_documentoModificar) {
                socio = s;
                break;
            }
        }
        if (socio != null) {
            System.out.println("No hay socio registrado con esa cédula");
        }

        System.out.println("Socio actual:");
        System.out.println(socio);

        System.out.println("Nuevo nombre: (enter para mantener)");
        String nuevoNombre = sc.nextLine();
        if (!nuevoNombre.isBlank()) {
            socio.setNombre(nuevoNombre);
        }

        System.out.println("Nuevo apellido paterno: (enter para mantener)");
        String nuevoAPaterno = sc.nextLine();
        if (!nuevoAPaterno.isBlank()) {
            socio.setApaterno(nuevoAPaterno);
        }

        System.out.println("Nuevo apellido materno: (enter para mantener)");
        String nuevoAMaterno = sc.nextLine();
        if (!nuevoAMaterno.isBlank()) {
            socio.setAmaterno(nuevoAMaterno);
        }

        System.out.println("Nueva cédula: (enter para mantener)");
        String cedulaStr = sc.nextLine();
        if (!cedulaStr.isBlank()) {
            socio.setNum_documento(Integer.parseInt(cedulaStr));
        }

        System.out.print("¿Desea modificar la fecha de nacimiento? (s/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.print("Nueva fecha de nacimiento (dd/MM/yyyy): ");
            String nuevaFechaNacimiento = sc.nextLine();
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                socio.setFecha_nacimiento(LocalDate.parse(nuevaFechaNacimiento, f));
            } catch (Exception e) {
                System.out.println("Fecha inválida. No se modificó.");
            }
        }

        System.out.println("Nuevo teléfono: (enter para mantener)");
        String telefonoStr = sc.nextLine();
        if (!telefonoStr.isBlank()) {
            socio.setTelefono(Integer.parseInt(telefonoStr));
        }

        System.out.println("Nuevo país: (enter para mantener)");
        String nuevoPais = sc.nextLine();
        if (!nuevoPais.isBlank()) {
            socio.setPais(nuevoPais);
        }

        System.out.println("Socio modificado con éxito!");

        guardarSociosEnArchivo();
    }

    public void eliminarSocios() {
        System.out.println("--ELIMINAR SOCIOS--");
        if(listaSocios.isEmpty()) {
            System.out.println("No hay socios registrados");
            return;
        }

        System.out.println("Socios disponibles:");
        for(Socio s : listaSocios) {
            System.out.println("Nombre: " + s.getNombre() + "Apellidos: " + s.getAmaterno() + " " + s.getApaterno() + " - Cédula: " + s.getNum_documento());
        }

        System.out.println("Ingrese la cédula del socio a eliminar");
        int num_documentoEliminar;
        try{
            num_documentoEliminar = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Error: Debe ingresar una cédula válida");
            sc.nextLine();
            return;
        }

        Socio socioAEliminar = null;
        for (Socio s : listaSocios) {
            if (s.getNum_documento() == num_documentoEliminar) {
                socioAEliminar = s;
                break;
            }
        }

        if (socioAEliminar == null) {
            System.out.println("No se encontró un socio con esa cédula");
            return;
        }

        System.out.println("¿Está seguro que desea eliminar este socio? (s/n): ");
        String confirmacion = sc.nextLine();
        if (confirmacion.equalsIgnoreCase("s")) {
            listaSocios.remove(socioAEliminar);
            System.out.println("Socio eliminado con éxito!");
        }else{
            System.out.println("Operación cancelada.");
        }

        guardarSociosEnArchivo();
    }

    public List<Socio> getListaSocios() {
        return listaSocios;
    }

    public void guardarSociosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("socios.txt"))) {
            for (Socio s : listaSocios) {
                String linea = s.getIdSocio() + ";" +
                        s.getNombre() + ";" +
                        s.getApaterno() + ";" +
                        s.getAmaterno() + ";" +
                        s.getNum_documento() + ";" +
                        s.getFecha_nacimiento() + ";" +
                        s.getTelefono() + ";" +
                        s.getPais();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar los socios: " + e.getMessage());
        }
    }

    public void cargarSociosDesdeArchivo() {
        File archivo = new File("socios.txt");
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                int id = Integer.parseInt(partes[0]);
                String nombre = partes[1];
                String apaterno = partes[2];
                String amaterno = partes[3];
                int documento = Integer.parseInt(partes[4]);
                LocalDate fechaNacimiento = LocalDate.parse(partes[5]);
                int telefono = Integer.parseInt(partes[6]);
                String pais = partes[7];

                Socio socio = new Socio(nombre, apaterno, amaterno, documento, fechaNacimiento, telefono, pais);
                socio.setIdSocio(id); // para mantener el ID original
                listaSocios.add(socio);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los socios: " + e.getMessage());
        }
    }
}
