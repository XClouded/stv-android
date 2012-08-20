package cn.com.supertv.cmslite.dao.account;

import org.springframework.stereotype.Component;
import cn.com.supertv.cmslite.entity.account.User;
import org.springside.modules.orm.hibernate.HibernateDao;

/**
 * 用户对象的泛型DAO类.
 * 
 * @author calvin
 */
@Component
public class UserDao extends HibernateDao<User, Long> {
}
