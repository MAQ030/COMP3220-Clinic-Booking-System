//AI was used to assist with and understand the implementation of this class due to unfamiliarity 
// with SQLite and JDBC interfaces and features
package clinic.repository;

import clinic.domain.Doctor;
import clinic.domain.TimeSlot;
import clinic.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class SQLiteTimeSlotRepository implements TimeSlotRepository { // implements the repository interface 
    private DatabaseManager databaseManager;

    // constructor 
    public SQLiteTimeSlotRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override  // override of save method in repository interface to save a timeslot 
    public TimeSlot save(TimeSlot timeSlot) {
        String sql = "INSERT INTO TimeSlot (doctorId, dayOfWeek, startTime) VALUES (?, ?, ?)";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, timeSlot.getDoctor().getDoctorId());
            statement.setString(2, timeSlot.getDayOfWeek().name());           // assign values to sql parameters
            statement.setString(3, timeSlot.getStartTime().toString());

            statement.executeUpdate();

            try (ResultSet result = statement.getGeneratedKeys()) { // retrieve auto generated slot ID
                if(result.next()) {
                    int slotId = result.getInt(1);
                    return new TimeSlot(slotId, timeSlot.getDoctor(), timeSlot.getDayOfWeek(), timeSlot.getStartTime());
                }
            }

            throw new IllegalStateException("Unable to generate a Time Slot ID.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save time slot.", e);
        }
    }

    @Override // override method from repository interface that finds a time slot by its unique slot ID
    public TimeSlot findById(int slotId) {
        String sql = "SELECT TimeSlot.slotId, TimeSlot.dayOfWeek, TimeSlot.startTime, Doctor.doctorId, Doctor.name, Doctor.specialty FROM TimeSlot JOIN Doctor ON TimeSlot.doctorId = Doctor.doctorId WHERE TimeSlot.slotId = ?";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, slotId);

            try (ResultSet result = statement.executeQuery()) {
                if(result.next()) {
                    // reconstruct associated doctor object 
                    Doctor doctor = new Doctor(result.getInt("doctorId"), result.getString("name"), result.getString("specialty"));
                    // reconstruct and return TimeSlot object 
                    return new TimeSlot(result.getInt("slotId"), doctor, DayOfWeek.valueOf(result.getString("dayOfWeek")), LocalTime.parse(result.getString("startTime")));
                }
            }

            return null; // return null if time slot doesnt exist 
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve time slot.", e);
        }
    }

    @Override // override of TimeSlot list from interface to find all time slots associated with a specific doctor 
    public List<TimeSlot> findByDoctor(int doctorId) {
        String sql = "SELECT TimeSlot.slotId, TimeSlot.dayOfWeek, TimeSlot.startTime, Doctor.doctorId, Doctor.name, Doctor.specialty FROM TimeSlot JOIN Doctor ON TimeSlot.doctorId = Doctor.doctorId WHERE Doctor.doctorId = ?";
        List<TimeSlot> timeSlots = new ArrayList<>();

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId); // assign the doctor id to the sql parameter 
            
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) { // while loop to iterate through each matching time slot 
                    // reconstruct the associated doctor object 
                    Doctor doctor = new Doctor(result.getInt("doctorId"), result.getString("name"), result.getString("specialty"));
                    TimeSlot timeSlot = new TimeSlot(result.getInt("slotId"), doctor, DayOfWeek.valueOf(result.getString("dayOfWeek")), LocalTime.parse(result.getString("startTime")));
                    timeSlots.add(timeSlot);

                }
            }

            return timeSlots;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve doctor's time slots.", e);
        }
    }

    @Override  // override method from repository interface that deletes a time slot using its ID
    public void delete(int slotId) {
        String sql = "DELETE FROM TimeSlot WHERE slotId = ?";
        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, slotId); // assign slot ID to the sql parameter 
            int rowsDeleted = statement.executeUpdate(); // execute the delete statement 
            
            if (rowsDeleted == 0) { // ensure that a time slot was deleted 
                throw new IllegalStateException("Time slot not found.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete time slot.", e);
        }
    }
} // end SQLiteTimeSlotRepository.java

