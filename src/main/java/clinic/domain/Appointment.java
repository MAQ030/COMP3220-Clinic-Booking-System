package clinic.domain;

import java.time.LocalDate;

public class Appointment {
    
    private int appointmentId;
    private Patient patient;
    private TimeSlot timeSlot;
    private LocalDate date;
    private String status;

    // constructor
    public Appointment(int appointmentId, Patient patient, TimeSlot timeSlot, LocalDate date, String status) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.timeSlot = timeSlot;
        this.date = date;
        this.status = status;
    }

    // getter methods 
    public int getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    //  method to update the status of an appointment if its cancelled 
    public void cancel() {
        this.status = "Cancelled";
    }
}
