package com.myweb.mavenproject1.dao;

import com.myweb.mavenproject1.entidades.Reporte;
import com.myweb.mavenproject1.util.HibernateUtil;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ReporteDAO {


    // GUARDAR REPORTE

    public void guardar(Reporte reporte) {

        // VALIDACIONES

        if (reporte.getUsuarioId() == null) {
            throw new RuntimeException("El usuario es obligatorio");
        }

        if (reporte.getTipoReporte() == null ||
                reporte.getTipoReporte().trim().isEmpty()) {

            throw new RuntimeException("El tipo de reporte es obligatorio");
        }

        if (reporte.getFormato() == null) {
            throw new RuntimeException("El formato es obligatorio");
        }

        // VALIDAR FORMATOS

        String formato = reporte.getFormato().toUpperCase();

        if (!formato.equals("PDF") &&
                !formato.equals("EXCEL") &&
                !formato.equals("XLSX")) {

            throw new RuntimeException(
                    "Formato inválido. Solo PDF o EXCEL"
            );
        }

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Transaction tx = session.beginTransaction();

            session.save(reporte);

            tx.commit();

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error al guardar reporte"
            );

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    // LISTAR REPORTES

    public List<Reporte> listar() {

    Session session = null;

    try {

        session = HibernateUtil.getSessionFactory().openSession();

        return session.createQuery(
                "FROM Reporte ORDER BY id DESC",
                Reporte.class
        ).list();

    } catch (Exception e) {

        e.printStackTrace();

        return Collections.emptyList();

    } finally {

        if (session != null) {
            session.close();
        }
    }
}
    // OBTENER REPORTE POR ID
    
    public Reporte obtenerPorId(int id) {

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            return session.get(Reporte.class, id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
}