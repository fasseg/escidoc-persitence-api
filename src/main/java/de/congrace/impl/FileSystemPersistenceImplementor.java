package de.congrace.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import de.congrace.api.PersistenceImplementor;
import de.congrace.model.Context;
import de.congrace.model.Identifier;
import de.congrace.model.Item;

/**
 * Realization of a PersistenceImplementor for FileSystem storage
 * 
 * @author ruckus
 * 
 */
public final class FileSystemPersistenceImplementor implements PersistenceImplementor {
	private static File getDirectory(final String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		} else if (!f.isDirectory() || !f.canWrite() || !f.canRead()) {
			throw new RuntimeException("unable to write to directory " + f.getAbsolutePath());
		}
		return f;
	}

	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;
	private final File directory;
	private final File contextDirectory;

	private final File itemDirectory;

	FileSystemPersistenceImplementor(final String path) {
		super();
		try {
			JAXBContext ctx = JAXBContext.newInstance(Context.class, Identifier.class, Item.class);
			this.marshaller = ctx.createMarshaller();
			this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			this.unmarshaller = ctx.createUnmarshaller();
			this.directory = FileSystemPersistenceImplementor.getDirectory(path);
			this.contextDirectory = FileSystemPersistenceImplementor.getDirectory(path + "/context");
			this.itemDirectory = FileSystemPersistenceImplementor.getDirectory(path + "/item");
		} catch (JAXBException jaxb) {
			throw new RuntimeException("Unable to instantiate Marshaller", jaxb);
		}
	}

	private void assertDirectoryAccesible(File dir) throws IOException {
		if (!dir.exists() || !dir.canRead() || !dir.canWrite() || !dir.isDirectory()) {
			throw new IOException("Unable to read/write to item directory " + itemDirectory.getAbsolutePath());
		}
	}

	public void delete(Identifier id) {

	}

	public <T> T load(Identifier id, Class<T> type) throws IOException {
		if (type == Context.class) {
			return (T) loadContext(id);
		} else if (type == Item.class) {
			return (T) loadItem(id);
		} else {
			throw new IOException("Unable to load objects of type " + type.getName());
		}
	}

	private Context loadContext(Identifier id) throws IOException {
		assertDirectoryAccesible(contextDirectory);
		InputStream is = null;
		try {
			is = new FileInputStream(new File(contextDirectory, id.getValue().toString() + ".xml"));
			return (Context) unmarshal(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private Item loadItem(Identifier id) throws IOException {
		assertDirectoryAccesible(itemDirectory);
		InputStream itemStream = null;
		try {
			itemStream = new FileInputStream(new File(itemDirectory, id.getValue().toString() + ".xml"));
			return (Item) unmarshal(itemStream);
		} finally {
			IOUtils.closeQuietly(itemStream);
		}
	}

	private void marshal(final Object blob, final OutputStream out) {
		try {
			marshaller.marshal(blob, out);
		} catch (JAXBException e) {
			throw new RuntimeException("Unable to marshall oject", e);
		}
	}

	public void save(final Object obj, final boolean overwrite) throws IOException {
		if (obj instanceof Context) {
			saveContext((Context) obj, overwrite);
		} else if (obj instanceof Item) {
			saveItem((Item) obj, overwrite);
		} else {
			throw new IOException("Unable to store objects of type " + obj.getClass());
		}
	}

	private void saveContext(final Context context, final boolean overwrite) throws IOException {
		final File f = new File(contextDirectory, context.getId().getValue().toString() + ".xml");
		assertDirectoryAccesible(contextDirectory);
		if (f.exists() && !overwrite) {
			throw new IOException(context.getId().toString() + " already exists in "
					+ contextDirectory.getAbsolutePath());
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			this.marshal(context, os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	private void saveItem(final Item item, final boolean overwrite) throws IOException {
		final File f = new File(itemDirectory, item.getId().getValue().toString() + ".xml");
		assertDirectoryAccesible(itemDirectory);
		if (f.exists() && !overwrite) {
			throw new IOException(item.getId().toString() + " already exists in "
					+ itemDirectory.getAbsolutePath());
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			this.marshal(item, os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

	private Object unmarshal(final InputStream is) {
		try {
			return unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			throw new RuntimeException("Unable to marshall oject", e);
		}
	}
}
