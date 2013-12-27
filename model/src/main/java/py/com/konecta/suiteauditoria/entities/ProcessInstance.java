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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Esta entidad representa a la tabla PROCESS_INSTANCE, en ella se almacenan
 * todas las invocaciones a procesos.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
@Entity
@Table(name = "PROCESS_INSTANCE")
@NamedQueries(value = { 
		@NamedQuery(name = "findByProcessId", 
				query = "SELECT i FROM ProcessInstance i " +
						"WHERE i.process.processId=:pProcessId") })
public class ProcessInstance implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCESS_INSTANCE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long processInstanceId;
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "PROCESS_ID", referencedColumnName = "PROCESS_ID", nullable = false)
	private Process process;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INIT_TIMESTAMP", nullable = false)
	private java.util.Calendar initTimestamp;
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "processInstance")
	private Collection<Activity> activities;

	public ProcessInstance() {
	}
	
	@PrePersist
	private void setDefaultValues() {
		if (this.initTimestamp == null) {
			this.initTimestamp = Calendar.getInstance();
		}
	}
	
	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public java.util.Calendar getInitTimestamp() {
		return initTimestamp;
	}

	public void setInitTimestamp(java.util.Calendar initTimestamp) {
		this.initTimestamp = initTimestamp;
	}
	
	@JsonIgnore
	public Collection<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}
}
