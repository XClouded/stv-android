package cn.com.supertv.cmslite.dao.cms;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import cn.com.supertv.cmslite.entity.cms.Movie;

@Component
public class MovieDao extends HibernateDao<Movie, Long> {

}
