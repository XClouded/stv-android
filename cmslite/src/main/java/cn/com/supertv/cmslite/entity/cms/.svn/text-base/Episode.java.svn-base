package cn.com.supertv.cmslite.entity.cms;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.com.supertv.cmslite.entity.IdEntity;

import com.google.common.collect.Lists;

/**
 * Episode.
 */
@Entity
@Table(name = "t_cmslite_episode")
public class Episode extends IdEntity {

	private Integer version;
	private Tv tv;
	private String videoFtpPath;
	private Integer episodeId;
	private MediaStateEnum state;
	private String ftpId;
	private Integer suggestedPrice;
	private Date createTime;
	private String createBy;
	private Date lastModifyTime;
	private String lastModifyBy;
	private List<Oplog> oplogs = Lists.newArrayList();

	public Episode() {
	}

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tv_id")
	public Tv getTv() {
		return this.tv;
	}

	public void setTv(Tv tv) {
		this.tv = tv;
	}

	@Column(name = "video_ftp_path", length = 200)
	public String getVideoFtpPath() {
		return this.videoFtpPath;
	}

	public void setVideoFtpPath(String videoFtpPath) {
		this.videoFtpPath = videoFtpPath;
	}

	@Column(name = "episode_id")
	public Integer getEpisodeId() {
		return this.episodeId;
	}

	public void setEpisodeId(Integer episodeId) {
		this.episodeId = episodeId;
	}

	@Column(name = "ftp_id", length = 200)
	public String getFtpId() {
		return this.ftpId;
	}

	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
	}

	@Column(name = "suggested_price")
	public Integer getSuggestedPrice() {
		return this.suggestedPrice;
	}

	public void setSuggestedPrice(Integer suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_by", length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time", length = 19)
	public Date getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	@Column(name = "last_modify_by", length = 50)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "episode")
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
}
