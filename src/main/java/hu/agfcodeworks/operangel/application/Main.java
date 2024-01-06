package hu.agfcodeworks.operangel.application;

import hu.agfcodeworks.operangel.application.configuration.Config;
import hu.agfcodeworks.operangel.application.ui.MainWindow;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Config.class);

        context.getBean(MainWindow.class);
    }
}