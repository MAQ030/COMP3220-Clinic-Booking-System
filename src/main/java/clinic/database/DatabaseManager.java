//AI was used to assist with and understand the implementation of this class due to unfamiliarity with SQLite 

package clinic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private String databaseUrl;

    //constructor 
    public DatabaseManager(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    // establish and return a connection to the SQLite database
    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(databaseUrl);

            // foreign key constraints enabled 
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            } catch (SQLException e) {
                connection.close();
                throw e;
            }

            return connection;

        } catch (SQLException e) { // catch sql exceptions
            throw new IllegalStateException("Database connection failed.", e); // display failed message 
        }
    }

    public void initializeDatabase() {
        // patient table 
        String createPatientTable = """
            CREATE TABLE IF NOT EXISTS Patient (patientId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, phone TEXT NOT NULL)""";
    
        // doctor table
        String createDoctorTable = """
            CREATE TABLE IF NOT EXISTS Doctor (doctorId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, specialty TEXT NOT NULL)""";   
    
        // time slot table 
        String createTimeSlotTable = """
            CREATE TABLE IF NOT EXISTS TimeSlot (slotId INTEGER PRIMARY KEY AUTOINCREMENT, doctorId INTEGER NOT NULL,
            dayOfWeek TEXT NOT NULL, startTime TEXT NOT NULL, FOREIGN KEY (doctorId) REFERENCES Doctor(doctorId), UNIQUE (doctorId, dayOfWeek, startTime))""";

        // appointment table 
        String createAppointmentTable = """
            CREATE TABLE IF NOT EXISTS Appointment (appointmentId INTEGER PRIMARY KEY AUTOINCREMENT, patientId INTEGER NOT NULL,
            slotId INTEGER NOT NULL, date TEXT NOT NULL, status TEXT NOT NULL, FOREIGN KEY (patientId) REFERENCES Patient(patientId),
            FOREIGN KEY (slotId) REFERENCES TimeSlot(slotId), CHECK (status IN ('Booked', 'Cancelled')))""";
        
        // prevent double bookings
        String preventDoubleBooking = """
            CREATE UNIQUE INDEX IF NOT EXISTS unique_active_booking ON Appointment(slotId, date) WHERE status = 'Booked' """;
        
        // execute above sql statements to create database tables 
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(createPatientTable); // create patient database table 
            statement.execute(createDoctorTable); // create doctor database table 
            statement.execute(createTimeSlotTable); // create time slot table 
            statement.execute(createAppointmentTable); // create appoinment table 
            statement.execute(preventDoubleBooking); // create the table to handle double bookings 
        } catch (SQLException e) { // catch any exceptions, failures 
            throw new IllegalStateException("Database initialization failed.", e); // display failure message
        }
    }
}
