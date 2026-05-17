package com.myweb.mavenproject1;

import com.myweb.mavenproject1.dao.DocumentoDAO;
import com.myweb.mavenproject1.dao.LoginUsuarioDAO;
import com.myweb.mavenproject1.dao.ReporteDAO;
import com.myweb.mavenproject1.dao.AlertasDAO;

import com.myweb.mavenproject1.entidades.Documento;
import com.myweb.mavenproject1.entidades.LoginUsuario;
import com.myweb.mavenproject1.entidades.Reporte;
import com.myweb.mavenproject1.entidades.Alertas;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        
        // LOGIN USUARIOS
        
        System.out.println("\n==============================");
        System.out.println(" PRUEBAS USUARIOS ");
        System.out.println("==============================");

        LoginUsuarioDAO daoUsuario = new LoginUsuarioDAO();

        LoginUsuario user = new LoginUsuario();

        user.setNombre("Roberto Montes");
        user.setContraseña("2327690");
        user.setRol("Funcionario2");

        List<LoginUsuario> usuarios = daoUsuario.listar();

        System.out.println("\nLISTA USUARIOS");

        for (LoginUsuario u : usuarios) {

            System.out.println("ID: " + u.getId());

            System.out.println("Nombre: " + u.getNombre());

            System.out.println("Rol: " + u.getRol());

            System.out.println("------------------------");
        }

        // ACTUALIZAR USUARIO

        for (LoginUsuario u : usuarios) {

            if (u.getId() == 8) {

                u.setNombre("Samir Cardona Actualizado");

                u.setContraseña("6748599");

                u.setRol("Funcionario3");

                daoUsuario.actualizar(u);

                System.out.println(
                        "Usuario actualizado correctamente"
                );

                break;
            }
        }

        // ELIMINAR USUARIOS

        for (LoginUsuario u : usuarios) {

            if (u.getId() == 9 ||
                    u.getId() == 10 ||
                    u.getId() == 11) {

                daoUsuario.eliminar(u.getId());

                System.out.println(
                        "Usuario eliminado correctamente ID: "
                                + u.getId()
                );
            }
        }


        // DOCUMENTOS
        
        System.out.println("\n==============================");
        System.out.println(" PRUEBAS DOCUMENTOS ");
        System.out.println("==============================");

        DocumentoDAO daoDocumento = new DocumentoDAO();

        Documento doc = new Documento();

        doc.setTipoDocumento("I");

        doc.setAsunto(
                "reunión por dependencias"
        );

        doc.setRemitente(
                "Gerencia"
        );

        doc.setDestinatario(
                "Gestión administrativa"
        );

        doc.setFechaRadicacion(new Date());

        doc.setFechaVencimiento(new Date());

        doc.setEstado("recibido");

        doc.setUsuarioId(1);

        doc.setDependencia("Gestión administrativa");

        doc.setObservaciones("Confirmar asistencia");

        doc.setFechaCreacion(new Date());

        daoDocumento.guardar(doc);

        List<Documento> documentos = daoDocumento.listar();

        for (Documento d : documentos) {

            System.out.println("ID: " + d.getId());

            System.out.println(
                    "Radicado: " + d.getNumeroRadicado()
            );

            System.out.println(
                    "Asunto: " + d.getAsunto()
            );

            System.out.println("------------------------");
        }


        // REPORTES

        System.out.println("\n==============================");
        System.out.println(" PRUEBAS MODULO REPORTES ");
        System.out.println("==============================");

        ReporteDAO daoReporte = new ReporteDAO();


        // PRUEBA 1 - LISTAR REPORTES

        System.out.println("\n--- LISTANDO REPORTES ---");

        List<Reporte> reportes = daoReporte.listar();

        if (reportes != null && !reportes.isEmpty()) {

            System.out.println(
                    "Total reportes: " + reportes.size()
            );

            for (Reporte rep : reportes) {

                System.out.println("--------------------------------");

                System.out.println(
                        "ID: " + rep.getId()
                );

                System.out.println(
                        "Usuario: " + rep.getUsuarioId()
                );

                System.out.println(
                        "Tipo: " + rep.getTipoReporte()
                );

                System.out.println(
                        "Descripcion: " + rep.getDescripcion()
                );

                System.out.println(
                        "Formato: " + rep.getFormato()
                );

                System.out.println(
                        "Ruta: " + rep.getRutaArchivo()
                );

                System.out.println(
                        "Fecha Inicio: " + rep.getFechaInicio()
                );

                System.out.println(
                        "Fecha Fin: " + rep.getFechaFin()
                );

                System.out.println(
                        "Fecha Generacion: "
                                + rep.getFechaGeneracion()
                );
            }

        } else {

            System.out.println("No existen reportes");
        }


        // PRUEBA 2 - REPORTE PDF HISTORICO

        System.out.println("\n--- CREANDO REPORTE PDF ---");

        Reporte pdf = new Reporte();

        pdf.setUsuarioId(1);

        pdf.setTipoReporte("Reporte Mensual");

        pdf.setDescripcion(
                "Reporte mensual de documentos gestionados"
        );

        pdf.setFechaGeneracion(
                java.sql.Date.valueOf("2026-05-10")
        );

        pdf.setFormato("PDF");

        pdf.setRutaArchivo(
                "/reportes/reporte_mensual_mayo.pdf"
        );

        pdf.setFechaInicio(
                java.sql.Date.valueOf("2026-05-01")
        );

        pdf.setFechaFin(
                java.sql.Date.valueOf("2026-05-31")
        );

        try {

            daoReporte.guardar(pdf);

            System.out.println(
                    "Reporte PDF guardado correctamente"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR AL CREAR REPORTE PDF"
            );

            e.printStackTrace();
        }


        // PRUEBA 3 - REPORTE EXCEL HISTORICO

        System.out.println("\n--- CREANDO REPORTE EXCEL ---");

        Reporte excel = new Reporte();

        excel.setUsuarioId(2);

        excel.setTipoReporte("Reporte Manual");

        excel.setDescripcion(
                "Reporte manual solicitado por fechas"
        );

        excel.setFechaGeneracion(
                java.sql.Date.valueOf("2026-04-30")
        );

        excel.setFormato("EXCEL");

        excel.setRutaArchivo(
                "/reportes/reporte_manual_abril.xlsx"
        );

        excel.setFechaInicio(
                java.sql.Date.valueOf("2026-04-01")
        );

        excel.setFechaFin(
                java.sql.Date.valueOf("2026-04-30")
        );

        try {

            daoReporte.guardar(excel);

            System.out.println(
                    "Reporte EXCEL guardado correctamente"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR AL CREAR REPORTE EXCEL"
            );

            e.printStackTrace();
        }


        // PRUEBA 4 - REPORTE PQRS

        System.out.println("\n--- CREANDO REPORTE PQRS ---");

        Reporte pqrs = new Reporte();

        pqrs.setUsuarioId(1);

        pqrs.setTipoReporte("Reporte PQRS");

        pqrs.setDescripcion(
                "Reporte consolidado de PQRS"
        );

        pqrs.setFechaGeneracion(
                java.sql.Date.valueOf("2026-05-15")
        );

        pqrs.setFormato("PDF");

        pqrs.setRutaArchivo(
                "/reportes/reporte_pqrs.pdf"
        );

        pqrs.setFechaInicio(
                java.sql.Date.valueOf("2026-04-01")
        );

        pqrs.setFechaFin(
                java.sql.Date.valueOf("2026-04-30")
        );

        try {

            daoReporte.guardar(pqrs);

            System.out.println(
                    "Reporte PQRS guardado correctamente"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR AL CREAR REPORTE PQRS"
            );

            e.printStackTrace();
        }


        // PRUEBA 5 - FORMATO INVALIDO

        System.out.println("\n--- PROBANDO FORMATO INVALIDO ---");

        Reporte reporteInvalido = new Reporte();

        reporteInvalido.setUsuarioId(1);

        reporteInvalido.setTipoReporte(
                "Reporte Incorrecto"
        );

        reporteInvalido.setDescripcion(
                "Prueba de formato no permitido"
        );

        reporteInvalido.setFechaGeneracion(
                java.sql.Date.valueOf("2026-05-16")
        );

        reporteInvalido.setFormato("TXT");

        reporteInvalido.setRutaArchivo(
                "/reportes/reporte.txt"
        );

        try {

            daoReporte.guardar(reporteInvalido);

            System.out.println(
                    "ERROR: el sistema permitio formato invalido"
            );

        } catch (Exception e) {

            System.out.println(
                    "Correcto: el sistema rechazo el formato invalido"
            );

            System.out.println(
                    e.getMessage()
            );
        }


        // PRUEBA 6 - REPORTE SIN USUARIO

        System.out.println(
                "\n--- PROBANDO REPORTE SIN USUARIO ---"
        );

        Reporte reporteSinUsuario = new Reporte();

        reporteSinUsuario.setTipoReporte(
                "Reporte Sin Usuario"
        );

        reporteSinUsuario.setDescripcion(
                "Prueba de validacion"
        );

        reporteSinUsuario.setFechaGeneracion(
                java.sql.Date.valueOf("2026-05-16")
        );

        reporteSinUsuario.setFormato("PDF");

        try {

            daoReporte.guardar(reporteSinUsuario);

            System.out.println(
                    "ERROR: se guardo reporte sin usuario"
            );

        } catch (Exception e) {

            System.out.println(
                    "Correcto: el sistema rechazo reporte sin usuario"
            );

            System.out.println(
                    e.getMessage()
            );
        }


        // PRUEBA 7 - LISTAR REPORTES ACTUALIZADOS

        System.out.println(
                "\n--- LISTANDO REPORTES ACTUALIZADOS ---"
        );

        List<Reporte> reportes2 = daoReporte.listar();

        if (reportes2 != null) {

            System.out.println(
                    "Total reportes actuales: "
                            + reportes2.size()
            );

            for (Reporte rep : reportes2) {

                System.out.println("--------------------------------");

                System.out.println(
                        "ID: " + rep.getId()
                );

                System.out.println(
                        "Tipo: " + rep.getTipoReporte()
                );

                System.out.println(
                        "Formato: " + rep.getFormato()
                );

                System.out.println(
                        "Fecha Generacion: "
                                + rep.getFechaGeneracion()
                );
            }
        }

        System.out.println(
                "\n FIN PRUEBAS REPORTES \n"
        );


        // ALERTAS
        

        System.out.println(
                "\n--- LISTANDO ALERTAS ---"
        );

        AlertasDAO daoAlertas = new AlertasDAO();

        List<Alertas> alertas =
                daoAlertas.listarAlertasPorUsuario(1L);

        System.out.println(
                "Total de alertas: " + alertas.size()
        );

        for (Alertas a : alertas) {

            System.out.println("ID: " + a.getId());

            System.out.println(
                    "Descripción: " + a.getDescripcion()
            );

            System.out.println(
                    "Estado: " + a.getEstado()
            );

            System.out.println("------------------------");
        }


        // PRUEBA 1 - ALERTA PENDIENTE
       
        System.out.println(
                "\n--- CREANDO ALERTA PENDIENTE ---"
        );

        Alertas nueva = new Alertas();

        nueva.setDocumentoId(1L);

        nueva.setUsuarioId(1L);

        nueva.setTipoAlerta("vencimiento");

        nueva.setDescripcion(
                "Documento próximo a vencer"
        );

        nueva.setEstado("pendiente");

        nueva.setFechaCreacion(new Date());

        nueva.setFechaNotificacion(new Date());

        try {

            daoAlertas.guardar(nueva);

            System.out.println(
                    "Alerta pendiente guardada correctamente"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR EN ALERTA NORMAL"
            );

            e.printStackTrace();
        }

       // PRUEBA 2 - ALERTA VENCIDA
        
        System.out.println(
                "\n--- PRUEBA ALERTA VENCIDA ---"
        );

        Alertas vencida = new Alertas();

        vencida.setDocumentoId(2L);

        vencida.setUsuarioId(1L);

        vencida.setTipoAlerta("vencimiento");

        vencida.setDescripcion(
                "Documento ya vencido"
        );

        vencida.setEstado("vencido");

        vencida.setFechaCreacion(new Date());

        vencida.setFechaNotificacion(new Date());

        vencida.setDiasRestantes(-1);

        try {

            daoAlertas.guardar(vencida);

            System.out.println(
                    "Se guardó alerta vencida"
            );

        } catch (Exception e) {

            System.out.println(
                    "El sistema rechazó alerta vencida"
            );

            e.printStackTrace();
        }
     
                // PRUEBA 3 - ALERTA SIN USUARIO
       
        System.out.println(
                "\n--- PRUEBA ALERTA SIN USUARIO ---"
        );

        Alertas alertaSinUsuario = new Alertas();

        alertaSinUsuario.setDocumentoId(3L);

        alertaSinUsuario.setTipoAlerta("vencimiento");

        alertaSinUsuario.setDescripcion(
                "Alerta inválida"
        );

        alertaSinUsuario.setEstado("pendiente");

        alertaSinUsuario.setFechaCreacion(new Date());

        try {

            daoAlertas.guardar(alertaSinUsuario);

            System.out.println(
                    "ERROR: Se guardó sin usuario"
            );

        } catch (Exception e) {

            System.out.println(
                    "Correcto: el sistema rechazó la alerta"
            );

            e.printStackTrace();
        }


     
        // LISTAR ALERTAS NUEVAMENTE
        
        System.out.println(
                "\n--- LISTANDO ALERTAS NUEVAMENTE ---"
        );

        List<Alertas> alertas2 =
                daoAlertas.listarAlertasPorUsuario(1L);

        for (Alertas a : alertas2) {

            System.out.println("ID: " + a.getId());

            System.out.println(
                    "Descripción: " + a.getDescripcion()
            );

            System.out.println(
                    "Estado: " + a.getEstado()
            );

            System.out.println("------------------------");
        }

        System.out.println(
                "\n FIN DE PRUEBAS \n"
        );

    }

}