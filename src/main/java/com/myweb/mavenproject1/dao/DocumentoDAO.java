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

            int consecutivo = obtenerSiguienteRadicado(tipo, anio);

            String radicado = "RAD-" + tipo + "-" + anio + "-" + String.format("%05d", consecutivo);

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
     * Obtener consecutivo por tipo y año
     */
    public int obtenerSiguienteRadicado(String tipo, int anio) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Query<String> q = session.createQuery(
            "SELECT numeroRadicado FROM Documento " +
            "WHERE numeroRadicado LIKE :patron " +
            "ORDER BY numeroRadicado DESC",
            String.class
        );

        String patron = "RAD-" + tipo + "-" + anio + "-%";
        q.setParameter("patron", patron);
        q.setMaxResults(1);

        String ultimo = q.uniqueResult();

        session.close();

        if (ultimo == null) {
            return 1;
        }

        try {
            String numeroStr = ultimo.substring(ultimo.length() - 5);
            int numero = Integer.parseInt(numeroStr);
            return numero + 1;
        } catch (Exception e) {
            return 1;
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