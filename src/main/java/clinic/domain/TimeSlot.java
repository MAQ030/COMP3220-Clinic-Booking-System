package clinic.domain;

import java.time.DayOfWeek;
import java.time.LocalTime; 

public class TimeSlot {
    
    private int slotId;
    private Doctor doctor;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;

    // constructor 
    public TimeSlot(int slotId, Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime) {
        this.slotId = slotId;
        this.doctor = doctor;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }

    // getter methods 
    public int getSlotId() {
        return slotId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    // to calculate the end time of a certain 30-minute time slot 
    public LocalTime getEndTime() {
        return startTime.plusMinutes(30); 
    }

} // end of TimeSlot.java 
