
package clinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import clinic.domain.Appointment;
import clinic.domain.Patient;
import clinic.domain.TimeSlot;
import clinic.repository.AppointmentRepository;
import clinic.repository.PatientRepository;
import clinic.repository.TimeSlotRepository;

// This code was developed with assistance from AI.
// Review, test, and understand the code before submission.

public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private TimeSlotRepository timeSlotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              TimeSlotRepository timeSlotRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public Appointment bookAppointment(int patientId, int slotId, LocalDate date) {

        Patient patient = patientRepository.findById(patientId);
        TimeSlot timeSlot = timeSlotRepository.findById(slotId);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        if (!isSlotAvailable(slotId, date)) {
            throw new IllegalArgumentException("Time slot is not available.");
        }

        Appointment appointment = new Appointment(0, patient, timeSlot, date, "Booked");

        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(int appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }

        appointment.cancel();
        appointmentRepository.update(appointment);
    }

    public List<Appointment> getPatientAppointment(int patientId) {
        return appointmentRepository.findByPatient(patientId);
    }

    public List<Appointment> getDoctorAppointment(int doctorId,
                                                  LocalDate startDate,
                                                  LocalDate endDate) {

        return appointmentRepository.findByDoctor(doctorId, startDate, endDate);
    }

    public List<TimeSlot> getAvailableSlots(int doctorId, LocalDate date) {

        List<TimeSlot> availableSlots = new ArrayList<>();
        List<TimeSlot> timeSlots = timeSlotRepository.findByDoctor(doctorId);

        for (TimeSlot timeSlot : timeSlots) {
            if (isSlotAvailable(timeSlot.getSlotId(), date)) {
                availableSlots.add(timeSlot);
            }
        }

        return availableSlots;
    }

    private boolean isSlotAvailable(int slotId, LocalDate date) {

        TimeSlot timeSlot = timeSlotRepository.findById(slotId);

        if (timeSlot == null || date == null) {
            return false;
        }

        if (timeSlot.getDayOfWeek() != date.getDayOfWeek()) {
            return false;
        }

        return !appointmentRepository.isSlotBooked(slotId, date);
    }
}
