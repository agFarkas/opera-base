package hu.agfcodeworks.operangel.application.ui.uidto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DbConnectionStatus {
    CLOSED("Lezárva"),

    AWAITING("Várakozás"),

    ESTABLISHED("Létrejött"),

    REFUSED("Megszakadt");

    private final String text;

    public String getText() {
        return this.text;
    }
}
