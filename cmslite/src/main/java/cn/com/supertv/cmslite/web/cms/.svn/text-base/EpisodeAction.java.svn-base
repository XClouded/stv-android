package cn.com.supertv.cmslite.web.cms;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.com.supertv.cmslite.entity.cms.Episode;
import cn.com.supertv.cmslite.service.cms.EpisodeManager;
import cn.com.supertv.cmslite.utils.CmsUtils;
import cn.com.supertv.cmslite.web.CrudActionSupport;

@Namespace("/cms")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "episode.action", type = "redirect") })
public class EpisodeAction extends CrudActionSupport<Episode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page<Episode> page = new Page<Episode>(CmsUtils.PAGE_COUNT);
	@Autowired
	private EpisodeManager episodeManager;

	public Page<Episode> getPage() {
		return page;
	}

	@Override
	public Episode getModel() {
		return null;
	}

	@Override
	public String list() throws Exception {

		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest());
		//设置默认排序方式
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = episodeManager.searchEpisodes(page, filters);
		return SUCCESS;

	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

}
