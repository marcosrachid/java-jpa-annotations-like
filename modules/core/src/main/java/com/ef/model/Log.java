package com.ef.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ef.model.annotations.Column;
import com.ef.model.annotations.CreateDate;
import com.ef.model.annotations.Id;
import com.ef.model.annotations.Table;

/**
 * PARSER_LOG entity
 * 
 * @author marcosrachid
 *
 */
@Table(name = "PARSER_LOG")
public class Log implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "IP_ID")
	private Long ipId;

	@Column(name = "COMMENT")
	private String comment;

	@CreateDate
	@Column(name = "CREATE_DATE")
	private LocalDateTime createDate;

	public Log() {
	}

	public Log(Long ipId, String comment) {
		this.ipId = ipId;
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	
	public static LogBuilder createBuilder() {
		return new LogBuilder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Log)) {
			return false;
		}
		Log that = (Log) obj;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", ipId=" + ipId + ", comment=" + comment + ", createDate=" + createDate + "]";
	}
	
	public static class LogBuilder {

		private Log log = null;

		private LogBuilder() {
			log = new Log();
		}
		
		public LogBuilder withId(Long id) {
			log.setId(id);
			return this;
		}

		public LogBuilder withIp(Long ipId) {
			log.setIpId(ipId);
			return this;
		}

		public LogBuilder withComment(String comment) {
			log.setComment(comment);
			return this;
		}
		
		public LogBuilder withCreateDate(LocalDateTime createDate) {
			log.setCreateDate(createDate);
			return this;
		}

		public Log build() {
			return log;
		}
	}

}
