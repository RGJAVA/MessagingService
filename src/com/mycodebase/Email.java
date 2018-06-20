package com.mycodebase;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Email {

	private String uID;
	private String from;
	private String to; 
	private String heading; 
	private String message; 
	private Date date;
	private String unread = "Yes"; 
	
	public String getUnread() {
		return unread;
	}
	public void setUnread(String unread) {
		this.unread = unread;
	}
	public String getFrom() {
		return from;
	}
	@XmlElement
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	@XmlElement
	public void setTo(String to) {
		this.to = to;
	}
	public String getHeading() {
		return heading;
	}
	@XmlElement
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public String getMessage() {
		return message;
	}
	@XmlElement
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	@XmlElement
	public void setDate(Date date) {
		this.date = date;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	} 
}
