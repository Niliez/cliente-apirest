package com.formacion.apirest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.apirest.entity.Producto;
import com.formacion.apirest.service.ProductoService;

@RestController
@RequestMapping("api")
public class ProductoController {
	
	
		//inyectamos el servicio
		@Autowired
		private ProductoService servicio; 
		
		@GetMapping("productos/")	//define peticion http get
		public List<Producto> index(){
			return servicio.mostrarProductos();
		}
		
		@GetMapping("productos/{id}")
		public ResponseEntity<?> show(@PathVariable long id) {
			Producto producto = null;
			Map<String,Object> response =new HashMap<>();
			
			try {
				producto = servicio.buscarProducto(id);
				
				if(producto == null) {
					response.put("mensaje", "El producto con ID: "+id+" no existe en la base de datos");
					return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
				}
			} catch (DataAccessException e) {
				//si hay error desde la base de datos
				response.put("mensaje","Error al realizar consulta en la base de datos");
				response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
					
			return new ResponseEntity<Producto>(producto,HttpStatus.OK);
		}
		

		@PostMapping("productos") //Guardamos un producto
		public ResponseEntity<?> save(@RequestBody Producto producto) {
			Producto productoNew = null;
			Map<String,Object> response =new HashMap<>();
			
			try {
				productoNew = servicio.guardarProducto(producto);
			} catch (DataAccessException e) {
				//si hay error desde la base de datos
				response.put("mensaje","Error al realizar insert en la base de datos");
				response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("mensaje","El producto ha sido creado con éxito!");
			response.put("producto",productoNew);
			
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
		}
		
		
		@PutMapping("productos/{id}")
		public ResponseEntity<?> update(@PathVariable long id,@RequestBody Producto producto) {
			Producto productoUpdate = null;
			Map<String,Object> response =new HashMap<>();
			
			
			
			
			
			try {
				
				productoUpdate = servicio.buscarProducto(id);
				if(productoUpdate != null) {
					productoUpdate.setNombre(producto.getNombre());
				}else {
					response.put("mensaje","Error: no se puede editar, el producto con ID: "+id+" no existe en la base de datos");	
					return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
				}
				
				servicio.guardarProducto(productoUpdate);
				
			} catch (DataAccessException e) {
				//si hay error desde la base de datos
				response.put("mensaje","Error al realizar update en la base de datos");
				response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			response.put("mensaje","El producto ha sido actualizado con éxito!");
			response.put("producto",productoUpdate);
			
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
		}
		
		@DeleteMapping("productos/{id}")
		public ResponseEntity<?> delete(@PathVariable long id) {
			Map<String,Object> response =new HashMap<>();
			Producto productoDelete = null;
			
			try {
				productoDelete = servicio.borrarProducto(id);
				
			} catch (DataAccessException e) {
				//si hay error desde la base de datos
				response.put("mensaje","Error al realizar delete en la base de datos");
				response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("mensaje","El producto ha sido eliminado con éxito!");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
			
		}
}
