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
import py.com.konecta.suiteauditoria.entities.Activity;
import py.com.konecta.suiteauditoria.entities.ProcessInstance;
import py.com.konecta.suiteauditoria.services.ActivityService;
import py.com.konecta.suiteauditoria.services.ProcessInstanceService;
import py.com.konecta.suiteauditoria.web.Messages;

@Path("process/instances")
@Produces(CustomMediaType.APPLICATION_JSON_UTF8)
@Consumes(CustomMediaType.APPLICATION_JSON_UTF8)
public class ProcessInstanceResource implements ReadableResource<Long> {

	@Inject
	private ProcessInstanceService processInstanceControl;
	@Inject
	private ActivityService activityControl;

	public ProcessInstanceResource() {
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

		List<ProcessInstance> instances = processInstanceControl
				.findRange(new int[] { start, end });

		validarEncontrado(instances, Messages.getString("NOT_FOUND"));

		return Response.ok(instances).build();
	}

	@Override
	@GET
	@Path("{id}")
	public Response findItem(@PathParam("id") Long instanceId)
			throws ApplicationException {

		validarArgumento(instanceId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));

		ProcessInstance instance = processInstanceControl.find(instanceId);

		validarEncontrado(instance, Messages.getString("NOT_FOUND"));

		return Response.ok(instance).build();
	}

	@GET
	@Path("{id}/activities")
	public Response listActivities(
			@PathParam("id") Long instanceId,
			@QueryParam(START_PARAM_NAME) @DefaultValue(START_PARAM_DEFAULT_VALUE) Integer start,
			@QueryParam(END_PARAM_NAME) @DefaultValue(END_PARAM_DEFAULT_VALUE) Integer end)
			throws ApplicationException {

		validarArgumento(instanceId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));

		List<Activity> activities = activityControl
				.findByProcessInstanceId(instanceId, start, end);

		validarEncontrado(activities, Messages.getString("NOT_FOUND"));

		return Response.ok(activities).build();
	}
}
