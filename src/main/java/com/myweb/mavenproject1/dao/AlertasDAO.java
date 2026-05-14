package com.myweb.mavenproject1.dao;

import com.myweb.mavenproject1.entidades.Alertas;
import com.myweb.mavenproject1.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AlertasDAO {

    public void guardar(Alertas alerta) throws Exception {

        Session session = null;
        Transaction tx = null;

        try {

            if (alerta == null) {
                throw new RuntimeException("La alerta no puede ser null");
            }

            if (alerta.getUsuarioId() == null) {
                throw new RuntimeException("Usuario obligatorio");
            }

            if (alerta.getTipoAlerta() == null) {
                throw new RuntimeException("Tipo alerta obligatorio");
            }

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            if (alerta == null) {
                throw new RuntimeException("La alerta no puede ser null");
            }

            if (alerta.getUsuarioId() == null) {
                throw new RuntimeException("Usuario obligatorio");
            }

            session.persist(alerta);

            tx.commit();

            System.out.println("✓ Alerta guardada");

        } catch (Exception ex) {

            if (tx != null) {
                tx.rollback();
            }

            ex.printStackTrace();

            throw ex;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public List<Alertas> listarAlertasPorUsuario(Long usuarioId) {

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Query<Alertas> query = session.createQuery(
                    "FROM Alertas WHERE usuarioId = :usuarioId",
                    Alertas.class
            );

            query.setParameter("usuarioId", usuarioId);

            return query.list();

        } catch (Exception ex) {

            ex.printStackTrace();

            return Collections.emptyList();

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public Alertas obtenerPorId(Long id) {

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            return session.get(Alertas.class, id);

        } catch (Exception ex) {

            ex.printStackTrace();

            return null;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public void actualizarEstado(Long id, String estado) throws Exception {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Alertas alerta = session.get(Alertas.class, id);

            if (alerta == null) {
                throw new RuntimeException("Alerta no encontrada");
            }

            alerta.setEstado(estado);

            session.merge(alerta);

            tx.commit();

            System.out.println("✓ Estado actualizado");

        } catch (Exception ex) {

            if (tx != null) {
                tx.rollback();
            }

            ex.printStackTrace();

            throw ex;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public int contarAlertasPendientes(Long usuarioId) {

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Alertas " +
                    "WHERE usuarioId = :usuarioId " +
                    "AND estado = 'pendiente'",
                    Long.class
            );

            query.setParameter("usuarioId", usuarioId);

            Long resultado = query.getSingleResult();

            return resultado.intValue();

        } catch (Exception ex) {

            ex.printStackTrace();

            return 0;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public List<Alertas> obtenerAlertasProximoVencimiento(int dias) {

        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date fechaLimite = new Date(
                    System.currentTimeMillis() +
                    (dias * 24L * 60L * 60L * 1000L)
            );

            Query<Alertas> query = session.createQuery(
                    "FROM Alertas " +
                    "WHERE fechaLimiteRespuesta <= :fechaLimite",
                    Alertas.class
            );

            query.setParameter("fechaLimite", fechaLimite);

            return query.list();

        } catch (Exception ex) {

            ex.printStackTrace();

            return Collections.emptyList();

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
}