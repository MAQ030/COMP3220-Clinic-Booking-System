package clinic.repository;

import clinic.domain.Patient;

public interface PatientRepository {
    
    // using methods from UML class diagram:

    Patient save(Patient patient); // save a new patient to database
    Patient findById(int patientId); // retrieve a patient using their unique patient ID
    
    void update(Patient patient); // update an existing patient's info 
}
