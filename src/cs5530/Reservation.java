package cs5530;

public class Reservation {
	private int hid;
	private java.util.Date from;
	private java.util.Date to;
	private double price;
	
	public Reservation(int hid, java.util.Date from, java.util.Date to, double price) {
		this.hid = hid;
		this.from = from;
		this.to = to;
		this.price = price;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getHid() {
		return hid;
	}

	public void setHid(int hid) {
		this.hid = hid;
	}

	public java.util.Date getFrom() {
		return from;
	}

	public void setFrom(java.util.Date from) {
		this.from = from;
	}

	public java.util.Date getTo() {
		return to;
	}

	public void setTo(java.util.Date to) {
		this.to = to;
	}
}