package br.com.aquiles.report.dao;

import br.com.aquiles.persistence.dao.DAO;
import br.com.aquiles.report.annotation.SRDatasource;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@SRDatasource
public class DAOAquilesReport extends DAO {

    @PersistenceContext(unitName = "aquilesReportDS")
    private EntityManager em;

    @Override
    public EntityManager getEm() {
        return em;
    }
}
