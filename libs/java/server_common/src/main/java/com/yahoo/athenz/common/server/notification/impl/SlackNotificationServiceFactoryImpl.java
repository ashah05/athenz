package com.yahoo.athenz.common.server.notification.impl;

import com.yahoo.athenz.common.server.notification.NotificationService;
import com.yahoo.athenz.common.server.notification.NotificationServiceFactory;

/*
 * This is a reference implementation.
 */
public class SlackNotificationServiceFactoryImpl implements NotificationServiceFactory {

    @Override
    public NotificationService create() {
        return new SlackNotificationService();
    }
}