//AI was used to assist with and understand the implementation of this class due to unfamiliarity 
// with SQLite and JDBC interfaces and features 
package clinic.repository;

import clinic.domain.Doctor;
import clinic.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;        // clinic and sql imports 
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class SQLiteDoctorRepository implements DoctorRepository { // this class implements the DoctorRepository interface
    private DatabaseManager databaseManager;

    // constructor for this class
    public SQLiteDoctorRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    
    @Override // override save method from repository interface to save a new doctor to the database 
    public Doctor save(Doctor doctor) {
        String sql = "INSERT INTO Doctor (name, specialty) VALUES (?, ?)";

        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSpecialty());   // assign values to sql parameters (?, ?)

            statement.executeUpdate(); // execute the insert statement above

            try(ResultSet result = statement.getGeneratedKeys()) { // retrieve the auto generated doctor ID 
                if(result.next()) {
                    int doctorId = result.getInt(1); 
                    return new Doctor(doctorId, doctor.getName(), doctor.getSpecialty());
                }
            }
        
            throw new IllegalStateException("Doctor ID unable to be generated."); // display feedback if error occurs 
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save doctor", e);
        }
    }

    @Override 
    public Doctor findById(int doctorId) {
        String sql = "SELECT doctorId, name, specialty FROM Doctor WHERE doctorId = ?";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);

            try(ResultSet result = statement.executeQuery()) {
                if(result.next()) {
                    return new Doctor(result.getInt("doctorId"), result.getString("name"), result.getString("specialty"));
                }
            }
        
        return null;  // return null if doctor doesnt exist 
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve doctor.", e);
        }
    }

    @Override  // override findBySpecialty method in repository interface to find doctors with a specific specialty 
    public List<Doctor> findBySpecialty(String specialty) {
        String sql = "SELECT doctorId, name, specialty FROM Doctor WHERE specialty = ?";

        List<Doctor> doctors = new ArrayList<>(); // create arraylist to store matching doctors 

        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, specialty); // pass specialty as a parameter 

            try(ResultSet result = statement.executeQuery()) { // execute the above select statement 
                while(result.next()) { // while loop to convert every returned record into a Doctor object, not if because could be more than one 
                    Doctor doctor = new Doctor(result.getInt("doctorId"), result.getString("name"), result.getString("specialty"));
                doctors.add(doctor);
                }
            }
            return doctors;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve doctors by specialty.", e); // display feedback if sqlexception caught
        }
    }

    @Override 
    public List<Doctor> findAll() { // override method from repository interface to get all registered doctors 
        String sql = "SELECT doctorId, name, specialty FROM Doctor"; 

        List<Doctor> doctors = new ArrayList<>();

        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
            while(result.next()) {
                Doctor doctor = new Doctor(result.getInt("doctorId"), result.getString("name"), result.getString("specialty"));
                doctors.add(doctor);
            }
        
        return doctors;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve doctors.", e);
        }
    }
} // end of SQLiteDoctorRepository.java 
