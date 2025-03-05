package models;

public class Persona {

    private String userId;
    private String username;
    private String password;
    private String token;

    // Constructorrs
    public Persona(String username, String password){
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
