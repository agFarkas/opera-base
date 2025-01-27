package hu.agfcodeworks.operangel.application.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.dialect.Dialect;

import java.sql.Driver;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum DbEngine {

    POSTGRES("postgresql", org.postgresql.Driver.class, org.hibernate.dialect.PostgresPlusDialect.class);

    private final String name;

    private Class<? extends Driver> driverClass;

    private Class<? extends Dialect> dialectClass;

    public static DbEngine byName(String name) {
        for (var dbEngine : DbEngine.values()) {
            if (Objects.equals(dbEngine.getName(), name)) {
                return dbEngine;
            }
        }

        throw new IllegalArgumentException("'%s' is not a supported database engine.".formatted(name));
    }
}
