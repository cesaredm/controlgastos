/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import view.Menu;
import model.*;
/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Init {
	Menu menu;	
	CuentasModel cuentasModel;
	EgresosModel egresosModel;
	ExportExcel exportExcel;
	CuentasController cuentasController;
	EgresosController egresosController;
	public Init(Menu menu){
		this.menu = menu;
		this.cuentasModel = new CuentasModel();
		this.egresosModel = new EgresosModel();
		this.exportExcel = new ExportExcel();
		this.cuentasController = new CuentasController(menu,cuentasModel);
		this.egresosController = new EgresosController(menu,egresosModel,exportExcel);
	}	

	public void start(){
		this.menu.setLocationRelativeTo(null);
		this.menu.setVisible(true);
	}
}
