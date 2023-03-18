/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.Comparator;
import model.FormularioSolicitante;


public class RankingNotas implements Comparator<FormularioSolicitante> {
    @Override
    public int compare(FormularioSolicitante f1, FormularioSolicitante f2) {
        return f2.getDetalleExamen().getPuntajeObtenido() - f1.getDetalleExamen().getPuntajeObtenido();
    }
}
