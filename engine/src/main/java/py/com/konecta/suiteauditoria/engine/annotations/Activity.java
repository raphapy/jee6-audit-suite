package py.com.konecta.suiteauditoria.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotaci&oacute;n se utiliza para marcar a los m&eacute;todos de clases
 * EJB como actividad de un proceso.
 * <p>
 * En el contexto del motor de auditor&iacute;a, un proceso es cualquier
 * m&eacute;todo marcado con la anotaci&oacute;n
 * {@code py.com.konecta.tools.suiteauditoria.annotations.Proccess}
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {
}
