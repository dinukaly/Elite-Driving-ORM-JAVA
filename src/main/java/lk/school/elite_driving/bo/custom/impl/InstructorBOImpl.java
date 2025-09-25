package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.InstructorBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.InstructorDAO;
import lk.school.elite_driving.dto.InstructorDTO;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class InstructorBOImpl implements InstructorBO {
    private final InstructorDAO instructorDAO = (InstructorDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.INSTRUCTOR);
    @Override
    public boolean addInstructor(InstructorDTO instructor) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    instructorDAO.save(DTOMapper.toEntity(instructor), session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateInstructor(InstructorDTO instructor) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    instructorDAO.update(DTOMapper.toEntity(instructor), session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteInstructor(String id) {
        try{
            TransactionalUtil.doInTransaction((Consumer<Session>) session ->
                    instructorDAO.delete(id, session)
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<InstructorDTO> getAllInstructors() {
        try{
            return TransactionalUtil.doInTransaction((Function<Session, List<InstructorDTO>>) session ->
                    instructorDAO.getAll(session).stream().map(DTOMapper::toDTO).toList()
            );
        }catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<InstructorDTO> getInstructorById(String id) {
        try{
            return TransactionalUtil.doInTransaction((Function<Session, Optional<InstructorDTO>>) session ->
                    instructorDAO.getById(id, session).map(DTOMapper::toDTO)
            );
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public String getNewInstructorId() {
        try {
            String lastPk = TransactionalUtil.doInTransaction((Function<Session, String>) session -> instructorDAO.getLastPk(session).orElse("I000"));
            int nextId = Integer.parseInt(lastPk.substring(1)) + 1;
            return String.format("I%03d", nextId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while getting new instructor ID", e);
        }
    }
}
