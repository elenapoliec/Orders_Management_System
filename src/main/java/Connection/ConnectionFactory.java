package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Aceasta clasa este (Connection Factory) responsabila de managementul sesiunilor cu baza de date PostgreSQL.
 * Implementeaza șablonul de proiectare Singleton pentru a garanta o singura instanta de configurare a driverului.
 * * @author Poliec Elena
 * @version 1.0
 */

public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://localhost:5432/school_db";
    private static final String USER = "postgres";
    private static final String PASS = "12345678";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Constructor privat pentru a impiedica instantierea directa din exterior.
     * incarca dinamic driverul nativ de PostgreSQL prin Reflection (Class.forName).
     */

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creeaza fizic o conexiune activa catre serverul bazei de date folosind credentialele configurate.
     * @return Instanta obiectului Connection sau null in caz de eroare.
     */

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Returneaza o conexiune deschisa catre baza de date apeland instanta unica Singleton.
     * @return Obiectul Connection utilizat pentru executia instructiunilor SQL.
     */

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Inchide in siguranta resursa de tip Connection daca aceasta este activa.
     * @param connection Obiectul conexiunii care trebuie eliberat.
     */

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }


    /**
     * Inchide in siguranta instructiunea SQL (Statement / PreparedStatement) pentru a preveni memory leaks.
     * @param statement Obiectul statement care trebuie eliberat.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Inchide in siguranta setul de rezultate intors de baza de date.
     * @param resultSet Structura de date ResultSet ce contine randurile returnate.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }

}
