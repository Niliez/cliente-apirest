package com.formacion.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.formacion.apirest.entity.Cliente;
import com.formacion.apirest.entity.Region;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long>{

	@Query("from Region")
	public List<Region> mostrarRegiones();
	
	public Cliente findByEmail(String email);
	
	@Query("select u from Cliente u where u.email = ?1")
	public Cliente findByEmail2(String email);
}
