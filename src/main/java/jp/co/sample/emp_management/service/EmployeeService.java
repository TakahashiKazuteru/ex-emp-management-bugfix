package jp.co.sample.emp_management.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import jp.co.sample.emp_management.form.InsertEmployeeForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 従業員情報を操作するサービス.
 *
 * @author igamasayuki
 */
@Service
@Transactional
public class EmployeeService {
    private final static int pageViewCount = 10;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * 従業員情報を全件取得します.
     *
     * @return 従業員情報一覧
     */
    public List<Employee> showList() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }
    
    /**
     * 従業員情報の名前をあいまい検索します.
     *
     * @param name 名前
     *
     * @return 従業員情報一覧
     */
    public List<Employee> showListByName(String name) {
        return employeeRepository.findByName(name);
    }
    
    /**
     * 従業員情報を取得します.
     *
     * @param id ID
     *
     * @return 従業員情報
     * @throws 検索されない場合は例外が発生します
     */
    public Employee showDetail(Integer id) {
        Employee employee = employeeRepository.load(id);
        return employee;
    }
    
    /**
     * 名前あいまい検索の指定されたページ番号の従業員情報を検索します.
     *
     * @param name 名前
     * @param page ページ数
     *
     * @return 従業員情報
     */
    public List<Employee> pageSearch(String name, int page) {
        page -= 1;
        page *= pageViewCount;
        return employeeRepository.findByPage(page, name, pageViewCount);
    }
    /**
     * 従業員あいまい検索のページ数を検索します.
     *
     * @param name 名前
     *
     * @return 検索結果件数
     */
    public List<Integer> employeePages(String name) {
        return pageCalc(employeeRepository.findRowCount(name));
    }
    
    /**
     * 従業員情報を更新します.
     *
     * @param employee 　更新した従業員情報
     */
    public void update(Employee employee) {
        employeeRepository.update(employee);
    }
    
    /**
     * IDの最大値を検索します.
     *
     * @return IDの最大値
     */
    public int findMaxId() {
        return employeeRepository.findMaxId();
    }
    
    /**
     * メールアドレスから従業員を検索します.
     *
     * @param email メールアドレス
     *
     * @return 従業員情報(見つからない場合nullを返す)
     */
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    /**
     * 従業員情報を1件登録します.
     *
     * @param form 社員情報
     */
    public void registEmployee(InsertEmployeeForm form) {
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(form, newEmployee);
        try {
            byte[] imageByteCode = Base64.getEncoder().encode(form.getImage().getBytes());
            String imageBase64 = "data:" + form.getImage().getContentType() + ";base64," + (new String(imageByteCode));
            newEmployee.setImage(imageBase64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        newEmployee.setSalary(Integer.parseInt(form.getSalary()));
        newEmployee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
        Date date = Date.valueOf(form.getHireDate());
        newEmployee.setHireDate(date);
        int nextId = employeeRepository.findMaxId() + 1;
        newEmployee.setId(nextId);
        employeeRepository.insert(newEmployee);
    }
    
    /**
     * データ数から必要なページ数を１からリストにして返します.
     *
     * @param dataCount データ数
     *
     * @return ページ数リスト
     */
    private List<Integer> pageCalc(int dataCount) {
        if (dataCount < pageViewCount) {
            dataCount = 1;
        } else if (dataCount % pageViewCount == 0) {
            dataCount /= pageViewCount;
        } else {
            dataCount /= pageViewCount;
            dataCount++;
        }
        List<Integer> dataCounts = new ArrayList<>();
        for (int i = 1; i <= dataCount; i++) {
            dataCounts.add(i);
        }
        return dataCounts;
    }
    
    public int pageStrToInt(String selectPage) {
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
        return page;
    }
}
