package com.ef.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ef.model.annotations.Column;
import com.ef.model.annotations.CreateDate;
import com.ef.model.annotations.Id;
import com.ef.model.annotations.Table;

/**
 * PARSER_ACCESS_REGISTRY entity
 * 
 * @author marcosrachid
 *
 */
@Table(name = "PARSER_ACCESS_REGISTRY")
public class Access implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "ACCESS_DATE")
	private LocalDateTime accessDate;

	@Column(name = "IP_ID")
	private Long ipId;

	@Column(name = "HTTP_METHOD")
	private String httpMethod;

	@Column(name = "HTTP_STATUS")
	private Integer httpStatus;

	@Column(name = "ACCESS_MEANS")
	private String accessMeans;

	@CreateDate
	@Column(name = "CREATE_DATE")
	private LocalDateTime createDate;

	public Access() {
	}

	public Access(LocalDateTime accessDate, Long ipId, String httpMethod, Integer httpStatus, String accessMeans) {
		this.accessDate = accessDate;
		this.ipId = ipId;
		this.httpMethod = httpMethod;
		this.httpStatus = httpStatus;
		this.accessMeans = accessMeans;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(LocalDateTime accessDate) {
		this.accessDate = accessDate;
	}

	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getAccessMeans() {
		return accessMeans;
	}

	public void setAccessMeans(String accessMeans) {
		this.accessMeans = accessMeans;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public static AccessBuilder createBuilder() {
		return new AccessBuilder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Access)) {
			return false;
		}
		Access that = (Access) obj;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public String toString() {
		return "Access [id=" + id + ", accessDate=" + accessDate + ", ipId=" + ipId + ", httpMethod=" + httpMethod
				+ ", httpStatus=" + httpStatus + ", accessMeans=" + accessMeans + ", createDate=" + createDate + "]";
	}
	
	public static class AccessBuilder {

		private Access access = null;

		private AccessBuilder() {
			access = new Access();
		}
		
		public AccessBuilder withId(Long id) {
			access.setId(id);
			return this;
		}

		public AccessBuilder withAccessDate(LocalDateTime accessDate) {
			access.setAccessDate(accessDate);
			return this;
		}

		public AccessBuilder withIp(Long ipId) {
			access.setIpId(ipId);;
			return this;
		}

		public AccessBuilder withHttpMethod(String httpMethod) {
			access.setHttpMethod(httpMethod);
			return this;
		}

		public AccessBuilder withHttpStatus(Integer httpStatus) {
			access.setHttpStatus(httpStatus);
			return this;
		}

		public AccessBuilder withAccessMeans(String accessMeans) {
			access.setAccessMeans(accessMeans);
			return this;
		}
		
		public AccessBuilder withCreateDate(LocalDateTime createDate) {
			access.setCreateDate(createDate);
			return this;
		}

		public Access build() {
			return access;
		}

	}

}
