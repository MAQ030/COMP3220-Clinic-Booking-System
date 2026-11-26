package clinic.repository;

import clinic.domain.Appointment;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {

    //using methods in UML class diagram:

    Appointment save(Appointment appointment); // save a new appointment to database
    Appointment findById(int appointmentId); // find an appointment using its unique appointment ID

    List<Appointment> findByPatient(int patientId); // find all appointments associated with a specific patient 
    List<Appointment> findByDoctor(int doctorId, LocalDate startDate, LocalDate endDate); // find all doctor's appointments within a specific date range

    void update(Appointment appointment); // method to update an existing appointment

    boolean isSlotBooked(int slotId, LocalDate date); // check to see if a time slot is already booked on a specific date 
    
} // end AppointmentRepository.java
