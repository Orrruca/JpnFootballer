package com.example.jpnfootballerlist.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.jpnfootballerlist.common.Utils;
import com.example.jpnfootballerlist.entity.JpnFootballer;
import com.example.jpnfootballerlist.form.JpnFootballerData;
import com.example.jpnfootballerlist.form.JpnFootballerQuery;
import com.example.jpnfootballerlist.repository.JpnFootballerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JpnFootballerService {
	
	private final JpnFootballerRepository jpnFootballerRepository;
	
	//新規登録と更新における入力チェック
	public boolean isValid(JpnFootballerData jpnFootballerData, BindingResult result) {
		boolean ans = true;
		
		//名前が全角スペースだけで構成されていたらエラー
		String name = jpnFootballerData.getName();
		if(name != null && !name.equals("")) {
			boolean isAllDoubleSpace = true;
			for(int i = 0; i < name.length(); i++) {
				if(name.charAt(i) != '　') {
					isAllDoubleSpace = false;
					break;
				}
			}
			if(isAllDoubleSpace) {
				FieldError fieldError = new FieldError(result.getObjectName(), "name", "件名が全角スペースです");
				result.addError(fieldError);
				ans = false;
			}
		}
		
		//生年月日が未来日付ならエラー
		String born = jpnFootballerData.getBorn();
		if(!born.equals("")) {
			LocalDate tody = LocalDate.now();
			LocalDate bornDate = null;
			try {
				bornDate = LocalDate.parse(born);
				if(tody.isBefore(bornDate)) {
					FieldError fieldError = new FieldError(result.getObjectName(), "born",
																				"生年月日は今日以前に設定してください");
					result.addError(fieldError);
					ans = false;
				}
			} catch(DateTimeException e) {
				FieldError fieldError = new FieldError(result.getObjectName(), "born",
						"生年月日を設定するときはyyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		return ans;
	}
	
	
	//一覧画面の検索における入力チェック
	public boolean isValid(JpnFootballerQuery jpnFootballerQuery, BindingResult result) {
		boolean ans = true;
		
		//生年月日の形式をチェック
		String born = jpnFootballerQuery.getBorn();
		if(!born.equals("")) {
			try {
				LocalDate.parse(born);
			} catch(DateTimeException e) {
				//parseできない場合
				FieldError fieldError = new FieldError(result.getObjectName(), "born",
																					"生年月日を入力するときはyyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		return ans;
	}
	
	
	//一覧画面における検索処理
	public List<JpnFootballer> doQuery(JpnFootballerQuery jpnFootballerQuery){
		List<JpnFootballer> jpnFootballerList = null;
		if(jpnFootballerQuery.getName().length() > 0) {
			//名前で部分一致検索
			jpnFootballerList = jpnFootballerRepository.findByNameLike("%" + jpnFootballerQuery.getName() + "%");
		} else if(jpnFootballerQuery.getTeam().length() > 0) {
			//所属チームで部分一致検索
			jpnFootballerList = jpnFootballerRepository.findByTeamLike("%" + jpnFootballerQuery.getTeam() + "%");
		} else if(jpnFootballerQuery.getAge() != null && jpnFootballerQuery.getAge() != -1) {
			//年齢（引数以下）で検索
			jpnFootballerList = jpnFootballerRepository.findByAgeLessThanEqualOrderByAgeDesc(jpnFootballerQuery.getAge());
		} else if(!jpnFootballerQuery.getBorn().equals("")) {
			//生年月日で完全一致検索
			jpnFootballerList = jpnFootballerRepository.findByBorn(Utils.str2date(jpnFootballerQuery.getBorn()));
		} else if((jpnFootballerQuery.getHeightBottom() != null && jpnFootballerQuery.getHeightBottom() != -1) && 
						(jpnFootballerQuery.getHeightTop() == null || jpnFootballerQuery.getHeightTop() == -1)) {
			//身長（引数以上）で検索
			jpnFootballerList = jpnFootballerRepository.findByHeightGreaterThanEqualOrderByHeightDesc(jpnFootballerQuery.getHeightBottom());
		} else if((jpnFootballerQuery.getHeightTop() != null && jpnFootballerQuery.getHeightTop() != -1) && 
						(jpnFootballerQuery.getHeightBottom() == null || jpnFootballerQuery.getHeightBottom() == -1)) {
			//身長（引数以下）で検索
			jpnFootballerList = jpnFootballerRepository.findByHeightLessThanEqualOrderByHeightDesc(jpnFootballerQuery.getHeightTop());
		} else if(jpnFootballerQuery.getHeightBottom() != null && jpnFootballerQuery.getHeightBottom() != -1 && 
					jpnFootballerQuery.getHeightTop() != null && jpnFootballerQuery.getHeightTop() != -1) {
			//身長（引数１と２の間）で検索
			jpnFootballerList = jpnFootballerRepository.findByHeightBetweenOrderByHeightDesc(jpnFootballerQuery.getHeightBottom(), jpnFootballerQuery.getHeightTop());
		} else if(jpnFootballerQuery.getPosition() != null && jpnFootballerQuery.getPosition() != -1) {
			//ポジションで完全一致検索
			jpnFootballerList = jpnFootballerRepository.findByPosition(jpnFootballerQuery.getPosition());
		} else if(jpnFootballerQuery.getMarketValue() != null && jpnFootballerQuery.getMarketValue() != -1) {
			//市場価値（引数以上）で検索
			jpnFootballerList = jpnFootballerRepository.findByMarketValueGreaterThanEqualOrderByMarketValueDesc(jpnFootballerQuery.getMarketValue());
		} else if(jpnFootballerQuery.getFoot() != null && jpnFootballerQuery.getFoot() != -1) {
			//利き足で完全一致検索
			jpnFootballerList = jpnFootballerRepository.findByFoot(jpnFootballerQuery.getFoot());
		} else {
			//入力条件が無ければ全件検索
			jpnFootballerList = jpnFootballerRepository.findAll();
		}
		return jpnFootballerList;
	}
	
	
	
	
	

}
