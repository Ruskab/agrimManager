package api;

import api.dtos.VehicleDto;

import java.time.LocalDate;

public class AgrimBackEndDomainFactory {

    public static final String REGISTRATION_PLATE = "111111";
    public static final int CLIENT_ID = 999;


    public static VehicleDto createVehicleDto() {
        return VehicleDto.builder().
                id(CLIENT_ID)
                .registrationPlate(REGISTRATION_PLATE)
                .clientId("4")
                .brand("Opel")
                .kms("03-03-2017 94744")
                .bodyOnFrame("VF1KC0JEF31065732")
                .lastRevisionDate(LocalDate.now().minusMonths(2))
                .itvDate(LocalDate.now().minusMonths(3))
                .nextItvDate(LocalDate.now().plusYears(1))
                .airFilterReference("1813029400")
                .oilFilterReference("1812344000")
                .fuelFilter("181315400")
                .motorOil("5.5  5W30").build();
    }


}
