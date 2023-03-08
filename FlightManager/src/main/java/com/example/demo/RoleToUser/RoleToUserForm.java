package com.example.demo.RoleToUser;


public class RoleToUserForm{
    private String username;
    private String roleName;

    public RoleToUserForm(){}

    public RoleToUserForm(String username, String roleName){
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername(){
        return this.username;
    }

    public String getRoleName(){
        return this.roleName;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setRoleName(String roleName){
        this.roleName = roleName;
    }
}
