package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.StudentDTO;

import java.util.List;

public interface StudentBO extends SuperBO {
    void registerStudent(StudentDTO studentDTO) throws Exception;
    boolean addStudent(StudentDTO studentDTO);
    boolean updateStudent(StudentDTO studentDTO);
    boolean deleteStudent(String id);
    StudentDTO getStudent(String id);
    List<StudentDTO> getAllStudents();
    String getNextStudentId();
    List<StudentDTO> search(String searchText);
}