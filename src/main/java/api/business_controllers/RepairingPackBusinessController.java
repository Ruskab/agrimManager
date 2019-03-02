package api.business_controllers;

import api.daos.DaoFactory;
import api.dtos.RepairingPackDto;
import api.entity.RepairingPack;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class RepairingPackBusinessController {
    public int create(RepairingPackDto repairingPackDto) {
        RepairingPack repairingPack = new RepairingPack(repairingPackDto.getInvoicedDate(), repairingPackDto.getInvoicedHours());
        DaoFactory.getFactory().getRepairingPackDao().create(repairingPack);
        return repairingPack.getId();
    }

    public List<RepairingPackDto> readAll() {
        return DaoFactory.getFactory().getRepairingPackDao().findAll().map(RepairingPackDto::new).collect(Collectors.toList());
    }

    public RepairingPackDto read(String id) {
        return DaoFactory.getFactory().getRepairingPackDao().read(Integer.parseInt(id)).map(RepairingPackDto::new)
                .orElseThrow(() -> new NotFoundException("Repairing pack not found"));
    }
}
