package cn.com.supertv.cmslite.web.cms;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.com.supertv.cmslite.entity.cms.Tv;
import cn.com.supertv.cmslite.service.cms.TvManager;
import cn.com.supertv.cmslite.utils.CmsUtils;
import cn.com.supertv.cmslite.web.CrudActionSupport;

@Namespace("/cms")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "tv.action", type = "redirect") })
public class TvAction extends CrudActionSupport<Tv> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Page<Tv> page = new Page<Tv>(CmsUtils.PAGE_COUNT);
	private Tv tv;
	private TvManager tvmanager;
	//批量生成数目
	private Integer number = CmsUtils.CREATE_NUMBER;
	//生成剧集数目
	private Integer episodeNumber = CmsUtils.EPISODE_NUMBER;
	//电视剧允许的开始号
	private long beginTvNum;

	@Override
	public Tv getModel() {
		return tv;
	}

	@Override
	public String list() throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest());
		//设置默认排序方式
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = tvmanager.searchTvs(page, filters);
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String save() throws Exception {
		tvmanager.saveTvs((int) beginTvNum, number, episodeNumber, tv.getGenreTreeIndex(), tv.getRegionTreeIndex(), tv.getProviderId(), tv.getFtpId(),
				tv.getPosterFtpPath(), tv.getPreviewFtpPath(),tv.getRecommended(),tv.getProductId());
		return RELOAD;
	}

	@Override
	public String delete() throws Exception {
		tvmanager.deleteTv(id);
		return RELOAD;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (id != null) {
			tv = tvmanager.getTv(id);
		} else {
			tv = new Tv();
		}
		beginTvNum = tvmanager.getTvBeginNum();
	}

	/**
	 * Description: 批量提交审核
	 * @Version1.0 2011-1-5 下午03:12:14 mustang created
	 * @return
	 */
	public String updateToCensoring() {
		if (tv == null) {
			tv = tvmanager.getTv(id);
		}
		tvmanager.updateToCensoring(tv);
		return RELOAD;
	}

	/**
	 * Description: 批量审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String updateToIngestable() {
		if (tv == null) {
			tv = tvmanager.getTv(id);
		}
		tvmanager.updateToIngestable(tv);
		return RELOAD;
	}

	/**
	 * Description: 批量注入
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String updateToIngested() {
		if (tv == null) {
			tv = tvmanager.getTv(id);
		}
		tvmanager.updateToIngested(tv);
		return RELOAD;
	}

	/**
	 * Description: 批量下线
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String offline() {
		if (tv == null) {
			tv = tvmanager.getTv(id);
		}
		tvmanager.offline(tv);
		return RELOAD;
	}

	public Tv getTv() {
		return tv;
	}

	public Page<Tv> getPage() {
		return page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Autowired
	public void setTvmanager(TvManager tvmanager) {
		this.tvmanager = tvmanager;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(Integer episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public long getBeginTvNum() {
		return beginTvNum;
	}

	public void setBeginTvNum(long beginTvNum) {
		this.beginTvNum = beginTvNum;
	}
}
