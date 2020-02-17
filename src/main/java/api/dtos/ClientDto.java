package api.dtos;

import api.entity.Client;

import java.io.Serializable;

public class ClientDto implements Serializable {

    private static final long serialVersionUID = 6285845014865471015L;

    private int id;

    private String fullName;

    private int hours;

    public ClientDto() {
    }

    public ClientDto(String fullName, int hours) {
        this.fullName = fullName;
        this.hours = hours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", hours=" + hours +
                '}';
    }
}
