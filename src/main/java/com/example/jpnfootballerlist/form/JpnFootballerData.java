package com.example.jpnfootballerlist.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.jpnfootballerlist.entity.JpnFootballer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JpnFootballerData {
	
	private Integer id;
	
	@NotBlank(message = "名前を入力してください")
	private String name;
	
	@Size(min=1, max=20, message = "適切な文字数で入力してください")
	private String team;
	
	@Max(value = 150, message = "年齢が高すぎます")
	private Integer age;
	
	@NotNull(message = "生年月日を入力してください")
	private String born;
	
	@Min(value = 50, message = "身長が低すぎます")
	private Integer height;
	
	private Integer position;
	
	private Integer marketValue;
	
	private Integer foot;
	
	public JpnFootballer toEntity() {
		JpnFootballer jpnFootballer = new JpnFootballer();
		jpnFootballer.setId(id);
		jpnFootballer.setName(name);
		jpnFootballer.setTeam(team);
		jpnFootballer.setAge(age);
		jpnFootballer.setHeight(height);
		jpnFootballer.setPosition(position);
		jpnFootballer.setMarketValue(marketValue);
		jpnFootballer.setFoot(foot);
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		long ms;
		try {
			ms = sdFormat.parse(born).getTime();
			jpnFootballer.setBorn(new Date(ms));
		} catch(ParseException e) {
			jpnFootballer.setBorn(null);
		}
		
		return jpnFootballer;
	}

}
