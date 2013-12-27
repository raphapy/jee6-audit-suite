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

import static javax.ejb.LockType.WRITE;

import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import py.com.konecta.suiteauditoria.entities.Process;

/**
 * En algunos casos se requiere crear procesos y que los mismos sean
 * inmediatamente visibles por otros hilos durante la auditor&iacute;a de
 * procesos.
 * <p>
 * Este singleton permite gestionar esa tarea de manera segura para acceso
 * concurrente.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@ConcurrencyManagement
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcessManagementService {

	@Resource
	private UserTransaction transaction;
	@EJB
	private ProcessService processService;
	
	public ProcessManagementService() {
		
	}
	
	/**
	 * Registra un proceso en la base de datos, en una transacci&oacute;n
	 * separada, si es que a&uacute;n no existe. Si es que existe, se referencia
	 * el proceso obtenido hacia el argumento {@code newProcess}.
	 * <p>
	 * Un uso com&uacute;n de este m&eacute;todo es cuando se necesitan crear
	 * procesos desde hilos separados, utilizar este mecanismo evitar&aacute;
	 * que un hilo no vea al proceso creado desde otro.
	 * 
	 * @param newProcess
	 * @return el {@code Process} creado o encontrado
	 * @throws IllegalArgumentException
	 *             En el caso que el argumento {@code newProcess} sea
	 *             {@code null} o bien cuando las propiedades processName o
	 *             implClassName o pImplMethodName del argumento
	 *             {@code newProcess} sean {@code null} o {@code ""}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Lock(WRITE)
	public Process registerProcessImmediately(Process newProcess)
			throws IllegalArgumentException {

		String pProcessName = null;
		String pImplClassName = null;
		String pImplMethodName = null;

		if (newProcess == null) {
			throw new IllegalArgumentException("El proceso no puede ser nulo.");
		} else {

			if (newProcess.getProcessName() == null
					|| "".equals(newProcess.getProcessName().trim())) {
				throw new IllegalArgumentException(
						"La propiedad processName del proceso no puede ser nula ni vacia.");
			} else {
				pProcessName = newProcess.getProcessName();
			}

			if (newProcess.getImplClassName() == null
					|| "".equals(newProcess.getImplClassName().trim())) {
				throw new IllegalArgumentException(
						"La propiedad implClassName del proceso no puede ser nula ni vacia.");
			} else {
				pImplClassName = newProcess.getImplClassName();
			}

			if (newProcess.getImplMethodName() == null
					|| "".equals(newProcess.getImplMethodName().trim())) {

				throw new IllegalArgumentException(
						"La propiedad implMethodName del proceso no puede ser nula ni vacia.");
			} else {
				pImplMethodName = newProcess.getImplMethodName();
			}
		}

		Process findedProcess = processService.findProcessByNameAndImplementDetail(
				pProcessName, pImplClassName, pImplMethodName);
		
		if (findedProcess == null) {
			try {
				transaction.begin();
				processService.create(newProcess);
				transaction.commit();
				
				findedProcess = newProcess;
				
			} catch (Exception e) {
				try {
					transaction.rollback();
				} catch (Exception e2) {
					e.printStackTrace();
				}
			}

		}

		return findedProcess;
	}
}
