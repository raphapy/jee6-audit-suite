package py.com.konecta.suiteauditoria.entities.config;

/**
 * Esta interfaz centraliza algunas configuraciones com&uacute;nes entre las clases del
 * modelo.
 * 
 * @author Rafael E. Benegas - rbenegas@konecta.com.py
 * 
 */
public interface ModelCommonConfiguration {
	//tamanho max para campos blob
	static final int BLOB_FIELD_LENGTH = 100000;
	static final int AUDIT_TRAIL_ENTRY_LENGHT = 50;
}
