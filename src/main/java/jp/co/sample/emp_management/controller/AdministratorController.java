package jp.co.sample.emp_management.controller;

import javax.servlet.http.HttpSession;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 *
 * @author igamasayuki
 */
@Controller
@RequestMapping("/")
public class AdministratorController {
    
    @Autowired
    private AdministratorService administratorService;
    
    @Autowired
    private HttpSession session;
    
    
    /**
     * 使用するフォームオブジェクトをリクエストスコープに格納する.
     *
     * @return フォーム
     */
    @ModelAttribute
    public InsertAdministratorForm setUpInsertAdministratorForm() {
        return new InsertAdministratorForm();
    }
    
    //  (SpringSecurityに任せるためコメントアウトしました)
    @ModelAttribute
    public LoginForm setUpLoginForm() {
        return new LoginForm();
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：管理者を登録する
    /////////////////////////////////////////////////////
    
    /**
     * 管理者登録画面を出力します.
     *
     * @return 管理者登録画面
     */
    @RequestMapping("/toInsert")
    public String toInsert(Model model) {
        return "administrator/insert";
    }
    
    /**
     * 管理者情報を登録します.
     *
     * @param form   管理者情報用フォーム
     * @param result ヴァリエーションエラー情報
     *
     * @return ログイン画面へリダイレクト
     */
    @RequestMapping("/insert")
    public String insert(@Validated InsertAdministratorForm form, BindingResult result, Model model) {
        boolean isInputError = false;
        if (result.hasErrors()) {
            isInputError = true;
        }
        if (administratorService.findByMailAddress(form.getMailAddress()) != null) {
            FieldError fieldError = new FieldError(result.getObjectName(), "mailAddress", "すでに登録されているメールアドレスです");
            result.addError(fieldError);
            isInputError = true;
        }
        if (!(form.getPassword().equals(form.getConfirmPassword()))) {
            FieldError fieldError = new FieldError(result.getObjectName(), "confirmPassword", "パスワードが一致しません");
            result.addError(fieldError);
            isInputError = true;
        }
        if (isInputError) {
            return toInsert(model);
        }
        Administrator administrator = new Administrator();
        // フォームからドメインにプロパティ値をコピー
        administrator.setName(form.getName());
        administrator.setPassword(form.getPassword());
        administratorService.insert(administrator);
        return "redirect:/";
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：ログインをする
    /////////////////////////////////////////////////////
    
    /**
     * ログイン画面を出力します.
     *
     * @return ログイン画面
     */
    @RequestMapping("/")
    public String toLogin() {
        return "administrator/login";
    }
    
    /**
     * ログインします.
     *
     * @param form   管理者情報用フォーム
     * @param result エラー情報格納用オブッジェクト
     *
     * @return ログイン後の従業員一覧画面
     */
    @RequestMapping("/login")
    public String login(LoginForm form, BindingResult result, Model model) {
        Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
        if (administrator == null) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
            return toLogin();
        }
        session.setAttribute("administratorName", administrator.getName());
        return "forward:/employee/showList";
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：ログアウトをする
    /////////////////////////////////////////////////////
    
    /**
     * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
     *
     * @return ログイン画面
     */
    @RequestMapping(value = "/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
    
}
