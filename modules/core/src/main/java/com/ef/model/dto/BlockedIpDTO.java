package com.ef.model.dto;

import java.io.Serializable;

/**
 * Data transfer object containing IPs to be blocked
 * 
 * @author marcosrachid
 *
 */
public class BlockedIpDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ipId;
	
	private String ip;
	
	private Long counter;
	
	public BlockedIpDTO() {
	}
	
	public BlockedIpDTO(Long ipId, String ip, Long counter) {
		this.ipId = ipId;
		this.ip = ip;
		this.counter = counter;
	}

	public Long getIpId() {
		return ipId;
	}

	public void setIpId(Long ipId) {
		this.ipId = ipId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getCounter() {
		return counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}
	
}
