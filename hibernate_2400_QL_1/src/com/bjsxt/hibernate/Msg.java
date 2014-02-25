package com.bjsxt.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Msg {
	private int id;
	private String cont;
	private Topic topic;
	
	public String getCont() {
		return cont;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	@ManyToOne
	public Topic getTopic() {
		return topic;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
}
