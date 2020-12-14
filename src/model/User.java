package model;

import java.io.Serializable;

public class User implements Serializable{
    private int id;
    private String username;
    private String password;
    private String fullName;
    private int userStatus;
    private int win;
    private int draw;
    private int lose;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

}
