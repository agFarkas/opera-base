package hu.agfcodeworks.operangel.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Column(name = "natural_id", nullable = false)
    private UUID naturalId;
}
