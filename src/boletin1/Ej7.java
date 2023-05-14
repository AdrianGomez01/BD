package boletin1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import dataBaseConnection.DataBaseConnection;
public class Ej7 {

	private static final int MAXIMO = 100000;
	private static final int MAXIMO2= 200000;
	
	public static void main(String[] args) {
		try (Connection con = DataBaseConnection.getInstance().getCon();
				PreparedStatement consultaPrepared = con.prepareStatement("insert into productlines(productLine) values(?)");
				Statement consulta = con.createStatement();) {
			/**
			 * delete from productlines where productLine like 'RAND%'
			 * (se puede introducir directamente en workbench)
			 * para poder eliminar lo que crea en la base de datos y poder
			 * ejecutar de nuevo el código
			 */
			
			int contador = 0;
			long miliSegundos = (long) System.currentTimeMillis();
			while(contador < MAXIMO) {
				String l = "insert into productlines(productLine) values(\"RANDOM_" + contador + "\");";
				consulta.executeUpdate(l);
				contador++;
			}	
			long miliSegundos2 = System.currentTimeMillis();
			
			while (contador < MAXIMO2) {
				consultaPrepared.setString(1, "RANDOM_" + contador);
				consultaPrepared.executeUpdate();
				contador++;
			}
			
			long miliSegundos3 = System.currentTimeMillis();
			
			System.out.println("La versión con Statement tarda: " + (miliSegundos2 - miliSegundos) + " mili segundos");
			System.out.println("La versión con PreparedStatement tarda: " + (miliSegundos3 - miliSegundos2) + " mili segundos");
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

}
