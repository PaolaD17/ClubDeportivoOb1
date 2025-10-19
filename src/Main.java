import Clases.Cancha;
import Clases.Socio;
import Gestores.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionTarifas gestorTarifas = new GestionTarifas(); // 1️⃣
        GestionServiciosExtras gestorServiciosExtras = new GestionServiciosExtras(); // 2️⃣

        GestionSocios gestorSocios = new GestionSocios(); // 3️⃣
        List<Socio> socios = gestorSocios.getListaSocios(); // 4️⃣

        GestionCanchas gestorCanchas = new GestionCanchas(); // 5️⃣ primero creás las canchas
        List<Cancha> canchas = gestorCanchas.getListaCanchas(); // 6️⃣ obtenés la lista

        GestionReservas gestorReservas = new GestionReservas(canchas, gestorSocios, gestorTarifas, gestorServiciosExtras);

        gestorCanchas.setGestorReservas(gestorReservas);

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
