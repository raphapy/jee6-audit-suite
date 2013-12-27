package py.com.konecta.suiteauditoria.engine.context;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import py.com.konecta.suiteauditoria.engine.interceptors.ProcessAuditInterceptor;

/**
 * Esta clase representa al contexto de auditor&iacute;a y permite el acceso a
 * los diferentes componentes del mismo.
 * 
 * En esta clase se centralizan datos comunes entre los componentes dentro del
 * contexto de auditor&iacute;a.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 */
@Stateless(name = "AuditContext")
public class AuditContext {
	public static final String CALLER_ACTIVITY = "CA";
	public static final String PROCESS_INSTANCE = "PI";
	public static final String PROCESS_ACTIVITIES = "PA";

	/**
	 * Este m&eacute;todo solicita al motor que se ingrese informaci&oacute;n
	 * adicional y se lo asocie a la auditor&iacute; en curso.
	 * 
	 * @param entry
	 *            Representa informaci&oacute; adicional durante el proceso de
	 *            auditor&iacute;a. Se truncar&aacute; su contenido a
	 *            {@code ModelCommonConfiguration.AUDIT_TRAIL_ENTRY_LENGHT}
	 */
	@Interceptors({ ProcessAuditInterceptor.class })
	public void addAuditTrailEntry(String entry) {
		// cuerpo del metodo queda en blanco adredemente pues la informacion
		// sera tomada por el interceptor
	}
}
