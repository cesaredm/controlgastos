/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class EgresosModel extends Conexion {

	private int id, cuenta;
	private String nota;
	private Timestamp fecha;
	private float monto, totalEgresos;
	private String valorBuscar;
	private Date fecha1, fecha2;
	PreparedStatement pst;
	Statement st;
	ResultSet rs;
	Connection cn;
	int banderin;
	public boolean validar;
	String consulta;
	String[] datos;
	public DefaultTableModel tableModel;

	public EgresosModel() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCuenta() {
		return cuenta;
	}

	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public String getValorBuscar() {
		return valorBuscar;
	}

	public void setValorBuscar(String valorBuscar) {
		this.valorBuscar = valorBuscar;
	}

	public Date getFecha1() {
		return fecha1;
	}

	public void setFecha1(Date fecha1) {
		this.fecha1 = fecha1;
	}

	public Date getFecha2() {
		return fecha2;
	}

	public void setFecha2(Date fecha2) {
		this.fecha2 = fecha2;
	}

	public float getTotalEgresos() {
		return totalEgresos;
	}

	public void setTotalEgresos(float totalEgresos) {
		this.totalEgresos = totalEgresos;
	}

	public void validar() {
	}

	public void guardar() {
		this.cn = conexion();
		this.consulta = "INSERT INTO egresos(fecha,monto,cuenta,nota) VALUES(?,?,?,?)";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setTimestamp(1, this.fecha);
			this.pst.setFloat(2, this.monto);
			this.pst.setInt(3, this.cuenta);
			this.pst.setString(4, this.nota);
			this.banderin = this.pst.executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar guardar gasto. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void editar() {
		this.cn = conexion();
		this.consulta = "SELECT * FROM egresos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, this.id);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.fecha = this.rs.getTimestamp("fecha");
				this.cuenta = this.rs.getInt("cuenta");
				this.monto = this.rs.getFloat("monto");
				this.nota = this.rs.getString("nota");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void eliminar(){
		this.cn = conexion();
		this.consulta = "DELETE FROM egresos WHERE id = ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setInt(1, id);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin > 0){
				JOptionPane.showMessageDialog(null, "Egreso eliminado con exito.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void actualizar() {
		this.cn = conexion();
		this.consulta = "UPDATE egresos SET fecha = ?,monto=?,cuenta =?,nota=? WHERE id =?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setTimestamp(1, this.fecha);
			this.pst.setFloat(2, this.monto);
			this.pst.setInt(3, this.cuenta);
			this.pst.setString(4, this.nota);
			this.pst.setInt(5, this.id);
			this.banderin = this.pst.executeUpdate();
			if(this.banderin>0){
				JOptionPane.showMessageDialog(null, "Movimiento actualizado con exito.");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Oops.. ocurrio un error al intentar guardar gasto. -> " + e);
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void mostrarPorCuenta() {
		this.cn = conexion();
		this.consulta = "SELECT g.id,c.cuenta,g.monto,g.nota,DATE_FORMAT(g.fecha,'%a, %d-%b-%y %r') AS fecha FROM egresos AS g INNER JOIN"
			+ " cuentas AS c ON(g.cuenta=c.id) WHERE c.cuenta LIKE ? AND DATE(g.fecha) BETWEEN ? AND ?"
			+ " ORDER BY c.cuenta ASC LIMIT 200";
		String[] titulos = {"ID", "CUENTA", "MONTO", "NOTA", "FECHA Y HORA"};
		this.datos = new String[5];
		this.tableModel = new DefaultTableModel(null, titulos) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		try {
			this.st = this.cn.createStatement();
			this.st.execute("SET lc_time_names = 'es_ES'");
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setString(1, "%" + this.valorBuscar + "%");
			this.pst.setDate(2, fecha1);
			this.pst.setDate(3, fecha2);
			this.rs = this.pst.executeQuery();
			while (this.rs.next()) {
				this.datos[0] = this.rs.getString("id");
				this.datos[1] = this.rs.getString("cuenta");
				this.datos[2] = this.rs.getString("monto");
				this.datos[3] = this.rs.getString("nota");
				this.datos[4] = this.rs.getString("fecha");
				this.tableModel.addRow(datos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void reportesEgresos(){
		this.cn = conexion();
		this.consulta = "SELECT SUM(monto) AS total FROM egresos WHERE fecha BETWEEN ? AND ?";
		try {
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setDate(1,this.fecha1);
			this.pst.setDate(2,this.fecha2);
			this.rs = this.pst.executeQuery();
			while(this.rs.next()){
				this.totalEgresos = this.rs.getFloat("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(EgresosModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
