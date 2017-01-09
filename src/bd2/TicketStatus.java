package bd2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "statusy_biletow")
public class TicketStatus {
	private byte id;
	private String status;
	
	@Id
	@Column(name="id_statusu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public byte getId() {
		return id;
	}
	public void setId(byte id) {
		this.id = id;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
