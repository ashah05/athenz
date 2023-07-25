package com.yahoo.athenz.common.server.notification;

import java.util.Objects;

public class NotificationSlack {
    private String channel;
    private String message;

    public NotificationSlack(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationSlack that = (NotificationSlack) o;
        return getChannel().equals(that.getChannel()) &&
                getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannel(), getMessage());
    }
}
