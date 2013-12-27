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

import py.com.konecta.suiteauditoria.entities.ProcessInstance;

@Stateless
public class ProcessInstanceService extends ServiceBase<ProcessInstance> {

	@PersistenceContext
	EntityManager em;

	public ProcessInstanceService() {
		super(ProcessInstance.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Busca todas las instancias de un proceso dado.
	 * 
	 * @param processId
	 *            Es el identificador del proceso al que pertenecen las
	 *            instancias
	 * @return {@code List<ProcessInstance> } que representa la lista de
	 *         instancias existentes del proceso o {@code null} si no existen
	 *         datos.
	 */
	public List<ProcessInstance> findByProcessId(Long processId) {
		return findByProcessId(processId, null);
	}

	/**
	 * Busca todas las instancias de un proceso dado y permite paginar el
	 * resultado.
	 * 
	 * @param processId
	 *            Es el identificador del proceso al que pertenecen las
	 *            instancias.
	 * @param start
	 *            Valor de inicio del rango
	 * @param end
	 *            Valor del fin del rango
	 * @return {@code List<ProcessInstance> } que representa la lista de
	 *         instancias existentes del proceso o {@code null} si no existen
	 *         datos.
	 */
	public List<ProcessInstance> findByProcessId(Long processId, int start,
			int end) {
		return findByProcessId(processId, new int[] { start, end });
	}

	/**
	 * Busca todas las instancias de un proceso dado y permite paginar, o no, el
	 * resultado.
	 * 
	 * @param processId
	 *            Es el identificador del proceso al que pertenecen las
	 *            instancias.
	 * @param range
	 *            Es un {@code int[]} en el que se encuentran el inicio y fin
	 *            del rango, en las procisiones 0 y 1 respectivamente. Si este
	 *            array es nulo o no contiene al menos 2 elementos, el resultado
	 *            no se pagina. Todos los elementos desde la pocisi&oacute;n 2
	 *            en adelante son ignorados.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ProcessInstance> findByProcessId(Long processId, int[] range) {
		Query query = em.createNamedQuery("findByProcessId", ProcessInstance.class);
		query.setParameter("pProcessId", processId);

		if (range != null && range.length >= 2) {
			query.setMaxResults(range[1] - range[0]);
			query.setFirstResult(range[0]);
		}

		return query.getResultList();
	}
}
