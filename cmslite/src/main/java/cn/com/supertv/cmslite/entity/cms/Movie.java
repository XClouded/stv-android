package cn.com.supertv.cmslite.entity.cms;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.com.supertv.cmslite.entity.IdEntity;

import com.google.common.collect.Lists;

/**
 * Movie.
 */
@Entity
@Table(name = "t_cmslite_movie", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Movie extends IdEntity {

	private Integer version;
	private String title;
	private String releaseYear;
	private String description;
	private String actors;
	private String directors;
	private Integer runTime;
	private Integer suggestedPrice;
	private Boolean recommended;
	private String ftpId;
	private String previewFtpPath;
	private String videoFtpPath;
	private String posterFtpPath;
	private String genreTreeIndex;
	private String regionTreeIndex;
	private MediaStateEnum state;
	private Integer previewRunTime;
	private String createBy;
	private Date createTime;
	private String lastModifyBy;
	private Date lastModifyTime;
	private String providerId;
	private Integer beginNum;
	private Integer num;
	private String productId;
	private List<Oplog> oplogs = Lists.newArrayList();

	public Movie() {
	}

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "title", unique = true, length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "release_year", length = 20)
	public String getReleaseYear() {
		return this.releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "actors", length = 200)
	public String getActors() {
		return this.actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	@Column(name = "directors", length = 100)
	public String getDirectors() {
		return this.directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	@Column(name = "run_time")
	public Integer getRunTime() {
		return this.runTime;
	}

	public void setRunTime(Integer runTime) {
		this.runTime = runTime;
	}

	@Column(name = "suggested_price")
	public Integer getSuggestedPrice() {
		return this.suggestedPrice;
	}

	public void setSuggestedPrice(Integer suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	@Column(name = "recommended")
	public Boolean getRecommended() {
		return this.recommended;
	}

	public void setRecommended(Boolean recommended) {
		this.recommended = recommended;
	}

	@Column(name = "ftp_id", length = 50)
	public String getFtpId() {
		return this.ftpId;
	}

	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
	}

	@Column(name = "preview_ftp_path")
	public String getPreviewFtpPath() {
		return this.previewFtpPath;
	}

	public void setPreviewFtpPath(String previewFtpPath) {
		this.previewFtpPath = previewFtpPath;
	}

	@Column(name = "video_ftp_path", length = 200)
	public String getVideoFtpPath() {
		return this.videoFtpPath;
	}

	public void setVideoFtpPath(String videoFtpPath) {
		this.videoFtpPath = videoFtpPath;
	}

	@Column(name = "poster_ftp_path", length = 200)
	public String getPosterFtpPath() {
		return this.posterFtpPath;
	}

	public void setPosterFtpPath(String posterFtpPath) {
		this.posterFtpPath = posterFtpPath;
	}

	@Column(name = "genre_tree_index", length = 50)
	public String getGenreTreeIndex() {
		return this.genreTreeIndex;
	}

	public void setGenreTreeIndex(String genreTreeIndex) {
		this.genreTreeIndex = genreTreeIndex;
	}

	@Column(name = "region_tree_index", length = 20)
	public String getRegionTreeIndex() {
		return this.regionTreeIndex;
	}

	public void setRegionTreeIndex(String regionTreeIndex) {
		this.regionTreeIndex = regionTreeIndex;
	}

	@Column(name = "preview_run_time")
	public Integer getPreviewRunTime() {
		return this.previewRunTime;
	}

	public void setPreviewRunTime(Integer previewRunTime) {
		this.previewRunTime = previewRunTime;
	}

	@Column(name = "create_by", length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_modify_by", length = 50)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time", length = 19)
	public Date getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Column(name = "provider_id")
	public String getProviderId() {
		return this.providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Column(name = "begin_num")
	public Integer getBeginNum() {
		return this.beginNum;
	}

	public void setBeginNum(Integer beginNum) {
		this.beginNum = beginNum;
	}

	@Column(name = "num")
	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "movie")
	public List<Oplog> getOplogs() {
		return this.oplogs;
	}

	public void setOplogs(List<Oplog> oplogs) {
		this.oplogs = oplogs;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Column(name = "state", length = 20)
	@Enumerated(EnumType.STRING)
	public MediaStateEnum getState() {
		return state;
	}

	public void setState(MediaStateEnum state) {
		this.state = state;
	}
	@Column(name = "product_id")
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
