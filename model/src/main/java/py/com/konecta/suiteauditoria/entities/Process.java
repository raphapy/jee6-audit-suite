package py.com.konecta.suiteauditoria.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Esta entidad representa a la tabla PROCESS, en ella se almacenan los procesos
 * a auditar.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@Entity
@Table(name = "PROCESS", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"PROCESS_NAME", "IMPL_CLASS_NAME", "IMPL_METHOD_NAME" }) })
@NamedQueries(value = { 
		@NamedQuery(name = "findProcessByNameAndImplementDetail", 
				query = "SELECT p FROM Process p " +
						"WHERE p.processName=:pProcessName " +
						"AND p.implClassName=:pImplClassName " +
						"AND p.implMethodName=:pImplMethodName") })
public class Process implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROCESS_ID", nullable = false)
	private Long processId;
	@Column(name = "PROCESS_NAME", nullable = false)
	private String processName;
	@Column(name = "IMPL_CLASS_NAME", nullable = false)
	private String implClassName;
	@Column(name = "IMPL_METHOD_NAME", nullable = false)
	private String implMethodName;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "process", cascade = { CascadeType.ALL })
	private Collection<ProcessInstance> instances;

	public Process() {
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
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
	
	@JsonIgnore
	public Collection<ProcessInstance> getInstances() {
		return instances;
	}

	public void setInstances(Collection<ProcessInstance> instances) {
		this.instances = instances;
	}
}
