package cn.com.supertv.cmslite.dao.account;

import org.springframework.stereotype.Component;
import cn.com.supertv.cmslite.entity.account.Authority;
import org.springside.modules.orm.hibernate.HibernateDao;

/**
 * 授权对象的泛型DAO.
 * 
 * @author calvin
 */
@Component
public class AuthorityDao extends HibernateDao<Authority, Long> {
}
