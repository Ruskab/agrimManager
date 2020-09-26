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
        mechanicBusinessController.searchBy(username, password).stream().findFirst()
                .orElseThrow(() -> NotFoundException.throwBecauseOf("Invalid credentials"));
    }

}
