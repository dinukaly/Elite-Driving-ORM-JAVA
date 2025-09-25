package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.CourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseBO extends SuperBO {
    boolean addCourse(CourseDTO course);
    boolean updateCourse(CourseDTO course);
    boolean deleteCourse(String courseId);
    List<CourseDTO> getAllCourses();
    Optional<CourseDTO> getCourseById(String courseId);
    String getNewCourseId();
}
