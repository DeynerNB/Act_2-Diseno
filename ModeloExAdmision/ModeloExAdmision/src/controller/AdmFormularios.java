/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.DAO.SingletonDAO;
import java.util.ArrayList;
import model.FormularioSolicitante;

import model.TEstadoSolicitante;

/**
 *
 * @author ersolano
 */
public class AdmFormularios {

    public AdmFormularios() {
    }
   
    public boolean registrarFormulario (DTOFormulario elDTO){
        
        // verifica que el solicitante no haya registrado otro anteriormente
        FormularioSolicitante elForm = SingletonDAO.getInstance().consultarFormulario (elDTO.getIdSolic());
        
        if (elForm == null){
            elForm = new FormularioSolicitante();
            elForm.setIdSolic(elDTO.getIdSolic());
            elForm.setNombreSolic(elDTO.getNombreSolic());
            elForm.setCorreoSolic(elDTO.getCorreoSolic());
            elForm.setCelularSolic(elDTO.getCelularSolic());
            elForm.setColegioSolic(elDTO.getColegioSolic());
            elForm.setDirSolicPCD(elDTO.getDirSolic());
            elForm.setDetalleDirSolic(elDTO.getDetalleDireccion());
            elForm.setCarreraSolic(elDTO.getCarreraSolic());
            
            boolean res= SingletonDAO.getInstance().agregarFormulario(elForm);
            return res;
        }
        return false;
    }
    
    public FormularioSolicitante consultarFormulario (int idSolic){
        return SingletonDAO.getInstance().consultarFormulario(idSolic);
    }
    
    public FormularioSolicitante getResultadoAdmision(int idSolic) {
        
        FormularioSolicitante formSolc = SingletonDAO.getInstance().consultarFormulario(idSolic);
        TEstadoSolicitante estadoForm = formSolc.getEstado();
        
        // Checkear si el estado del solicitante cambio
        if (estadoForm == TEstadoSolicitante.ADMITIDO || estadoForm == TEstadoSolicitante.POSTULANTE || estadoForm == TEstadoSolicitante.RECHAZADO) {
            return formSolc;
        }
        return null;
    }
    
    public ArrayList<FormularioSolicitante> getFormulariosPorCarrera(String nomCarrera) {
        ArrayList<FormularioSolicitante> forms = new ArrayList();
        ArrayList<FormularioSolicitante> tablaFormularios = SingletonDAO.getInstance().getFormularios();
        
        for (FormularioSolicitante form : tablaFormularios) {
            if (form.getCarreraSolic().getNombre().equals(nomCarrera)) {
                forms.add(form);
            }
        }
        return forms;
    }
}
