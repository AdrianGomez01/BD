package dataBaseConnection;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection implements Closeable {
    private static DataBaseConnection dbc;

    private static String url = "jdbc:mysql://localhost:3306/classicmodels?useSSL=false&serverTimezone=UTC";
    private static String usuario = "root";
    private static String password = "1234";

    private Connection con;

    private DataBaseConnection() throws SQLException {
        con = DriverManager.getConnection(url, usuario, password);

    }

    public static DataBaseConnection getInstance() throws SQLException {
        if (dbc == null) {
            dbc = new DataBaseConnection();
        }
        return dbc;
    }

    @Override
    public void close() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getCon() {
        return con;
    }
}
