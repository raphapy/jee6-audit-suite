package py.com.konecta.suiteauditoria.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotaci&oacute;n se utiliza para marcar a los m&eacute;todos de clases
 * EJB como procesos y proporcionar informaci&oacute;n adicional acerca de
 * &eacute;l.
 * <p>
 * En el contexto del motor de auditor&iacute;a, un proceso es cualquier
 * m&eacute;todo marcado con esta anotaci&oacute;n.
 * 
 * <p>
 * El atributo {@code name} representa al nombre del proceso.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Process {
	String name();
}
