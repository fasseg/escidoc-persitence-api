package de.congrace.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Immutable Data transfer object
 * @author fasseg
 *
 */
@XmlRootElement(name="id")
public class Identifier {
	@XmlAttribute
	private final String value;

	public Identifier(String value) {
		super();
		this.value = value;
	}
	
	//JAXB needs this private constructor on root elements
	private Identifier(){
		super();
		this.value=null;
	}

	public String getValue() {
		return value;
	}
	
	public static Identifier randomIdentifier(){
		return new Identifier(UUID.randomUUID().toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identifier other = (Identifier) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}
