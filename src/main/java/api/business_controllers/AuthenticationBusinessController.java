package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.dtos.MechanicDto;
import api.exceptions.NotFoundException;

public class AuthenticationBusinessController {

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    public void authenticateCredentials(String username, String password) throws NotFoundException {
        if (!existentCredentials(username, password)) {
            throw NotFoundException.throwBecauseOf("Invalid Credentials");
        }
    }

    private boolean existentCredentials(String username, String password) {
        return mechanicBusinessController.findBy(username).stream()
                .map(MechanicDto::getPassword)
                .anyMatch(password::equals);
    }


}
