package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.LessonDTO;
import lk.school.elite_driving.exception.SchedulingException;

import java.util.ArrayList;

public interface LessonBO extends SuperBO {
    void scheduleLesson(LessonDTO lessonDTO) throws SchedulingException;
    ArrayList<LessonDTO> getAllLessons() throws Exception;
    void assignLesson(LessonDTO lessonDTO) throws Exception;
    void deleteLesson(String lessonId) throws Exception;
    LessonDTO getLessonById(String lessonId) throws Exception;
}