create table ID_GENERATOR (
	ENTITY_NAME varchar(50),
	ID_VALUE int,
	primary key (ENTITY_NAME)
);

create table JOB (
	JOB_ID INT IDENTITY,
	STATE varchar(20),
	SOURCE_URL varchar(100),
	DESTINATION_URL varchar(100),
	CALLBACK_URL varchar(100),
	TP_OPTION varchar(20),
	TP_WIDTH int,
	TP_HEIGHT int,
	TP_BEGINTIME int,
	TP_ENDTIME int,
	TP_INTERVAL int,
	EXCEPTION_MESSAGE varchar(255),
	primary key (JOB_ID)
);

create table JOB_OUTPUTFORMAT (
	JOB_ID INT,
	LIST_IDX INT,
	WIDTH INT,
	HEIGHT INT,
	BITRATE INT,
	CONTAINER varchar(20),
	VIDEO_CODEC varchar(20),
	AUDIO_CODEC varchar(20)
);
create INDEX JOB_OUTPUTFORMAT_IDX ON JOB_OUTPUTFORMAT (JOB_ID, LIST_IDX);