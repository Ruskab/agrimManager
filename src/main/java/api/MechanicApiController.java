package api;

import api.businessControllers.MechanicBusinessController;
import api.dtos.MechanicDto;

public class MechanicApiController {
    public static final String MECHANICS = "mechanics";
    private MechanicBusinessController mechanicBusinesssController = new MechanicBusinessController();

    public int create(MechanicDto mechanicDto) {
        return this.mechanicBusinesssController.create(mechanicDto);
    }
}
