package boletin1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import dataBaseConnection.DataBaseConnection;

public class Ej8 {

	public static void main(String[] args) {
		try (Connection con = DataBaseConnection.getInstance().getCon();
				Statement consulta = con.createStatement();) {

			consulta.executeUpdate("DELETE FROM productlines WHERE productLine LIKE \"RANDOM%\"");

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
