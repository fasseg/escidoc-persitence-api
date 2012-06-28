package de.congrace.impl;

import java.io.IOException;

import de.congrace.api.PersistenceImplementor;
import de.congrace.api.PersistenceService;
import de.congrace.model.Identifier;

/**
 * Implements the bridge pattern by decoupling abstraction and API
 * @author fasseg
 *
 */
public final class PersistenceServiceImpl implements PersistenceService {
	private final PersistenceImplementor implementor;

	public PersistenceServiceImpl(PersistenceImplementor implementor) {
		this.implementor = implementor;
	}

	public <T> void delete(Identifier id,Class<T> type) throws IOException {
		implementor.delete(id,type);
	}

	public <T> T load(Identifier id, Class<T> type) throws IOException {
		return implementor.load(id,type);
	}

	public <T> void save(T object,boolean overwrite) throws IOException {
		implementor.save(object,overwrite);
	};

}
