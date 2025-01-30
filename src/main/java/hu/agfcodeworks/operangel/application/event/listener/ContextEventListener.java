package hu.agfcodeworks.operangel.application.event.listener;

import hu.agfcodeworks.operangel.application.event.ContextEvent;

public interface ContextEventListener {

    void statusChanged(ContextEvent event);
}
