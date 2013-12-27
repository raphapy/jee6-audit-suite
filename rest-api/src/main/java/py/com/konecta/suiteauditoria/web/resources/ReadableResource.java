package py.com.konecta.suiteauditoria.web.resources;

import javax.ws.rs.core.Response;

import py.com.konecta.commons.exceptions.ApplicationException;

/**
 * Esta interfaz puede aplicarse a recursos de solo lectura para permitir listar
 * sus items y buscar un unico elemento identificado por su id.
 * 
 * @author Rafael E. Benegas - <rbenegas@konecta.com.py>
 * 
 * @param <KT>
 *            Indica el tipo del id del recurso
 */
public interface ReadableResource<KT> extends ResourceInterface {

	public Response listItems(Integer start, Integer end) throws ApplicationException;

	public Response findItem(KT id) throws ApplicationException;
}
