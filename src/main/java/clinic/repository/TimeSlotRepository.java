package clinic.repository;

import clinic.domain.TimeSlot; //import TimeSlot.java from domain
import java.util.List; // import list 

public interface TimeSlotRepository {

    //using methods in UML class diagram:
    
    TimeSlot save(TimeSlot timeSlot); // save a new time slot to the database 
    TimeSlot findById(int slotId); // find a time slot using its unique slot ID

    List<TimeSlot> findByDoctor(int doctorId); // find all time slots associated with a specific doctor 

    void delete(int slotId); // method to remove a time slot from the database

} // end of TimeSlotRepository.java 
