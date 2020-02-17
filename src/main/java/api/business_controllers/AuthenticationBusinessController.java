package api.business_controllers;

import api.exceptions.NotFoundException;

public class AuthenticationBusinessController {

    private MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    public void authenticateCredentials(String username, String password) throws NotFoundException {
        if (nonExistentCredentials(username, password)) {
            throw NotFoundException.throwBecauseOf("Invalid Credentials");
        }
    }

    private boolean nonExistentCredentials(String username, String password) {
        return mechanicBusinessController.findBy(username).stream().noneMatch(mechanicDto -> password.equals(mechanicDto.getPassword()));
    }


}
