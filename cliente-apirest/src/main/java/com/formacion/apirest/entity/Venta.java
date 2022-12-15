package com.formacion.apirest.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="ventas")
public class Venta implements Serializable{
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private double iva;
	private double subtotal;
	private double total;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private List<Producto> producto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	private Cliente cliente;
	
	
	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public double getIva() {
		return iva;
	}




	public void setIva(double iva) {
		this.iva = iva;
	}




	public double getSubtotal() {
		return subtotal;
	}




	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}




	public double getTotal() {
		return total;
	}




	public void setTotal(double total) {
		this.total = total;
	}


	


	public List<Producto> getProducto() {
		return producto;
	}




	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}




	public Cliente getCliente() {
		return cliente;
	}




	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
