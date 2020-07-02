package jp.co.sample.emp_management.form;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 社員情報登録時に使用するフォーム
 *
 * @author Kazuteru,Takahashi
 */
public class InsertEmployeeForm {
    /** 名前 */
    @NotBlank(message="空欄では登録できません")
    private String name;
    /** 画像パス */
    @Pattern(regexp="^.*(\\.png|\\.jpg)$",message="pngかjpgファイルを指定してください")
    private String image;
    /** 性別 */
    @NotBlank(message="どちらか選択してください")
    private String gender;
    /** 入社日 */
    @NotBlank(message="空欄では登録できません")
    private String hireDate;
    /** メールアドレス */
    @NotBlank(message="空欄では登録できません")
    @Email(message="メールアドレスを入力してください")
    private String mailAddress;
    /** 郵便番号 */
    @NotBlank(message="空欄では登録できません")
    @Pattern(regexp="^[0-9]{7}$",message="数字７桁で入力してください")
    private String zipCode;
    /** 住所 */
    @NotBlank(message="空欄では登録できません")
    private String address;
    /** 電話番号 */
    @NotBlank(message="空欄では登録できません")
    @Pattern(regexp="^0[0-9]{1,3}-[0-9]{3,4}-[0-9]{4}$",message="電話番号を入力してください")
    private String telephone;
    /** 給与額 */
    @NotBlank(message="空欄では登録できません")
    @Range(min=0,max=500000000,message = "0～50000000の間で入力してください")
    private String salary;
    /** 特性 */
    private String characteristics;
    /** 扶養人数 */
    @NotBlank(message="空欄では登録できません")
    @Range(min=0,max=30,message="0～30の間で入力してください")
    private String dependentsCount;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getMailAddress() {
        return mailAddress;
    }
    
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getSalary() {
        return salary;
    }
    
    public void setSalary(String salary) {
        this.salary = salary;
    }
    
    public String getCharacteristics() {
        return characteristics;
    }
    
    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
    
    public String getDependentsCount() {
        return dependentsCount;
    }
    
    public void setDependentsCount(String dependentsCount) {
        this.dependentsCount = dependentsCount;
    }
}
