package cn.com.supertv.cmslite.entity.cms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.com.supertv.cmslite.entity.IdEntity;

/**
 * Oplog.
 */
@Entity
@Table(name = "t_cmslite_oplog")
public class Oplog extends IdEntity {

	private Episode episode;
	private Movie movie;
	private Ktv ktv;
	private String comment;
	private String fromState;
	private String toState;
	private String dtype;
	private String operatorName;
	private Date createTime;

	public Oplog() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "episode_id")
	public Episode getEpisode() {
		return this.episode;
	}

	public void setEpisode(Episode episode) {
		this.episode = episode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	public Movie getMovie() {
		return this.movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ktv_id")
	public Ktv getKtv() {
		return this.ktv;
	}

	public void setKtv(Ktv ktv) {
		this.ktv = ktv;
	}

	@Column(name = "comment")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "from_state", length = 50)
	public String getFromState() {
		return this.fromState;
	}

	public void setFromState(String fromState) {
		this.fromState = fromState;
	}

	@Column(name = "to_state", length = 50)
	public String getToState() {
		return this.toState;
	}

	public void setToState(String toState) {
		this.toState = toState;
	}

	@Column(name = "dtype", length = 20)
	public String getDtype() {
		return this.dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	@Column(name = "operator_name", length = 50)
	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
