package com.example.demo.model;

public enum CrudQueue {
    CREATE(Constants.CREATE_VALUE),
    READ(Constants.READ_VALUE),
    UPDATE(Constants.UPDATE_VALUE),
    DELETE(Constants.DELETE_VALUE);
    public final String queueName;

    private CrudQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    // in order to use Enum in Annotations, static values needed
    public static class Constants {
        public static final String CREATE_VALUE = "create-queue";
        public static final String READ_VALUE = "read-queue";
        public static final String UPDATE_VALUE = "update-queue";
        public static final String DELETE_VALUE = "delete-queue";
    }
}