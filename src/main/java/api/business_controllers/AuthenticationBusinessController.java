package api.business_controllers;

import api.daos.DaoFactory;
import api.daos.DaoSupplier;
import api.exceptions.NotFoundException;

public class AuthenticationBusinessController {

    static {
        DaoFactory.setFactory(DaoSupplier.HIBERNATE.createFactory());
    }

    private final MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    public void authenticateCredentials(String username, String password) throws NotFoundException {
        if (mechanicBusinessController.searchBy(username, password).isEmpty()) {
            throw NotFoundException.throwBecauseOf("Invalid credentials");
        }
    }

}
