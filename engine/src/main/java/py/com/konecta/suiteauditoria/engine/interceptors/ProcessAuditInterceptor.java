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
package py.com.konecta.suiteauditoria.engine.interceptors;

import static py.com.konecta.suiteauditoria.engine.context.AuditContext.CALLER_ACTIVITY;
import static py.com.konecta.suiteauditoria.engine.context.AuditContext.PROCESS_ACTIVITIES;
import static py.com.konecta.suiteauditoria.engine.context.AuditContext.PROCESS_INSTANCE;
import static py.com.konecta.suiteauditoria.engine.context.ThreadLocalContextHolder.cleanupThread;
import static py.com.konecta.suiteauditoria.engine.context.ThreadLocalContextHolder.get;
import static py.com.konecta.suiteauditoria.engine.context.ThreadLocalContextHolder.put;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import py.com.konecta.suiteauditoria.engine.ProcessAuditor;
import py.com.konecta.suiteauditoria.engine.context.AuditContext;
import py.com.konecta.suiteauditoria.entities.Activity;
import py.com.konecta.suiteauditoria.entities.AuditTrail;
import py.com.konecta.suiteauditoria.entities.Process;
import py.com.konecta.suiteauditoria.entities.ProcessInstance;
import py.com.konecta.suiteauditoria.entities.config.ModelCommonConfiguration;

/**
 * Esta clase representa al interceptor utilizado por el motor de auditoria para
 * capturar las invocaciones a los m&eacute;todos de clases EJB seg&uacute;n lo
 * especificado en "Interceptors Spec for Java EE" {@link http
 * ://docs.oracle.com/javaee/6/tutorial/doc/gkeed.html}
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */

public class ProcessAuditInterceptor {

	@EJB
	private ProcessAuditor auditor;

	public ProcessAuditInterceptor() {
	}

	/**
	 * Este m&eacute;todo es invocado autom&aacute;ticamente por el servidor de
	 * aplicaciones cuando se utilice a la clase como interceptora.
	 * 
	 * @param context
	 *            Este par&aacute;metro es inyectado por el servidor de
	 *            aplicaciones.
	 * @return Retorna un Object que proviene de la respuesta del m&eacute;todo
	 *         auditado. Cuando el m&eacute;todo auditado es de tipo
	 *         {@code void}, se retorna {@code null}.
	 * @throws Throwable
	 *             Propagaci&oacute;n de cualquier objeto que implemente
	 *             {@code Throwable}. Siempre ser&aacute; una propagaci&oacute;n
	 *             proveniente desde el m&eacute;todo auditado.
	 */
	@SuppressWarnings("unchecked")
	@AroundInvoke
	public Object doInvokeInterception(InvocationContext context)
			throws Throwable {

		Object invokeResult = null;
		py.com.konecta.suiteauditoria.engine.annotations.Process processAnnotation = null;
		py.com.konecta.suiteauditoria.engine.annotations.Activity activityAnnotation = null;
		Activity caller = null;

		Process process = null;
		ProcessInstance processInstance = null;
		Activity currentActivity = null;
		Collection<Activity> activities = null;

		try {

			// Se busca las anotaciones significativas para nuestro contexto de
			// auditoria. @Process y @Activity
			processAnnotation = context
					.getMethod()
					.getAnnotation(
							py.com.konecta.suiteauditoria.engine.annotations.Process.class);

			activityAnnotation = context
					.getMethod()
					.getAnnotation(
							py.com.konecta.suiteauditoria.engine.annotations.Activity.class);

			// Se obtiene el "llamante" desde el context holder
			caller = (Activity) get(CALLER_ACTIVITY);

			// Si no existe un llamante y el metodo interceptado es un @Process
			if (caller == null && processAnnotation != null) {

				// preparamos la instancia del proceso y la actividad por
				// defecto que es el proceso por sí mismo
				process = new Process();
				process.setProcessName(processAnnotation.name());
				process.setImplClassName(context.getTarget().getClass()
						.getCanonicalName());
				process.setImplMethodName(context.getMethod().getName());

				processInstance = new ProcessInstance();
				processInstance.setInitTimestamp(Calendar.getInstance());
				processInstance.setProcess(process);

				// guardamos la instancia del proceso en el contexto
				put(PROCESS_INSTANCE, processInstance);

				// actividad por defecto, es el proceso mismo
				currentActivity = new Activity();
				currentActivity.setImplClassName(context.getTarget().getClass()
						.getCanonicalName());
				currentActivity
						.setImplMethodName(context.getMethod().getName());
				currentActivity.setProcessInstance(processInstance);
				currentActivity.setInitTimestamp(Calendar.getInstance());
				currentActivity
						.setRawInput(Arrays.copyOf(context.getParameters(),
								context.getParameters().length));

				// aregamos a la lista de activities
				activities = new ArrayList<Activity>();
				activities.add(currentActivity);

				// agregamos la lista de activities al contexto
				put(PROCESS_ACTIVITIES, activities);

				// agregamos al activity actual como caller.
				put(CALLER_ACTIVITY, currentActivity);

				// invocamos al metodo
				invokeResult = context.proceed();

				currentActivity.setRawOutput(invokeResult);

				// si no, si existe un "llamante" y el metodo interceptado es un
				// @Activity
			} else if (caller != null && activityAnnotation != null) {

				// recuperamos la instancia del proceso
				processInstance = (ProcessInstance) get(PROCESS_INSTANCE);

				// actividad por defecto, es el proceso mismo
				currentActivity = new Activity();
				currentActivity.setImplClassName(context.getTarget().getClass()
						.getCanonicalName());
				currentActivity
						.setImplMethodName(context.getMethod().getName());
				currentActivity.setProcessInstance(processInstance);
				currentActivity.setInitTimestamp(Calendar.getInstance());
				currentActivity
						.setRawInput(Arrays.copyOf(context.getParameters(),
								context.getParameters().length));
				// seteamos el Activity padre
				currentActivity.setParentActivity(caller);

				activities = (ArrayList<Activity>) get(PROCESS_ACTIVITIES);
				activities.add(currentActivity);

				// agregamos al activity actual como caller.
				put(CALLER_ACTIVITY, currentActivity);

				// invocamos al metodo
				invokeResult = context.proceed();

				currentActivity.setRawOutput(invokeResult);

				// sino, si se invoco al servicio addAuditTrailEntry
			} else if (caller != null
					&& context.getTarget() instanceof AuditContext
					&& context.getMethod().equals(
							AuditContext.class.getMethod("addAuditTrailEntry",
									java.lang.String.class))) {

				String entry = (String) context.getParameters()[0];

				if (entry != null && entry.length() > 0) {

					// truncamos la entrada
					if (entry.length() > ModelCommonConfiguration.AUDIT_TRAIL_ENTRY_LENGHT) {
						entry = entry
								.substring(
										0,
										ModelCommonConfiguration.AUDIT_TRAIL_ENTRY_LENGHT);
					}

					Collection<AuditTrail> auditTrailEntries = ((Activity) get(CALLER_ACTIVITY))
							.getAuditTrailEntries();

					if (auditTrailEntries == null) {
						auditTrailEntries = new ArrayList<AuditTrail>();
					}

					AuditTrail auditTrail = new AuditTrail();
					auditTrail.setEntryTimestamp(Calendar.getInstance());
					auditTrail.setEntry(entry);
					auditTrail.setActivity((Activity) get(CALLER_ACTIVITY));

					auditTrailEntries.add(auditTrail);

					((Activity) get(CALLER_ACTIVITY))
							.setAuditTrailEntries(auditTrailEntries);
				}

				// sino, solo proceder a la llamada del metodo
			} else {

				return context.proceed();

			}

		} catch (Throwable t) {

			// si es un proceso o una actividad
			if ((caller == null && processAnnotation != null)
					|| (caller != null && activityAnnotation != null)) {

				currentActivity.setRawStackTrace(Arrays.copyOf(
						t.getStackTrace(), t.getStackTrace().length));
			}

			throw t;

		} finally {

			// restauramos el caller
			put(CALLER_ACTIVITY, caller);

			// si es un proceso solicitamos la auditoria
			if (caller == null && processAnnotation != null) {

				try {

					// limpiamos el contexto
					cleanupThread();

					// procesamos la auditoria asincronamente
					auditor.doAudit(processInstance, activities);

				} catch (Exception e) {
					// TODO: falta definir que hacer si la auditoria falla.
					// Analizar la posibilidad de un metodo de registro de
					// contingencia.
					e.printStackTrace();
				}

			}
		}

		return invokeResult;
	}
}
