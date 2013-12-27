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
package py.com.konecta.suiteauditoria.web.resources;


public interface ResourceInterface {
	static final String START_PARAM_NAME = "start";
	static final String END_PARAM_NAME = "end";
	static final String START_PARAM_DEFAULT_VALUE = "0";
	static final String END_PARAM_DEFAULT_VALUE = "30";
	static final String PAGINATION_MAX_SIZE = "4000";
	static final String PAGINATION_ALL_ITEM = "-1";
	static final String SINCE_ID_PARAM_NAME = "since_id";
}
