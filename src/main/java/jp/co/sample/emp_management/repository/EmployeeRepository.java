package jp.co.sample.emp_management.repository;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 *
 * @author igamasayuki
 */
@Repository
public class EmployeeRepository {
    
    /**
     * Employeeオブジェクトを生成するローマッパー.
     */
    private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setImage(rs.getString("image"));
        employee.setGender(rs.getString("gender"));
        employee.setHireDate(rs.getDate("hire_date"));
        employee.setZipCode(rs.getString("zip_code"));
        employee.setAddress(rs.getString("address"));
        employee.setTelephone(rs.getString("telephone"));
        employee.setSalary(rs.getInt("salary"));
        employee.setCharacteristics(rs.getString("characteristics"));
        employee.setDependentsCount(rs.getInt("dependents_count"));
        return employee;
    };
    private final RowMapper<Integer> INT_ROW_MAPPER = (rs, i) -> {
        Integer maxId = rs.getInt("num");
        return maxId;
    };
    
    @Autowired
    private NamedParameterJdbcTemplate template;
    
    /**
     * 従業員一覧情報を入社日順で取得します.
     *
     * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
     */
    public List<Employee> findAll() {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC;";
        
        List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);
        
        return developmentList;
    }
    
    /**
     * 指定された番号から指定された数の従業員情報を検索します
     *
     * @param number        検索開始位置の番号
     * @param pageViewCount 検索件数
     *
     * @return 従業員情報(0件ならサイズ0の従業員情報を返す)
     */
    public List<Employee> findByPage(int number, int pageViewCount) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees " +
                "ORDER BY hire_date DESC OFFSET :number LIMIT :pageViewCount;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("number", number).addValue("pageViewCount", pageViewCount);
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }
    
    /**
     * 指定された名前の検索結果の番号から指定された数の従業員情報を検索します
     *
     * @param number 検索開始位置の番号
     * @param name 名前
     * @param pageViewCount 検索件数
     * @return 従業員情報
     */
    public List<Employee> findByNameAndPage(int number, String name, int pageViewCount) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees " +
                "WHERE name LIKE :name ORDER BY hire_date DESC OFFSET :number LIMIT :pageViewCount;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("number", number).addValue("pageViewCount", pageViewCount);
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }
    
    /**
     * 名前のあいまい検索を行い対応する従業員一覧情報を入社日順で取得します.
     *
     * @param name 名前
     *
     * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
     */
    public List<Employee> findByName(String name) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE name LIKE :name ORDER BY hire_date DESC;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }
    
    /**
     * 主キーから従業員情報を取得します.
     *
     * @param id 検索したい従業員ID
     *
     * @return 検索された従業員情報
     * @throws 従業員が存在しない場合は例外を発生します
     */
    public Employee load(Integer id) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";
        
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        
        Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);
        
        return development;
    }
    
    /**
     * 従業員情報登録数を検索します.
     *
     * @return 従業員情報登録数()
     */
    public int findRowCount() {
        String sql = "SELECT COUNT(id) AS num FROM employees;";
        List<Integer> rowCount = template.query(sql, INT_ROW_MAPPER);
        return rowCount.get(0);
    }
    
    /**
     * 名前あいまい検索の結果件数を検索します.
     *
     * @param name 名前
     *
     * @return 検索結果件数
     */
    public int findRowCountByName(String name) {
        String sql = "SELECT COUNT(id) AS num FROM employees WHERE name = :name;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
        List<Integer> rowCount = template.query(sql, param, INT_ROW_MAPPER);
        return rowCount.get(0);
    }
    
    /**
     * IDの最大値を検索します.
     *
     * @return IDの最大値
     */
    public int findMaxId() {
        String sql = "SELECT MAX(id) AS num FROM employees;";
        List<Integer> maxId = template.query(sql, INT_ROW_MAPPER);
        if (maxId == null) {
            return 0;
        }
        return maxId.get(0);
    }
    
    /**
     * 従業員情報を変更します.
     */
    public void update(Employee employee) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        
        String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
        template.update(updateSql, param);
    }
    
    /**
     * 従業員情報を1件登録します.
     *
     * @param employee 従業員情報
     */
    public void insert(Employee employee) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        String insertSql = "INSERT INTO employees " +
                "VALUES(:id,:name,:image,:gender,:hireDate,:mailAddress,:zipCode,:address,:telephone,:salary,:characteristics,:dependentsCount);";
        template.update(insertSql, param);
    }
    
    
    /**
     * メールアドレスから従業員を検索します.
     *
     * @param email メールアドレス
     *
     * @return 従業員情報(見つからない場合nullを返す)
     */
    public Employee findByEmail(String email) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE mail_address = :email;";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
        List<Employee> employeeList = template.query(sql, param, EMPLOYEE_ROW_MAPPER);
        if (employeeList.size() == 0) {
            return null;
        }
        return employeeList.get(0);
    }
}
