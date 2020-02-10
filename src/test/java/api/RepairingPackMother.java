package api;

import api.entity.RepairingPack;

import java.time.LocalDate;

public class RepairingPackMother {

    public static final LocalDate INVOICED_DATE = LocalDate.now().minusDays(1);
    public static final int INVOICED_HOURS = 15;

    public static RepairingPack create(LocalDate invoicedDate, int invoicedHours) {
        return new RepairingPack(invoicedDate, invoicedHours);
    }

    public static RepairingPack repairingPack() {
        return create(INVOICED_DATE, INVOICED_HOURS);
    }

    public static RepairingPack withInvoicedDate(LocalDate localDate) {
        return create(localDate, INVOICED_HOURS);
    }

    public static RepairingPack withInvoicedHours(Integer invoicedHours) {
        return create(INVOICED_DATE, invoicedHours);
    }

}
