/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import model.CuentasCmb;
import view.Menu;
import model.EgresosModel;
import java.util.Date;
import javax.swing.JOptionPane;
import model.ExportExcel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class EgresosController implements ActionListener {

	Menu menu;
	EgresosModel egresosModel;
	CuentasCmb cuentasCmb;
	ExportExcel exportExcel;
	int filaseleccionada;
	DecimalFormat formato;

	public EgresosController(Menu menu, EgresosModel egresosModel, ExportExcel exportExcel) {
		this.menu = menu;
		this.egresosModel = egresosModel;
		this.exportExcel = exportExcel;
		this.formato = new DecimalFormat("###,###,###,#00.00");
		this.menu.jcFecha.setDate(new Date());
		this.menu.jcFechaInicio.setDate(new Date());
		this.menu.jcFecha2.setDate(new Date());
		this.menu.btnActualizarEgreso.setEnabled(false);
		this.mostar("", new Date(), new Date());
		this.menu.btnLimpiar.addActionListener(this);
		this.menu.btnGuardarEgreso.addActionListener(this);
		this.menu.btnBuscarEgresos.addActionListener(this);
		this.menu.btnActualizarEgreso.addActionListener(this);
		this.menu.optEditarEgreso.addActionListener(this);
		this.menu.btnExportar.addActionListener(this);
		this.menu.optEliminarEgreso.addActionListener(this);
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.jcFecha.setDate(new Date());
			this.menu.jsMonto.setValue(0);
			this.menu.cmbCuenta.setSelectedItem(new CuentasCmb(1, null));
			this.menu.txtAreaNota.setText("");
			this.menu.btnGuardarEgreso.setEnabled(true);
			this.menu.btnActualizarEgreso.setEnabled(false);
		}
	}

	public void guardar() {
		this.cuentasCmb = (CuentasCmb) this.menu.cmbCuenta.getSelectedItem();
		this.egresosModel.setFecha(new java.sql.Timestamp(this.menu.jcFecha.getDate().getTime()));
		this.egresosModel.setCuenta(this.cuentasCmb.getId());
		this.egresosModel.setMonto(Float.parseFloat(this.menu.jsMonto.getValue().toString()));
		this.egresosModel.setNota(this.menu.txtAreaNota.getText());
		this.egresosModel.guardar();
		this.limpiar(true);
		this.mostar("", new Date(), new Date());
	}

	public void editar() {
		this.filaseleccionada = this.menu.tblEgresos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			this.egresosModel.setId(Integer.parseInt(this.menu.tblEgresos.getValueAt(filaseleccionada, 0).toString()));
			this.egresosModel.editar();
			this.menu.jcFecha.setDate(this.egresosModel.getFecha());
			this.menu.jsMonto.setValue(this.egresosModel.getMonto());
			this.menu.cmbCuenta.setSelectedItem(new CuentasCmb(this.egresosModel.getCuenta(), null));
			this.menu.txtAreaNota.setText(this.egresosModel.getNota());
			this.menu.btnGuardarEgreso.setEnabled(false);
			this.menu.btnActualizarEgreso.setEnabled(true);
		}
	}

	public void mostar(String value, Date fecha1, Date fecha2) {
		this.egresosModel.setValorBuscar(value);
		this.egresosModel.setFecha1(new java.sql.Date(fecha1.getTime()));
		this.egresosModel.setFecha2(new java.sql.Date(fecha2.getTime()));
		this.egresosModel.mostrarPorCuenta();
		this.menu.tblEgresos.setModel(this.egresosModel.tableModel);
		this.egresosModel.reportesEgresos();
		this.menu.lblTotalEgresos.setText(this.formato.format(this.egresosModel.getTotalEgresos()));
	}

	public void actualizar() {
		this.cuentasCmb = (CuentasCmb) this.menu.cmbCuenta.getSelectedItem();
		this.egresosModel.setFecha(new java.sql.Timestamp(this.menu.jcFecha.getDate().getTime()));
		this.egresosModel.setCuenta(this.cuentasCmb.getId());
		this.egresosModel.setMonto((float) this.menu.jsMonto.getValue());
		this.egresosModel.setNota(this.menu.txtAreaNota.getText());
		this.egresosModel.actualizar();
		this.limpiar(true);
		this.mostar("", new Date(), new Date());
		this.menu.btnGuardarEgreso.setEnabled(true);
		this.menu.btnActualizarEgreso.setEnabled(false);
	}

	public void eliminar() {
		this.filaseleccionada = this.menu.tblEgresos.getSelectedRow();
		if (this.filaseleccionada != -1) {
			int confirmar = JOptionPane.showConfirmDialog(
			     null,
			     "Seguro que quieres eliminar este egreso.?",
			     "Advertencia",
			     JOptionPane.YES_NO_OPTION
			);
			if(confirmar == JOptionPane.YES_OPTION){
				this.egresosModel.setId(Integer.parseInt(this.menu.tblEgresos.getValueAt(this.filaseleccionada, 0).toString()));
				this.egresosModel.eliminar();
				this.mostar("", new Date(), new Date());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnLimpiar": {
				this.limpiar(true);
			}
			break;
			case "btnGuardarEgreso": {
				this.guardar();
			}
			break;
			case "btnActualizarEgreso": {
				this.actualizar();
			}
			break;
			case "btnBuscarEgreso": {
				this.mostar(
				     this.menu.txtBuscarEgreso.getText(),
				     this.menu.jcFechaInicio.getDate(),
				     this.menu.jcFecha2.getDate()
				);
			}
			break;
			case "optEditarEgreso": {
				this.editar();
			}
			break;
			case "optEliminarEgreso": {
				this.eliminar();
			}
			break;
			case "btnExportar": {
				try {
					String name = JOptionPane.showInputDialog(null, "Ingrese el nombre de archivo", "libro1");
					if (!name.equals("")) {
						this.exportExcel.crearExcel(
						     name,
						     new java.sql.Date(this.menu.jcFechaInicio.getDate().getTime()),
						     new java.sql.Date(this.menu.jcFecha2.getDate().getTime())
						);
					}
				} catch (Exception error) {

				}

			}
			break;
		}
	}
}
