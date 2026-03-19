package Module2.services;

import Module2.dto.DtoInterface;

import java.util.List;

public interface ServiceInterface<T extends DtoInterface> {
    List<T> getAllEntities();
    T getEntity(long id);
    T createEntity(T dto);
    T updateEntity(long id, T dto);
    T deleteEntity(long id);
}
