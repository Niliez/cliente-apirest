package com.formacion.apirest.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacion.apirest.entity.Cliente;
import com.formacion.apirest.entity.Region;
import com.formacion.apirest.service.ClienteService;

@RestController
@RequestMapping("api")
@CrossOrigin(origins="http://localhost:4200/")
public class ClienteController {
	
	//inyectamos el servicio
	@Autowired
	private ClienteService servicio; 
	
	@GetMapping("clientes")//define peticion http get
	//@RequestMapping(method = RequestMethod.GET,path = "clientes")
	public List<Cliente> index(){
		return servicio.mostrarClientes();
	}
	
	/*@GetMapping("clientes/{id}")
	public Cliente show(@PathVariable long id) {
		return servicio.buscarCliente(id);
	}*/
	@GetMapping("clientes/{id}")
	public ResponseEntity<?> show(@PathVariable long id) {
		Cliente cliente = null;
		Map<String,Object> response =new HashMap<>();
		
		try {
			cliente = servicio.buscarCliente(id);
			
			if(cliente == null) {
				response.put("mensaje", "El cliente con ID: "+id+" no existe en la base de datos");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar consulta en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
				
		return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
	}
	
	/*@PostMapping("clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente save(@RequestBody Cliente cliente) {
		return servicio.guardarCliente(cliente);
	}*/
	@PostMapping("clientes")
	public ResponseEntity<?> save(@RequestBody Cliente cliente) {
		Cliente clienteNew = null;
		Map<String,Object> response =new HashMap<>();
		
		try {
			clienteNew = servicio.guardarCliente(cliente);
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar insert en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El cliente ha sido creado con éxito!");
		response.put("cliente",clienteNew);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	
	
	
	/*@PutMapping("clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente update(@PathVariable long id,@RequestBody Cliente cliente) {
		Cliente clienteUpdate = servicio.buscarCliente(id);
		
		clienteUpdate.setNombre(cliente.getNombre());
		clienteUpdate.setApellido(cliente.getApellido());
		clienteUpdate.setEmail(cliente.getEmail());
		clienteUpdate.setTelefono(cliente.getTelefono());
		clienteUpdate.setCreateAt(cliente.getCreateAt());
		
		return servicio.guardarCliente(clienteUpdate);
	}*/
	
	@PutMapping("clientes/{id}")
	public ResponseEntity<?> update(@PathVariable long id,@RequestBody Cliente cliente) {
		Cliente clienteUpdate = null;
		Map<String,Object> response =new HashMap<>();
		
		try {
			
			clienteUpdate = servicio.buscarCliente(id);
			if(clienteUpdate != null) {
				clienteUpdate.setNombre(cliente.getNombre());
				clienteUpdate.setApellido(cliente.getApellido());
				clienteUpdate.setEmail(cliente.getEmail());
				clienteUpdate.setTelefono(cliente.getTelefono());
				clienteUpdate.setCreateAt(cliente.getCreateAt());
				clienteUpdate.setRegion(cliente.getRegion());
			}else {
				response.put("mensaje","Error: no se puede editar, el cliente con ID: "+id+" no existe en la base de datos");	
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			
			servicio.guardarCliente(clienteUpdate);
			
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar update en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		response.put("mensaje","El cliente ha sido actualizado con éxito!");
		response.put("cliente",clienteUpdate);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	
	
	
	//ejercicio, agregar manejo de errores en este metodo
	/*@DeleteMapping("clientes/{id}")
	public Cliente delete(@PathVariable long id) {
		return servicio.borrarCliente(id);
		
	}*/
	@DeleteMapping("clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable long id) {
		Map<String,Object> response =new HashMap<>();
		Cliente clienteDelete = null;
		
		try {
			
			clienteDelete = servicio.borrarCliente(id);
			
			String nombreImagenAnterior= clienteDelete.getImagen();
			
			if( nombreImagenAnterior != null && nombreImagenAnterior.length()>0) {
				Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
				File archivoImagenAnterior = rutaImagenAnterior.toFile();
				
				if( archivoImagenAnterior.exists() && archivoImagenAnterior.canRead() ) {
					archivoImagenAnterior.delete();
				}
			}
			
			
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar delete en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","El cliente ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		
	}
	
	
	
	@PostMapping("clientes/uploads")
	public ResponseEntity<?> uploadImage(@RequestParam("archivo") MultipartFile archivo,@RequestParam("id") long id ){
		Map<String,Object> response = new HashMap<>();
		
		Cliente cliente = null; 
		String nombreArchivo=""; 
		
		try {
			cliente = servicio.buscarCliente(id);
			
			
			if( !archivo.isEmpty() && cliente != null) {
				
				
				String nombreImagenAnterior= cliente.getImagen();
				
				if( nombreImagenAnterior != null && nombreImagenAnterior.length()>0) {
					Path rutaImagenAnterior = Paths.get("uploads").resolve(nombreImagenAnterior).toAbsolutePath();
					File archivoImagenAnterior = rutaImagenAnterior.toFile();
					
					if( archivoImagenAnterior.exists() && archivoImagenAnterior.canRead() ) {
						archivoImagenAnterior.delete();
					}
				}
				
				
				
				//nombreArchivo = archivo.getOriginalFilename();//nombrar al archivo
				nombreArchivo = UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ", "");
				Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();//path para guardar el archivo
				
				
				try {
					
					Files.copy(archivo.getInputStream(), rutaArchivo);
				} catch (IOException e) {
					response.put("mensaje","Error al subir la imagen del cliente");
					response.put("error: ", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
					return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				}
					
			}
			
			
			
		} catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar consulta cliente por id en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		cliente.setImagen(nombreArchivo);
		
		try {
			servicio.guardarCliente(cliente);
		}catch (DataAccessException e) {
			//si hay error desde la base de datos
			response.put("mensaje","Error al realizar guardar cliente en la base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	
		
		response.put("cliente",cliente);
		response.put("mensaje","Archivo subido correctamente: "+nombreArchivo);
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	

	@GetMapping("clientes/img/{nombreImagen:.+}")
	public ResponseEntity<Resource> showImage(@PathVariable String nombreImagen){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
		Resource recurso = null;
		
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if( !recurso.exists() && !recurso.isReadable() ) {
			throw new RuntimeException("Error: no se puede cargar la imagen "+nombreImagen);
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+recurso.getFilename()+"\"");
		
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}

	
	@GetMapping("clientes/regiones")
	public List<Region> showRegions(){
		return servicio.mostrarRegiones();
	}
	
	@GetMapping("clientes/email/{email}")
	public ResponseEntity<?> showClientEmail(@PathVariable String email) {
		Map<String,Object> response =new HashMap<>();
		Cliente clienteEmail = null;
		
		try {
			
			clienteEmail = servicio.buscarClienteEmail(email);
			
			if(clienteEmail == null) {
				response.put("mensaje","Error el email "+email+" no existe en la base de datos");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (DataAccessException e) {
			response.put("mensaje","Error al realizar consulta por email a cliente");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		return new ResponseEntity<Cliente>(clienteEmail,HttpStatus.OK);
	}
	
}
