package com.formacion.apirest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.formacion.apirest.entity.Producto;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long>{

}
