package com.formacion.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacion.apirest.entity.Venta;
import com.formacion.apirest.repository.VentasRepository;


@Service
public class VentaServiceImpl implements VentaService{

	
	@Autowired
	private VentasRepository repositorio;
	
	@Override
	public List<Venta> mostrarVentas() {
		return repositorio.findAll();
	}

	@Override
	public Venta buscarVenta(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Venta guardarVenta(Venta venta) {
		// TODO Auto-generated method stub
		return repositorio.save(venta);
	}

	@Override
	public Venta borrarVenta(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
