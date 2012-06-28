package de.congrace.api;

import java.io.IOException;

import de.congrace.model.Identifier;

/**
 * The implementor interface for the Persistence Service
 * @author ruckus
 *
 */
public interface PersistenceImplementor {
	public void save(Object blob,boolean overwrite) throws IOException;
	public void delete(Identifier id) throws IOException;
	public <T> T load(Identifier id,Class<T> type) throws IOException;
}
