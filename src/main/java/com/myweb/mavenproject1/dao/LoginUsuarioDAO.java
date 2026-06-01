package com.myweb.mavenproject1.dao;

import com.myweb.mavenproject1.entidades.LoginUsuario;
import com.myweb.mavenproject1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoginUsuarioDAO {

    public void guardar(LoginUsuario usuario) {
        if (usuario.getNombre() != null) {
            // Convertir siempre a minúscula antes de guardar
            String nombreLower = usuario.getNombre().trim().toLowerCase();
            usuario.setNombre(nombreLower);
        }
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        
        try {
            session.save(usuario);
            tx.commit();
            System.out.println("Usuario guardado correctamente: " + usuario.getNombre());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<LoginUsuario> listar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<LoginUsuario> lista = session.createQuery("FROM LoginUsuario", LoginUsuario.class).list();
        session.close();
        return lista;
    }

    public void actualizar(LoginUsuario usuario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.update(usuario);
            tx.commit();
            System.out.println("Usuario actualizado");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void eliminar(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            LoginUsuario usuario = session.get(LoginUsuario.class, id);
            if (usuario != null) {
                session.delete(usuario);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public LoginUsuario validarLogin(String nombre, String contrasena) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM LoginUsuario " +
                         "WHERE nombre = :nombre " +
                         "AND contrasena = :contrasena";

            return session.createQuery(hql, LoginUsuario.class)
                    .setParameter("nombre", nombre.trim())   
                    .setParameter("contrasena", contrasena)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}