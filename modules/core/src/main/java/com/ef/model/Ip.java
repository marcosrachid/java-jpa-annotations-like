package com.ef.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.ef.model.annotations.Column;
import com.ef.model.annotations.CreateDate;
import com.ef.model.annotations.Id;
import com.ef.model.annotations.Table;

/**
 * PARSER_IP entity
 * 
 * @author marcosrachid
 *
 */
@Table(name = "PARSER_IP")
public class Ip implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "IP")
	private String ip;

	@Column(name = "ACTIVE")
	private Boolean active;

	@Column(name = "BLOCKED_DATE")
	private LocalDateTime blockedDate = null;

	@CreateDate
	@Column(name = "CREATE_DATE")
	private LocalDateTime createDate;

	public Ip() {
	}

	public Ip(String ip, Boolean active) {
		this.ip = ip;
		this.active = active;
	}
	
	public Ip(Long id, Boolean active, LocalDateTime blockedDate) {
		this.id = id;
		this.active = active;
		this.blockedDate = blockedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDateTime getBlockedDate() {
		return blockedDate;
	}

	public void setBlockedDate(LocalDateTime blockedDate) {
		this.blockedDate = blockedDate;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	
	public static IpBuilder createBuilder() {
		return new IpBuilder();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ip)) {
			return false;
		}
		Ip that = (Ip) obj;
		return Objects.equals(this.ip, that.ip);
	}

	@Override
	public String toString() {
		return "Ip [id=" + id + ", ip=" + ip + ", active=" + active + ", blockedDate=" + blockedDate + ", createDate="
				+ createDate + "]";
	}
	
	public static class IpBuilder {
		
		private Ip ip = null; 
		
		private IpBuilder() {
			ip = new Ip();
		}
		
		public IpBuilder withId(Long id) {
			ip.setId(id);
			return this;
		}
		
		public IpBuilder withIp(String ipNumber) {
			ip.setIp(ipNumber);
			return this;
		}
		
		public IpBuilder withActive(Boolean active) {
			ip.setActive(active);
			return this;
		}
		
		public IpBuilder withBlockedDate(LocalDateTime blockedDate) {
			ip.setBlockedDate(blockedDate);
			return this;
		}
		
		public IpBuilder withCreateDate(LocalDateTime createDate) {
			ip.setCreateDate(createDate);
			return this;
		}
		
		public Ip build() {
			return ip;
		}
	}

}
