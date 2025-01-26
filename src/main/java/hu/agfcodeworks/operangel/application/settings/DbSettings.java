package hu.agfcodeworks.operangel.application.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
public class DbSettings {

    DbEngine dbEngine;

    String host;

    int port;

    String name;

    String username;

    String password;

}
