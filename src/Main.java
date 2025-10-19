import Gestores.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionTarifas gestorTarifas = new GestionTarifas(); // 1️⃣ primero: lo necesita Reservas
        GestionServiciosExtras gestorServiciosExtras = new GestionServiciosExtras(); // 2️⃣ también lo necesita Reservas

        GestionSocios gestorSocios = new GestionSocios(); // 3️⃣ luego: para obtener la lista de socios
        List<Socio> socios = gestorSocios.getListaSocios(); // 4️⃣ obtenés la lista

        GestionReservas gestorReservas = new GestionReservas(socios, gestorTarifas, gestorServiciosExtras); // 5️⃣ ahora sí: con todo lo necesario

        GestionCanchas gestorCanchas = new GestionCanchas(gestorReservas); // 6️⃣ finalmente: depende de Reservas

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("CLUB DEPORTIVO ISLA MALA");
            System.out.println("1. Gestionar Canchas");
            System.out.println("2. Gestionar Socios");
            System.out.println("3. Gestionar Tarifas");
            System.out.println("4. Gestionar Servicios Extras");
            System.out.println("5. Gestionar Reservas");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    gestorCanchas.mostrarMenuCanchas();
                    break;
                case 2:
                    gestorSocios.mostrarMenuSocios();
                    break;
                case 3:
                    gestorTarifas.mostrarMenuTarifas();
                    break;
                case 4:
                    gestorServiciosExtras.mostrarMenuServiciosExtras();
                    break;
                case 5:
                    gestorReservas.mostrarMenuReservas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Intenta de nuevo");
            }
        }
    }
}
