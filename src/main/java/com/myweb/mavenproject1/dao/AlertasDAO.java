package com.myweb.mavenproject1.dao;

import com.myweb.mavenproject1.entidades.Alertas;
import com.myweb.mavenproject1.entidades.LoginUsuario;
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

            // =========================
            // VALIDACIONES BASICAS
            // =========================

            if (alerta == null) {
                throw new RuntimeException(
                        "La alerta no puede ser null"
                );
            }

            if (alerta.getUsuarioId() == null) {
                throw new RuntimeException(
                        "El usuario es obligatorio"
                );
            }

            if (alerta.getTipoAlerta() == null ||
                alerta.getTipoAlerta().trim().isEmpty()) {

                throw new RuntimeException(
                        "El tipo de alerta es obligatorio"
                );
            }

            // =========================
            // ABRIR SESION
            // =========================

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            tx = session.beginTransaction();

            // =========================
            // VALIDAR USUARIO EXISTENTE
            // =========================

            LoginUsuario usuario = session.get(
                    LoginUsuario.class,
                    alerta.getUsuarioId()
            );

            if (usuario == null) {

                throw new RuntimeException(
                        "El usuario no existe en la base de datos"
                );
            }

            // =========================
            // VALIDAR ALERTA DUPLICADA
            // =========================

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) " +
                    "FROM Alertas a " +
                    "WHERE a.usuarioId = :usuarioId " +
                    "AND a.tipoAlerta = :tipoAlerta " +
                    "AND a.estado = :estado",
                    Long.class
            );

            query.setParameter(
                    "usuarioId",
                    alerta.getUsuarioId()
            );

            query.setParameter(
                    "tipoAlerta",
                    alerta.getTipoAlerta()
            );

            query.setParameter(
                    "estado",
                    alerta.getEstado()
            );

            Long total = query.getSingleResult();

            if (total > 0) {

                throw new RuntimeException(
                        "Ya existe una alerta igual para este usuario"
                );
            }

            // =========================
            // FECHA AUTOMATICA
            // =========================

            if (alerta.getFechaCreacion() == null) {
                alerta.setFechaCreacion(new Date());
            }

            // =========================
            // GUARDAR ALERTA
            // =========================

            session.persist(alerta);

            tx.commit();

            System.out.println(
                    "Alerta guardada correctamente"
            );

        } catch (Exception ex) {

            if (tx != null) {
                tx.rollback();
            }

            System.out.println(
                    "Error al guardar alerta: "
                    + ex.getMessage()
            );

            ex.printStackTrace();

            throw ex;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Lista alertas por usuario
     */
    public List<Alertas> listarAlertasPorUsuario(Long usuarioId) {

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            Query<Alertas> query = session.createQuery(
                    "FROM Alertas " +
                    "WHERE usuarioId = :usuarioId " +
                    "ORDER BY fechaCreacion DESC",
                    Alertas.class
            );

            query.setParameter(
                    "usuarioId",
                    usuarioId
            );

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

    /**
     * Obtiene alerta por ID
     */
    public Alertas obtenerPorId(Long id) {

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

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

    /**
     * Actualiza estado de alerta
     */
    public void actualizarEstado(
            Long id,
            String estado
    ) throws Exception {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            tx = session.beginTransaction();

            Alertas alerta = session.get(
                    Alertas.class,
                    id
            );

            if (alerta == null) {

                throw new RuntimeException(
                        "Alerta no encontrada"
                );
            }

            alerta.setEstado(estado);

            session.merge(alerta);

            tx.commit();

            System.out.println(
                    "Estado actualizado correctamente"
            );

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

    /**
     * Cuenta alertas pendientes
     */
    public int contarAlertasPendientes(Long usuarioId) {

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) " +
                    "FROM Alertas " +
                    "WHERE usuarioId = :usuarioId " +
                    "AND estado = 'pendiente'",
                    Long.class
            );

            query.setParameter(
                    "usuarioId",
                    usuarioId
            );

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

    /**
     * Obtiene alertas próximas a vencer
     */
    public List<Alertas> obtenerAlertasProximoVencimiento(
            int dias
    ) {

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            Date fechaLimite = new Date(
                    System.currentTimeMillis() +
                    (dias * 24L * 60L * 60L * 1000L)
            );

            Query<Alertas> query = session.createQuery(
                    "FROM Alertas " +
                    "WHERE fechaLimiteRespuesta <= :fechaLimite " +
                    "AND estado = 'pendiente' " +
                    "ORDER BY fechaLimiteRespuesta ASC",
                    Alertas.class
            );

            query.setParameter(
                    "fechaLimite",
                    fechaLimite
            );

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

    /**
     * Elimina alerta por ID
     */
    public void eliminar(Long id) throws Exception {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            tx = session.beginTransaction();

            Alertas alerta = session.get(
                    Alertas.class,
                    id
            );

            if (alerta == null) {

                throw new RuntimeException(
                        "La alerta no existe"
                );
            }

            session.remove(alerta);

            tx.commit();

            System.out.println(
                    "Alerta eliminada correctamente"
            );

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
    
        /**
        * Lista todas las alertas
        */
       public List<Alertas> listar() {

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            return session.createQuery(
                    "FROM Alertas ORDER BY fechaCreacion DESC",
                    Alertas.class
            ).list();

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