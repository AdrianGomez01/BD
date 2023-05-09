package boletin1;

import dataBaseConnection.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ej6 {
    public static void main(String[] args) {

        try (Connection con = DataBaseConnection.getInstance().getCon();
             PreparedStatement consulta = con.prepareStatement("select c.customerNumber, c.customerName " +
                     "from customers c, employees e " +
                     "where c.salesRepEmployeeNumber = e.employeeNumber " +
                     "AND e.firstName = ? " +
                     "AND e.lastName = ?");
             Scanner sc = new Scanner(System.in);) {

            boolean error = true;
            String nombreEmpleado;
            String apellidoEmpleado;
            do {
                System.out.println("Introduzca el nombre del empleado: ");
                nombreEmpleado = sc.nextLine();
                if (nombreEmpleado.length() > 0) {
                    error = false;
                }
            } while (error);

            do {
                System.out.println("Introduzca el apellido del empleado: ");
                apellidoEmpleado = sc.nextLine();
                if (apellidoEmpleado.length() > 0) {
                    error = false;
                }
            } while (error);

            consulta.setString(1, nombreEmpleado);
            consulta.setString(2, apellidoEmpleado);
            ResultSet resultado = consulta.executeQuery();

            while (resultado.next()) {
                System.out.println(resultado.getString("salesRepEmployeeNumber"));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
