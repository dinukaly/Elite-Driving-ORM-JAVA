module lk.school.elite_driving {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires jbcrypt;
    requires com.jfoenix;
    opens lk.school.elite_driving.enitity to org.hibernate.orm.core;
    opens lk.school.elite_driving.config to jakarta.persistence;
    opens lk.school.elite_driving.tm to javafx.base;

    opens lk.school.elite_driving to javafx.fxml;
    exports lk.school.elite_driving;
    exports lk.school.elite_driving.controller;
    exports lk.school.elite_driving.controller.admin;
    exports lk.school.elite_driving.controller.receptionist;
    exports lk.school.elite_driving.controller.instructor;
    exports lk.school.elite_driving.controller.lesson;
    exports lk.school.elite_driving.controller.payment;
    exports lk.school.elite_driving.dto;
    exports lk.school.elite_driving.tm;

    opens lk.school.elite_driving.controller.course to javafx.fxml;
    opens lk.school.elite_driving.controller.student to javafx.fxml;
    opens lk.school.elite_driving.controller.payment to javafx.fxml;
    opens lk.school.elite_driving.dto to javafx.fxml;



}