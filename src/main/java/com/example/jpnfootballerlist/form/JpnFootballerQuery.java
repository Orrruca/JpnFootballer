package com.example.jpnfootballerlist.form;

import lombok.Data;

@Data
public class JpnFootballerQuery {

	private String name;
	private String team;
	private Integer age;
	private String born;
	private Integer heightBottom;
	private Integer heightTop;
	private Integer position;
	private Integer marketValue;
	private Integer foot;
	
	public JpnFootballerQuery () {
		name = "";
		team = "";
		age = null;
		born = "";
		heightBottom = null;
		heightTop = null;
		position = -1;
		marketValue = null;
		foot = -1;
	}
	
}
