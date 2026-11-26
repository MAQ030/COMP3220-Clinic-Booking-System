package clinic.repository;

import clinic.domain.Doctor; // import doctor.java from domain
import java.util.List; // import list 

public interface DoctorRepository {
    
    // using methods in UML class diagram:

    Doctor save(Doctor doctor); // save new doctor to the database 
    Doctor findById(int doctorId); // find a doctor using their unique Doctor ID

    List<Doctor> findBySpecialty(String specialty); // find doctor with a specific specialty
    List<Doctor> findAll(); // get all registered doctors 

} // end DoctorRepository.java
