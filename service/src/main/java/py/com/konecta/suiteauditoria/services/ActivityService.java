/*
Copyright 2013 Rafael E. Benegas

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package py.com.konecta.suiteauditoria.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.konecta.suiteauditoria.entities.Activity;

@Stateless
public class ActivityService extends ServiceBase<Activity> {

	@PersistenceContext(unitName = "suiteAuditoriaPU")
	private EntityManager em;

	public ActivityService() {
		super(Activity.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Busca todas las actividades de una instancia de proceso dada.
	 * 
	 * @param processInstanceId
	 *            Es el identificador de la instancia a la que pertenecen las
	 *            actividades.
	 * @return Una lista {@code List<Activity> } que representa la lista de
	 *         actividades de la instancia dada o {@code null} si no existen
	 *         datos.
	 */
	public List<Activity> findByProcessInstanceId(Long processInstanceId) {
		return findByProcessInstanceId(processInstanceId, null);
	}

	/**
	 * Busca todas las actividades de una instancia de proceso dada y permite
	 * paginar el resultado.
	 * 
	 * @param processInstanceId
	 *            Es el identificador de la instancia a la que pertenecen las
	 *            actividades.
	 * @param start
	 *            Valor de inicio del rango
	 * @param end
	 *            Valor del fin del rango
	 * @return Una lista {@code List<Activity> } que representa la lista de
	 *         actividades de la instancia dada o {@code null} si no existen
	 *         datos.
	 */
	public List<Activity> findByProcessInstanceId(Long processInstanceId,
			int start, int end) {
		return findByProcessInstanceId(processInstanceId, new int[] { start,
				end });
	}

	/**
	 * Busca todas las actividades de una instancia de proceso dada y permite
	 * paginar, o no, el resultado.
	 * 
	 * @param processInstanceId
	 *            Es el identificador de la instancia a la que pertenecen las
	 *            actividades.
	 * @param range
	 *            Es un {@code int[]} en el que se encuentran el inicio y fin
	 *            del rango, en las procisiones 0 y 1 respectivamente. Si este
	 *            array es nulo o no contiene al menos 2 elementos, el resultado
	 *            no se pagina. Todos los elementos desde la pocisi&oacute;n 2
	 *            en adelante son ignorados.
	 * @return Una lista {@code List<Activity> } que representa la lista de
	 *         actividades de la instancia dada o {@code null} si no existen
	 *         datos.
	 */
	@SuppressWarnings("unchecked")
	private List<Activity> findByProcessInstanceId(Long processInstanceId,
			int[] range) {
		Query query = em.createNamedQuery("findByProcessInstanceId",
				Activity.class);
		query.setParameter("pProcessInstanceId", processInstanceId);

		if (range != null && range.length >= 2) {
			query.setMaxResults(range[1] - range[0]);
			query.setFirstResult(range[0]);
		}

		return query.getResultList();
	}
}
