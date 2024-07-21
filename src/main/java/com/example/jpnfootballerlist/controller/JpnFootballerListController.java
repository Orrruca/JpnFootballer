package com.example.jpnfootballerlist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.jpnfootballerlist.dao.JpnFootballerDaoImpl;
import com.example.jpnfootballerlist.entity.JpnFootballer;
import com.example.jpnfootballerlist.form.JpnFootballerData;
import com.example.jpnfootballerlist.form.JpnFootballerQuery;
import com.example.jpnfootballerlist.repository.JpnFootballerRepository;
import com.example.jpnfootballerlist.service.JpnFootballerService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class JpnFootballerListController {
	
	private final JpnFootballerRepository jpnFootballerRepository;
	private final JpnFootballerService jpnFootballerService;
	private final HttpSession session;
	@PersistenceContext
	private EntityManager entityManager;
	JpnFootballerDaoImpl jpnFootballerDaoImpl;
	
	@PostConstruct
	public void init() {
		jpnFootballerDaoImpl = new JpnFootballerDaoImpl(entityManager);
	}
	
	@GetMapping("/jpnFootballer")
	public String showJpnFootballerList(Model model) {
		//一覧を検索して表示する
		List<JpnFootballer> jpnFootballerList = jpnFootballerRepository.findAll();
		model.addAttribute("jpnFootballerList", jpnFootballerList);
		model.addAttribute("jpnFootballerQuery", new JpnFootballerQuery());
		return "jpnFootballerList";
	}
	
	//jpnFootballer一覧画面で新規追加がクリックされた時。新規登録したい場合のこと
	@GetMapping("/jpnFootballer/create")
	public String createJpnFootballer(Model model) {
		model.addAttribute("jpnFootballerData", new JpnFootballerData());
		session.setAttribute("mode", "create");
		return "jpnFootballerForm";
	}
	
	//jpnFootballer入力画面で登録ボタンがクリックされた時
	@PostMapping("/jpnFootballer/create")
	public String createJpnFootballer(@ModelAttribute @Validated JpnFootballerData jpnFootballerData,
																										BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = jpnFootballerService.isValid(jpnFootballerData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			JpnFootballer jpnFootballer = jpnFootballerData.toEntity();
			jpnFootballerRepository.saveAndFlush(jpnFootballer);
			return "redirect:/jpnFootballer";
		} else {
			return "jpnFootballerForm";
		}
	}
	
	//jpnFootballer入力画面でキャンセル登録ボタンがクリックされた時
	@PostMapping("/jpnFootballer/cancel")
	public String cancel() {
		return "redirect:/jpnFootballer";
	}
	
	//jpnFootballer一覧画面で名前リンクがクリックされた時。更新・削除・キャンセルしたい場合のこと
	@GetMapping("/jpnFootballer/{id}")
	public String jpnFootballerById(@PathVariable(name="id") int id, Model model) {
		JpnFootballer jpnFootballer = jpnFootballerRepository.findById(id).get();
		model.addAttribute("jpnFootballerData", jpnFootballer);
		session.setAttribute("mode", "update");
		return "jpnFootballerForm";
	}
	
	//jpnFootballer入力画面で更新ボタンがクリックされた時
	@PostMapping("/jpnFootballer/update")
	public String updateJpnFootballer(@ModelAttribute @Validated JpnFootballerData jpnFootballerData,
														BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = jpnFootballerService.isValid(jpnFootballerData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなし
			JpnFootballer jpnFootballer = jpnFootballerData.toEntity();
			jpnFootballerRepository.saveAndFlush(jpnFootballer);
			return "redirect:/jpnFootballer";
		} else {
			//エラーあり
			return "jpnFootballerForm";
		}
	}
	
	//jpnFootballer入力画面で削除ボタンがクリックされた時
	@PostMapping("/jpnFootballer/delete")
	public String deleteJpnFootballer(@ModelAttribute JpnFootballerData jpnFootballerData) {
		jpnFootballerRepository.deleteById(jpnFootballerData.getId());
		return "redirect:/jpnFootballer";
	}
	
	//jpnFootballer一覧画面で検索ボタンがクリックされた時
	@PostMapping("/jpnFootballer/query")
	public String queryJpnFootballer(@ModelAttribute JpnFootballerQuery jpnFootballerQuery, BindingResult result, Model model) {
		List<JpnFootballer> jpnFootballerList = null;
		if(jpnFootballerService.isValid(jpnFootballerQuery, result)) {
			//エラーが無ければ検索
			//jpnFootballerList = jpnFootballerService.doQuery(jpnFootballerQuery);
			//↓
			//JPQLによる検索
			//jpnFootballerList = jpnFootballerDaoImpl. findByJPQL(jpnFootballerQuery);
			//↓Criteria APIによる検索
			jpnFootballerList = jpnFootballerDaoImpl. findByCriteria(jpnFootballerQuery);
		}
		model.addAttribute("jpnFootballerList", jpnFootballerList);
		return "jpnFootballerList";
	}
	
	
	
}
