//AI was used to assist with and understand the implementation of this class due to unfamiliarity 
// with SQLite and JDBC interfaces and features
package clinic.repository;

import clinic.database.DatabaseManager;
import clinic.domain.Appointment;
import clinic.domain.Patient;
import clinic.domain.Doctor;
import clinic.domain.TimeSlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.ArrayList;

public class SQLiteAppointmentRepository implements AppointmentRepository{ // this class implements the appointment repository interface
    private DatabaseManager databaseManager; // provide access to database connection

    // constructor 
    public SQLiteAppointmentRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override // override method from repository interface to save an appointment 
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO Appointment (patientId, slotId, date, status) VALUES (?, ?, ?, ?)";

        // establish connection with database and prepare statement
        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, appointment.getPatient().getPatientId());
            statement.setInt(2, appointment.getTimeSlot().getSlotId()); // assign values to sql parameters 
            statement.setString(3, appointment.getDate().toString()); 
            statement.setString(4, appointment.getStatus());

            statement.executeUpdate(); // execute the above insert statement (inserting appointment into db)

            try(ResultSet result = statement.getGeneratedKeys()) { // retrieve auto generated appointment ID 
                if(result.next()) {
                    int appointmentId = result.getInt(1);
                    // return appointment object with the generated ID
                    return new Appointment(appointmentId, appointment.getPatient(), appointment.getTimeSlot(), appointment.getDate(), appointment.getStatus());
                }
            }
            throw new IllegalStateException("Appointment ID was not generated."); // throw exception if SQLite did not return a generated ID
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save appointment.", e); // handle database errors including attempting to book an already booked time slot
        }
    }

    @Override  // override findById method to retrieve appointment using its unique ID 
    public Appointment findById(int appointmentId) {
        // join appointment, patient, timeslot, and doctor tables 
        String sql = "SELECT Appointment.appointmentId, Appointment.date, Appointment.status, Patient.patientId, Patient.name AS patientName, Patient.Phone, TimeSlot.slotId, TimeSlot.dayOfWeek, TimeSlot.startTime, DOCTOR.doctorId, Doctor.name AS doctorName, Doctor.specialty FROM Appointment JOIN Patient ON Appointment.patientId = Patient.patientId JOIN TimeSlot ON Appointment.slotId = TimeSlot.slotId JOIN Doctor ON TimeSlot.doctorId = Doctor.doctorId WHERE Appointment.appointmentId = ?";

        // establish connection with database, prepare select statement above 
        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointmentId); // assign appointmentID to sql parameter 

            try (ResultSet result = statement.executeQuery()) { // execute select statement above and retrieve matching record
                if (result.next()) {
                    return createAppointment(result);
                }
            }
            return null; // return null if appointment doesn't exist 
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve appointment.", e); // handle errors 
        }

    }

    @Override  // override findByPatient method to retrieve all appts belonging to a patient 
    public List<Appointment> findByPatient(int patientId) {
        String sql = "SELECT Appointment.appointmentId, Appointment.date, Appointment.status, Patient.patientId, Patient.name AS patientName, Patient.phone, TimeSlot.slotId, TimeSlot.dayOfWeek, TimeSlot.startTime, Doctor.doctorId, Doctor.name AS doctorName, Doctor.specialty FROM Appointment JOIN Patient ON Appointment.patientId = Patient.patientId JOIN TimeSlot ON Appointment.slotId = TimeSlot.slotId JOIN Doctor ON TimeSlot.doctorId = Doctor.doctorId WHERE Patient.patientId = ? ORDER BY Appointment.date, TimeSlot.startTime";
   
        List<Appointment> appointments = new ArrayList<>();

        // establish connection
        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId); // assign patient ID to sql parameter 

            try(ResultSet result = statement.executeQuery()) { // execute select statement and find all matching records 
                while(result.next()) { // reconstruct every matching appointment
                    appointments.add(createAppointment(result)); // add to list 
                }
            }
            return appointments;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve patient's appointments.", e); // handle errors 
        }
    }

    @Override // override method to retrieve a doctor's appts within a date range 
    public List<Appointment> findByDoctor(int doctorId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT Appointment.appointmentId, Appointment.date, Appointment.status, Patient.patientId, Patient.name AS patientName, Patient.phone, TimeSlot.slotId, TimeSlot.dayOfWeek, TimeSlot.startTime, Doctor.doctorId, Doctor.name AS doctorName, Doctor.specialty FROM Appointment JOIN Patient ON Appointment.patientId = Patient.patientId JOIN TimeSlot ON Appointment.slotId = TimeSlot.slotId JOIN Doctor ON TimeSlot.doctorId = Doctor.doctorId WHERE Doctor.doctorId = ? AND Appointment.date BETWEEN ? AND ? ORDER BY Appointment.date, TimeSlot.startTime";
        
        List<Appointment> appointments = new ArrayList<>();

        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);
            statement.setString(2, startDate.toString());   // assing doctor ID and date range to sql parameters
            statement.setString(3, endDate.toString());

            try(ResultSet result = statement.executeQuery()) {
                while(result.next()) { // reconstruct every matching appointment 
                    appointments.add(createAppointment(result));
                }
            }
            return appointments;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to retrieve doctor's appointments.", e); // handle errors 
        }
    }

    @Override // override of repository interface to update an existing appointment in database 
    public void update(Appointment appointment) {
        String sql = "UPDATE Appointment SET patientId = ?, slotId = ?, date = ?, status = ? WHERE appointmentId = ?";

        try(Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointment.getPatient().getPatientId());
            statement.setInt(2, appointment.getTimeSlot().getSlotId());
            statement.setString(3, appointment.getDate().toString());   // assign updated appointment info
            statement.setString(4, appointment.getStatus());
            statement.setInt(5, appointment.getAppointmentId());

            int rowsUpdated = statement.executeUpdate(); // execute update statement, retrieve number of affected rows 

            if (rowsUpdated == 0) {
                throw new IllegalStateException("Appointment not found.");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update appointment.", e);
        }
    }

    @Override // override method to check if a time slot is already booked 
    public boolean isSlotBooked(int slotId, LocalDate date) {
        String sql = "SELECT appointmentId FROM Appointment WHERE slotId = ? AND date = ? AND status = 'Booked'";

        try (Connection connection = databaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, slotId);
            statement.setString(2, date.toString());   // assign time slot ID and appt date to sql parameters

            try (ResultSet result = statement.executeQuery()) { // execute select statement and search for matching active booking
                return result.next(); // return true if a matching active booking exists, false if no matching active booking exists 
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check time slot booking.", e); // handle errors 
        }
    }

    // private helper method to reconstruct an appointment
    private Appointment createAppointment(ResultSet result) throws SQLException {
            // reconstruct patient object using retrieved patient info
            Patient patient = new Patient(result.getInt("patientId"), result.getString("patientName"), result.getString("phone"));

            //reconstruct doctor object using retrieved doctor info
            Doctor doctor = new Doctor(result.getInt("doctorId"), result.getString("doctorName"), result.getString("specialty"));

            // reconstruct timeslot object using retrieved timeslot info 
            TimeSlot timeSlot = new TimeSlot(result.getInt("slotId"), doctor, DayOfWeek.valueOf(result.getString("dayOfWeek")), LocalTime.parse(result.getString("startTime")));

            // reconstruct + return the appointment object using all other objects 
            return new Appointment(result.getInt("appointmentId"), patient, timeSlot, LocalDate.parse(result.getString("date")), result.getString("status"));
        }
} // end SQLiteAppointmentRepository.java

