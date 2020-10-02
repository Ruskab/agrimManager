package front;

import api.dtos.RepairingPackDto;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Credentials;
import front.dtos.Vehicle;

import java.time.LocalDate;

public class AgrimDomainFactory {

    public static Vehicle createVehicle() {
        return byDefault().clientId("3").build();
    }

    public static Credentials fakeCredentials() {
        return Credentials.builder()
                .username(MechanicDtoMother.FAKE_NAME)
                .password(MechanicDtoMother.FAKE_PASSWORD)
                .build();
    }

    public static Vehicle createVehicle(String clientId) {
        return Vehicle.builder().registrationPlate("AA1234BB").clientId(clientId).build();
    }

    public static RepairingPackDto createRepairingPackDto() {
        return RepairingPackDto.builder().invoicedDate(LocalDate.now()).invoicedHours(10).build();
    }


    public static Vehicle.VehicleBuilder byDefault() {
        return Vehicle.builder()
                .id(2)
                .registrationPlate("111111")
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
                .motorOil("5.5  5W30");
    }

}
