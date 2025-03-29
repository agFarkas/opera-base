package hu.agfcodeworks.operangel.application.ui.constants;

import java.time.format.DateTimeFormatter;

public interface UiConstants {

    String EMPTY_STRING = "";

    String RETURN_AND_LINE_BREAK = "\r\n";

    String LIST_LINE_BREAK = RETURN_AND_LINE_BREAK + " - ";

    String YES = "Igen";

    String NO = "Nem";

    String LIST_ITEM_CREATE_NEW = "Új létrehozása...";

    String INVALID_VALUES = "Érvénytelen adatok";

    String SYSTEM_ERROR = "Rendszerhiba";

    String INVALID_OPERATION = "Érvénytelen művelet";

    String UNEXPECTED_ERROR_ERROR_MESSAGE_PATTERN = "Nem várt hiba történt: %s";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    Object[] yesNoOptions = {YES, NO};

    Object[] roleChangeOperationOptions = {
            "Új létrehozása",
            "Meglévő móodsítása"
    };

}
