package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.CourseDAO;
import lk.school.elite_driving.dao.custom.PaymentDAO;
import lk.school.elite_driving.dao.custom.StudentDAO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.enitity.Course;
import lk.school.elite_driving.enitity.Payment;
import lk.school.elite_driving.enitity.Student;
import lk.school.elite_driving.exception.RegistrationException;

import java.util.List;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {
    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.STUDENT);
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.COURSE);
    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);


    @Override
    public void registerStudent(StudentDTO studentDTO) throws Exception {
        TransactionalUtil.doInTransaction(session -> {
            Student student = DTOMapper.toEntity(studentDTO);

            // Fetch and set the courses to ensure they are managed entities
            List<Course> courses = studentDTO.getCourses().stream()
                    .map(courseDTO -> courseDAO.getById(courseDTO.getCourseId(), session).orElse(null))
                    .collect(Collectors.toList());
            student.setCourses(courses);

            studentDAO.save(student, session);
            // Handle payments separately to avoid circular reference issues
            if (studentDTO.getPayments() != null && !studentDTO.getPayments().isEmpty()) {

                studentDTO.getPayments().forEach(paymentDTO -> {
                    Payment payment = DTOMapper.toEntity(paymentDTO, session);
                    payment.setStudent(student);
                    paymentDAO.save(payment, session);
                });



//                for (PaymentDTO paymentDTO : studentDTO.getPayments()) {
//                    Payment payment = DTOMapper.toEntity(paymentDTO, session);
//                    payment.setStudent(student);
//                    paymentDAO.save(payment, session);
//                }
            }
        });
    }

    @Override
    public boolean addStudent(StudentDTO studentDTO) {
        return false;
    }

    @Override
    public boolean updateStudent(StudentDTO studentDTO) {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                Student existingStudent = studentDAO.getById(studentDTO.getStudentId(), session).orElse(null);
                if (existingStudent == null) {
                    return false;
                }
                
                Student updatedStudent = DTOMapper.toEntity(studentDTO);
                
                // Fetch and set the courses to ensure they are managed entities
                if (studentDTO.getCourses() != null) {
                    List<Course> courses = studentDTO.getCourses().stream()
                            .map(courseDTO -> courseDAO.getById(courseDTO.getCourseId(), session).orElse(null))
                            .collect(Collectors.toList());
                    updatedStudent.setCourses(courses);
                }
                
                studentDAO.update(updatedStudent, session);
                return true;
            });
        } catch (RegistrationException e) {
            throw new RegistrationException("Error while updating student", e);
        }
    }

    @Override
    public boolean deleteStudent(String id) {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                studentDAO.delete(id, session);
                return true;
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while deleting student", e);
        }
    }

    @Override
    public StudentDTO getStudent(String id) {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                Student student = studentDAO.getById(id, session).orElse(null);
                if (student == null) {
                    return null;
                }
                
                StudentDTO studentDTO = DTOMapper.toDTO(student);
                if (student.getCourses() != null) {
                    studentDTO.setCourses(student.getCourses().stream()
                            .map(course -> DTOMapper.toDTO(course))
                            .collect(Collectors.toList()));
                }
                return studentDTO;
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while fetching student", e);
        }
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                List<Student> students = studentDAO.getAll(session);
                return students.stream().map(student -> {
                    StudentDTO studentDTO = DTOMapper.toDTO(student);
                    // Set course names for display
                    if (student.getCourses() != null) {
                        studentDTO.setCourses(student.getCourses().stream()
                                .map(course -> DTOMapper.toDTO(course))
                                .collect(Collectors.toList()));
                    }
                    return studentDTO;
                }).collect(Collectors.toList());
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while fetching all students", e);
        }
    }

    @Override
    public String getNextStudentId() {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                String lastPk = studentDAO.getLastPk(session).orElse("S000");
                int nextId = Integer.parseInt(lastPk.substring(1)) + 1;
                return String.format("S%03d", nextId);
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while getting new student ID", e);
        }
    }

    @Override
    public List<StudentDTO> search(String searchText) {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                List<Student> students = studentDAO.getAll(session);
                return students.stream()
                        .filter(student -> 
                            student.getStudentName().toLowerCase().contains(searchText.toLowerCase()) ||
                            student.getStudentId().toLowerCase().contains(searchText.toLowerCase()) ||
                            student.getStudentEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                            student.getStudentContact().toLowerCase().contains(searchText.toLowerCase())
                        )
                        .map(student -> {
                            StudentDTO studentDTO = DTOMapper.toDTO(student);
                            if (student.getCourses() != null) {
                                studentDTO.setCourses(student.getCourses().stream()
                                        .map(course -> DTOMapper.toDTO(course))
                                        .collect(Collectors.toList()));
                            }
                            return studentDTO;
                        })
                        .collect(Collectors.toList());
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while searching students", e);
        }
    }
}