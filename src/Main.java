import Gestores.GestionCanchas;
import Gestores.GestionReservas;
import Gestores.GestionSocios;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionReservas gestorReservas = new GestionReservas();
        GestionCanchas gestorCanchas = new GestionCanchas(gestorReservas);
        GestionSocios gestorSocios = new GestionSocios();

        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("CLUB DEPORTIVO ISLA MALA");
            System.out.println("1. Gestionar Canchas");
            System.out.println("2. Gestionar Socios");
            System.out.println("3. Gestionar Tarifas");
            System.out.println("4. Gestionar Reservas");
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
                    //gestorTarifas.mostrarMenuTarifas();
                    break;
                case 4:
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
