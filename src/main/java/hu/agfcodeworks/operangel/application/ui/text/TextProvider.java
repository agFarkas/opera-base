package hu.agfcodeworks.operangel.application.ui.text;

public interface TextProvider<T> {

    String provide(T object);
}
