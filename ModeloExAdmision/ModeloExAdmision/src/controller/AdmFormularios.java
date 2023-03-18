/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.DAO.SingletonDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import model.Carrera;
import model.CentroAplicacion;
import model.Configuracion;
import model.DatosExamen;
import model.FormularioSolicitante;
import model.TEstadoSolicitante;

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
        
        if (elForm == null && elDTO.getCarreraSolic() != null){
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
    public boolean registrarCitaExamen(int numero, Date fecha, CentroAplicacion lugar){
        
        DatosExamen examen = SingletonDAO.getInstance().consultarFormulario(numero).getDetalleExamen();
        if(examen == null)return false;
       
        examen.setCitaExamen(fecha);
        examen.setLugarExamen(lugar);
        return true;
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
    public void simularAplicacionExamen(){
        //for poniendo los que no vinieron
        //for poniendo puntajes a los que vinieron de acuerdo a ARCH Config Puntage Max
        ArrayList<FormularioSolicitante> formulariosRegistrados = SingletonDAO.getInstance().getTablaFormularios();
        int max = Configuracion.getInstance().getMaximoPuntaje();
        for (FormularioSolicitante formulario : formulariosRegistrados) {
            int random = (int)(Math.random()*(1-10+1)+10);
            if(random == 2) {
                formulario.setEstado(TEstadoSolicitante.AUSENTE);
                formulario.getDetalleExamen().setPuntajeObtenido(0);
            }
            else{
                int rand = (int)(Math.random()*(600-max+1)+max);
                formulario.getDetalleExamen().setPuntajeObtenido(rand);
            }
        }
    }
    
    public void definirEstadoAdmisionCandidatos(){
        
        //for formularios: condiciones de punto 6
        ArrayList<FormularioSolicitante> formulariosRegistrados = SingletonDAO.getInstance().getTablaFormularios();
        Collections.sort(formulariosRegistrados,new RankingNotas());
        HashMap<String, Carrera> mapaCarreras = new HashMap<>();
        
        for (Carrera carrera : SingletonDAO.getInstance().getCarreras()) {
            mapaCarreras.put(carrera.getCodigo()+carrera.getSede().getCodigo(), carrera);
        }
        
        
        for (FormularioSolicitante formulario : formulariosRegistrados) {
            
            if(formulario.getEstado() != TEstadoSolicitante.AUSENTE){
                
                String clave = formulario.getCarreraSolic().getCodigo()+formulario.getCarreraSolic().getSede().getCodigo();
                if(formulario.getDetalleExamen().getPuntajeObtenido()>=mapaCarreras.get(clave).getPuntajeMinimo()
                        && mapaCarreras.get(clave).getMaxAdmision() > 0){
                    formulario.setEstado(TEstadoSolicitante.ADMITIDO);
                    mapaCarreras.get(clave).setMaxAdmision(mapaCarreras.get(clave).getMaxAdmision()-1);
                }
                else if(formulario.getDetalleExamen().getPuntajeObtenido()>=mapaCarreras.get(clave).getPuntajeMinimo())
                    formulario.setEstado(TEstadoSolicitante.POSTULANTE);
                else
                    formulario.setEstado(TEstadoSolicitante.RECHAZADO);
            
            }
            
            
        }
    }
}
