package server;

import javax.persistence.*;

import model.*;

public class AdminDb {
	
	public void login(String usr, String pass){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("StreamingProject");
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT u FROM Usuario u WHERE u.login = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, usr);
		Usuario u = (Usuario) query.getSingleResult();
		
	}

}
