package py.com.konecta.suiteauditoria.entities;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Esta entidad representa a la tabla ACTIVITY, en ella se almacenan todas las
 * actividades que forman parte del proceso.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@Entity
@Table(name = "ACTIVITY")
@NamedQueries(value = { 
		@NamedQuery(name = "findByProcessInstanceId", 
				query = "SELECT a FROM Activity a " +
						"WHERE a.processInstance.processInstanceId=:pProcessInstanceId") })
public class Activity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACTIVITY_ID")
	private Long activityId;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "PROCESS_INSTANCE_ID", referencedColumnName = "PROCESS_INSTANCE_ID", nullable = false)
	private ProcessInstance processInstance;
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_ACTIVITY", referencedColumnName = "ACTIVITY_ID", nullable = true)
	private Activity parentActivity;
	@Column(name = "IMPL_CLASS_NAME", nullable = false)
	private String implClassName;
	@Column(name = "IMPL_METHOD_NAME", nullable = false)
	private String implMethodName;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INIT_TIMESTAMP", nullable = false)
	private Calendar initTimestamp;
	@Lob
	@Column(name = "INPUT")
	private byte[] input;
	@Lob
	@Column(name = "OUTPUT")
	private byte[] output;
	@Lob
	@Column(name = "STACK_TRACE")
	private byte[] stackTrace;
	@OneToMany(mappedBy = "parentActivity")
	private Collection<Activity> childrensActivities;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activity", cascade = { CascadeType.ALL })
	private Collection<AuditTrail> auditTrailEntries;

	/*
	 * Estos tres miembros almacenan datos del contexto de invocacion en forma
	 * cruda(tal cual llegan al contexto). Para mejorar el rendimiendo en tiempo
	 * de ejecucion estos formatos crudos son procesados solo antes de la
	 * persistencia. Estos miembros no son campos de la tabla.
	 */
	@Transient
	private Object[] rawInput;
	@Transient
	private Object rawOutput;
	@Transient
	private StackTraceElement[] rawStackTrace;

	public Activity() {
	}
	
	@PrePersist
	private void setDefaultValues() {
		if (this.initTimestamp == null) {
			this.initTimestamp = Calendar.getInstance();
		}
	}
	
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	@JsonIgnore
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	
	@JsonIgnore
	public Activity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(Activity parentActivity) {
		this.parentActivity = parentActivity;
	}

	public String getImplClassName() {
		return implClassName;
	}

	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}

	public String getImplMethodName() {
		return implMethodName;
	}

	public void setImplMethodName(String implMethodName) {
		this.implMethodName = implMethodName;
	}

	public Calendar getInitTimestamp() {
		return initTimestamp;
	}

	public void setInitTimestamp(Calendar initTimestamp) {
		this.initTimestamp = initTimestamp;
	}

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

	public byte[] getOutput() {
		return output;
	}

	public void setOutput(byte[] output) {
		this.output = output;
	}

	public byte[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(byte[] stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	@JsonIgnore
	public Collection<Activity> getChildrensActivities() {
		return childrensActivities;
	}

	public void setChildrensActivities(Collection<Activity> childrensActivities) {
		this.childrensActivities = childrensActivities;
	}
	
	@JsonIgnore
	public Collection<AuditTrail> getAuditTrailEntries() {
		return auditTrailEntries;
	}

	public void setAuditTrailEntries(Collection<AuditTrail> auditTrailEntries) {
		this.auditTrailEntries = auditTrailEntries;
	}
	
	@JsonIgnore
	public Object[] getRawInput() {
		return rawInput;
	}

	public void setRawInput(Object[] rawInput) {
		this.rawInput = rawInput;
	}
	
	@JsonIgnore
	public Object getRawOutput() {
		return rawOutput;
	}

	public void setRawOutput(Object rawOutput) {
		this.rawOutput = rawOutput;
	}
	
	@JsonIgnore
	public StackTraceElement[] getRawStackTrace() {
		return rawStackTrace;
	}

	public void setRawStackTrace(StackTraceElement[] rawStackTrace) {
		this.rawStackTrace = rawStackTrace;
	}
}
