/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.CuentasCmb;
import view.Menu;
import model.CuentasModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CuentasController implements ActionListener {

	Menu menu;
	CuentasModel cuentasModel;
	int filaseccionada;

	public CuentasController(Menu menu, CuentasModel cuentasModel) {
		this.menu = menu;
		this.cuentasModel = cuentasModel;
		this.menu.btnActualizar.setEnabled(false);
		this.mostrarCuentasCmb();
		this.mostrar("");
		this.menu.btnGuardarCuenta.addActionListener(this);
		this.menu.btnActualizar.addActionListener(this);
		this.menu.optEditarCuenta.addActionListener(this);
	}

	public void limpiar(boolean limpiar) {
		if (limpiar) {
			this.menu.txtNombreCuenta.setText("");
			this.menu.btnActualizar.setEnabled(false);
			this.menu.btnGuardarCuenta.setEnabled(true);
		}
	}

	public void guardar() {
		this.cuentasModel.setNombre(this.menu.txtNombreCuenta.getText());
		this.cuentasModel.guardar();
		this.limpiar(this.cuentasModel.validar);
		this.mostrar("");
		this.mostrarCuentasCmb();
	}

	public void mostrar(String value) {
		this.cuentasModel.mostrar(value);
		this.menu.tblCuentas.setModel(this.cuentasModel.tableModel);
	}

	public void mostrarCuentasCmb() {
		this.cuentasModel.mostrarCuentasCmb();
		this.menu.cmbCuenta.setModel(this.cuentasModel.comboModel);
	}

	public void editar() {
		this.filaseccionada = this.menu.tblCuentas.getSelectedRow();
		if (this.filaseccionada != -1) {
			this.cuentasModel.setId((Integer.parseInt(this.menu.tblCuentas.getValueAt(filaseccionada, 0).toString())));
			this.cuentasModel.editar();
			this.menu.txtNombreCuenta.setText(this.cuentasModel.getNombre());
			this.menu.btnGuardarCuenta.setEnabled(false);
			this.menu.btnActualizar.setEnabled(true);
		}
	}

	public void actualizar() {
		this.cuentasModel.setNombre(this.menu.txtNombreCuenta.getText());
		this.cuentasModel.actualizar();
		this.limpiar(this.cuentasModel.validar);
		this.mostrar("");
		this.mostrarCuentasCmb();
		this.menu.btnGuardarCuenta.setEnabled(true);
		this.menu.btnActualizar.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "btnGuardarCuenta": {
				this.guardar();
			}
			break;
			case "btnActualizarCuenta":{
				this.actualizar();
			}break;
			case "optEditarCuenta":{
				this.editar();
			}break;
		}
	}
}
