package com.formacion.apirest.service;

import java.util.List;

import com.formacion.apirest.entity.Cliente;
import com.formacion.apirest.entity.Venta;

public interface VentaService {

	
	
		public List<Venta> mostrarVentas();
		
		
		public Venta buscarVenta(long id);
		
		
		public Venta guardarVenta(Venta venta);
		
	
		public Venta borrarVenta(long id);
		
}
