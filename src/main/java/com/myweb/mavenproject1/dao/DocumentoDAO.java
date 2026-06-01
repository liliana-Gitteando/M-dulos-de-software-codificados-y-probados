package com.myweb.mavenproject1.dao;

import com.myweb.mavenproject1.entidades.Documento;
import com.myweb.mavenproject1.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

/**                                                          
 * Clase DAO para operaciones CRUD de Documento
 */
public class DocumentoDAO {

    /**
     * Guardar
     */
    public void guardar(Documento documento) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            // VALIDACIÓN 1: Campo asunto obligatorio
            if (documento.getAsunto() == null || documento.getAsunto().isEmpty()) {
                throw new Exception("El asunto es obligatorio");
            }

            // VALIDACIÓN 2: Remitente obligatorio
            if (documento.getRemitente() == null || documento.getRemitente().isEmpty()) {
                throw new Exception("El remitente es obligatorio");
            }

            // VALIDACIÓN 3: Destinatario obligatorio
            if (documento.getDestinatario() == null || documento.getDestinatario().isEmpty()) {
                throw new Exception("El destinatario es obligatorio");
            }

            /**
            * RADICADO POR TIPO Y AÑO
            */
                String tipo = documento.getTipoDocumento(); // I, S, E
            int anio = java.time.LocalDate.now().getYear();

            int mes = java.time.LocalDate.now().getMonthValue();

            int consecutivo =
                    obtenerSiguienteRadicado(tipo, anio, mes);

             String radicado =
                "RAD-" + tipo + "-" + anio + "-"
                + String.format("%02d", mes) + "-"
                + String.format("%05d", consecutivo);

            documento.setNumeroRadicado(radicado);

            System.out.println("Radicado generado: " + radicado);
            

            session.save(documento);
            tx.commit();
            System.out.println("Documento guardado correctamente");

        } catch (Exception e) {
            tx.rollback();
            System.out.println("Error al guardar documento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    

    /**
     * Listar 
     */
    public List<Documento> listar() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Documento> lista = null;

        try {
            lista = session.createQuery("FROM Documento", Documento.class).list();
        } catch (Exception e) {
            System.out.println("Error al listar documentos");
            e.printStackTrace();
        } finally {
            session.close();
        }

        return lista;
    }
    
    public Documento buscarPorRadicado(String radicado) {

    Session session =
            HibernateUtil.getSessionFactory().openSession();

    try {

        Query<Documento> q =
                session.createQuery(
                        "FROM Documento WHERE numeroRadicado = :radicado",
                        Documento.class
                );

        q.setParameter("radicado", radicado);
        return q.uniqueResult();

        } catch (Exception e) {

        e.printStackTrace();
        return null;

         } finally {

        session.close();
    }
}

    /**
     * Actualizar 
     */
    public void actualizar(Documento documento) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            session.update(documento);
            tx.commit();
            System.out.println("Documento actualizado correctamente");
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Error al actualizar documento");
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

/**
 * Obtener consecutivo por tipo, año y mes
 */
     public int obtenerSiguienteRadicado(String tipo, int anio, int mes) {

    Session session = HibernateUtil.getSessionFactory().openSession();

    try {

        String patron =
                "RAD-" + tipo + "-" + anio + "-"
                + String.format("%02d", mes) + "-%";

        Query<String> q = session.createQuery(
            "SELECT d.numeroRadicado "
            + "FROM Documento d "
            + "WHERE d.numeroRadicado LIKE :patron",
            String.class
        );

        q.setParameter("patron", patron);

        List<String> resultados = q.list();

        int maximo = 0;

        for (String radicado : resultados) {

            System.out.println("Radicado encontrado: " + radicado);

            String numeroStr =
                    radicado.substring(radicado.lastIndexOf("-") + 1);

            int numero = Integer.parseInt(numeroStr);

            if (numero > maximo) {
                maximo = numero;
            }
        }

        return maximo + 1;

    } catch (Exception e) {

        System.out.println("Error obteniendo consecutivo");

        e.printStackTrace();

        return 1;

    } finally {

        session.close();
    }
}
    /**
     * Validar si ya existe un radicado 
     */
    public boolean existeRadicado(int numero) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Query<Documento> q = session.createQuery(
            "FROM Documento WHERE numeroRadicado = :num", Documento.class
        );

        q.setParameter("num", numero);

        boolean existe = q.uniqueResult() != null;

        session.close();

        return existe;
    }
}