package jp.co.sample.emp_management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Email;

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
    public String showList(Model model) {
        List<Employee> employeeList = employeeService.showList();
        model.addAttribute("employeeList", employeeList);
        return "employee/list";
    }
    
    /**
     * あいまい検索を行い従業員一覧画面に反映させる
     *
     * @return 従業員一覧画面
     */
    @RequestMapping("/search")
    public String search(String searchName, Model model) {
        List<Employee> employeeList;
        if (searchName.equals("")) {
            employeeList = employeeService.showList();
            return "employee/list";
        }
        employeeList = employeeService.showListByName(searchName);
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
        completeMap.put("suggestions",list);
        return completeMap;
    }
    
}
