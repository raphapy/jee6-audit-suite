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
