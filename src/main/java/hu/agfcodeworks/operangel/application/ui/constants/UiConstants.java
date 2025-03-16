package hu.agfcodeworks.operangel.application.ui.constants;

import java.time.format.DateTimeFormatter;

public interface UiConstants {

    String EMPTY_STRING = "";

    String RETURN_AND_LINE_BREAK = "\r\n";

    String LIST_LINE_BREAK = "\r\n - ";

    String YES = "Igen";

    String NO = "Nem";

    String LIST_ITEM_CREATE_NEW = "Új hozzáadása...";

    String INVALID_VALUES = "Érvénytelen értékek";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    Object[] yesNoOptions = {YES, NO};

    Object[] roleChangeOperationOptions = {
            "Új létrehozása",
            "Meglévő móodsítása"
    };
}
