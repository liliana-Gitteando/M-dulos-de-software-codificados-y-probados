package com.myweb.mavenproject1.service;

import com.myweb.mavenproject1.entidades.Alertas;
import com.myweb.mavenproject1.dao.AlertasDAO;
import com.myweb.mavenproject1.util.DiasHabilesUtil; 
import java.util.Date;
import java.util.List;

public class AlertasPQRSService {
    
    private AlertasDAO alertasDAO = new AlertasDAO();
    private static final int PLAZO_PQRS = 15; // días hábiles
    
  
    private void validarParametrosPQRS(Long documentoId, Long usuarioId, Date fechaRadica) throws Exception {
        if (documentoId == null || documentoId <= 0) {
            throw new Exception("documentoId es obligatorio y debe ser válido");
        }
        if (usuarioId == null || usuarioId <= 0) {
            throw new Exception("usuarioId es obligatorio y debe ser válido");
        }
        if (fechaRadica == null) {
            throw new Exception("fechaRadica no puede ser nula");
        }
    }
      public void evaluarVencimientoPQRS(Long documentoId, Long usuarioId, Date fechaRadica) {
        
        // Validar entrada
        try {
            validarParametrosPQRS(documentoId, usuarioId, fechaRadica);
        } catch (Exception e) {
            System.err.println("Error de validación: " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        // Calcular fecha límite (15 días hábiles desde radicación)
        Date fechaLimite = DiasHabilesUtil.sumarDiasHabiles(fechaRadica, PLAZO_PQRS);
        
        // Calcular días restantes hoy
        int diasRestantes = DiasHabilesUtil.calcularDiasHabiles(new Date(), fechaLimite);
        
        Alertas alerta = new Alertas();
        alerta.setDocumentoId(documentoId);
        alerta.setUsuarioId(usuarioId);  
        alerta.setFechaLimiteRespuesta(fechaLimite);
        alerta.setDiasRestantes(diasRestantes);  
        alerta.setEsPqrs(true); 
        
        if (diasRestantes < 0) {
            // VENCIDO
            alerta.setTipoAlerta("vencido");
            alerta.setDescripcion("PQRS vencido hace " + Math.abs(diasRestantes) + " días hábiles");
        } else if (diasRestantes <= 3) {
            // POR VENCER (alerta temprana)
            alerta.setTipoAlerta("por_vencer");
            alerta.setDescripcion("Quedan " + diasRestantes + " días hábiles para responder");
        } else {
            // Dentro del plazo, no requiere alerta urgente
            alerta.setTipoAlerta("en_plazo");
            alerta.setDescripcion("PQRS dentro del plazo legal");
        }
        
        alerta.setEstado("pendiente");
        alerta.setFechaCreacion(new Date());
        
        
        try {

            alertasDAO.guardar(alerta);

        } catch (Exception e) {

            System.out.println("Error al guardar alerta PQRS");

            e.printStackTrace();
        } 
            }
    
    // Listar alertas de un usuario 
    public List<Alertas> listarAlertasUsuario(Long usuarioId) {
        return alertasDAO.listarAlertasPorUsuario(usuarioId);
    }
}