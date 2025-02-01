package hu.agfcodeworks.operangel.application.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TextUtilTest {

    @ParameterizedTest
    @ArgumentsSource(TextUnificationArgumentProvider.class)
    void unifyTextTest(String original, String expected) {
        assertThat(TextUtil.unify(original))
                .isEqualTo(expected);
    }

    static class TextUnificationArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("Antonín Dvořák", "antonin dvorak"),
                    Arguments.of("Leoš Janáček", "leos janacek"),
                    Arguments.of("Jenůfa", "jenufa"),
                    Arguments.of("Števa Buryja", "steva buryja"),
                    Arguments.of("Đđ", "dd"),
                    Arguments.of("Łł", "ll"),
                    Arguments.of("àáâãäå", "aaaaaa"),
                    Arguments.of("çćĉċč", "ccccc"),
                    Arguments.of("ďđ", "dd"),
                    Arguments.of("ēĕėęě", "eeeee"),
                    Arguments.of("ĝğġģ", "gggg"),
                    Arguments.of("ĥħ", "hh"),
                    Arguments.of("ĩīĭįí", "iiiii"),
                    Arguments.of("ĵ", "j"),
                    Arguments.of("ķ", "k"),
                    Arguments.of("ļŀł", "lll"),
                    Arguments.of("ñńǹņň", "nnnnn"),
                    Arguments.of("òóôõöōŏő", "oooooooo"),
                    Arguments.of("ŕȑŗř", "rrrr"),
                    Arguments.of("śŝşš", "ssss"),
                    Arguments.of("ţťŧ", "ttt"),
                    Arguments.of("ùúûüũūŭůűų", "uuuuuuuuuu"),
                    Arguments.of("ŵ", "w"),
                    Arguments.of("ýÿŷ", "yyy")
            );
        }
    }
}
