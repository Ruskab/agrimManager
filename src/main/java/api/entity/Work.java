package api.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date invoicedDate;

    private int invoicedHours;

}
