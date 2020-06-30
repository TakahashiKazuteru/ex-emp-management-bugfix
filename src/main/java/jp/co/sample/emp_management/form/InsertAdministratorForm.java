package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 管理者情報登録時に使用するフォーム.
 *
 * @author igamasayuki
 *
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank(message="空欄では登録できません")
	private String name;
	/** メールアドレス */
	@NotBlank(message="空欄では登録できません")
	@Email(message="メールアドレスを入力してください")
	private String mailAddress;
	/** パスワード */
	@NotBlank(message="空欄では登録できません")
	private String password;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ "]";
	}
	
}
