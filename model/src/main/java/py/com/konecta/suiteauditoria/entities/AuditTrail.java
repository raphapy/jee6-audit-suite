package py.com.konecta.suiteauditoria.entities;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import py.com.konecta.suiteauditoria.entities.config.ModelCommonConfiguration;


/**
 * Esta entidad representa a la tabla AUDIT_TRAIL, en ella se almacenan las
 * entradas de auditor&iacute;a que se ingresan de manera expl&iacute;cita a
 * trav&eacute;s del motor.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@Entity
@Table(name = "AUDIT_TRAIL")
@NamedQueries(value = { 
		@NamedQuery(name = "findByActivityId", 
				query = "SELECT at FROM AuditTrail at " +
						"WHERE at.activity.activityId=:pActivityId") })
public class AuditTrail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "AUDIT_TRAIL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auditTrailId;
	@ManyToOne(optional = false)
	@JoinColumn(name = "ACTIVITY_ID", referencedColumnName = "ACTIVITY_ID", nullable = false)
	private Activity activity;
	@Column(name = "ENTRY", nullable = false, length = ModelCommonConfiguration.AUDIT_TRAIL_ENTRY_LENGHT)
	private String entry;
	@Column(name = "ENTRY_TIMESTAMP", nullable = false)
	private Calendar entryTimestamp;

	public AuditTrail() {
	}

	@PrePersist
	private void setDefaultValues() {
		if (this.entryTimestamp == null) {
			this.entryTimestamp = Calendar.getInstance();
		}
	}

	public Long getAuditTrailId() {
		return auditTrailId;
	}

	public void setAuditTrailId(Long auditTrailId) {
		this.auditTrailId = auditTrailId;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Calendar getEntryTimestamp() {
		return entryTimestamp;
	}

	public void setEntryTimestamp(Calendar entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}
}
