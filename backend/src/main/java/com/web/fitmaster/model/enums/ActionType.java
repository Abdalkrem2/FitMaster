package com.web.fitmaster.model.enums;

import lombok.Getter;
@Getter
public enum ActionType {
    CREATE("created"),
    UPDATE("updated"),
    DELETE("deleted"),
    RENEW("renewed"),
    LOGIN("logged in"),
    ADD("added");

    private final String label;

    ActionType(String label) {
        this.label = label;
    }

}
