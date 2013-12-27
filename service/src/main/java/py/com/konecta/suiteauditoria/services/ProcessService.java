package py.com.konecta.suiteauditoria.services;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.konecta.suiteauditoria.entities.Process;

@SuppressWarnings("unchecked")
@Stateless
public class ProcessService extends ServiceBase<Process> {

	@PersistenceContext(unitName = "suiteAuditoriaPU")
	private EntityManager em;

	public ProcessService() {
		super(Process.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Busca por nombre del proceso, clase y m&eacute;todo en la que se
	 * implementa al proceso.
	 * 
	 * @param processName
	 * @param implClassName
	 * @param implMethodName
	 * @return una instancia de {@code Process} si existe en la BD y
	 *         {@code null} si no existe.
	 */
	public Process findProcessByNameAndImplementDetail(String processName,
			String implClassName, String implMethodName) {
		Query query = em.createNamedQuery(
				"findProcessByNameAndImplementDetail", Process.class);
		query.setParameter("pProcessName", processName);
		query.setParameter("pImplClassName", implClassName);
		query.setParameter("pImplMethodName", implMethodName);
		Collection<Process> list = query.getResultList();
		if (list != null && !list.isEmpty()) {
			return list.iterator().next();
		} else {
			return null;
		}
	}
}
