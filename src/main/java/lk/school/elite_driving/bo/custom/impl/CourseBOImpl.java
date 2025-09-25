package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.CourseDAO;
import lk.school.elite_driving.dto.CourseDTO;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class CourseBOImpl implements CourseBO {
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.COURSE);
    @Override
    public boolean addCourse(CourseDTO course) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    courseDAO.save(DTOMapper.toEntity(course), session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateCourse(CourseDTO course) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    courseDAO.update(DTOMapper.toEntity(course), session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteCourse(String courseId) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    courseDAO.delete(courseId, session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        try{
            return TransactionalUtil.doInTransaction((Function<Session, List<CourseDTO>>) session ->
                    courseDAO.getAll(session).stream().map(DTOMapper::toDTO).toList()
            );
        }catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<CourseDTO> getCourseById(String courseId) {
        try{
            return TransactionalUtil.doInTransaction((Function<Session, Optional<CourseDTO>>) session ->
                    courseDAO.getById(courseId, session).map(DTOMapper::toDTO)
            );
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public String getNewCourseId() {
        try {
            String lastPk = TransactionalUtil.doInTransaction((Function<Session, String>) session -> courseDAO.getLastPk(session).orElse("C000"));
            int nextId = Integer.parseInt(lastPk.substring(1)) + 1;
            return String.format("C%03d", nextId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while getting new course ID", e);
        }
    }
}
