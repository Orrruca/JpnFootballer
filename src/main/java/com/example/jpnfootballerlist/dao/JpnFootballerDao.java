package com.example.jpnfootballerlist.dao;

import java.util.List;

import com.example.jpnfootballerlist.entity.JpnFootballer;
import com.example.jpnfootballerlist.form.JpnFootballerQuery;

public interface JpnFootballerDao {
	
	//JPQLによる検索
	List<JpnFootballer> findByJPQL(JpnFootballerQuery jpnFootballerQuery);
	
	//Criteria APIによる検索
	List<JpnFootballer> findByCriteria(JpnFootballerQuery jpnFootballerQuery);

}
