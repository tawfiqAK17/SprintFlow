package com.ensa.SprintFlow.mapper;

public interface Mapper<T, U> {

  public U mapToDto(T entity);

  public T mapToEntity(U dto);
}
