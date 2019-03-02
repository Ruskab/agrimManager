package api.daos;

import api.entity.RepairingPack;

import java.util.stream.Stream;

public interface RepairingPackDao extends GenericDao<RepairingPack, Integer>{
    Stream<RepairingPack> findAll();
}

