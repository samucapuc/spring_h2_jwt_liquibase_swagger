--liquibase formatted sql
--changeset samuel.chaves:001.1 context:test
	CREATE TABLE JOB (
	    ID              BIGINT  GENERATED BY DEFAULT AS IDENTITY,
	    IS_ACTIVE       BOOLEAN,
	    NAME_JOB        VARCHAR(255),
	    ID_PARENT_JOB   BIGINT,
	    PRIMARY KEY ( ID )
	);
	
	CREATE TABLE TASK (
	    ID_TASK        INTEGER GENERATED BY DEFAULT AS IDENTITY,
	    IS_COMPLETED   BOOLEAN,
	    CREATED_AT     DATE,
	    NAME_TASK      VARCHAR(255),
	    VERSION        INTEGER,
	    WEIGHT         INTEGER,
	    ID_JOB         BIGINT,
	    PRIMARY KEY ( ID_TASK )
	);
	
	ALTER TABLE JOB ADD CONSTRAINT FK_JOB_JOB FOREIGN KEY ( ID_PARENT_JOB )
	    REFERENCES JOB;
	
	ALTER TABLE TASK ADD CONSTRAINT FK_TASK_JOB FOREIGN KEY ( ID_JOB )
	    REFERENCES JOB;
	    
	INSERT INTO TASK (
	    IS_COMPLETED   ,
	    CREATED_AT     ,
	    NAME_TASK      ,
	    VERSION        ,
	    WEIGHT         ,
	    ID_JOB         
	) VALUES(TRUE,CURRENT_DATE(),'FIRST TASK',0,5,NULL);
	
	
	INSERT INTO TASK (
	    IS_COMPLETED   ,
	    CREATED_AT     ,
	    NAME_TASK      ,
	    VERSION        ,
	    WEIGHT         ,
	    ID_JOB         
	) VALUES(FALSE,CURRENT_DATE(),'SECOND TASK',0,8,NULL);
--rollback TRUNCATE TABLE TASK;