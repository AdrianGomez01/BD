package boletin1;

import dataBaseConnection.DataBaseConnection;

import java.sql.*;
import java.util.Scanner;

public class Ej3 {

    public static void main(String[] args) {

        try (Connection con = DataBaseConnection.getInstance().getCon();
             PreparedStatement consulta = con.prepareStatement("select productName as Nombre, MSRP as Precio " +
                     "from products " +
                     "where MSRP < ? AND UPPER(productName) like ?");
             Scanner sc = new Scanner(System.in);) {

            double precioMin = 0;
            boolean error = true;
            String letraInicial;

            do {
                System.out.println("Introduzca un precio de venta minimo: ");
                try {
                    precioMin = Double.parseDouble(sc.nextLine());
                    error = false;
                } catch (NumberFormatException e) {
                    e.getMessage();
                }
            } while (error);

            do {
                System.out.println("Introduzca la letra por la que empieza el nombre del producto: ");
                letraInicial = sc.nextLine().toUpperCase();
            } while (letraInicial.length() == 0);

            consulta.setDouble(1, precioMin);
            consulta.setString(2, letraInicial.charAt(0) + "%");
            ResultSet resultados = consulta.executeQuery();

            while (resultados.next()) {
                System.out.println(resultados.getString("Nombre") + " " + resultados.getDouble("Precio"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
