package hu.agfcodeworks.operangel.application.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@MappedSuperclass
public abstract class Colleague extends AbstractEntity {

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "family_name", nullable = false)
    private String familyName;
}
