package hu.agfcodeworks.operangel.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class StatusDto {

    private StatusCategory category;

    private String message;
}
