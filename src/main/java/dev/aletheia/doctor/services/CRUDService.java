package dev.aletheia.doctor.services;

import dev.aletheia.doctor.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

abstract public class CRUDService<T, R> {
    @Autowired
    private ModelMapper modelMapper;
    private final Class<T> entityClass;
    private final Class<R> dtoClass;

    protected CRUDService(Class<T> entityClass, Class<R> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public T find(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public T findOrFail(Long id) {
        return getRepository().findById(id).orElseThrow(NotFoundException::new);
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public List<R> getAllDTO() {
        return getAll().stream().map(this::convertToDto).toList();
    }

    public T create(T dto) {
        return save(dto);
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(Long id) {
        getRepository().deleteById(id);
    }

    public R convertToDto(T entity) {
        return modelMapper.map(entity,  dtoClass);
    }

    public T getFromDto(R dto) {
        return modelMapper.map(dto, entityClass);
    }

    abstract public JpaRepository<T, Long> getRepository();
}
