package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.InstructorDTO;

import java.util.List;
import java.util.Optional;

public interface InstructorBO extends SuperBO {
    boolean addInstructor(InstructorDTO instructor);
    boolean updateInstructor(InstructorDTO instructor);
    boolean deleteInstructor(String id);
    List<InstructorDTO> getAllInstructors();
    Optional<InstructorDTO> getInstructorById(String id);
    String getNewInstructorId();
}
