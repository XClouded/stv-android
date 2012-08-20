package cn.com.supertv.cmslite.service.cms;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;

import cn.com.supertv.cmslite.dao.cms.TvDao;
import cn.com.supertv.cmslite.entity.cms.MediaStateEnum;
import cn.com.supertv.cmslite.entity.cms.Tv;
import cn.com.supertv.common.cms.ws.CMSWebService;
import cn.com.supertv.common.cms.ws.dto.EpisodeDTO;
import cn.com.supertv.common.cms.ws.dto.TvDTO;
import cn.com.supertv.common.cms.ws.dto.TypeEnum;
import cn.com.supertv.common.cms.ws.result.CreateTvResult;
import cn.com.supertv.common.cms.ws.result.OfflineResult;
import cn.com.supertv.common.cms.ws.result.UpdateToCensoringResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestableResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestedResult;

import com.google.common.collect.Lists;

@Component
@Transactional
public class TvManager {
	private static Logger logger = LoggerFactory.getLogger(KtvManager.class);
	@Autowired
	private TvDao tvDao;
	@Autowired
	private CMSWebService cmsWebService;

	@Transactional(readOnly = true)
	public Tv getTv(Long id) {
		return tvDao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Tv> getAllTv() {
		return tvDao.getAll();
	}

	public void saveTv(Tv tv) {
		tvDao.save(tv);
	}

	public void deleteTv(Long id) {
		tvDao.delete(id);
	}

	/**
	 * Description: 批量生成TV及剧集
	 * @Version1.0 2010-12-29 下午05:01:39 mustang created
	 * @param beginnum 开始号
	 * @param number 批量生成数目
	 * @param episodeNumber 生成剧集数	
	 * @param style 风格
	 * @param area 区域
	 */
	public void saveTvs(int beginnum, int number, int episodeNumber, String style, String area, String providerId,
			String ftpId, String posterFtpPath, String previewFtpPath,Boolean recommended,String productId) {

		Tv tv = new Tv();
		tv.setTitle("tv_beginnum:" + beginnum + "_num:" + number);
		tv.setBeginNum(beginnum);
		tv.setGenreTreeIndex(style);
		tv.setRegionTreeIndex(area);
		tv.setNum(number);
		tv.setPreviewRunTime(100);
		tv.setProviderId(providerId);
		tv.setSuggestedPrice(number);
		tv.setPosterFtpPath(posterFtpPath);
		tv.setPreviewFtpPath(previewFtpPath);
		tv.setFtpId(ftpId);
		tv.setRunTime(100);
		tv.setEpisodeNumber(episodeNumber);
		tv.setState(MediaStateEnum.EDITABLE);
		tv.setRecommended(recommended);
		tv.setReleaseYear("2000");
		tv.setProductId(productId);
		/*for (int j = 1; j <= episodeNumber; j++) {
			Episode es = new Episode();
			es.setCreateTime(Calendar.getInstance().getTime());
			es.setOplogs(null);
			es.setSuggestedPrice(1);
			es.setTv(tv);
			es.setEpisodeId(j);
			es.setVideoFtpPath(CmsUtils.TV_EPISODE_VIDEOFTPPATH);
			es.setFtpId(CmsUtils.TV_EPISODE_FTPID);
			es.setState(MediaStateEnum.EDITABLE);
			tv.getEpisodes().add(es);
		}*/
		tvDao.save(tv);

		for (int i = beginnum; i < beginnum + number; i++) {
			//组装DTO
			TvDTO dto = new TvDTO();
			dto.setTitle("tv_" + i);
			dto.setGenreTreeIndex(tv.getGenreTreeIndex());
			dto.setRegionTreeIndex(tv.getRegionTreeIndex());
			dto.setPreviewRunTime(tv.getPreviewRunTime());
			dto.setProviderId(tv.getProviderId());
			dto.setSuggestedPrice(tv.getSuggestedPrice());
			dto.setPosterFtpPath(tv.getPosterFtpPath());
			dto.setPreviewFtpPath(tv.getPreviewFtpPath());
			dto.setFtpId(tv.getFtpId());
			dto.setRunTime(tv.getRunTime());
			dto.setRecommended(recommended);
			dto.setDescription("description");
			dto.setReleaseYear("2000");
			dto.setActors("actors");
			dto.setDirectors("directors");
			dto.setVolumn(episodeNumber);
			for (int j = 1; j <= episodeNumber; j++) {
				EpisodeDTO edto = new EpisodeDTO();
				edto.setEpisodeId(j);
				edto.setFtpId(ftpId);
				edto.setSuggestedPrice(100);
				//TODO 地址可能不同，需要指定相同策略
				edto.setVideoFtpPath(previewFtpPath);
				dto.getEpisodeList().add(edto);
			}
			CreateTvResult result = cmsWebService.createTv(dto);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: TV列表分页查询 
	 * @Version1.0 2010-12-17 下午03:50:26 mustang created
	 * @param page
	 * @param filters
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Tv> searchTvs(Page<Tv> page, List<PropertyFilter> filters) {
		return tvDao.findPage(page, filters);
	}

	/**
	 * Description: 查找TV总数
	 * @Version1.0 2010-12-29 下午04:46:42 mustang created
	 * @return
	 */
	public long getTvBeginNum() {
		Page<Tv> page = new Page<Tv>(1);
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = tvDao.findPage(page);
		if (page != null) {
			List<Tv> list = page.getResult();
			if (list != null && !list.isEmpty()) {
				Tv tv = list.get(0);
				if (tv != null) {
					return (tv.getBeginNum() + tv.getNum());
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
	public void updateToCensoring(Tv tv) {
		List<String> list = Lists.newArrayList();
		if (tv != null) {
			for (int i = tv.getBeginNum(); i < tv.getBeginNum() + tv.getNum(); i++) {

				for (int j = 1; j <= tv.getEpisodeNumber(); j++) {
					list.add("tv_" + i + "#" + j);
				}
			}
			tv.setState(MediaStateEnum.CENSORING);
			this.saveTv(tv);
		}
		if (!list.isEmpty()) {
			UpdateToCensoringResult result = cmsWebService.updateToCensoring(list, TypeEnum.EPISODE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngestable(Tv tv) {
		List<String> list = Lists.newArrayList();
		if (tv != null) {
			for (int i = tv.getBeginNum(); i < tv.getBeginNum() + tv.getNum(); i++) {

				for (int j = 1; j <= tv.getEpisodeNumber(); j++) {
					list.add("tv_" + i + "#" + j);
				}
			}
			tv.setState(MediaStateEnum.INGESTABLE);
			this.saveTv(tv);
		}
		if (!list.isEmpty()) {
			UpdateToIngestableResult result = cmsWebService.updateToIngestable(list, TypeEnum.EPISODE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量注入
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngested(Tv tv) {
		List<String> list = Lists.newArrayList();
		if (tv != null) {
			for (int i = tv.getBeginNum(); i < tv.getBeginNum() + tv.getNum(); i++) {

				for (int j = 1; j <= tv.getEpisodeNumber(); j++) {
					list.add("tv_" + i + "#" + j);
				}
			}
			tv.setState(MediaStateEnum.INGESTED);
			this.saveTv(tv);
		}
		if (!list.isEmpty()) {
			UpdateToIngestedResult result = cmsWebService.updateToIngested(list, TypeEnum.EPISODE,tv.getProductId());
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量下线
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void offline(Tv tv) {

		List<String> list = Lists.newArrayList();
		if (tv != null) {
			for (int i = tv.getBeginNum(); i < tv.getBeginNum() + tv.getNum(); i++) {

				for (int j = 1; j <= tv.getEpisodeNumber(); j++) {
					list.add("tv_" + i + "#" + j);
				}
			}
			tv.setState(MediaStateEnum.ZOMBIE);
			this.saveTv(tv);
		}
		if (!list.isEmpty()) {
			OfflineResult result = cmsWebService.offline(list, TypeEnum.EPISODE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

}
