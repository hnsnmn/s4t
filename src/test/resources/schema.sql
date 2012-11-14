create table ID_GENERATOR (
	TABLE_NAME varchar(100) not null,
	ID_VALUE integer,
	primary key(TABLE_NAME)
);

insert into ID_GENERATOR values ('JOB_GEN', 1);

create table JOB (
	JOB_ID integer,
	STATE varchar(20),
	SOURCE_URL varchar(100),
	DESTINATION_URL varchar(100),
	CALLBACK_URL varchar(100),
	EXCEPTION_MESSAGE varchar(255),
	primary key (JOB_ID)
);