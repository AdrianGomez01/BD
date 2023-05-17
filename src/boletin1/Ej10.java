package boletin1;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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

        realizarPedido();

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

        try (Connection con = DataBaseConnection.getInstance().getCon()) {

            int numCliente = getNumeroCliente(con);
            String categoria = getCategoria(con);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Este método pide un numero de cliente al usuario, comprobando si existe en la BD y devolviendolo
     *
     * @param con - Conexion a la base de datos que tenemos que proporcionar.
     * @return - numero del cliente
     */
    public static int getNumeroCliente(Connection con) {
        try (Statement consultaClientes = con.createStatement()) {

            //Consultamos los numeros y nombres de todos los clientes
            ResultSet resultadoClientes = consultaClientes.executeQuery("SELECT customerNumber, customerName from customers");

            //Creamos un HasMap para guardar los numeros de cliente de nuestros clientes, y así posteriormente acceder
            // rapidamente al nombre del cliente segun su numero de cliente

            HashMap<Integer, String> arrayClientes = new HashMap<>();

            while (resultadoClientes.next()) {
                System.out.printf("%d : %s\n", resultadoClientes.getInt("customerNumber"),
                        resultadoClientes.getString("customerName"));

                //Anhadimos al HasMap el num de cliente de todos los clientes y el nombre asociado.
                arrayClientes.put(resultadoClientes.getInt("customerNumber"),
                        resultadoClientes.getString("customerName"));
            }

            int numCliente = -1;
            //Mientras que el HasMap no contenga el numero de cliente escrito por teclado sigue pidiendolo
            do {
                System.out.println("Elija uno de los clientes");
                numCliente = Integer.parseInt(sc.nextLine());

                //Mientras que el array no contenga el cliente escrito por teclado saltará el siguiente mensaje
                if (!arrayClientes.containsKey(numCliente)) {
                    System.out.println("El cliente introducido no se encuentra en la base de datos");
                } else {
                    //Al pedirle la clave numCliente nos devuelve el valor asociado a esa clave, en este caso el nombre.
                    System.out.println("Se ha seleccionado el cliente numero " + arrayClientes.get(numCliente));
                }
            } while (!arrayClientes.containsKey(numCliente));

            return numCliente;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Este metodo nos muestra todas las categorias y nos pide que introduzcamos una
     *
     * @param con
     * @return
     */
    public static String getCategoria(Connection con) {

        try (Statement consultaCategorias = con.createStatement()) {

            ResultSet listadoCategorias = consultaCategorias.executeQuery("SELECT productline , textDescription from productLines");

            HashMap<String, String> mapCategorias = new HashMap<>();

            while (listadoCategorias.next()) {
                System.out.printf("%s : %s\n", listadoCategorias.getString("productline"),
                        listadoCategorias.getString("textDescription"));

                //Anhadimos al HasMap el num de cliente de todos los clientes y el nombre asociado.
                mapCategorias.put(listadoCategorias.getString("productline"),
                        listadoCategorias.getString("textDescription"));
            }

            String nombreCategoria;
            //Mientras que el HasMap no contenga el numero de cliente escrito por teclado sigue pidiendolo
            do {
                System.out.println("Elija una de las siguientes categorias: ");
                nombreCategoria = sc.nextLine();

                //Mientras que el array no contenga el cliente escrito por teclado saltará el siguiente mensaje
                if (!mapCategorias.containsKey(nombreCategoria)) {
                    System.out.println("La categoria introducida no se encuentra en la base de datos");
                } else {
                    //Al pedirle la clave numCliente nos devuelve el valor asociado a esa clave, en este caso el nombre.
                    System.out.println("Se ha seleccionado la categoria " + nombreCategoria);
                }
            } while (!mapCategorias.containsKey(nombreCategoria));

            return nombreCategoria;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}