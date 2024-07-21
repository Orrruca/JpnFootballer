package com.example.jpnfootballerlist.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	
	//JpnFootballer一覧画面
	@GetMapping("/jpnFootballer")
	public String showJpnFootballerList(Model model, @PageableDefault(page=0, size=10, sort="id")Pageable pageable) {
		//一覧を検索して表示する
		Page<JpnFootballer> jpnFootballerPage = jpnFootballerRepository.findAll(pageable);
		model.addAttribute("jpnFootballerQuery", new JpnFootballerQuery());
		model.addAttribute("jpnFootballerPage", jpnFootballerPage);
		model.addAttribute("jpnFootballerList", jpnFootballerPage.getContent());
		session.setAttribute("jpnFootballerQuery", new JpnFootballerQuery());
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
	public String queryJpnFootballer(@ModelAttribute JpnFootballerQuery jpnFootballerQuery, BindingResult result,
														Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<JpnFootballer> jpnFootballerPage = null;
		if(jpnFootballerService.isValid(jpnFootballerQuery, result)) {
			//エラーが無ければ検索
			//jpnFootballerList = jpnFootballerService.doQuery(jpnFootballerQuery);
			//↓
			//JPQLによる検索
			//jpnFootballerList = jpnFootballerDaoImpl. findByJPQL(jpnFootballerQuery);
			//↓Criteria APIによる検索
			jpnFootballerPage = jpnFootballerDaoImpl. findByCriteria(jpnFootballerQuery, pageable);
			
			//入力された検索条件をセッションに保存
			session.setAttribute("jpnFootballerQuery", jpnFootballerQuery);
			
			model.addAttribute("jpnFootballerPage", jpnFootballerPage);
			model.addAttribute("jpnFootballerList", jpnFootballerPage.getContent());
		} else {
			//エラーがあった場合検索
			model.addAttribute("jpnFootballerPage", null);
			model.addAttribute("jpnFootballerList", null);
		}
		
		return "jpnFootballerList";
	}
	
	//各ページリンクをクリックしたとき
	@GetMapping("/jpnFootballer/query")
	public String queryJpnFootballer(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		//セッションに保存されている条件で検索
		JpnFootballerQuery jpnFootballerQuery = (JpnFootballerQuery)session.getAttribute("jpnFootballerQuery");
		Page<JpnFootballer> jpnFootballerPage = jpnFootballerDaoImpl.findByCriteria(jpnFootballerQuery, pageable);
		
		model.addAttribute("jpnFootballerQuery", jpnFootballerQuery); //検索条件表示用
		model.addAttribute("jpnFootballerPage", jpnFootballerPage); //page情報
		model.addAttribute("jpnFootballerList", jpnFootballerPage.getContent()); //検索結果
		
		return "jpnFootballerList";
	}
	
	
	
}
