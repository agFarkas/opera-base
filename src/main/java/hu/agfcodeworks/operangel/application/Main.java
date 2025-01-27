package hu.agfcodeworks.operangel.application;

import hu.agfcodeworks.operangel.application.ui.MainWindow;
import hu.agfcodeworks.operangel.application.util.ContextUtil;

public class Main {

    public static void main(String[] args) {
        ContextUtil.startContext();

        new MainWindow();

    }
}