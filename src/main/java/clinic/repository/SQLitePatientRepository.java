//AI was used to assist with and understand the implementation of this class due to unfamiliarity 
// with SQLite and JDBC interfaces and features 

package clinic.repository;

import clinic.domain.Patient;               //import patient domain class and database manager
import clinic.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;           // JDBC imports used to communicate with SQLite database
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLitePatientRepository implements PatientRepository { // this class implements the PatientRepository interface
    
    private DatabaseManager databaseManager; // access to SQLite database connection

    //constructor
    public SQLitePatientRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override  // override save method from the patient repository interface 
    public Patient save(Patient patient) {
        
        String sql = "INSERT INTO Patient (name, phone) VALUES (?, ?) "; // question marks represent placeholders for later

        // establish connection and prepare sql statement 
        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getPhone());  // assign values to the sql parameters 

            statement.executeUpdate(); // execute insert statement

            try(ResultSet result = statement.getGeneratedKeys()) { // get the automatically generated patient ID
                if(result.next()) { // move to the first row of the result and check if it exists
                    int patientId = result.getInt(1); // get the generated ID from the first column
                    return new Patient(patientId, patient.getName(), patient.getPhone()); // return a new patient object containing the generated ID
                }

            }
            throw new IllegalStateException("Patient ID was not generated."); // throw exception if SQLite did not return a generated ID


        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save patient", e); // handle database errors and preserve original exception
        }

    }

    @Override // override findById method to find a patient using their unique patient ID
    public Patient findById(int patientId) {
        String sql = "SELECT patientId, name, phone FROM Patient WHERE patientId = ?";

        // establish connection and prepare statement
        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId); // assign patient ID to sql parameter 

            try (ResultSet result = statement.executeQuery()) { // execute the SELECT statement above, store returned rows in a ResultSet
                if (result.next()) {
                    return new Patient (result.getInt("patientId"), result.getString("name"), result.getString("phone"));
                }
            }
       
            return null; // return null if the patient doesn't exist 
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve patient.", e);
        }

    }

    @Override  // override update method from patient repository to update an existing patient's information
    public void update(Patient patient) {
        String sql = "UPDATE Patient SET name = ?, phone = ? WHERE patientId = ?";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getPhone());        // assign values to the sql parameters 
            statement.setInt(3, patient.getPatientId());

            int rowsUpdated = statement.executeUpdate(); // execute the above update statement 
            if (rowsUpdated == 0) { // check if patient exists 
                throw new IllegalStateException("Patient not found.");
            }
        } catch (SQLException e) { // catch sql exceptions
            throw new IllegalStateException("Could not update patient information.", e); // display feedback message     
        }
    }


} // end SQLitePatientRepository.java
