package api.business_controllers;

import api.dtos.MechanicDto;
import api.exceptions.NotFoundException;

import java.util.List;

public class AuthenticationBusinessController {

    private MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    public void authenticateCredentials(String username, String password) {
        List<MechanicDto> mechanicDtos = mechanicBusinessController.findBy(username);
        mechanicDtos.stream().filter(mechanicDto -> password.equals(mechanicDto.getPassword())).findFirst()
                .orElseThrow(() -> new NotFoundException("Invalid Credentials"));
    }


}
