package api.business_controllers;

import api.daos.DaoFactory;
import api.dtos.RepairingPackDto;
import api.entity.RepairingPack;

public class RepairingPackBusinessController {
    public int create(RepairingPackDto repairingPackDto) {
        RepairingPack repairingPack = new RepairingPack(repairingPackDto.getInvoicedDate(), repairingPackDto.getInvoicedHours());
        DaoFactory.getFactory().getRepairingPackDao().create(repairingPack);
        return repairingPack.getId();
    }
}
