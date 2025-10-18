package Gestores;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GestionSocios {
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


        }
    }
}
