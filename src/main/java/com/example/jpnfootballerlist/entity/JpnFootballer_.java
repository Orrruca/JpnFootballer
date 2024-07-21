package com.example.jpnfootballerlist.entity;

import java.sql.Date;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(JpnFootballer.class)
public abstract class JpnFootballer_ {

	public static volatile SingularAttribute<JpnFootballer, Integer> id;
	public static volatile SingularAttribute<JpnFootballer, String> name;
	public static volatile SingularAttribute<JpnFootballer, String> team;
	public static volatile SingularAttribute<JpnFootballer, Integer> age;
	public static volatile SingularAttribute<JpnFootballer, Date> born;
	public static volatile SingularAttribute<JpnFootballer, Integer> height;
	public static volatile SingularAttribute<JpnFootballer, Integer> position;
	public static volatile SingularAttribute<JpnFootballer, Integer> marketValue;
	public static volatile SingularAttribute<JpnFootballer, Integer> foot;
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TEAM = "team";
	public static final String AGE = "age";
	public static final String BORN = "born";
	public static final String HEIGHT = "height";
	public static final String POSITION = "position";
	public static final String MARKET_VALUE = "marketValue";
	public static final String FOOT = "foot";
	
}
