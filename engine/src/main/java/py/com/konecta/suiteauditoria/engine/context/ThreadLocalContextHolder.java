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
package py.com.konecta.suiteauditoria.engine.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContextHolder {

	private static final ThreadLocal<Map<String, Object>> THREAD_WITH_CONTEXT = new ThreadLocal<Map<String, Object>>();

	private ThreadLocalContextHolder() {
	}
	
	private static void initContextHolder() {
		if (THREAD_WITH_CONTEXT.get() == null) {
			THREAD_WITH_CONTEXT.set(new HashMap<String, Object>());
		}
	}
	public static void put(String key, Object payload) {
		initContextHolder();
		THREAD_WITH_CONTEXT.get().put(key, payload);
	}

	public static Object get(String key) {
		initContextHolder();
		return THREAD_WITH_CONTEXT.get().get(key);
	}

	public static void cleanupThread() {
		initContextHolder();
		THREAD_WITH_CONTEXT.remove();
	}

	public static boolean containsKey(Object key) {
		initContextHolder();
		return THREAD_WITH_CONTEXT.get().containsKey(key);
	}

	public static boolean containsValue(Object value) {
		initContextHolder();
		return THREAD_WITH_CONTEXT.get().containsValue(value);
	}
}
