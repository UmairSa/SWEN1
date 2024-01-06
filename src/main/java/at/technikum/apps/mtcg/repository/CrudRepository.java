package at.technikum.apps.mtcg.repository;

import java.util.List;

import java.util.Optional;

public interface CrudRepository <T, ID> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    T update(T entity);
    void deleteById(ID id);
}
