package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Dao<T, K> {

    private static final Logger logger = LoggerFactory.getLogger(Dao.class);

    // Fábrica compartida por todos los DAO
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");

    protected EntityManager em = emf.createEntityManager();

    public EntityManager getEntityManager() {
        return this.em;
    }

    // Crear un EntityManager para otras clases
    public static EntityManager crearEntityManager() {
        return emf.createEntityManager();
    }

    // Cerrar la fábrica al finalizar la aplicación
    public static void cerrarFabrica() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    public abstract T find(K cod);

    public T create(T t) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(t);
        transaction.commit();

        logger.info("Registro creado correctamente: {}", t.getClass().getSimpleName());

        return t;
    }

    public T update(T t) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        T actualizado = em.merge(t);
        transaction.commit();

        logger.info("Registro actualizado correctamente: {}", t.getClass().getSimpleName());

        return actualizado;
    }

    public boolean delete(T t) {
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.remove(em.contains(t) ? t : em.merge(t));

            transaction.commit();

            logger.info("Registro eliminado correctamente: {}", t.getClass().getSimpleName());

            return true;

        } catch (jakarta.persistence.PersistenceException e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            em.clear();

            logger.warn("No se ha podido eliminar el registro: {}", t.getClass().getSimpleName(), e);

            return false;
        }
    }

    public void cerrar() {
        if (em.isOpen()) {
            em.close();
        }
    }
}