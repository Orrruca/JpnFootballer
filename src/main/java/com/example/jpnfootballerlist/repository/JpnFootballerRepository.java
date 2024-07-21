package com.example.jpnfootballerlist.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpnfootballerlist.entity.JpnFootballer;

@Repository
public interface JpnFootballerRepository extends JpaRepository<JpnFootballer, Integer>{
	
	//名前の部分一致
	List<JpnFootballer> findByNameLike(String name);
	//所属チームの部分一致
	List<JpnFootballer> findByTeamLike(String team);
	//年齢が引数以下の選手、降順
	List<JpnFootballer> findByAgeLessThanEqualOrderByAgeDesc(Integer age);
	//生年月日の完全一致
	List<JpnFootballer> findByBorn(Date born);
	//身長が引数以上の選手、降順
	List<JpnFootballer> findByHeightGreaterThanEqualOrderByHeightDesc(Integer heightBottom);
	//身長が引数以下の選手、降順
	List<JpnFootballer> findByHeightLessThanEqualOrderByHeightDesc(Integer heightTop);
	//身長が引数１と２の間の選手、降順
	List<JpnFootballer> findByHeightBetweenOrderByHeightDesc(Integer heightBottom, Integer heightTop);
	//ポジションの完全一致
	List<JpnFootballer> findByPosition(Integer position);
	//市場価値が引数値より大きい選手、降順
	List<JpnFootballer> findByMarketValueGreaterThanEqualOrderByMarketValueDesc(Integer marketValue);
	//利き足の完全一致
	List<JpnFootballer> findByFoot(Integer foot);

}
