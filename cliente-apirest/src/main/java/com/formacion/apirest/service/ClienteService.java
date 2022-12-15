package com.formacion.apirest.service;

import java.util.List;

import com.formacion.apirest.entity.Cliente;
import com.formacion.apirest.entity.Region;

public interface ClienteService {
	
	//metodo para mostrar todos los clientes
	public List<Cliente> mostrarClientes();
	
	//metodo que busque cliente por id
	public Cliente buscarCliente(long id);
	
	//metodo para crear un nuevo cliente
	public Cliente guardarCliente(Cliente cliente);
	
	//metodo para borrar un cliente
	public Cliente borrarCliente(long id);
	
	//metodo para buscar por email
	public Cliente buscarClienteEmail(String email);
	
	//metodo para mostrar regiones
	public List<Region> mostrarRegiones();
	

}
