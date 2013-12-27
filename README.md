#jee6-audit-suite
A simple suite for auditing tasks of business methods based in Java EE6.
###Abstract
For the auditor, business method represents a "Process" and, in turn, business methods invoked from the "Process" are known as "Activities of the process".
<br>The audit of a process results in the persistence of a "process instance and its related activities." Each activity is stored along with its input, output and stack trace (if exceptions occur at runtime). The input, output and stack trace are serialized to JSON and stored in binary representation.
###Features:
This suite provides the following:
* Audit of business methods using simple java annotations.
* Persistence of audit data.
* A simple REST API for access to audit data.

The audit mechanism is based on JEE6 Interceptors, so this suite is intended for use in an Java EE6 compliant application server to audit business methods implemented with technologies such as EJB and CDI.
##How to audit processes?
###Setting the environment.
JPA Persistence Unit is required with the name "AuditorPU" that is bound to a Datasource configured with a user who has permissions to create audit scheme (a few tables).
<br>In the beans.xml file must be enabled Interceptor "py.com.konecta.suiteauditoria.DefaultAuditInterceptor".
###Defining audit targets.
####Example with EJB:
```java
@Stateless
public class MyEJBService {

    @EJB
    MyEJBService s;
    
    @Process(name="Example Name for Process")
    public Output businessMethod(Input i) {
      //This method is a defined process but in this case is, implicitly, an activity
      s.otherBusinessMethod(i);
      
    }
    
    @Process(name="Example Name for another Process")
    public void otherBusinessMethod(Input i) {
      ...
      //here, an activity of this process is called
      s.doSomething(i);
    }
    
    
    @Activity(name="Optional Name for acivity")
    public Output doSomething(Input i) {
      ...
    }
}
```
####Example with CDI:
```java
@RequestScoped
public class MyCDIBean {
    
    @Inject
    MyEJBService s;
    
    @Process(name="Example Name for Process")
    public Output businessMethodCallerInCDI(Input i) {
      
      //This method is a defined process but in this case is, implicitly, an activity
      s.otherBusinessMethod(i);
      //call of process activity
      s.doSomething(i);
    } 
}
```
###Releases:
This tool is still in development. Soon will come the first release candidate.
###License
Apache v2.0
