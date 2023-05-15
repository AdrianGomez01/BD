package boletin1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import dataBaseConnection.DataBaseConnection;

public class Ej9 {

	public static void main(String[] args) {
		/**
		 * begin transaction (con.setAutocommit(false);- comenzar la transacción.
		 * commit - si va todo bien, se ejecuta y cambian los datos de la 
		 * base de datos. 
		 * rollback - si va algo mal, se vuelve al principio, no se tocan los 
		 * datos de la tabla principal y la tabla nueva auxiliar se elimina
		 * (va en el catch).
		 */

		try (Scanner teclado = new Scanner(System.in);
				Connection con = DataBaseConnection.getInstance().getCon();
				PreparedStatement consultaExisteCliente = con.prepareStatement("select customerNumber from customers where customerName = ?");
				//PreparedStatement consultaDeleteFromPayments = con.prepareStatement(
						//"delete from payments where customerName=?");
				//PreparedStatement consultaDeleteFromOrderDetails = con.prepareStatement(
						//"delete from orderDetails where customerName=?");
				//PreparedStatement consultaDeleteFromOrders = con.prepareStatement(
						//"delete from orders where customerName=?");
				
				/**
				 * si se hace cogiendo el id del cliente, sólo haría falta el preparedStatement
				 * para consultaExisteCliente que sería el único en el que pido por teclado.
				 * El resto pueden ser Statement ya que sólo se ejecutan 1 vez y no interviene
				 * el usuario
				 */
				Statement consultaDelete = con.createStatement();
				//Para que funcione el while creo otro statement
				Statement consultaSeleccionaPedidos = con.createStatement();
				) {

			
			System.out.println("Introduce el nombre del empleado: ");
			String nombreCliente = teclado.nextLine();
			/**
			 * el 1 del setString es el primer "?" que está en la consulta
			 * del preparedStatement
			 */
			consultaExisteCliente.setString(1, nombreCliente);
			
			/**
			 * se señala que estamos en una transacción
			 * lo que ejecute aquí no se cambia en la base de datos
			 * hasta hacer el commit final
			 */
			con.setAutoCommit(false);
			
			ResultSet resultExisteCliente = consultaExisteCliente.executeQuery();
			
			/**
			 * lo suyo sería hacer un do while hasta que ele nombre del cliente
			 * sea correcto, y no haría falta el rollback devuelve false si
			 *  no hay más iteraciones
			 */
			if (!resultExisteCliente.next()) {
				System.out.println("No existe un cliente con ese nombre");
				
				/**
				 * volver al estado anterior, anula lo que hayamos hecho hasta ahora
				 * es opcional pues se cierra en el catch si va mal y no se hace commit
				 */
				con.rollback();
				//para salir de la ejecución
				return;
			}			
			
			try {
				/**
				 * Recuperamos el número de cliente que se guardó en el resultset
				 * para utilizarlo en la consulta de consultaDelete.executeUpdate();
				 * En vez de "customerNumber" podría poner "1" siendo la posición
				 * de columna que ocupa, ya al hacer el preparedStatement
				 * el "select customerNumber" sólo tiene esa columna
				 */
				int customerNumber = resultExisteCliente.getInt("customerNumber");
				
				//primero borramos los pagos
				consultaDelete.executeUpdate("delete from payments where customerNumber = " + customerNumber);
				
				/**
				 * selecciono todos los pedidos del cliente en cuestión para poder relacionarlo con
				 * con la tabla de orderDetails y borrar también allí al cliente
				 */	
				ResultSet resultNumeroPedidosDeCliente = consultaSeleccionaPedidos.executeQuery(
						"select orderNumber from orders where customerNumber = " + customerNumber);
				
				/**
				 * while para ir borrando todos los pedidos where el número de cliente de la tabla
				 * orderDetails 
				 */
				while (resultNumeroPedidosDeCliente.next()) {
					consultaDelete.executeUpdate("delete from orderDetails where orderNumber = " + resultNumeroPedidosDeCliente);
				}
				
				//borramos los pedidos tanto de orders como de customers
				consultaDelete.executeUpdate("delete from orders where customerNumber = " + customerNumber);
				consultaDelete.executeUpdate("delete from customers where customerNumber = " + customerNumber);
				
				/**
				 * finalizadas las consultas de la transacción,realizamos 
				 * el commit para que se traslade a la base de datos. Si
				 * hay errores en el orden de borrados (por ejemplo delete from
				 * customers antes que delete from payments), no se ejecuta 
				 * ninguna consulta. Tiene que estar todo correcto para que 
				 * el commit se ejecute
				 */
				con.commit();
			}
			catch (SQLException e){
				System.out.println("No se ha podido ejecutar" + " " + e.getMessage());
				//.rollback es para volver al estado anterior, anula lo
				//que hayamos hecho hasta ahora
				con.rollback();
			}		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
