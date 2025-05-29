package edu.upc.dsa.models;


public class Questionari {

    private String data;
    private String title;
    private String message;
    private String sender;

    // Constructor vacío requerido por Retrofit
    public Questionari() {}

    // Constructor completo
    public Questionari(String data, String title, String message, String sender) {
        this.data = data;
        this.title = title;
        this.message = message;
        this.sender = sender;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
