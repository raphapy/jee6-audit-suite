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
