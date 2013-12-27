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
package py.com.konecta.suiteauditoria.entities.config;

/**
 * Esta interfaz centraliza algunas configuraciones com&uacute;nes entre las clases del
 * modelo.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
public interface ModelCommonConfiguration {
	//tamanho max para campos blob
	static final int BLOB_FIELD_LENGTH = 100000;
	static final int AUDIT_TRAIL_ENTRY_LENGHT = 50;
}
