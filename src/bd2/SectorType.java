package bd2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="typy_sektora")
public class SectorType {
	private byte id;
	private String typeName;
	
	@Id
	@Column(name="id_typu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	
	@Column(name="nazwa_typu")
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
