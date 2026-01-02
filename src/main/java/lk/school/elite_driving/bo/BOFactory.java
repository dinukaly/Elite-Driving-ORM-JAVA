package lk.school.elite_driving.bo;

import lk.school.elite_driving.bo.custom.impl.*;

public class BOFactory {
 private static BOFactory boFactory;

    private BOFactory(){

    }
    public static BOFactory getBoFactory(){
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOTypes{
        USER, COURSE, LESSON, STUDENT, PAYMENT, INSTRUCTOR
    }
    public SuperBO getBO(BOTypes botypes){
        return switch (botypes) {
            case USER -> new AuthBOImpl();
            case COURSE -> new CourseBOImpl();
            case INSTRUCTOR -> new InstructorBOImpl();
            case STUDENT -> new StudentBOImpl();
            case PAYMENT -> new PaymentBOImpl();
            case LESSON -> new LessonBOImpl();
            default -> null;
        };
    }
}