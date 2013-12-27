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

import static py.com.konecta.commons.utiles.ServiceUtils.validarArgumento;
import static py.com.konecta.commons.utiles.ServiceUtils.validarEncontrado;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import py.com.konecta.commons.exceptions.ApplicationException;
import py.com.konecta.commons.http.CustomMediaType;
import py.com.konecta.suiteauditoria.entities.Process;
import py.com.konecta.suiteauditoria.entities.ProcessInstance;
import py.com.konecta.suiteauditoria.services.ProcessInstanceService;
import py.com.konecta.suiteauditoria.services.ProcessService;
import py.com.konecta.suiteauditoria.web.Messages;

@Path("process")
@Produces(CustomMediaType.APPLICATION_JSON_UTF8)
@Consumes(CustomMediaType.APPLICATION_JSON_UTF8)
public class ProcessResource implements ReadableResource<Long> {

	@Inject
	private ProcessService processControl;
	@Inject
	private ProcessInstanceService processInstanceControl;

	public ProcessResource() {
	}

	@Override
	@GET
	public Response listItems(
			@QueryParam(START_PARAM_NAME) @DefaultValue(START_PARAM_DEFAULT_VALUE) Integer start,
			@QueryParam(END_PARAM_NAME) @DefaultValue(END_PARAM_DEFAULT_VALUE) Integer end)
			throws ApplicationException {

		// si se supera el limite de items
		validarArgumento(
				!(end.compareTo(Integer.parseInt(PAGINATION_MAX_SIZE)) > 0),
				Messages.getString(Messages.Default.PAGINATION_MAX_SIZE_EXCEEDED_MSG));

		List<Process> processList = processControl.findRange(new int[] { start,
				end });

		validarEncontrado(processList, Messages.getString("NOT_FOUND"));

		return Response.ok(processList).build();
	}

	@Override
	@GET
	@Path("{id}")
	public Response findItem(@PathParam("id") Long processId)
			throws ApplicationException {

		validarArgumento(processId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));

		Process processRes = processControl.find(processId);

		validarEncontrado(processRes, Messages.getString("NOT_FOUND"));

		return Response.ok(processRes).build();
	}

	@GET
	@Path("{id}/instances")
	public Response listInstances(
			@PathParam("id") Long processId,
			@QueryParam(START_PARAM_NAME) @DefaultValue(START_PARAM_DEFAULT_VALUE) Integer start,
			@QueryParam(END_PARAM_NAME) @DefaultValue(END_PARAM_DEFAULT_VALUE) Integer end)
			throws ApplicationException {

		validarArgumento(processId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));

		List<ProcessInstance> instances = processInstanceControl
				.findByProcessId(processId, start, end);

		validarEncontrado(instances, Messages.getString("NOT_FOUND"));

		return Response.ok(instances).build();
	}
}
