package com.dhruvijain.deliveryplanner.model;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession(User user) {
        this.loggedInUser = user;
    }

    public static void createSession(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        instance = null;
    }
}
