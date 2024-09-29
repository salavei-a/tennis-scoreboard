package com.asalavei.tennisscoreboard.dbaccess.repositories;

import com.asalavei.tennisscoreboard.dbaccess.entities.MatchEntity;

import java.util.List;

public class MatchHibernateRepository extends BaseHibernateRepository<MatchEntity> implements MatchRepository {

    @Override
    public List<MatchEntity> findAll(int pageNumber, int pageSize) {
        return executeInTransaction(session -> {
            String query = "from MatchEntity m join fetch m.firstPlayer join fetch m.secondPlayer order by m.id desc";

            return session.createQuery(query)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        });
    }

    @Override
    public List<MatchEntity> findAllByPlayerName(String name, int pageNumber, int pageSize) {
        return executeInTransaction(session -> {
            String query = "from MatchEntity m join fetch m.firstPlayer fp join fetch m.secondPlayer sp " +
                           "where lower(fp.name) like :name or lower(sp.name) like :name order by m.id desc";

            return session.createQuery(query)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();

        });
    }

    @Override
    public int countTotalPages(int pageSize) {
        return executeInTransaction(session -> {
            Long countResults = session.createQuery("select count (m.id) from MatchEntity m", Long.class)
                    .uniqueResult();

            return (int) Math.ceil((double) countResults / pageSize);
        });
    }

    @Override
    public int countTotalPagesByPlayerName(String name, int pageSize) {
        return executeInTransaction(session -> {
            String query = "select count (m.id) from MatchEntity m " +
                           "join m.firstPlayer fp join m.secondPlayer sp " +
                           "where lower(fp.name) like :name or lower(sp.name) like :name";

            Long countResults = session.createQuery(query, Long.class)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .uniqueResult();

            return (int) Math.ceil((double) countResults / pageSize);
        });
    }
}
