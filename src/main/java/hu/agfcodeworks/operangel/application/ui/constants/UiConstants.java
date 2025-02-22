package hu.agfcodeworks.operangel.application.ui.constants;

import java.time.format.DateTimeFormatter;

public interface UiConstants {

    String EMPTY_STRING = "";

    String LIST_ITEM_CREATE_NEW = "Új hozzáadása...";

    String INVALID_VALUES = "Érvénytelen értékek";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
}
