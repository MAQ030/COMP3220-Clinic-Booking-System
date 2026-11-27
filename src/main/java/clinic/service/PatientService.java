
package clinic.service;

import clinic.domain.Patient;
import clinic.repository.PatientRepository;

// AI-assisted implementation; review and test before submission.

public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(String name, String phone) {

        if (!validatePatient(name, phone)) {
            throw new IllegalArgumentException("Name and phone are required.");
        }

        Patient patient = new Patient(0, name.trim(), phone.trim());

        return patientRepository.save(patient);
    }

    public Patient getPatient(int patientId) {
        return patientRepository.findById(patientId);
    }

    public void updatePatient(int patientID, String name, String phone) {

        if (!validatePatient(name, phone)) {
            throw new IllegalArgumentException("Name and phone are required.");
        }

        Patient patient = patientRepository.findById(patientID);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        patient.setName(name.trim());
        patient.setPhone(phone.trim());

        patientRepository.update(patient);
    }

    private boolean validatePatient(String name, String phone) {
        return name != null && !name.isBlank()
                && phone != null && !phone.isBlank();
    }
}
