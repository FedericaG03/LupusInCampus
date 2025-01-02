package com.example.lupusincampus;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private String email = "";
    private String password = "";

    // Getter e setter per email e password
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
