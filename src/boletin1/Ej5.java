package boletin1;

import dataBaseConnection.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ej5 {
    public static void main(String[] args) {

        try (Connection con = DataBaseConnection.getInstance().getCon();
             PreparedStatement consulta = con.prepareStatement("select productVendor as Proveedor " +
                     "from products " +
                     "where productName = ?");
             Scanner sc = new Scanner(System.in);) {

            boolean error= true;
            String nombreProducto;
            do {
                System.out.println("Introduzca el nombre del producto del que desea saber el proveedor: ");
                nombreProducto = sc.nextLine();
                if (nombreProducto.length() > 0){
                    error = false;
                }
            }while (error);

            consulta.setString(1,nombreProducto);
            ResultSet resultado = consulta.executeQuery();

            while (resultado.next()){
                System.out.println(resultado.getString("Proveedor"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
