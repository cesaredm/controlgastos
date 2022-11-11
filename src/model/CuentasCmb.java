/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CuentasCmb {
	private int id;
	private String nombre;	

	public CuentasCmb(int id, String nombre){
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString(){
		return this.nombre;
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;	
		}	
		if(getClass() != obj.getClass()){
			return false;	
		}
		final CuentasCmb other = (CuentasCmb) obj;
		if(this.id != other.getId()){
			return false;	
		}
		return true;
	}
}
