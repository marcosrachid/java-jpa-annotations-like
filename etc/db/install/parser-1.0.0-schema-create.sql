CREATE DATABASE IF NOT EXISTS Parser;
USE Parser;
CREATE TABLE IF NOT EXISTS PARSER_IP (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IP VARCHAR(50) NOT NULL,
	ACTIVE TINYINT(1) NOT NULL,
	BLOCKED_DATE TIMESTAMP NULL,
	CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PK_PI_ID PRIMARY KEY (ID),
	CONSTRAINT UK_PI_IP UNIQUE KEY (IP)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='IP registry loaded from access file' AUTO_INCREMENT=1;
CREATE TABLE IF NOT EXISTS PARSER_ACCESS_REGISTRY (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	ACCESS_DATE TIMESTAMP NOT NULL,
	IP_ID BIGINT NOT NULL,
	HTTP_METHOD VARCHAR(50),
	HTTP_STATUS INT,
	ACCESS_MEANS VARCHAR(250),
	CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PK_PAR_ID PRIMARY KEY (ID),
	CONSTRAINT FK_PAR_IPID_PI_ID FOREIGN KEY (IP_ID) REFERENCES PARSER_IP(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='Access registry loaded from access file' AUTO_INCREMENT=1;
CREATE TABLE IF NOT EXISTS PARSER_LOG (
	ID BIGINT NOT NULL AUTO_INCREMENT,
	IP_ID BIGINT NOT NULL,
	COMMENT VARCHAR(250),
	CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PK_PL_ID PRIMARY KEY (ID),
	CONSTRAINT FK_PL_IPID_PI_ID FOREIGN KEY (IP_ID) REFERENCES PARSER_IP(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='Parser log with reason why ip was blocked' AUTO_INCREMENT=1;