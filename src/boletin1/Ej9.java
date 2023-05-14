package boletin1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import dataBaseConnection.DataBaseConnection;

public class Ej9 {

	public static void main(String[] args) {
		/**
		 * begin transaction - comenzar la transacción commit - si va todo bien, se
		 * ejecuta y cambian los datos de la tabla principal rollback - si va algo mal,
		 * se vuelve al principio, no se tocan los datos de la tabla principal y la
		 * tabla nueva auxiliar se elimina (va en el catch)
		 */

		try (Scanner teclado = new Scanner(System.in);
				Connection con = DataBaseConnection.getInstance().getCon();
				PreparedStatement consultaDeleteFromCustomers = con.prepareStatement(
						"delete from customers where customerName=?");
				PreparedStatement consultaExisteCliente = con.prepareStatement("select * from customers where customerName = ?");
				PreparedStatement consultaDeleteFromPayents = con.prepareStatement(
						"delete from payments where customerName=?");
				PreparedStatement consultaDeleteFrom = con.prepareStatement(
						"delete from customers where customerName=?");) {

			
			System.out.println("Introduce el nombre del empleado: ");
			String nombreCliente = teclado.nextLine();
			consultaExisteCliente.setString(1, nombreCliente);
			
			/**
			 * se señala que estamos en una transacción
			 * lo que ejecute aquí no se cambia en la base de datos
			 * hasta hacer el commit final
			 */
			con.setAutoCommit(false);
			
			ResultSet resultExisteCliente = consultaExisteCliente.executeQuery();
			
			//devuelve false si no hay más iteraciones
			if (!resultExisteCliente.next()) {
				System.out.println("No existe un cliente con ese nombre");
				//volver al estado anterior, anula lo que hayamos hecho hasta ahora
				//es opcional pues se cierra en el catch si va mal y no se hace commit
				con.rollback();
				//para salir del if
				return;
			}
			
			
			try {
				consultaDeleteFrom.executeUpdate();
			}
			catch (SQLException e){
				System.out.println("No se ha podido ejecutar" + " " + e.getMessage());
				//.rollback es para volver al estado anterior, anula lo
				//que hayamos hecho hasta ahora
				con.rollback();
			}
		
			
			
		} catch (SQLException e) {
			
		}

	}
}
