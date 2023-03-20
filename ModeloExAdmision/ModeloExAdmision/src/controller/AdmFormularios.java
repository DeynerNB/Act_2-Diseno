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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Carrera;
import model.CentroAplicacion;
import model.Configuracion;
import model.DatosExamen;
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
    public void simularAplicacionExamen(){
        
        ArrayList<FormularioSolicitante> formulariosRegistrados = SingletonDAO.getInstance().getTablaFormularios();
        int max = Configuracion.getInstance().getMaximoPuntaje();
        for (FormularioSolicitante formulario : formulariosRegistrados) {
            int random = (int)(Math.random()*(1-10+1)+10);
            if(random == 2) {
                formulario.setEstado(TEstadoSolicitante.AUSENTE);
                formulario.getDetalleExamen().setPuntajeObtenido(0);
            }
            else{
                int rand = (int)(Math.random()*(400-max+1)+max);//nota minima 400, para que existan varios ADMITIDOS
                formulario.getDetalleExamen().setPuntajeObtenido(rand);
            }
        }
    }
    
    public void definirEstadoAdmisionCandidatos(){
        
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
    
    public String getResultadoAdmision(int idSolic) {
        
        FormularioSolicitante formSolc = SingletonDAO.getInstance().consultarFormulario(idSolic);
        if (formSolc == null)
            return "Solicitante no registrado";
        
        TEstadoSolicitante estadoForm = formSolc.getEstado();
        
        // Checkear si el estado del solicitante es permitido
        if (estadoForm == TEstadoSolicitante.ADMITIDO || estadoForm == TEstadoSolicitante.POSTULANTE || estadoForm == TEstadoSolicitante.RECHAZADO) {
            return formSolc.strResultadoCompleto();
        }
        return "Aun no se encuentra disponible";
    }
    
    public ArrayList<FormularioSolicitante> getResultadosFormularios_Solicitante(String CodigoCarrera) {
        ArrayList<FormularioSolicitante> forms = SingletonDAO.getInstance().getTablaFormularios();
        
        for (int i = 0; i < forms.size(); i++) {
            if (!forms.get(i).getCarreraSolic().getCodigo().equals(CodigoCarrera)) {
                forms.remove(i--);
            }
        }
        if (!forms.isEmpty())
            forms.sort((o1, o2) -> Integer.valueOf( o1.getIdSolic()).compareTo(o2.getIdSolic()) );
        
        return forms;
    }
    
    public ArrayList<FormularioSolicitante> getResultadosFormularios_Estado(String CodigoCarrera) {
        ArrayList<FormularioSolicitante> forms = SingletonDAO.getInstance().getTablaFormularios();
        
        for (int i = 0; i < forms.size(); i++) {
            if (!forms.get(i).getCarreraSolic().getCodigo().equals(CodigoCarrera) || forms.get(i).getEstado().equals(TEstadoSolicitante.AUSENTE)) {
                forms.remove(i--);
            }
        }
        
        return forms;
    }
}
