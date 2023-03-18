/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.DAO.SingletonDAO;
import java.util.ArrayList;
import java.util.List;
import model.Carrera;
import model.Configuracion;
import model.FormularioSolicitante;

/**
 *
 * @author ersolano
 */
public class Controlador {
    
    private AdmConfiguracion admConfig = new AdmConfiguracion();
    private AdmCarreras admCarreras = new AdmCarreras();
    private AdmFormularios admFormularios = new AdmFormularios();
    
    public Controlador() {
    }
    
    public boolean editarPuntajeGeneralAdmision(int nuevoValor){
        return admConfig.editarPuntajeAdmision(nuevoValor);
    }

    public int getPuntajeGeneralAdmision() {
        return admConfig.getPuntajeAdmision();
    }
    
    public boolean guardarConfiguracion(){
        return admConfig.guardarConfiguracion();
    }
    
    public List<Carrera> getCarreras(){
        return admCarreras.getCarreras();
    }
      
    public List<Carrera> getCarrerasPorSede(String sede){
        return admCarreras.getCarreras(sede);
    }
    
    public boolean editarCapacidadAdmision(String codigoCarrera, String codigoSede, int capacidad){
        return admCarreras.editarCarrera(codigoCarrera, codigoSede, capacidad);                
    }
    
    public boolean editarPuntajeMinimoAdmision(String codigoCarrera, String codigoSede, int puntaje){
        return admCarreras.editarCarrera(puntaje, codigoCarrera, codigoSede);                
    }

//    public Object getParam(String param, Class elTipo) {
//        if (Integer.class.equals(elTipo)) {
//            return Configuracion.getInstance().getParam(param, Integer.class);
//        }
//        if (String.class.equals(elTipo)) {
//            return Configuracion.getInstance().getParam(param, String.class);
//        }
//        if (Double.class.equals(elTipo)) {
//            return Configuracion.getInstance().getParam(param, Double.class);
//        }
//        return null;
//
//    }

    public boolean registrarFormulario(DTOFormulario elDTO) {
        // se hace cualquier otra operación que se pudiera requerir 
        return admFormularios.registrarFormulario(elDTO);
    }
    
    public FormularioSolicitante getFormulario(int idSolic){
        return admFormularios.consultarFormulario(idSolic);
    }
    //generrarCitas()
    //llama al generar citas del Generador de Citas.
    //recorrer todos los formularios, a los datos del examen Setear atributos. Enviar correo
    //poner algunos Ausentes; y el rsto Candidato
    private boolean generarCitas(){
        
        GeneradorCitas citas = new GeneradorCitas();
        return citas.asignarCitasASolicitantes();
    }
    
    public String getResultadoAdmision(int idSolic) {
        return admFormularios.getResultadoAdmision(idSolic);
    }
    
    public ArrayList<FormularioSolicitante> getFormulariosPorCarrera(boolean Ordenado) {
        return admFormularios.getResultadosFormularios(Ordenado);
    }
    
    //simular aiplicacion()
    //llamr al sumular del ADM formularios:
    //todos los formularios NO ausentes , en datos de xmanen poner putaje.
    //el puntje es de acuerdo al MAX establecido por la U
    public void simularAplicacionExamen(){
        this.generarCitas();
        
        admFormularios.simularAplicacionExamen();
    }
    
    //definir situacion()
    //llamr al definir situacio del ADM formularios:
    //todos los formularios de acuerdo a la Carrera y sede se hace un ranking decendente
    //y de acuerdo ala Capacidad establecida. Se determinan los estados de aacuerdo al 
    //punto 6
    
    public void definirSituacionCandidatos(){
        
        admFormularios.definirEstadoAdmisionCandidatos();
    }
}
