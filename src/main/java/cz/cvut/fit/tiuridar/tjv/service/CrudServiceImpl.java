package cz.cvut.fit.tiuridar.tjv.service;

import cz.cvut.fit.tiuridar.tjv.domain.EntityWithId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithId<ID>, ID> implements CrudService<T, ID>  {
    @Override
    @Transactional
    public T create(T e) {
        if(e.getId() != null && getRepository().existsById(e.getId()))
            throw new IllegalArgumentException();
        return getRepository().save(e);
    }
    @Override
    @Transactional
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    @Transactional
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        if(!getRepository().existsById(id))
            throw new IllegalArgumentException();
        getRepository().deleteById(id);
    }

    protected abstract CrudRepository<T, ID> getRepository();
}
