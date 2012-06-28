package de.congrace.api;

import java.io.IOException;

import de.congrace.model.Identifier;

public interface PersistenceService {
	public <T> void save(T object,boolean overwrite) throws IOException;
	public <T> T load(Identifier id,Class<T> type) throws IOException;
	public <T> void delete(Identifier id) throws IOException;
}
