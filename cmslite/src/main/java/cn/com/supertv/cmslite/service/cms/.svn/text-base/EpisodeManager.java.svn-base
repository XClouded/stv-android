package cn.com.supertv.cmslite.service.cms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;

import cn.com.supertv.cmslite.dao.cms.EpisodeDao;
import cn.com.supertv.cmslite.entity.cms.Episode;

@Component
@Transactional
public class EpisodeManager {
	@Autowired
	private EpisodeDao episodeDao;

	// Episode Manager //
	@Transactional(readOnly = true)
	public Episode getEpisode(Long id) {
		return episodeDao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Episode> getAllEpisode() {
		return episodeDao.getAll();
	}
	
	public void saveEpisode(Episode episode) {
		episodeDao.save(episode);
	}

	public void deleteEpisode(Long id) {
		episodeDao.delete(id);
	}
	
	/**
	 * Description: 电视剧集列表分页查询 
	 * @Version1.0 2010-12-17 下午03:50:26 mustang created
	 * @param page
	 * @param filters
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Episode> searchEpisodes(Page<Episode> page, List<PropertyFilter> filters) {
		return episodeDao.findPage(page, filters);
	}
	
}

