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

import cn.com.supertv.cmslite.dao.cms.MovieDao;
import cn.com.supertv.cmslite.entity.cms.MediaStateEnum;
import cn.com.supertv.cmslite.entity.cms.Movie;
import cn.com.supertv.common.cms.ws.CMSWebService;
import cn.com.supertv.common.cms.ws.dto.MovieDTO;
import cn.com.supertv.common.cms.ws.dto.TypeEnum;
import cn.com.supertv.common.cms.ws.result.CreateMovieResult;
import cn.com.supertv.common.cms.ws.result.OfflineResult;
import cn.com.supertv.common.cms.ws.result.UpdateToCensoringResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestableResult;
import cn.com.supertv.common.cms.ws.result.UpdateToIngestedResult;

import com.google.common.collect.Lists;

@Component
@Transactional
public class MovieManager {
	private static Logger logger = LoggerFactory.getLogger(MovieManager.class);
	@Autowired
	private MovieDao movieDao;
	@Autowired
	private CMSWebService cmsWebService;

	@Transactional(readOnly = true)
	public Movie getMovie(Long id) {
		return movieDao.get(id);
	}

	@Transactional(readOnly = true)
	public List<Movie> getAllMovie() {
		return movieDao.getAll();
	}

	public void saveMovie(Movie movie) {
		movieDao.save(movie);
	}

	public void deleteMovie(Long id) {
		movieDao.delete(id);
	}

	/**
	 * Description: 批量生成movies
	 * @Version1.0 2010-12-29 下午06:31:48 mustang created
	 * @param beginnum
	 * @param number
	 * @param style
	 * @param area
	 * @param providerId
	 * @param producer
	 */
	public void saveMoviess(int beginnum, int number, String style, String area, String providerId, String ftpId,
			String videoFtpPath, String posterFtpPath, String previewFtpPath,Boolean recommended,String productId) {
		List<MovieDTO> movieList = Lists.newArrayList();
		Movie mv = new Movie();
		mv.setActors("actors");
		mv.setBeginNum(beginnum);
		mv.setCreateBy("createBy");
		mv.setCreateTime(Calendar.getInstance().getTime());
		mv.setDirectors("directors");
		mv.setGenreTreeIndex(style);
		mv.setNum(number);
		mv.setProviderId(providerId);
		mv.setRegionTreeIndex(area);
		mv.setSuggestedPrice(1);
		mv.setTitle("movie_beginnum:" + beginnum + "_num:" + number);
		mv.setVideoFtpPath(videoFtpPath);
		mv.setFtpId(ftpId);
		mv.setPosterFtpPath(posterFtpPath);
		mv.setPreviewFtpPath(previewFtpPath);
		mv.setState(MediaStateEnum.EDITABLE);
		mv.setRunTime(100);
		mv.setPreviewRunTime(100);
		mv.setRecommended(recommended);
		mv.setReleaseYear("2000");
		mv.setDescription("description");
		mv.setProductId(productId);
		movieDao.save(mv);
		for (int i = beginnum; i < beginnum + number; i++) {
			//DTO组包
			MovieDTO dto = new MovieDTO();
			dto.setActors(mv.getActors());
			dto.setDirectors(mv.getDirectors());
			dto.setFtpId(mv.getFtpId());
			dto.setGenreTreeIndex(mv.getGenreTreeIndex());
			dto.setPosterFtpPath(mv.getPosterFtpPath());
			dto.setPreviewFtpPath(mv.getPreviewFtpPath());
			dto.setProviderId(mv.getProviderId());
			dto.setRegionTreeIndex(mv.getRegionTreeIndex());
			dto.setSuggestedPrice(mv.getSuggestedPrice());
			dto.setTitle("movie_" + i);
			dto.setVideoFtpPath(mv.getVideoFtpPath());
			dto.setRunTime(mv.getRunTime());
			dto.setPreviewRunTime(100);
			dto.setRecommended(recommended);
			dto.setDescription("故事背景 1954年第5届世界杯（瑞士）战后的德国民众忙碌于战后重建的日常工作之中。德国国家足球队在瑞士打入了世界杯的决赛，将在瑞士城市伯尔尼迎战当时世界公认最强的足球队匈牙利国家足球队，胜算近无。然而，德国国家足球队不可思议地战胜了匈牙利队，第1次赢得了世界杯冠军，这一难得的胜利被世人誉为“伯尔尼奇迹”。故事主线沃尔特曼的该部影片系根据德国入围世界杯决赛的真实历程，融合一个11岁小球迷的虚构情节做为故事的主线。讲述这名男孩的父亲--昔日的一位德国士兵，被当成战俘在苏联关了10年后，于世界杯期间回到了这个鲁尔谷");
			dto.setReleaseYear("2000");
			movieList.add(dto);
		}
		if (movieList != null && !movieList.isEmpty()) {
			CreateMovieResult result = cmsWebService.createMovies(movieList);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}

	}

	/**
	 * Description: Movie列表分页查询 
	 * @Version1.0 2010-12-17 下午03:50:26 mustang created
	 * @param page
	 * @param filters
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<Movie> searchMovies(Page<Movie> page, List<PropertyFilter> filters) {
		return movieDao.findPage(page, filters);
	}

	/**
	 * Description: 查找movie开始号
	 * @Version1.0 2010-12-29 下午04:46:42 mustang created
	 * @return
	 */
	public long getMovieBeginNum() {

		Page<Movie> page = new Page<Movie>(1);
		if (!page.isOrderBySetted()) {
			page.setOrderBy("id");
			page.setOrder(Page.DESC);
		}
		page = movieDao.findPage(page);
		if (page != null) {
			List<Movie> list = page.getResult();
			if (list != null && !list.isEmpty()) {
				Movie mv = list.get(0);
				if (mv != null) {
					return (mv.getBeginNum() + mv.getNum());
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
	public void updateToCensoring(Movie mv) {
		List<String> list = Lists.newArrayList();
		if (mv != null) {
			for (int i = mv.getBeginNum(); i < mv.getBeginNum() + mv.getNum(); i++) {
				list.add("movie_" + i);
			}
			mv.setState(MediaStateEnum.CENSORING);
			this.saveMovie(mv);
		}
		if (!list.isEmpty()) {
			UpdateToCensoringResult result = cmsWebService.updateToCensoring(list, TypeEnum.MOVIE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量审核
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngestable(Movie mv) {
		List<String> list = Lists.newArrayList();
		if (mv != null) {
			for (int i = mv.getBeginNum(); i < mv.getBeginNum() + mv.getNum(); i++) {
				list.add("movie_" + i);
			}
			mv.setState(MediaStateEnum.INGESTABLE);
			this.saveMovie(mv);

		}
		if (!list.isEmpty()) {
			UpdateToIngestableResult result = cmsWebService.updateToIngestable(list, TypeEnum.MOVIE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量注入
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void updateToIngested(Movie mv) {
		List<String> list = Lists.newArrayList();
		if (mv != null) {
			for (int i = mv.getBeginNum(); i < mv.getBeginNum() + mv.getNum(); i++) {
				list.add("movie_" + i);
			}
			mv.setState(MediaStateEnum.INGESTED);
			this.saveMovie(mv);
		}
		if (!list.isEmpty()) {
			UpdateToIngestedResult result = cmsWebService.updateToIngested(list, TypeEnum.MOVIE,mv.getProductId());
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

	/**
	 * Description: 批量下线
	 * @Version1.0 2011-1-5 下午02:59:57 mustang created
	 * @param ids
	 */
	public void offline(Movie mv) {
		List<String> list = Lists.newArrayList();
		if (mv != null) {
			for (int i = mv.getBeginNum(); i < mv.getBeginNum() + mv.getNum(); i++) {
				list.add("movie_" + i);
			}
			mv.setState(MediaStateEnum.ZOMBIE);
			this.saveMovie(mv);
		}
		if (!list.isEmpty()) {
			OfflineResult result = cmsWebService.offline(list, TypeEnum.MOVIE);
			logger.debug("响应结果：" + result.getCode() + ":" + result.getMessage());
		}
	}

}
