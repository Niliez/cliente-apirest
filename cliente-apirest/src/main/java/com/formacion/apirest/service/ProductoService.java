package com.formacion.apirest.service;

import java.util.List;

import com.formacion.apirest.entity.Producto;

public interface ProductoService {
	//metodo para mostrar todos los productos
		public List<Producto> mostrarProductos();
		
		//metodo que busque producto por id
		public Producto buscarProducto(long id);
		
		//metodo para crear un nuevo producto
		public Producto guardarProducto(Producto producto);
		
		//metodo para borrar un producto
		public Producto borrarProducto(long id);
}
