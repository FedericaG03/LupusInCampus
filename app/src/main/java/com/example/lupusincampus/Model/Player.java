package com.example.lupusincampus.Model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private List<Player> friendsList;
    private String role;

    public Player() {
        this.friendsList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Player> getFriendsList() {
        return friendsList;
    }

    public int friendIdList(String nickname){
        Player p = new Player();
        p.setNickname(nickname);
        return friendsList.stream().filter(v -> { return v.equals(p);}).findFirst().get().getId();
    }

    public void setFriendsList(List<Player> friendsList) {
        this.friendsList = friendsList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Player p = (Player) obj;
        return p.nickname.equals(this.nickname);
    }
}
