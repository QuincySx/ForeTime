package com.smallraw.foretime.app.event;

import android.support.annotation.IntDef;

/**
 * 添加、修改、删除任务 发出的全局事件
 */
public class TaskChangeEvent {
    /**
     * 添加
     */
    public static final int ADD = 0;
    /**
     * 修改内容
     */
    public static final int UPDATE = 1;
    /**
     * 删除
     */
    public static final int DELETE = 2;

    @IntDef({ADD, UPDATE, DELETE})
    public @interface TaskChangeType {
    }

    /**
     * 内容修改类型
     */
    @TaskChangeType
    private final int changeType;

    /**
     * 被修改任务的 ID
     */
    private final long taskID;

    /**
     * 被修改任务的类型
     */
    private final int taskType;

    public TaskChangeEvent(int changeType) {
        this.changeType = changeType;
        this.taskID = 0;
        this.taskType = -1;
    }

    public TaskChangeEvent(int changeType, long taskID, int taskType) {
        this.changeType = changeType;
        this.taskID = taskID;
        this.taskType = taskType;
    }

    public int getChangeType() {
        return changeType;
    }

    public long getTaskID() {
        return taskID;
    }

    public int getTaskType() {
        return taskType;
    }
}
