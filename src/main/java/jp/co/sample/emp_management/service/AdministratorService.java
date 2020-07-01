package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 *
 * @author igamasayuki
 */
@Service
@Transactional
public class AdministratorService {
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    /**
     * 管理者情報を登録します.
     *
     * @param administrator 　管理者情報
     */
    public void insert(Administrator administrator) {
        String hashPassword = BCrypt.hashpw(administrator.getPassword(), BCrypt.gensalt());
        administrator.setPassword(hashPassword);
        administratorRepository.insert(administrator);
    }
    
    /**
     * ログインをします.
     *
     * @param mailAddress メールアドレス
     * @param password    パスワード
     *
     * @return 管理者情報　存在しない場合はnullが返ります
     */
    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddressAndPassward(mailAddress, password);
        return administrator;
    }
    
    /**
     * メールアドレスが登録されているか確認します
     *
     * @param mailAddress メールアドレス
     *
     * @return 従業員情報(未登録ならばnull)
     */
    public Administrator findByMailAddress(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress);
    }
}
