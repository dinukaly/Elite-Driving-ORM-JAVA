package lk.school.elite_driving.dao;

import lk.school.elite_driving.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory(){}

    public static DAOFactory getInstance(){
        return daoFactory==null?daoFactory=new DAOFactory():daoFactory;
    }

    public enum DAOType{
        USER,
        COURSE,
        STUDENT,
        INSTRUCTOR,
        LESSON,
        PAYMENT
    }

    public SuperDAO getDAO(DAOType daoType){
        switch (daoType){
            case USER:
                return new UserDAOImpl();
            case COURSE:
                return new CourseDAOImpl();
            case STUDENT:
                return new StudentDAOImpl();
            case INSTRUCTOR:
                return new InstructorDAOImpl();
            case LESSON:
                return new LessonDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                return null;
        }
    }
}
