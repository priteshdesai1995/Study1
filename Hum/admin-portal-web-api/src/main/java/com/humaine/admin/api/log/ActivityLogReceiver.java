package com.humaine.admin.api.log;

public interface ActivityLogReceiver {
    void receive(ActivityLogObject activityLogObject);
}