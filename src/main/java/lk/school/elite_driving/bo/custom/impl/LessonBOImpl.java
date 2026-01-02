package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.LessonBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.CourseDAO;
import lk.school.elite_driving.dao.custom.InstructorDAO;
import lk.school.elite_driving.dao.custom.LessonDAO;
import lk.school.elite_driving.dao.custom.StudentDAO;
import lk.school.elite_driving.dto.LessonDTO;
import lk.school.elite_driving.enitity.Course;
import lk.school.elite_driving.enitity.Instructor;
import lk.school.elite_driving.enitity.Lesson;
import lk.school.elite_driving.enitity.Student;
import lk.school.elite_driving.exception.SchedulingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LessonBOImpl implements LessonBO {
    
    private final LessonDAO lessonDAO = (LessonDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.LESSON);
    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.STUDENT);
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.COURSE);
    private final InstructorDAO instructorDAO = (InstructorDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.INSTRUCTOR);

    @Override
    public void scheduleLesson(LessonDTO lessonDTO) throws SchedulingException {
        TransactionalUtil.doInTransaction(session -> {
            try {
                Student student = null;
                if (lessonDTO.getStudentId() != null) {
                    student = studentDAO.getById(lessonDTO.getStudentId(), session)
                            .orElseThrow(() -> new SchedulingException("Student not found with ID: " + lessonDTO.getStudentId()));
                }

                Course course = null;
                if (lessonDTO.getCourseId() != null) {
                    course = courseDAO.getById(lessonDTO.getCourseId(), session)
                            .orElseThrow(() -> new SchedulingException("Course not found with ID: " + lessonDTO.getCourseId()));
                }

                Instructor instructor = null;
                if (lessonDTO.getInstructorId() != null) {
                    instructor = instructorDAO.getById(lessonDTO.getInstructorId(), session)
                            .orElseThrow(() -> new SchedulingException("Instructor not found with ID: " + lessonDTO.getInstructorId()));
                }

                Lesson lesson = DTOMapper.toEntity(lessonDTO);
                lesson.setStudent(student);
                lesson.setCourse(course);
                lesson.setInstructor(instructor);

                lessonDAO.save(lesson, session);
                return null;
            } catch (SchedulingException e) {
                try {
                    throw new SchedulingException("Failed to schedule lesson: " + e.getMessage());
                } catch (SchedulingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public ArrayList<LessonDTO> getAllLessons() throws Exception {
        return TransactionalUtil.doInTransaction(session -> {
            List<Lesson> lessons = lessonDAO.getAll(session);
            return lessons.stream()
                    .map(this::mapToDTOWithRelationships)
                    .collect(Collectors.toCollection(ArrayList::new));
        });
    }

    @Override
    public void assignLesson(LessonDTO lessonDTO) throws Exception {
        TransactionalUtil.doInTransaction(session -> {
            try {
                Lesson existingLesson = lessonDAO.getById(lessonDTO.getLessonId(), session)
                        .orElseThrow(() -> new SchedulingException("Lesson not found with ID: " + lessonDTO.getLessonId()));

                Student student = null;
                if (lessonDTO.getStudentId() != null) {
                    student = studentDAO.getById(lessonDTO.getStudentId(), session)
                            .orElseThrow(() -> new SchedulingException("Student not found with ID: " + lessonDTO.getStudentId()));
                }

                Course course = null;
                if (lessonDTO.getCourseId() != null) {
                    course = courseDAO.getById(lessonDTO.getCourseId(), session)
                            .orElseThrow(() -> new SchedulingException("Course not found with ID: " + lessonDTO.getCourseId()));
                }

                Instructor instructor = null;
                if (lessonDTO.getInstructorId() != null) {
                    instructor = instructorDAO.getById(lessonDTO.getInstructorId(), session)
                            .orElseThrow(() -> new SchedulingException("Instructor not found with ID: " + lessonDTO.getInstructorId()));
                }

                existingLesson.setLessonName(lessonDTO.getLessonName());
                existingLesson.setLessonTime(lessonDTO.getLessonTime());
                existingLesson.setStatus(lessonDTO.getStatus());
                existingLesson.setStudent(student);
                existingLesson.setCourse(course);
                existingLesson.setInstructor(instructor);

                lessonDAO.update(existingLesson, session);
                return null;
            } catch (Exception e) {
                try {
                    throw new SchedulingException("Failed to assign lesson: " + e.getMessage());
                } catch (SchedulingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void deleteLesson(String lessonId) throws Exception {
        TransactionalUtil.doInTransaction(session -> {
            lessonDAO.delete(lessonId, session);
            return null;
        });
    }

    @Override
    public LessonDTO getLessonById(String lessonId) throws Exception {
        return TransactionalUtil.doInTransaction(session -> {
            Optional<Lesson> lesson = lessonDAO.getById(lessonId, session);
            return lesson.map(this::mapToDTOWithRelationships).orElse(null);
        });
    }
    
    private LessonDTO mapToDTOWithRelationships(Lesson lesson) {
        LessonDTO lessonDTO = DTOMapper.toDTO(lesson);
        
        if (lesson.getStudent() != null) {
            lessonDTO.setStudentId(lesson.getStudent().getStudentId());
            lessonDTO.setStudentName(lesson.getStudent().getStudentName());
        }
        
        if (lesson.getCourse() != null) {
            lessonDTO.setCourseId(lesson.getCourse().getCourseId());
            lessonDTO.setCourseName(lesson.getCourse().getCourseName());
        }
        
        if (lesson.getInstructor() != null) {
            lessonDTO.setInstructorId(lesson.getInstructor().getInstructorId());
            lessonDTO.setInstructorName(lesson.getInstructor().getInstructorName());
        }
        
        return lessonDTO;
    }
}