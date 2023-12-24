package com.javalab.boot.constant;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public String getRole() {
        return this.name(); // 열거형 상수의 이름을 반환
    }
}
