package boletin1;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import dataBaseConnection.DataBaseConnection;

public class Ej10 {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        /* -Primer ejercicio/metodo-
        if (insertarProducto()) {
            System.out.println("El producto se ha insertado con exito");
        } else {
            System.out.println("Hubo un error al insertar el producto");
        }
        */


    }

    public static boolean insertarProducto() {
        try (Connection con = DataBaseConnection.getInstance().getCon();
             PreparedStatement consultaPreparada = con.prepareStatement("INSERT INTO products values (?,?,?,?,?,?,?,?,?)");
             PreparedStatement consultaCategorias = con.prepareStatement("SELECT productLine from productLines");
             PreparedStatement consultaCodigos = con.prepareStatement("SELECT productCode from products where productCode = ?");
             Statement consulta = con.createStatement()) {

            // Petición del código del producto
            boolean salida = false;
            do {
                System.out.println("Inserte el codigo del producto: ");
                String codigoProducto = sc.nextLine();
                consultaCodigos.setString(1, codigoProducto);
                ResultSet resultadoCodigos = consultaCodigos.executeQuery();
                if (!resultadoCodigos.next()) {
                    consultaPreparada.setString(1, codigoProducto);
                    salida = true;
                } else {
                    System.out.println("El código que intentas introducir ya existe en la base de datos");
                }
            } while (!salida);

            System.out.println("Inserte el nombre del producto: ");
            String nombreProducto = sc.nextLine();
            consultaPreparada.setString(2, nombreProducto);

            ResultSet listadoCategorias = consultaCategorias.executeQuery();
            ArrayList<String> arrayCategorias = new ArrayList<>();
            while (listadoCategorias.next()) {
                arrayCategorias.add(listadoCategorias.getString(1));
            }
            String[] listaCategoriasArray = new String[arrayCategorias.size()];
            String categoria = UserDataCollector.getStringDeOpciones("Seleccione una de las categorias siguientes:", arrayCategorias.toArray(listaCategoriasArray));
            consultaPreparada.setString(3, categoria);

            System.out.println("Inserte la escala del producto: ");
            String escalaProducto = sc.nextLine();
            consultaPreparada.setString(4, escalaProducto);

            System.out.println("Inserte el vendedor del producto: ");
            String vendedorProducto = sc.nextLine();
            consultaPreparada.setString(5, vendedorProducto);

            System.out.println("Inserte la descripcion del producto: ");
            String descripcionProducto = sc.nextLine();
            consultaPreparada.setString(6, descripcionProducto);

            System.out.println("Inserte el stock del producto: ");
            int cantidadProducto = Integer.parseInt(sc.nextLine());
            consultaPreparada.setInt(7, cantidadProducto);

            System.out.println("Inserte el precio del producto: ");
            float precioProducto = Float.parseFloat(sc.nextLine());
            consultaPreparada.setFloat(8, precioProducto);

            System.out.println("Inserte el MSRP del producto:");
            float msrp = Float.parseFloat(sc.nextLine());
            consultaPreparada.setFloat(9, msrp);

            if (consultaPreparada.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean realizarPedido() {

        try (Connection con = DataBaseConnection.getInstance().getCon();
             Statement consultaClientes = con.createStatement();
             Scanner sc = new Scanner(System.in);
        ) {

            //Consultamos los numeros y nombres de todos los clientes
            ResultSet resultadoClientes = consultaClientes.executeQuery("SELECT customerNumber, customerName from customers");

            //Creamos un array para guardar los numeros de cliente de nuestros clientes
            ArrayList<Integer> arrayClientes = new ArrayList<Integer>();

            while (resultadoClientes.next()) {
                System.out.printf("%d : %s\n", resultadoClientes.getInt("customerNumber"),
                        resultadoClientes.getString("customerName"));
                //Anhadimos al array el num de cliente de todos los clientes
                arrayClientes.add(resultadoClientes.getInt(1));
            }

            int numCliente = -1;
            //Mientras que el array no contenga el numero de cliente escrito por teclado sigue pidiendolo
            do {
                System.out.println("Elija uno de los clientes");
                numCliente = Integer.parseInt(sc.nextLine());
            } while (!arrayClientes.contains(numCliente));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}