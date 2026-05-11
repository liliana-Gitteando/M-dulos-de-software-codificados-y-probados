package com.myweb.mavenproject1;

import com.myweb.mavenproject1.dao.DocumentoDAO;
import com.myweb.mavenproject1.dao.LoginUsuarioDAO;
import com.myweb.mavenproject1.entidades.Documento;
import com.myweb.mavenproject1.entidades.LoginUsuario;
import com.myweb.mavenproject1.dao.ReporteDAO;
import com.myweb.mavenproject1.entidades.Reporte;
import com.myweb.mavenproject1.dao.AlertasDAO;
import com.myweb.mavenproject1.entidades.Alertas;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // DOCUMENTOS

        DocumentoDAO dao = new DocumentoDAO();

        Documento doc = new Documento();
        
        doc.setTipoDocumento("S"); 
        doc.setAsunto("Invitación desayuno celebración día de las madres");
        doc.setRemitente("Generatión Z sistematization S.A.");
        doc.setDestinatario("Herramientas y productos Ltda.");
        doc.setFechaRadicacion(new Date());
        doc.setFechaVencimiento(new Date());
        doc.setEstado("enviado");
        doc.setUsuarioId(1);
        doc.setDependencia("Dirección general");
        doc.setObservaciones("");
        doc.setFechaCreacion(new Date());

        dao.guardar(doc);

        List<Documento> documentos = dao.listar();

        for (Documento d : documentos) {
            System.out.println("ID: " + d.getId());
            System.out.println("Radicado: " + d.getNumeroRadicado());
            System.out.println("Asunto: " + d.getAsunto());
            System.out.println("------------------------");
        }

        // LOGIN

        LoginUsuarioDAO daoUsuario = new LoginUsuarioDAO();

        LoginUsuario user = new LoginUsuario();
        user.setNombre("Roberto Montes");
        user.setContraseña("2327690");
        user.setRol("Funcionario2");

        // daoUsuario.guardar(user);

        List<LoginUsuario> usuarios = daoUsuario.listar();

        System.out.println("LISTA USUARIOS");

        for (LoginUsuario u : usuarios) {
            System.out.println("ID: " + u.getId());
            System.out.println("Nombre: " + u.getNombre());
            System.out.println("Rol: " + u.getRol());
            System.out.println("------------------------");
        }

        for (LoginUsuario u : usuarios) {
            if (u.getId() == 8) {

                u.setNombre("Samir Cardona Actualizado");
                u.setContraseña("6748599");
                u.setRol("Funcionario3");

                daoUsuario.actualizar(u);

                System.out.println("Usuario actualizado correctamente");
                break;
            }
        }

        for (LoginUsuario u : usuarios) {
            if (u.getId() == 9 || u.getId() == 10 || u.getId() == 11) {
                daoUsuario.eliminar(u.getId());
                System.out.println("Usuario eliminado correctamente ID: " + u.getId());
            }
        }

        // REPORTES

        System.out.println("\n\n PRUEBAS DE REPORTES");

        ReporteDAO daoReporte = new ReporteDAO();

        System.out.println("\n--- Listando Reportes ---");
        List<Reporte> reportes = daoReporte.listar();

        if (reportes != null) {
            System.out.println("Total de reportes: " + reportes.size());
            for (Reporte rep : reportes) {
                System.out.println("ID: " + rep.getId() +
                        " | Tipo: " + rep.getTipoReporte() +
                        " | Usuario: " + rep.getUsuarioId() +
                        " | Descripción: " + rep.getDescripcion());
            }
        } else {
            System.out.println("Error al listar reportes");
        }

        System.out.println("\n--- Obteniendo Reporte por ID ---");
        Reporte reporteUnico = daoReporte.obtenerPorId(1);
        if (reporteUnico != null) {
            System.out.println("Reporte encontrado:");
            System.out.println("  ID: " + reporteUnico.getId());
            System.out.println("  Tipo: " + reporteUnico.getTipoReporte());
            System.out.println("  Descripción: " + reporteUnico.getDescripcion());
            System.out.println("  Formato: " + reporteUnico.getFormato());
            System.out.println("  Ruta: " + reporteUnico.getRutaArchivo());
        } else {
            System.out.println("Reporte no encontrado");
        }

        System.out.println("\n Creando Nuevo Reporte");
        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setUsuarioId(2);
        nuevoReporte.setTipoReporte("Reporte de Prueba");
        nuevoReporte.setDescripcion("Este es un reporte de prueba creado desde NetBeans");
        nuevoReporte.setFechaGeneracion(new Date());
        nuevoReporte.setFormato("PDF");
        nuevoReporte.setRutaArchivo("/reportes/prueba_netbeans.pdf");

        daoReporte.guardar(nuevoReporte);
        System.out.println("Nuevo reporte guardado exitosamente");

        System.out.println("\n Listando Reportes Nuevamente");
        List<Reporte> reportesActualizados = daoReporte.listar();

        if (reportesActualizados != null) {
            System.out.println("Total de reportes ahora: " + reportesActualizados.size());
            for (Reporte rep : reportesActualizados) {
                System.out.println("  ID: " + rep.getId() + " | Tipo: " + rep.getTipoReporte());
            }
        }

        // ALERTAS

        System.out.println("\n--- LISTANDO ALERTAS ---");

        AlertasDAO daoAlertas = new AlertasDAO();
        List<Alertas> alertas = daoAlertas.listarAlertas();

        System.out.println("Total de alertas: " + alertas.size());

        for (Alertas a : alertas) {
            System.out.println("ID: " + a.getId());
            System.out.println("Descripción: " + a.getDescripcion());
            System.out.println("Estado: " + a.getEstado());
            System.out.println("------------------------");
        }

        System.out.println("\n--- CREANDO ALERTA DE PRUEBA ---");

        Alertas nueva = new Alertas();
        nueva.setDocumentoId(1L);
        nueva.setUsuarioId(1L);
        nueva.setTipoAlerta("VENCIMIENTO");
        nueva.setDescripcion("Documento próximo a vencer");
        nueva.setEstado("PENDIENTE");
        nueva.setFechaCreacion(new Date());
        nueva.setFechaNotificacion(new Date());

        daoAlertas.saveAlerta(nueva);

        System.out.println("Alerta guardada correctamente");

        System.out.println("\n--- LISTANDO ALERTAS NUEVAMENTE ---");

        List<Alertas> alertas2 = daoAlertas.listarAlertas();

        for (Alertas a : alertas2) {
            System.out.println("ID: " + a.getId());
            System.out.println("Descripción: " + a.getDescripcion());
            System.out.println("Estado: " + a.getEstado());
            System.out.println("------------------------");
        }

        System.out.println("\n FIN DE PRUEBAS \n");
    }
}