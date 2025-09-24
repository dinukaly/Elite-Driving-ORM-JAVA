package lk.school.elite_driving.bo;

import lk.school.elite_driving.bo.custom.impl.AuthBOImpl;
import lk.school.elite_driving.bo.custom.impl.CourseBOImpl;
import lk.school.elite_driving.bo.custom.impl.InstructorBOImpl;
import lk.school.elite_driving.bo.custom.impl.PaymentBOImpl;
import lk.school.elite_driving.bo.custom.impl.StudentBOImpl;
import lk.school.elite_driving.bo.custom.impl.LessonBOImpl;

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
        switch (botypes){
            case USER:
                return new AuthBOImpl();
//            case COURSE:
//                return new CourseBOImpl();
            case INSTRUCTOR:
                return new InstructorBOImpl();
            case STUDENT:
                return new StudentBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case LESSON:
                return new LessonBOImpl();
            default:
                return null;
        }
    }
}