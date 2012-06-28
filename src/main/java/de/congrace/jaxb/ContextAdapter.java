package de.congrace.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.congrace.model.Context;
import de.congrace.model.Identifier;

/**
 * Helper class for JAXB Serialization/deserialization in order to keep the model immutable
 * @author fasseg
 *
 */
public class ContextAdapter extends XmlAdapter<ContextAdapter.ContextValue, Context> {
	public ContextValue marshal(Context v) throws Exception {
		ContextValue val = new ContextValue();
		val.id = v.getId();
		val.name = v.getName();
		return val;
	};

	@Override
	public Context unmarshal(ContextValue v) throws Exception {
		return new Context.Builder(v.name).id(v.id).build();
	}

	static class ContextValue {
		private Identifier id;
		private String name;

		private ContextValue() {
			super();
		}
	}
}
