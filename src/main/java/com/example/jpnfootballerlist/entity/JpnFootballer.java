package com.example.jpnfootballerlist.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="jpn_footballer")
@Data
public class JpnFootballer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="team")
	private String team;
	
	@Column(name="age")
	private Integer age;
	
	@Column(name="born")
	private Date born;
	
	@Column(name="height")
	private Integer height;
	
	//０GK/１DF/２MF/３FW
	@Column(name="position")
	private Integer position;
	
	@Column(name="market_value")
	private Integer marketValue;
	
	//０左利き/１右利き
	@Column(name="foot")
	private Integer foot;
	
}
