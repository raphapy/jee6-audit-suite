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
