package hu.agfcodeworks.operangel.application.ui.text;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class AutoCompleterDocument<T> extends PlainDocument {

    @NonNull
    private final JTextField textField;

    @NonNull
    private final List<T> items;

    @NonNull
    private final Function<T, String> textProvider;

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        var originalText = getOriginalText();

        if (offs == originalText.length()) {
            var stub = obtainStub(offs, str);
            var descriptionOpt = searchForElementStartingWith(stub);

            if (descriptionOpt.isPresent()) {
                var description = descriptionOpt.get();
                var substring = description.substring(offs + str.length());

                super.insertString(offs, str + substring, a);

                textField.select(offs + str.length(), getLength());
            } else {
                super.insertString(offs, str, a);
            }
        } else {
            super.insertString(offs, str, a);
        }
    }

    private String obtainStub(int offs, String str) throws BadLocationException {
        var originalText = getOriginalText();

        if (Objects.isNull(originalText) || originalText.isEmpty()) {
            return str;
        }

        if (offs == originalText.length()) {
            return originalText + str;
        }

        return str;
    }

    private String getOriginalText() throws BadLocationException {
        return getText(0, getLength());
    }

    private Optional<String> searchForElementStartingWith(String chunk) {
        return items.stream()
                .filter(t -> textProvider.apply(t).startsWith(chunk))
                .findFirst()
                .map(textProvider);
    }
}
