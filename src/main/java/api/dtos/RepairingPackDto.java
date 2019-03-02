package api.dtos;

import api.entity.RepairingPack;

import java.time.LocalDate;

public class RepairingPackDto {

    private int id;
    private LocalDate invoicedDate;
    private int invoicedHours;

    public RepairingPackDto(){

    }

    public RepairingPackDto(RepairingPack repairingPack){
        this.id = repairingPack.getId();
        this.invoicedDate = repairingPack.getInvoicedDate();
        this.invoicedHours = repairingPack.getInvoicedHours();
    }

    public RepairingPackDto(LocalDate invoicedDate, int invoicedHours) {
        this.invoicedDate = invoicedDate;
        this.invoicedHours = invoicedHours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getInvoicedDate() {
        return invoicedDate;
    }

    public void setInvoicedDate(LocalDate invoicedDate) {
        this.invoicedDate = invoicedDate;
    }

    public int getInvoicedHours() {
        return invoicedHours;
    }

    public void setInvoicedHours(int invoicedHours) {
        this.invoicedHours = invoicedHours;
    }

    @Override
    public String toString() {
        return "RepairingPackDto{" +
                "id=" + id +
                ", invoicedDate=" + invoicedDate +
                ", invoicedHours=" + invoicedHours +
                '}';
    }
}
