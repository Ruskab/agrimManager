package api.daos;

import api.daos.hibernate.DaoFactoryHibr;

import java.util.function.Supplier;

public enum DaoSupplier {

    HIBERNATE(DaoFactoryHibr::new);

    private final Supplier<DaoFactory> daoFactory;

    DaoSupplier(Supplier<DaoFactory> daoFactory) {
        this.daoFactory = daoFactory;
    }

    public DaoFactory createFactory() {
        return daoFactory.get();
    }

}
