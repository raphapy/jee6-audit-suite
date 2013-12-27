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
package py.com.konecta.suiteauditoria.engine;

import java.util.Collection;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.codehaus.jackson.map.ObjectMapper;

import py.com.konecta.suiteauditoria.entities.Activity;
import py.com.konecta.suiteauditoria.entities.Process;
import py.com.konecta.suiteauditoria.entities.ProcessInstance;
import py.com.konecta.suiteauditoria.services.ProcessInstanceService;
import py.com.konecta.suiteauditoria.services.ProcessManagementService;

/**
 * Esta clase representa al Auditor de Procesos. Su finalidad es la de registrar
 * todos los datos &uacute;tiles del contexto de invocaci&oacute; a un proceso.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcessAuditor {

	// transaccion a manejar durante el registro de auditoria
	@Resource
	private UserTransaction transaction;
	
	//servicios locales
	@EJB 
	private ProcessInstanceService processInstanceService;
	@EJB 
	private ProcessManagementService processManagementService;
	
	public ProcessAuditor() {
	}

	/**
	 * Este m&eacute;todo contiene todas las rutinas de auditor&iacute;a. Se
	 * encarga de serializar los atributos {@code arguments}, {@code callResult}
	 * y {@code stackTrace} para su posterior persistencia.
	 * 
	 * @param processInstance
	 *            Representa la instancia del proceso y sus actividades
	 * @param activities
	 *            Las actividades del proceso
	 */
	@Asynchronous
	public void doAudit(ProcessInstance processInstance,
			Collection<Activity> activities) {

		try {

			if (processInstance == null) {

				throw new IllegalArgumentException(
						"Se requiere la instancia del proceso");
			} else {

				if (processInstance.getProcess() == null) {
					throw new IllegalArgumentException(
							"Se debe especificar un Proceso al cual pertenece la instacia");
				}

			}

			if (activities == null || activities.size() == 0) {

				throw new IllegalArgumentException(
						"Se requiere al menos un Activity");

			}

			transaction.begin();
			Process auditTargetProcess = processInstance.getProcess();

			// acceso thread safe al registro de procesos
			auditTargetProcess = processManagementService
					.registerProcessImmediately(auditTargetProcess);

			processInstance.setProcess(auditTargetProcess);

			// cocinamos los raw datas de las actividades
			ObjectMapper mapper = new ObjectMapper();
			for (Activity activity : activities) {
				if (activity != null) {

					// input
					if (activity.getRawInput() != null
							&& activity.getRawInput().length > 0) {

						String argumentsArrayRepresentation = mapper
								.writeValueAsString(activity.getRawInput());

						activity.setInput(argumentsArrayRepresentation
								.getBytes());
					}

					// stack trace u output
					if (activity.getRawStackTrace() != null
							&& activity.getRawStackTrace().length > 0) {

						String stackTraceRepresentation = mapper
								.writeValueAsString(activity.getRawStackTrace());

						activity.setStackTrace(stackTraceRepresentation
								.getBytes());

					} else if (activity.getRawOutput() != null) {

						String outputRepresentation = mapper
								.writeValueAsString(activity.getRawOutput());
						activity.setOutput(outputRepresentation.getBytes());
					}
				}
			}

			processInstance.setActivities(activities);

			// persistimos la instancia del proceso
			processInstanceService.create(processInstance);

			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();

			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
