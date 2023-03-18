/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.DAO.SingletonDAO;
import java.util.ArrayList;
import java.util.Date;
import model.CentroAplicacion;
import model.FormularioSolicitante;


public class GeneradorCitas {

    UtilitarioComunicacion correo;
    AdmFormularios adm;
    public GeneradorCitas() {
        correo = new UtilitarioComunicacion();
        adm = new AdmFormularios();
    }
    
    public boolean asignarCitasASolicitantes(){
        
        ArrayList<FormularioSolicitante> formulariosRegistrados = SingletonDAO.getInstance().getTablaFormularios();
        for (FormularioSolicitante formulario : formulariosRegistrados) {
            int numero = (int)(Math.random()*(0-2+1)+2);
            CentroAplicacion lugar = SingletonDAO.getInstance().getTablaCentros().get(numero);
            if(!(adm.registrarCitaExamen(formulario.getNumero(), new Date("11/11/11"), lugar))) return false;
            this.notificarFormulario(formulario);
        }
        return true;
    }
    public void notificarFormulario(FormularioSolicitante form){
        String emisor = "admisiones@itcr.ac.cr";
        String destinatario = form.getCorreoSolic();
        String asunto = "Detalles de su cita de admision";
        String cuerpo = form.getDetalleExamen().getLugarExamen().toString() + form.getDetalleExamen().getCitaExamen();
        correo.enviarCorreo( emisor,  destinatario,   asunto,  cuerpo);
        
    }
    
}
