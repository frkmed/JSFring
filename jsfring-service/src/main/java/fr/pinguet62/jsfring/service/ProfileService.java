package fr.pinguet62.jsfring.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.SearchResults;
import com.mysema.query.jpa.impl.JPAQuery;

import fr.pinguet62.jsfring.model.sql.Profile;
import fr.pinguet62.jsfring.service.config.CacheConfig;

/** The service for {@link Profile}. */
@Service
public class ProfileService extends AbstractService<Profile, Integer> {

    @Override
    @CacheEvict(value = CacheConfig.PROFILE_CACHE, allEntries = true)
    @Transactional
    public Profile create(Profile object) {
        return dao.save(object);
    }

    @Override
    @CacheEvict(value = CacheConfig.PROFILE_CACHE, allEntries = true)
    @Transactional
    public void delete(Profile object) {
        dao.delete(object);
    }

    @Override
    @Cacheable(CacheConfig.PROFILE_CACHE)
    @Transactional(readOnly = true)
    public List<Profile> find(JPAQuery query) {
        return dao.find(query);
    }

    @Override
    @Cacheable(CacheConfig.PROFILE_CACHE)
    @Transactional(readOnly = true)
    public SearchResults<Profile> findPanginated(JPAQuery query) {
        return dao.findPaginated(query);
    }

    @Override
    @Cacheable(CacheConfig.PROFILE_CACHE)
    @Transactional(readOnly = true)
    public Profile get(Integer id) {
        return super.get(id);
    }

    @Override
    @Cacheable(CacheConfig.PROFILE_CACHE)
    @Transactional(readOnly = true)
    public List<Profile> getAll() {
        return super.getAll();
    }

    @Override
    @CacheEvict(value = CacheConfig.PROFILE_CACHE, allEntries = true)
    @Transactional
    public Profile update(Profile object) {
        return dao.save(object);
    }

}