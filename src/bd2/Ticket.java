package bd2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.jar.Attributes.Name;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name="bilety")
public class Ticket {
	private int id;
	private Customer customer;
	private Seat seat;
	private Event event;
	private Date sellDate;
	private TicketStatus ticketStatus;
	private Carnet carnet;
	private BigDecimal price;
	private BigDecimal discount;

	@Id
	@Column(name="id_biletu")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="klienci_id_klienta")
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	@ManyToOne
	@JoinColumn(name="miejsca_id_miejsca")
	public Seat getSeat() {
		return seat;
	}
	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	@ManyToOne
	@JoinColumn(name="wydarzenia_id_wydarzenia")
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	@Column(name="data_sprzedazy")
	public Date getSellDate() {
		return sellDate;
	}
	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}
	@ManyToOne
	@JoinColumn(name="statusy_biletow_id_statusu")
	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	@ManyToOne
	@JoinColumn(name="karnety_id_karnetu")
	public Carnet getCarnet() {
		return carnet;
	}
	public void setCarnet(Carnet carnet) {
		this.carnet = carnet;
	}
	@Column(name="cena_biletu")
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@Column(name="uzyskany_upust")
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
}
