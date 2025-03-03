package hu.agfcodeworks.operangel.application.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TextUtil {

    private final Map<Character, Character> unificationMap = new HashMap<>();

    static {
        initUnificationBy("àáâãäå", 'a');
        initUnificationBy("çćĉċč", 'c');
        initUnificationBy("ďđ", 'd');
        initUnificationBy("ēĕėęě", 'e');
        initUnificationBy("ĝğġģ", 'g');
        initUnificationBy("ĥħ", 'h');
        initUnificationBy("ĩīĭįí", 'i');
        initUnificationBy("ĵ", 'j');
        initUnificationBy("ķ", 'k');
        initUnificationBy("ļŀł", 'l');
        initUnificationBy("ñńǹņň", 'n');
        initUnificationBy("òóôõöōŏő", 'o');
        initUnificationBy("ŕȑȑŗř", 'r');
        initUnificationBy("śŝşš", 's');
        initUnificationBy("ţťŧ", 't');
        initUnificationBy("ùúûüũūŭůűų", 'u');
        initUnificationBy("ŵ", 'w');
        initUnificationBy("ýÿŷ", 'y');
    }

    private void initUnificationBy(String initialChars, char targetChar) {
        for (var character : initialChars.toCharArray()) {
            unificationMap.put(character, targetChar);
        }
    }


    public String unify(@NonNull String originalText) {
        var chars = originalText.toLowerCase()
                .toCharArray();

        for (var i = 0; i < chars.length; i++) {
            var origChar = chars[i];
            if (unificationMap.containsKey(origChar)) {
                chars[i] = unificationMap.get(origChar);
            }
        }

        return new String(chars);
    }
}
