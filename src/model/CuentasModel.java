/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CuentasModel extends Conexion {

	private int id;
	private String nombre;
	String[] datos;
	public boolean validar;

	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	Connection cn;
	String consulta;
	public DefaultTableModel tableModel;
	public DefaultComboBoxModel comboModel;

	public CuentasModel() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void validar() {
		if (this.nombre.equals("")) {
			this.validar = false;
		} else {
			this.validar = true;
		}
	}

	public void guardar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "INSERT INTO cuentas(cuenta) VALUES(?)";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombre);
				if(this.pst.executeUpdate()>0){
					JOptionPane.showMessageDialog(null, "Cuenta agregada con exito.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(CuentasModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM cuentas WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.nombre = this.rs.getString("cuenta");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CuentasModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.validar();
		if (this.validar) {
			this.cn = conexion();
			this.consulta = "UPDATE cuentas SET cuenta = ? WHERE id = ?";
			try {
				this.pst = this.cn.prepareStatement(this.consulta);
				this.pst.setString(1, this.nombre);
				this.pst.setInt(2, this.id);
				if(this.pst.executeUpdate()>0){
					JOptionPane.showMessageDialog(null, "Cuenta actualizada con exito.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					this.cn.close();
				} catch (SQLException ex) {
					Logger.getLogger(CuentasModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	public void mostrar(String value) {
		this.cn = conexion();
		this.consulta = "SELECT * FROM cuentas WHERE cuenta LIKE ?";
		this.datos = new String[2];
		String[] titulos = {"ID", "CUENTA"};
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + value + "%");
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("cuenta");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CuentasModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrarCuentasCmb() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM cuentas ORDER BY cuenta ASC";
		this.comboModel = new DefaultComboBoxModel();
		try {
			this.st = this.cn.createStatement();
			this.rs = this.st.executeQuery(this.consulta);
			while (this.rs.next()) {
				this.comboModel.addElement(new CuentasCmb(this.rs.getInt("id"), this.rs.getString("cuenta")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(CuentasModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
