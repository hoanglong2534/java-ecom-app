package com.longg.gky.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_USER_AGE = "userAge";
    private static final String KEY_USER_ADDRESS = "userAddress";

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";

    // Giả lập database người dùng
    private static final Map<String, String> userPasswords = new HashMap<>();
    private static final Map<String, String> userRoles = new HashMap<>();
    private static final Map<String, String> userAges = new HashMap<>();
    private static final Map<String, String> userAddresses = new HashMap<>();

    static {
        userPasswords.put("admin", "admin");
        userRoles.put("admin", ROLE_ADMIN);
        userAges.put("admin", "25");
        userAddresses.put("admin", "Hà Nội");
    }

    public static void createAdminIfNotExists(Context context) {}

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean login(Context context, String username, String password) {
        if (userPasswords.containsKey(username) && userPasswords.get(username).equals(password)) {
            String role = userRoles.get(username);
            String age = userAges.get(username);
            String address = userAddresses.get(username);

            SharedPreferences.Editor editor = getPrefs(context).edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_USER_ROLE, role);
            editor.putString(KEY_USER_NAME, username);
            editor.putString(KEY_USER_PASSWORD, password);
            editor.putString(KEY_USER_AGE, age);
            editor.putString(KEY_USER_ADDRESS, address);
            editor.apply();
            return true;
        }
        return false;
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.clear();
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static String getRole(Context context) {
        return getPrefs(context).getString(KEY_USER_ROLE, ROLE_USER);
    }

    public static String getUserName(Context context) {
        return getPrefs(context).getString(KEY_USER_NAME, "");
    }

    public static String getUserAge(Context context) {
        return getPrefs(context).getString(KEY_USER_AGE, "");
    }

    public static String getUserAddress(Context context) {
        return getPrefs(context).getString(KEY_USER_ADDRESS, "");
    }

    public static boolean changePassword(Context context, String oldPassword, String newPassword) {
        SharedPreferences prefs = getPrefs(context);
        String currentPassword = prefs.getString(KEY_USER_PASSWORD, null);
        String username = prefs.getString(KEY_USER_NAME, null);

        if (username != null && currentPassword != null && currentPassword.equals(oldPassword)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_USER_PASSWORD, newPassword);
            editor.apply();
            userPasswords.put(username, newPassword);
            return true;
        }
        return false;
    }

    public static boolean saveUserWithRole(Context context, String username, String password, String role, String age, String address) {
        if (userPasswords.containsKey(username)) {
            return false; // User đã tồn tại
        }
        userPasswords.put(username, password);
        userRoles.put(username, role);
        userAges.put(username, age);
        userAddresses.put(username, address);
        return true;
    }
}
