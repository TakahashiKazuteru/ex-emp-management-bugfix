package jp.co.sample.emp_management.controller;

import java.util.*;

import jp.co.sample.emp_management.form.InsertEmployeeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 *
 * @author igamasayuki
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * 使用するフォームオブジェクトをリクエストスコープに格納する.
     *
     * @return フォーム
     */
    @ModelAttribute
    public UpdateEmployeeForm setUpForm() {
        return new UpdateEmployeeForm();
    }
    
    @ModelAttribute
    public InsertEmployeeForm setUpInsertEmployeeForm() {
        return new InsertEmployeeForm();
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：従業員一覧を表示する
    /////////////////////////////////////////////////////
    
    /**
     * 従業員一覧画面を出力します.
     *
     * @param model モデル
     *
     * @return 従業員一覧画面
     */
    @RequestMapping("/showList")
    public String showList(String selectPage, String searchName, Model model) {
        if (searchName == null || "".equals(searchName)) {
            model.addAttribute("pages", employeeService.employeePages());
        } else {
            model.addAttribute("pages", employeeService.employeeNamePages(searchName));
        }
        int page;
        if (selectPage == null) {
            page = 1;
        } else {
            try {
                page = Integer.parseInt(selectPage);
            } catch (Exception e) {
                page = 1;
            }
        }
        if (page < 1) {
            page = 1;
        }
        List<Employee> employeeList;
        if (searchName == null || "".equals(searchName)) {
            employeeList = employeeService.pageSearch(page);
        } else {
            employeeList = employeeService.namePageSearch(searchName, page);
            model.addAttribute("searchWord", searchName);
        }
        if (employeeList.size() == 0) {
            model.addAttribute("employeeList_notFound", true);
        }
        model.addAttribute("employeeList", employeeList);
        return "employee/list";
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：従業員詳細を表示する
    /////////////////////////////////////////////////////
    
    /**
     * 従業員詳細画面を出力します.
     *
     * @param id    リクエストパラメータで送られてくる従業員ID
     * @param model モデル
     *
     * @return 従業員詳細画面
     */
    @RequestMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        return "employee/detail";
    }
    
    /////////////////////////////////////////////////////
    // ユースケース：従業員詳細を更新する
    /////////////////////////////////////////////////////
    
    /**
     * 従業員詳細(ここでは扶養人数のみ)を更新します.
     *
     * @param form 従業員情報用フォーム
     *
     * @return 従業員一覧画面へリダクレクト
     */
    @RequestMapping("/update")
    public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showDetail(form.getId(), model);
        }
        Employee employee = new Employee();
        employee.setId(form.getIntId());
        employee.setDependentsCount(form.getIntDependentsCount());
        employeeService.update(employee);
        return "redirect:/employee/showList";
    }
    
    /**
     * 社員登録画面を表示.
     *
     * @return 社員登録画面
     */
    @RequestMapping("/register")
    public String register(Model model) {
        return "employee/register";
    }
    
    /**
     * 社員情報を登録する.
     *
     * @param form   社員情報
     * @param result バリデーションエラー情報
     * @param model  リクエストスコープ
     *
     * @return 従業員リスト
     */
    @RequestMapping("/regist")
    synchronized public String regist(@Validated InsertEmployeeForm form, BindingResult result, Model model) {
        if (form.getMailAddress() != null) {
            Employee employee = employeeService.findByEmail(form.getMailAddress());
            if (employee != null) {
                FieldError fieldError = new FieldError(result.getObjectName(), "mailAddress", "すでに登録されているメールアドレスです");
                result.addError(fieldError);
            }
        }
        if (form.getHireDate() == null) {
            FieldError fieldError = new FieldError(result.getObjectName(), "hireDate", "空欄では登録できません");
            result.addError(fieldError);
        }
        if (form.getImage().isEmpty()) {
            FieldError fieldError = new FieldError(result.getObjectName(), "image", "");
        }
        
        if (result.hasErrors()) {
            return register(model);
        }
        //郵便番号から住所のフィル
        employeeService.registEmployee(form);
        return "redirect:/employee/showList";
    }
    
    /**
     * 名前の検索候補をJSON形式で返すWeb API
     *
     * @param inputData 名前
     *
     * @return 検索候補のJSON
     */
    @ResponseBody
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    synchronized public Map<String, List<String>> complete(String inputData) {
        Map<String, List<String>> completeMap = new HashMap<>();
        if (inputData.length() < 1) {
            return completeMap;
        }
        List<String> list = new ArrayList<>();
        for (Employee employee : employeeService.showListByName(inputData)) {
            list.add(employee.getName());
        }
        completeMap.put("suggestions", list);
        return completeMap;
    }
    
}
