package cn.com.supertv.cmslite.web.cms;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.com.supertv.cmslite.entity.cms.Ktv;
import cn.com.supertv.cmslite.service.cms.KtvManager;
import cn.com.supertv.cmslite.utils.CmsUtils;
import cn.com.supertv.cmslite.web.CrudActionSupport;

@Namespace("/cms")
@Results({ @Result(name = CrudActionSupport.RELOAD, location = "ktv.action", type = "redirect") })
public class KtvAction extends CrudActionSupport<Ktv> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Page<Ktv> page = new Page<Ktv>(CmsUtils.PAGE_COUNT);
	private Ktv ktv;
	public Ktv getKtv() {
		return ktv;
	}

	public void setKtv(Ktv ktv) {
		this.ktv = ktv;
	}

	@Autowired
	private KtvManager ktvManager;
	//批量生成数目
	private Integer number = CmsUtils.CREATE_NUMBER;
	//开始号
	private long beginKtvNum;
	@Autowired
	private KtvManager ktvManger;

	public Page<Ktv> getPage() {
		return page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Ktv getModel() {
		return ktv;
	}

	@Override
	public String list() throws Exception {
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest());
		//设置默认排序方式
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = ktvManger.searchKtvs(page, filters);
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String save() throws Exception {
		ktvManager.saveKtvs((int) beginKtvNum, number, ktv.getGenreTreeIndex(), ktv.getProviderId(),ktv.getFtpId(),ktv.getVideoFtpPath(),ktv.getPosterFtpPath(),ktv.getRecommended(),ktv.getProductId(),ktv.getRegionTreeIndex());
		return RELOAD;
	}

	@Override
	public String delete() throws Exception {
		ktvManger.deleteKtv(id);
		return RELOAD;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (id != null) {
			ktv = ktvManger.getKtv(id);
		} else {
			ktv = new Ktv();
		}
		beginKtvNum = ktvManager.getKtvBeginNum();
	}

	
	/**
	 * Description: 批量提交审核
	 * @Version1.0 2011-1-5 下午03:12:14 mustang created
	 * @return
	 */
	public String updateToCensoring() {
		if(ktv==null){
			ktv = ktvManger.getKtv(id);
		}
		ktvManager.updateToCensoring(ktv);
		return RELOAD;
	} 
	/**
	 * Description: 批量审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String updateToIngestable() {
		if(ktv==null){
			ktv = ktvManger.getKtv(id);
		}
		ktvManager.updateToIngestable(ktv);
		return RELOAD;
	}
	/**
	 * Description: 批量注入
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String updateToIngested() {
		if(ktv==null){
			ktv = ktvManger.getKtv(id);
		}
		ktvManager.updateToIngested(ktv);
		return RELOAD;
	}
	/**
	 * Description: 批量下线
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public String offline() {
		if(ktv==null){
			ktv = ktvManger.getKtv(id);
		}
		ktvManager.offline(ktv);
		return RELOAD;
	}
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public long getBeginKtvNum() {
		return beginKtvNum;
	}
	public void setBeginKtvNum(long beginKtvNum) {
		this.beginKtvNum = beginKtvNum;
	}


}
