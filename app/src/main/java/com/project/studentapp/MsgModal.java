package com.project.studentapp;

public class MsgModal {
    private String message;

    public MsgModal(String cnt) {
        this.message = cnt;
    }

    public String getMessage() {
        return message;
    }

    public void setCnt(String cnt) {
        this.message = cnt;
    }
}
