package de.congrace.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import org.junit.Test;

import de.congrace.api.PersistenceService;
import de.congrace.model.Context;
import de.congrace.model.Identifier;
import de.congrace.model.Item;

import static org.junit.Assert.*;

public class PersitenceServiceTest {
	private PersistenceService service = new PersistenceServiceImpl(new FileSystemPersistenceImplementor("target/data"));
	private static final String SEPARATOR = "::-----------------------------------------------::";

	@Test
	public void testSaveContext() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		service.save(ctx, false);
	}

	@Test
	public void testSaveItem() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		Item itm = new Item.Builder("test-item-1", ctx.getId())
				.id(Identifier.randomIdentifier())
				.location(URI.create("file://example.com/content"))
				.build();
		service.save(itm, false);
	}

	@Test
	public void testLoadContext() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		service.save(ctx, false);
		Context ctxLoaded = service.load(ctx.getId(), Context.class);
		assertEquals(ctxLoaded, ctx);
	}

	@Test
	public void testLoadItem() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		Item itm = new Item.Builder("test-item-1", ctx.getId())
				.id(Identifier.randomIdentifier())
				.location(URI.create("file://example.com/content"))
				.build();
		service.save(itm, false);
		Item loaded = service.load(itm.getId(), Item.class);
		assertEquals(loaded, itm);
	}

	@Test
	public void testDeleteContext() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		service.save(ctx, false);
		service.delete(ctx.getId(), Context.class);
	}

	@Test(expected=IOException.class)
	public void testDeleteContextFail() throws Exception {
		Context ctx = new Context.Builder("test-context-1")
				.id(Identifier.randomIdentifier())
				.build();
		Item itm = new Item.Builder("test-item-1", ctx.getId())
				.id(Identifier.randomIdentifier())
				.location(URI.create("file://example.com/content"))
				.build();
		service.save(ctx, false);
		service.save(itm, false);
		// the context is in use so the following call should throw IOException
		service.delete(ctx.getId(), Context.class);
	}
}
