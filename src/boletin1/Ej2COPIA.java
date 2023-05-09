package boletin1;

import java.sql.*;
import dataBaseConnection.DataBaseConnection;

public class Ej2COPIA {
    public static void main(String[] args) {

        try (Connection con = DataBaseConnection.getInstance().getCon();Statement consulta = con.createStatement()) {

            ResultSet resultados = consulta.executeQuery("select productName as Nombre, MSRP as Precio from products where MSRP <400");

            while (resultados.next()){
                System.out.println(resultados.getString("Nombre") + " " + resultados.getDouble("Precio"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
