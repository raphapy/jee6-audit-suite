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
package py.com.konecta.suiteauditoria.web;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	
	public interface Default {
		public static final String NOT_FOUND_MSG = "NOT_FOUND";
		public static final String INTERNAL_ERROR_MSG = "INTERNAL_ERROR";
		public static final String PAGINATION_MAX_SIZE_EXCEEDED_MSG = "PAG_MAX_SIZE_EXCEEDED";
		public static final String REQUIRED_ID_MSG = "REQUIRED_ID";
	}
	
	private static final String BUNDLE_NAME = "messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
