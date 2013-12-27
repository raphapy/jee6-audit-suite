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
import py.com.konecta.suiteauditoria.entities.AuditTrail;
import py.com.konecta.suiteauditoria.services.ActivityService;
import py.com.konecta.suiteauditoria.services.AuditTrailService;
import py.com.konecta.suiteauditoria.web.Messages;

@Path("process/instances/activities")
@Produces(CustomMediaType.APPLICATION_JSON_UTF8)
@Consumes(CustomMediaType.APPLICATION_JSON_UTF8)
public class ActivityResource implements ReadableResource<Long> {

	@Inject
	private ActivityService activityControl;
	@Inject
	private AuditTrailService auditTrailControl;
	
	public ActivityResource() {
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
		List<Activity> activities = activityControl.findRange(new int[] {
				start, end });
		validarEncontrado(activities, Messages.getString("NOT_FOUND"));

		return Response.ok(activities).build();
	}

	@Override
	@GET
	@Path("{id}")
	public Response findItem(@PathParam("id") Long activityId)
			throws ApplicationException {

		validarArgumento(activityId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));
		Activity activity = activityControl.find(activityId);
		validarEncontrado(activity, Messages.getString("NOT_FOUND"));

		return Response.ok(activity).build();
	}

	@GET
	@Path("{id}/audit-trails")
	public Response listActivities(
			@PathParam("id") Long activityId,
			@QueryParam(START_PARAM_NAME) @DefaultValue(START_PARAM_DEFAULT_VALUE) Integer start,
			@QueryParam(END_PARAM_NAME) @DefaultValue(END_PARAM_DEFAULT_VALUE) Integer end)
			throws ApplicationException {

		validarArgumento(activityId,
				Messages.getString(Messages.Default.REQUIRED_ID_MSG));

		List<AuditTrail> auditTrails = auditTrailControl
				.findByActivityId(activityId, start, end);

		validarEncontrado(auditTrails, Messages.getString("NOT_FOUND"));

		return Response.ok(auditTrails).build();
	}
}
