package api.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "repairing_pack")
public class RepairingPack {

    private static final long serialVersionUID = 1905122041950251207L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate invoicedDate;

    private int invoicedHours;

    public RepairingPack(){
        //JPA
    }

    public RepairingPack(int id, LocalDate invoicedDate, int invoicedHours) {
        this.id = id;
        this.invoicedDate = invoicedDate;
        this.invoicedHours = invoicedHours;
    }

    public RepairingPack(LocalDate invoicedDate, int invoicedHours) {
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
}
