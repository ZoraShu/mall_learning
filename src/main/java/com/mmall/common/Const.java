package com.mmall.common;

/**
 * Created by yueshu on 8/28/17.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0; // common user
        int ROLE_ADMIN = 1; //admin
    }
}
