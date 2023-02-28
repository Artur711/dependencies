package com.java17.dependencies.annotations.util;

public enum ROLE {
    ROLE_ADMIN, ROLE_USER;

    public String roleName() {
        return name().replace("ROLE_", "");
    }
}
