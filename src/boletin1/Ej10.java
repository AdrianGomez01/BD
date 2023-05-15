package boletin1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import dataBaseConnection.DataBaseConnection;

public class Ej10 {
	
	private static final Scanner teclado = new Scanner(System.in);
	
	public static void main(String[] args) {
		
	}
	
	public static void insertarProducto() {
		try (Connection con = DataBaseConnection.getInstance().getCon();
				PreparedStatement consultaPreparada = con.prepareStatement("insert into productos values(?,?,?,?,?,?,?,?,?)");
				PreparedStatement consultaCategorias = con.prepareStatement("select productLine from productLines");
				Statement consulta = con.createStatement();
				){
			
			System.out.println("Inserte el código del producto");
			String codigoProducto = teclado.nextLine();
			System.out.println("Inserte el nombre del producto");
			String nombreProducto = teclado.nextLine();
			
			ResultSet listadoCategorias = consultaCategorias.executeQuery();
			ArrayList<String> arrayCategorias = new ArrayList<>();
			
			while (listadoCategorias.next()) {
				arrayCategorias.add(listadoCategorias.getString(1));
			}
			String categoriaSeleccionada = UserDataCollector.getStringDeOpciones(
					"Seleccione una de las categorías siguientes: ", arrayCategorias.toArray(new String[0])); 
			System.out.println("Inserte la escala del producto");
			String escalaProducto = teclado.nextLine();
			System.out.println("Inserte el vendedor del producto");
			String vendedorProducto = teclado.nextLine();
			System.out.println("Inserte la descripción del producto");
			String descrpcionProducto = teclado.nextLine();
			System.out.println("Inserte el stock del producot");
			int cantidadProducto = Integer.parseInt(teclado.nextLine());
			System.out.println("Inserte el precio del prodcuto");
			float precioProducto = ;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
