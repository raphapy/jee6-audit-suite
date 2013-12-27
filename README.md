#jee6-audit-suite
A simple suite for auditing tasks of business methods based in Java EE6.
##Abstract
For the auditor, business method represents a "Process" and, in turn, business methods invoked from the "Process" are known as "Activities of the process".
<br>The audit of a process results in the persistence of a "process instance and its related activities." Each activity is stored along with its input, output and stack trace (if exceptions occur at runtime). The input, output and stack trace are serialized to JSON and stored in binary representation.
###Features:
This suite provides the following:
* Audit of business methods using simple java annotations.
* Persistence of audit data.
* A simple REST API for access to audit data.

The audit mechanism is based on JEE6 Interceptors, so this suite is intended for use in an Java EE6 compliant application server to audit business methods implemented with technologies such as EJB and CDI.
##Como auditar procesos?
###Configurando el entorno.
Se requiere una Unidad de Persistencia JPA con el nombre "AuditorPU" que se encuentre enlazado a un Datasource configurado con un usuario que tenga permisos para crear el esquema de auditoria(unas pocas tablas).
###Definiendo objetivos de auditoria
####Ejemplo con EJB:
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
####Ejemplo con CDI:
```java
@RequestScoped
public class MyCDIBean {
    
    @Inject
    MyEJBService s;
    
    @Process(name="Example Name for Process")
    public Output businessMethodInCDI(Input i) {
      
      //This method is a defined process but in this case is, implicitly, an activity
      s.otherBusinessMethod(i);
      //call of process activity
      s.doSomething(i);
    } 
}
```

License
----

APACHE v2.0
