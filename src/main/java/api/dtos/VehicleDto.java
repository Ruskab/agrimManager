package api.dtos;

public class VehicleDto {

    private int id;
    private String registrationPlate;
    private String brand;
    private String clientId;

    public VehicleDto(int id, String registrationPlate, String brand, String clientId) {
        this.id = id;
        this.registrationPlate = registrationPlate;
        this.brand = brand;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "id=" + id +
                ", registrationPlate='" + registrationPlate + '\'' +
                ", brand='" + brand + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
