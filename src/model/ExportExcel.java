/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class ExportExcel extends Conexion {

	ResultSet rs;
	Connection cn;
	PreparedStatement pst;
	ResultSetMetaData mdata;
	Statement st;
	String consulta;

	public ExportExcel() {

	}

	public void getData(Date fecha1, Date fecha2) {
		this.cn = conexion();
		this.consulta = "select c.cuenta, DATE_FORMAT(e.fecha,'%d-%M-%y %r') AS fecha,monto,nota from egresos as e inner join cuentas as c on(e.cuenta=c.id) where DATE(fecha) between ? AND ? order by c.cuenta asc";
		try {
			this.st = this.cn.createStatement();
			this.st.execute("SET lc_time_names = 'es_ES'");
			this.pst = this.cn.prepareStatement(this.consulta);
			this.pst.setDate(1, fecha1);
			this.pst.setDate(2, fecha2);
			this.rs = this.pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void crearExcel(String name, Date fecha1, Date fecha2) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Resultado Filtro");
			HSSFRow rowhead = sheet.createRow(0);
			this.getData(fecha1, fecha2);
			this.mdata = this.rs.getMetaData();
			rowhead.createCell(0).setCellValue("Fecha");
			rowhead.createCell(1).setCellValue("Cuenta");
			rowhead.createCell(2).setCellValue("Monto");
			rowhead.createCell(3).setCellValue("Nota");
			int cont = 0;
			while (this.rs.next()) {
				cont++;
				HSSFRow row = sheet.createRow(cont);
				try {
					row.createCell(0, CellType.STRING).setCellValue(this.rs.getString("fecha"));
					row.createCell(1, CellType.STRING).setCellValue(this.rs.getString("cuenta"));
					row.createCell(2, CellType.NUMERIC).setCellValue(this.rs.getFloat("monto"));
					row.createCell(3, CellType.STRING).setCellValue(this.rs.getString("nota"));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			FileOutputStream fileOut = new FileOutputStream("c:\\ExportacionesExcel\\" + name + ".xls");
			wb.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null, "Libro de excel "+ name + " fue creado con exito."
			     + " \n Puede encontrarlo en la siguiente direccion : C:/ExportacionesExcel");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.cn.close();
			} catch (SQLException ex) {
				Logger.getLogger(ExportExcel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
