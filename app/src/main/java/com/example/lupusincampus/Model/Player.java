package com.example.lupusincampus.Model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int id;
    private ArrayList<Player> friendsList;
    private String nickname;
    private String email;
    private Ruolo role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFriendsList(ArrayList<Player> friendsList) {
        this.friendsList = friendsList;
    }

    public Player(){
        this.friendsList = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Player> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<Player> friendsList) {
        this.friendsList = (ArrayList) friendsList;
    }

    public Ruolo getRole() {
        return role;
    }

    public void setRole(Ruolo role) {
        this.role = role;
    }
}

