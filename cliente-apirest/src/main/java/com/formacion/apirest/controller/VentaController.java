package com.formacion.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.apirest.entity.Cliente;
import com.formacion.apirest.entity.Venta;
import com.formacion.apirest.service.VentaService;

@RestController
@RequestMapping("api")
public class VentaController {
	
	
	@Autowired
	private VentaService servicio;
	
	@GetMapping("ventas")
	public List<Venta> index(){
		return servicio.mostrarVentas();
	}
	
	@PostMapping("ventas")
	public ResponseEntity<?> save(@RequestBody Venta venta) {
		Venta ventaNew = null;
		Map<String,Object> response =new HashMap<>();
		
		try {
			ventaNew = servicio.guardarVenta(venta);
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar insert en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El cliente ha sido creado con éxito!");
		response.put("cliente",ventaNew);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

}
