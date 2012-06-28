package de.congrace.api;

import java.io.IOException;

import de.congrace.model.Identifier;


/**
 * PersistenceService is the user interface for the persistence layer
 * @author fasseg
 *
 */
public interface PersistenceService {
	public <T> void save(T object,boolean overwrite) throws IOException;
	public <T> T load(Identifier id,Class<T> type) throws IOException;
	public void delete(Identifier id) throws IOException;
}
