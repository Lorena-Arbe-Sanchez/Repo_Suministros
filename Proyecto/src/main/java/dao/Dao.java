package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public abstract class Dao<T, K> {

    // Una única fábrica para toda la aplicación
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");

    protected EntityManager em = emf.createEntityManager();

    public EntityManager getEntityManager() {
        return this.em;
    }

    // Permitir crear EntityManager desde otras clases
    public static EntityManager crearEntityManager() {
        return emf.createEntityManager();
    }

    // Cerrar la fábrica al finalizar la aplicación
    public static void cerrarFabrica() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    public abstract T find(K id);

    public T create(T t) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(t);
        transaction.commit();
        return t;
    }

    public T update(T t) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        T actualizado = em.merge(t);
        transaction.commit();
        return actualizado;
    }

    public void delete(T t) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // remove necesita una entidad gestionada.
        em.remove(em.contains(t) ? t : em.merge(t));

        transaction.commit();
    }

    public void cerrar() {
        if (em.isOpen()) {
            em.close();
        }
    }
}