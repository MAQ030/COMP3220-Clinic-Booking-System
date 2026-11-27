
package clinic.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import clinic.domain.Doctor;
import clinic.domain.TimeSlot;
import clinic.repository.DoctorRepository;
import clinic.repository.TimeSlotRepository;

// This code was developed with assistance from AI.
// The code was reviewed, tested, and understood before submission.

public class DoctorService {

    private DoctorRepository doctorRepository;
    private TimeSlotRepository timeSlotRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         TimeSlotRepository timeSlotRepository) {
        this.doctorRepository = doctorRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public Doctor registerDoctor(String name, String specialty) {
        Doctor doctor = new Doctor(0, name, specialty);
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctor(int doctorId) {
        return doctorRepository.findById(doctorId);
    }

    public List<Doctor> findDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty);
    }

    public TimeSlot addTimeSlot(int doctorId, DayOfWeek day, LocalTime startTime) {

        Doctor doctor = doctorRepository.findById(doctorId);

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found.");
        }

        if (day == null || day == DayOfWeek.SATURDAY
                || day == DayOfWeek.SUNDAY || startTime == null) {
            throw new IllegalArgumentException("Enter a weekday and start time.");
        }

        TimeSlot timeSlot = new TimeSlot(0, doctor, day, startTime);

        return timeSlotRepository.save(timeSlot);
    }

    public void removeTimeSlot(int slotId) {
        timeSlotRepository.delete(slotId);
    }

    public List<TimeSlot> getDoctorTimeSlots(int doctorId) {
        return timeSlotRepository.findByDoctor(doctorId);
    }
}
