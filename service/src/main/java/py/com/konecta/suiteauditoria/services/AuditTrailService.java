package py.com.konecta.suiteauditoria.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.konecta.suiteauditoria.entities.AuditTrail;

@Stateless
public class AuditTrailService extends ServiceBase<AuditTrail> {

	@PersistenceContext(unitName = "suiteAuditoriaPU")
	private EntityManager em;

	public AuditTrailService() {
		super(AuditTrail.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Busca todos los rastros de auditor&iacute;a de una actividad dada.
	 * 
	 * @param activityId
	 *            Es el identificador de la actividad a la que pertenecen los
	 *            rastros de auditor&iacute;a.
	 * @return Una lista {@code List<AuditTrail> } que representa la lista de
	 *         rastros de auditor&iacute;a de la actividad dada o {@code null}
	 *         si no existen datos.
	 */
	public List<AuditTrail> findByActivityId(Long activityId) {
		return findByActivityId(activityId, null);
	}

	/**
	 * Busca todos los rastros de auditor&iacute;a de una actividad dada y
	 * permite paginar el resultado.
	 * 
	 * @param activityId
	 *            Es el identificador de la actividad a la que pertenecen los
	 *            rastros de auditor&iacute;a.
	 * @param start
	 *            Valor de inicio del rango
	 * @param end
	 *            Valor del fin del rango
	 * @return Una lista {@code List<AuditTrail> } que representa la lista de
	 *         rastros de auditor&iacute;a de la actividad dada o {@code null}
	 *         si no existen datos.
	 */
	public List<AuditTrail> findByActivityId(Long activityId, int start, int end) {
		return findByActivityId(activityId, new int[] { start, end });
	}

	/**
	 * Busca todos los rastros de auditor&iacute;a de una actividad dada y
	 * permite paginar, o no, el resultado.
	 * 
	 * @param activityId
	 *            Es el identificador de la actividad a la que pertenecen los
	 *            rastros de auditor&iacute;a.
	 * @param range
	 *            Es un {@code int[]} en el que se encuentran el inicio y fin
	 *            del rango, en las procisiones 0 y 1 respectivamente. Si este
	 *            array es nulo o no contiene al menos 2 elementos, el resultado
	 *            no se pagina. Todos los elementos desde la pocisi&oacute;n 2
	 *            en adelante son ignorados.
	 * @return Una lista {@code List<AuditTrail> } que representa la lista de
	 *         rastros de auditor&iacute;a de la actividad dada o {@code null}
	 *         si no existen datos.
	 */
	@SuppressWarnings("unchecked")
	private List<AuditTrail> findByActivityId(Long activityId, int[] range) {
		Query query = em.createNamedQuery("findByActivityId",
				AuditTrail.class);
		query.setParameter("pActivityId", activityId);

		if (range != null && range.length >= 2) {
			query.setMaxResults(range[1] - range[0]);
			query.setFirstResult(range[0]);
		}

		return query.getResultList();
	}
}
