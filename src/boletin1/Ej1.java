package boletin1;

import java.sql.*;

public class Ej1 {
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3306/classicmodels?useSSL=false&serverTimezone=UTC";
        String usuario="root";
        String password="1234";
        try (Connection con = DriverManager.getConnection(url,usuario,password);Statement consulta = con.createStatement()) {

            ResultSet resultados = consulta.executeQuery("select productLine, textDescription from productLine");

            while (resultados.next()){
                System.out.println(resultados.getString("productLine") + " " + resultados.getString("textDescription"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
