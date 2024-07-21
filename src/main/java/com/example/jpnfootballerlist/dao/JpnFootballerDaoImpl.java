package com.example.jpnfootballerlist.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.jpnfootballerlist.common.Utils;
import com.example.jpnfootballerlist.entity.JpnFootballer;
import com.example.jpnfootballerlist.entity.JpnFootballer_;
import com.example.jpnfootballerlist.form.JpnFootballerQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JpnFootballerDaoImpl implements JpnFootballerDao{
	
	private final EntityManager entityManager;
	
	//JPQLによる検索
	@Override
	public List<JpnFootballer> findByJPQL(JpnFootballerQuery jpnFootballerQuery){
		//ここを"JpnFootballer"にすると実行時エラーになる
		StringBuilder sb = new StringBuilder("select t from JpnFootballer t where 1 = 1");
		List<Object> params = new ArrayList<>();
		int pos = 0;
		
		//実行するJPQLの組み立て
		//名前
		if(jpnFootballerQuery.getName().length() > 0) {
			sb.append(" and t.name like ?" + (++pos));
			params.add("%" + jpnFootballerQuery.getName() + "%");
		}
		//所属チーム
		if(jpnFootballerQuery.getTeam().length() > 0) {
			sb.append(" and t.team = ?" + (++pos));
			params.add("%" + jpnFootballerQuery.getTeam() + "%");
		}
		//年齢以下
		if(jpnFootballerQuery.getAge() != null) {
			sb.append(" and t.age <= ?" + (++pos));
			params.add( jpnFootballerQuery.getAge());
		}
		//生年月日
		if(!jpnFootballerQuery.getBorn().equals("")) {
			sb.append(" and t.born = ?" + (++pos));
			params.add(Utils.str2date(jpnFootballerQuery.getBorn()));
		}
		//身長以上
		if(jpnFootballerQuery.getHeightBottom() != null) {
			sb.append(" and t.height >= ?" + (++pos));
			params.add( jpnFootballerQuery.getHeightBottom());
		}
		//身長以下
		if(jpnFootballerQuery.getHeightTop() != null) {
			sb.append(" and t.height <= ?" + (++pos));
			params.add( jpnFootballerQuery.getHeightTop());
		}
		//ポジション
		if(jpnFootballerQuery.getPosition() != -1) {
			sb.append(" and t.position = ?" + (++pos));
			params.add( jpnFootballerQuery.getPosition());
		}
		//市場価値以上
		if(jpnFootballerQuery.getMarketValue() != null) {
			sb.append(" and t.marketValue >= ?" + (++pos));
			params.add( jpnFootballerQuery.getMarketValue());
		}
		//利き足
		if(jpnFootballerQuery.getFoot() != -1) {
			sb.append(" and t.foot = ?" + (++pos));
			params.add( jpnFootballerQuery.getFoot());
		}
		//order
		sb.append(" order by id");
		
		Query query = entityManager.createQuery(sb.toString());
		for(int i = 0; i < params.size(); ++i) {
			query = query.setParameter(i + 1, params.get(i));
		}
		
		@SuppressWarnings("unchecked")
		List<JpnFootballer> list = query.getResultList();
		return list;
	}
	
	
	//Criteria APIによる検索
	@Override
	public Page<JpnFootballer> findByCriteria(JpnFootballerQuery jpnFootballerQuery, Pageable pageable){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JpnFootballer> query = builder.createQuery(JpnFootballer.class);
		Root<JpnFootballer> root = query.from(JpnFootballer.class);
		List<Predicate> predicates = new ArrayList<>();
		
		//名前
		String name = "";
		if(jpnFootballerQuery.getName().length() > 0) {
			name = "%" + jpnFootballerQuery.getName() + "%";
		} else {
			name = "%";
		}
		predicates.add(builder.like(root.get(JpnFootballer_.NAME), name));
		
		//所属チーム
		if(jpnFootballerQuery.getTeam().length() > 0) {
			predicates.add(
					builder.and(
							builder.like(
									root.get(JpnFootballer_.TEAM), "%" + jpnFootballerQuery.getTeam() + "%")));
		}
		
		//年齢以下
		if(jpnFootballerQuery.getAge() != null) {
			predicates.add(
					builder.and(
							builder.lessThanOrEqualTo(
									root.get(JpnFootballer_.AGE), jpnFootballerQuery.getAge())));
		}
		
		//生年月日
		if(!jpnFootballerQuery.getBorn().equals("")) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(JpnFootballer_.BORN), Utils.str2date(jpnFootballerQuery.getBorn()))));
		}
		
		//身長以上
		if(jpnFootballerQuery.getHeightBottom() != null) {
			predicates.add(
					builder.and(
							builder.greaterThanOrEqualTo(
									root.get(JpnFootballer_.HEIGHT), jpnFootballerQuery.getHeightBottom())));
		}
		
		//身長以下
		if(jpnFootballerQuery.getHeightTop() != null) {
			predicates.add(
					builder.and(
							builder.lessThanOrEqualTo(
									root.get(JpnFootballer_.HEIGHT), jpnFootballerQuery.getHeightTop())));
		}
		
		//ポジション
		if(jpnFootballerQuery.getPosition() != -1) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(JpnFootballer_.POSITION), jpnFootballerQuery.getPosition())));
		}
		
		//市場価値以上
		if(jpnFootballerQuery.getMarketValue() != null) {
			predicates.add(
					builder.and(
							builder.greaterThanOrEqualTo(
									root.get(JpnFootballer_.MARKET_VALUE), jpnFootballerQuery.getMarketValue())));
		}
		
		//利き足
		if(jpnFootballerQuery.getFoot() != -1) {
			predicates.add(
					builder.and(
							builder.equal(
									root.get(JpnFootballer_.FOOT), jpnFootballerQuery.getFoot())));
		}
		
		//SELECT作成
		Predicate[] predArray = new Predicate[predicates.size()];
		predicates.toArray(predArray);
		query = query.select(root).where(predArray).orderBy(builder.asc(root.get(JpnFootballer_.id)));
		
		//クエリ生成
		TypedQuery<JpnFootballer> typedQuery = entityManager.createQuery(query);
		//該当レコード数取得
		int totalRows = typedQuery.getResultList().size();
		//先頭レコードの位置設定
		typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		//１ページ当たりの件数
		typedQuery.setMaxResults(pageable.getPageSize());
		//検索
		Page<JpnFootballer> page = new PageImpl<JpnFootballer>(typedQuery.getResultList(), pageable, totalRows);
		
		//検索
		//List<JpnFootballer> list = entityManager.createQuery(query).getResultList();
		
		return page;
		
	}
}
