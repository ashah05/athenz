package com.yahoo.athenz.common.server.notification;

public interface NotificationToSlackConverter {
    /**
     *
     * @param notification object to be converted to Slack
     * @return The Slack content for the given Notification
     */
    NotificationSlack getNotificationAsSlack(Notification notification);
}