package com.formacion.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.formacion.apirest.entity.Venta;

@Repository
public interface VentasRepository extends JpaRepository<Venta, Long>{

}
