package com.ensa.SprintFlow.mapper;

public interface Mapper<Entity, RequestEntity, ResponseEntity> {

  public ResponseEntity mapToDto(Entity entity);

  public Entity mapToEntity(RequestEntity dto);
}
