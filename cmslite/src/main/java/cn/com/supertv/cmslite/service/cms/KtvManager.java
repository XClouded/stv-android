package cn.com.supertv.cmslite.service.cms;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;

import cn.com.supertv.cmslite.dao.cms.KtvDao;
import cn.com.supertv.cmslite.entity.cms.Ktv;
import cn.com.supertv.cmslite.entity.cms.MediaStateEnum;
import cn.com.supertv.common.cms.ws.CMSWebService;
import cn.com.supertv.common.cms.ws.dto.KtvDTO;
import cn.com.supertv.common.cms.ws.dto.TypeEnum;
import cn.com.supertv.common.cms.ws.result.CreateKtvResult;
import cn.com.supertv.common.cms.ws.result.OfflineResult;
import cn.com.supertv.common.cms.ws.result.UpdateToCensoringResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestableResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestedResult;

import com.google.common.collect.Lists;

@Component
@Transactional
public class KtvManager {
	private static Logger logger = LoggerFactory.getLogger(KtvManager.class);
	@Autowired
	private KtvDao ktvDao;

	@Autowired
	private CMSWebService cmsWebService;

	@Transactional(readOnly = true)
	public Ktv getKtv(Long id) {
		return ktvDao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Ktv> getAllKtv() {
		return ktvDao.getAll();
	}

	public void saveKtv(Ktv ktv) {
		ktvDao.save(ktv);
	}

	public void deleteKtv(Long id) {
		ktvDao.delete(id);
	}

	/**
	 * Description: KTV列表分页查询 
	 * @Version1.0 2010-12-17 下午03:50:26 mustang created
	 * @param page
	 * @param filters
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Ktv> searchKtvs(Page<Ktv> page, List<PropertyFilter> filters) {
		return ktvDao.findPage(page, filters);
	}

	/**
	 * Description: 批量生成KTV
	 * @Version1.0 2010-12-29 下午06:31:48 mustang created
	 * @param beginnum
	 * @param number
	 * @param style
	 * @param area
	 * @param providerId
	 * @param producer
	 */
	public void saveKtvs(int beginnum, int number, String style, String providerId, String ftpId, String videoFtpPath,
			String posterFtpPath ,Boolean recommended,String productId,String regionTreeIndex) {
		List<KtvDTO> ktvList = Lists.newArrayList();
		Ktv ktv = new Ktv();
		ktv.setActors("actors");
		ktv.setAlbum("album");
		ktv.setArranged("arranged");
		ktv.setAuthors("authors");
		ktv.setGenreTreeIndex(style);
		ktv.setProducer("producer");
		ktv.setProviderId(providerId);
		ktv.setSuggestedPrice(1);
		ktv.setTitle("ktv_beginnum:" + beginnum + "_num:" + number);
		ktv.setVideoFtpPath(videoFtpPath);
		ktv.setFtpId(ftpId);
		ktv.setPosterFtpPath(posterFtpPath);
		ktv.setCreateTime(Calendar.getInstance().getTime());
		ktv.setBeginNum(beginnum);
		ktv.setNum(number);
		ktv.setState(MediaStateEnum.EDITABLE);
		ktv.setRunTime(100);
		ktv.setRecommended(recommended);
		ktv.setDescription("description");
		ktv.setReleaseYear("2000");
		ktv.setProductId(productId);
		ktv.setRegionTreeIndex(regionTreeIndex);
		this.saveKtv(ktv);
		for (int i = beginnum; i < beginnum + number; i++) {
			//组装DTO
			KtvDTO dto = new KtvDTO();
			dto.setActors(ktv.getActors());
			dto.setAlbum(ktv.getAlbum());
			dto.setArranged(ktv.getArranged());
			dto.setAuthors(ktv.getAuthors());
			dto.setGenreTreeIndex(ktv.getGenreTreeIndex());
			dto.setProducer(ktv.getProducer());
			dto.setProviderId(ktv.getProviderId());
			dto.setSuggestedPrice(ktv.getSuggestedPrice());
			dto.setTitle("ktv_" + i);
			dto.setVideoFtpPath(ktv.getVideoFtpPath());
			dto.setFtpId(ktv.getFtpId());
			dto.setPosterFtpPath(ktv.getPosterFtpPath());
			dto.setRunTime(ktv.getRunTime());
			dto.setRecommended(recommended);
			dto.setReleaseYear("2000");
			dto.setDescription("description");
			dto.setRegionTreeIndex(regionTreeIndex);
			ktvList.add(dto);
		}
		if (ktvList != null && !ktvList.isEmpty()) {
			CreateKtvResult result = cmsWebService.createKtvs(ktvList);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 查找KTV总数
	 * @Version1.0 2010-12-29 下午04:46:42 mustang created
	 * @return
	 */
	public long getKtvBeginNum() {
		Page<Ktv> page = new Page<Ktv>(1);
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = ktvDao.findPage(page);
		if (page != null) {
			List<Ktv> list = page.getResult();
			if (list != null && !list.isEmpty()) {
				Ktv ktv = list.get(0);
				if (ktv != null) {
					return (ktv.getBeginNum() + ktv.getNum());
				}
			}
		}
		return 1;
	}

	/**
	 * Description: 批量提交审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToCensoring(Ktv ktv) {
		List<String> list = Lists.newArrayList();
		if (ktv != null) {
			for (int i = ktv.getBeginNum(); i < ktv.getBeginNum() + ktv.getNum(); i++) {
				list.add("ktv_" + i);
			}
			ktv.setState(MediaStateEnum.CENSORING);
			this.saveKtv(ktv);
		}
		if (!list.isEmpty()) {
			UpdateToCensoringResult result = cmsWebService.updateToCensoring(list, TypeEnum.KTV);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngestable(Ktv ktv) {
		List<String> list = Lists.newArrayList();
		if (ktv != null) {
			for (int i = ktv.getBeginNum(); i < ktv.getBeginNum() + ktv.getNum(); i++) {
				list.add("ktv_" + i);
			}
			ktv.setState(MediaStateEnum.INGESTABLE);
			this.saveKtv(ktv);

		}
		if (!list.isEmpty()) {
			UpdateToIngestableResult result = cmsWebService.updateToIngestable(list, TypeEnum.KTV);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量注入
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngested(Ktv ktv) {
		List<String> list = Lists.newArrayList();
		if (ktv != null) {
			for (int i = ktv.getBeginNum(); i < ktv.getBeginNum() + ktv.getNum(); i++) {
				list.add("ktv_" + i);
			}
			ktv.setState(MediaStateEnum.INGESTED);
			this.saveKtv(ktv);
		}
		if (!list.isEmpty()) {
			UpdateToIngestedResult result = cmsWebService.updateToIngested(list, TypeEnum.KTV,ktv.getProductId());
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量下线
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void offline(Ktv ktv) {
		List<String> list = Lists.newArrayList();
		if (ktv != null) {
			for (int i = ktv.getBeginNum(); i < ktv.getBeginNum() + ktv.getNum(); i++) {
				list.add("ktv_" + i);
			}
			ktv.setState(MediaStateEnum.ZOMBIE);
			this.saveKtv(ktv);
		}
		if (!list.isEmpty()) {
			OfflineResult result = cmsWebService.offline(list, TypeEnum.KTV);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

}
