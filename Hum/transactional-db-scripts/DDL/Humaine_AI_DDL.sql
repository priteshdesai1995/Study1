create schema humainedev;

create table humainedev.accountmaster(
	accountid BIGSERIAL primary key,
	username varchar(64) not null,
	siteurl varchar(255),
	status varchar(64),
	email varchar(64) unique not null,
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null,
	sessiontimeout int,
	api_key varchar(64) NULL
);
GRANT SELECT, UPDATE, USAGE ON SEQUENCE humainedev.giftcard_id_seq TO bvdev;
GRANT SELECT, UPDATE, TRUNCATE, REFERENCES, INSERT, DELETE, TRIGGER ON TABLE humainedev.giftcard TO bvdev;

create table humainedev.accountdetails(
	accountdetailid BIGSERIAL primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	address varchar(255),
	expectation_comment varchar(255),
	consumers_from varchar(255),
	current_analytics_solution varchar(255),
	e_shop_hosted_on varchar(255),
	headquarter_location varchar(255),
	highest_product_price varchar(32),
	business_name varchar(255),
	no_of_employees varchar(150),
	no_of_products varchar(32),
	tracking_data bool,
	tracking_data_type varchar(255),
	url varchar(255),
	city varchar(255),
	country varchar(255),
	full_name varchar(50),
	phonenumber varchar(32),
	state varchar(255)
);

CREATE TABLE humainedev.data_tracking_providers (
	accountid bigint references humainedev.accountmaster(accountid),
	provider varchar(255)
);


CREATE TABLE humainedev.account_industries (
	accountid bigint references humainedev.accountmaster(accountid),
	industry varchar(255)
);

create table humainedev.user(
	userid varchar(64) primary key,
	externaluserid varchar(64),
	accountid bigint references humainedev.accountmaster(accountid),
	deviceid varchar(64),
	bodegaid varchar(64),
	devicetype varchar(255),
	pageloadtime bigint,
	createdon timestamp
);



create table humainedev.inventorymaster(
	inventoryid BIGSERIAL primary key,
	productid varchar(64),
	accountid bigint references humainedev.accountmaster(accountid),
	name varchar(255),
	description varchar(1000),
	category varchar(64),
	price numeric(15,2),
	relatedimages varchar(255),
	addedon timestamp with time zone not null
);

create table humainedev.eventmaster(
	eventid varchar(64) primary key,
	description varchar(255),
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null
);

create table humainedev.metricsmaster(

	metricid varchar(64) primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	name varchar(64),
	description varchar(255),
	refreshfrequency numeric(10,2),
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null
);

create table humainedev.userdemographics(

	userid varchar(64) primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	age numeric(3),
	gender varchar(64),
	education varchar(64),
	income numeric(10,2),
	familysize numeric(5),
	rece varchar(64)
);

create table humainedev.dashboardmetrics
(
	metricid varchar(64) primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	metricvalue varchar(64),
	modifiedon timestamp with time zone not null
);

create table humainedev.saledata(
	saleid BIGSERIAL primary key,
	userid varchar(64) references humainedev.user(userid),
	accountid bigint references humainedev.accountmaster(accountid),
	saleon timestamp not null,
	sessionid varchar(64),
	productid varchar(64),
	saleamount numeric(40,2),
	productquantity int,
	usereventid int8 NULL,
	coupon_code varchar(50)
);


CREATE TABLE humainedev.userevent (
	usereventid bigserial NOT null PRIMARY KEY,
	accountid bigint references humainedev.accountmaster(accountid),
	eventid varchar(64) references humainedev.eventmaster(eventid),
	pageurl text NULL,
	menu_id text NULL,
	menu_name varchar(255) NULL,
	menu_url text NULL,
	post_id text NULL,
	post_title varchar(255) NULL,
	productid varchar(255) NULL,
	product_metadata jsonb NULL,
	sessionid varchar(255) NULL,
	social_media_platform varchar(255) NULL,
	social_media_url text NULL,
	"timestamp" timestamp NULL,
	target_element text NULL,
	highlighted_text text NULL,
	product_image_url text NULL,
	rating_value float8 NULL,
	attribute_data text NULL,
	userid varchar(64) references humainedev.user(userid)
);

create table humainedev.usersession(
	sessionid varchar(64) primary key,
	userid varchar(64) references humainedev.user(userid),
	accountid bigint references humainedev.accountmaster(accountid),
	deviceid varchar(64),
	city varchar(64),
	state varchar(64),
	country varchar(64),
	lat float8,
	long float8, 
	starttime timestamp not null,
	endtime timestamp
);


create table humainedev.pagemaster(
	pageid BIGSERIAL primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	pageurl text,
	pagename varchar(255),
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null	
);


create table humainedev.usersessionarchive(
	sessionid varchar(64),
	userid varchar(64),
	accountid bigint,
	deviceid varchar(64),
	city varchar(64),
	state varchar(64),
	country varchar(64),
	lat float8,
	long float8, 
	starttime timestamp,
	endtime timestamp
);

create table humainedev.usereventarchive(
	usereventid BIGSERIAL,
	userid varchar(64),
	accountid bigint,
	eventid varchar(64),
	sessionid varchar(255),
	productid varchar(255),
	pageurl text NULL,
	timestamp timestamp,
	menu_id text NULL,
	menu_name varchar(255) NULL,
	menu_url text NULL,
	post_id text NULL,
	post_title varchar(255) NULL,
    product_metadata jsonb NULL,
    social_media_platform varchar(255) NULL,
	social_media_url text NULL,

);

create table humainedev.saledataarchive(
	saleid BIGSERIAL,
	userid varchar(64),
	accountid bigint,
	saleon timestamp with time zone,
	sessionid varchar(64),
	productid varchar(64),
	saleamount numeric(5,2),
	productquantity int,
	usereventid int8 NULL,
	coupon_code varchar(50) NULL
);


create table humainedev.dashboardmetricsarchive
(
	metricid varchar(64),
	accountid bigint,
	metricvalue varchar(64),
	modifiedon timestamp with time zone not null
);

create table humainedev.accounttracker(
	id BIGSERIAL primary key,
	accountid bigint references humainedev.accountmaster(accountid),
	email varchar(64) not null,
	filename varchar(255) not null,
	ismailsend boolean default false,
	description text,
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null
);

create index index_usereventarchive on humainedev.usereventarchive(usereventid,userid,accountid);

create index index_saledataarchive on humainedev.saledataarchive(saleid,userid,accountid);

create index index_usersessionarchive on humainedev.usersessionarchive(sessionid,userid,accountid);


CREATE TABLE humainedev.big_five_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT big_five_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_big_five_master_value UNIQUE (value)
);

CREATE TABLE humainedev.age_group_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT age_group_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_age_group_master_value UNIQUE (value)
);

CREATE TABLE humainedev.gender_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    is_other bool NULL DEFAULT true,
    CONSTRAINT gender_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_gender_master_value UNIQUE (value)
);

CREATE TABLE humainedev.family_size_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT family_size_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_family_size_master_value UNIQUE (value)
);

CREATE TABLE humainedev.ethnicity_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT ethnicity_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_ethnicity_master_value UNIQUE (value)
);

CREATE TABLE humainedev.buying_master (
	id bigserial NOT NULL,
	createdon timestamp NULL,
	value varchar(64) NULL,
	CONSTRAINT buying_master_pkey PRIMARY KEY (id),
	CONSTRAINT uk_buying_master_value UNIQUE (value)
);

CREATE TABLE humainedev.persuasive_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT persuasive_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_persuasive_master_value UNIQUE (value)
);

CREATE TABLE humainedev.values_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT uk_values_master_value UNIQUE (value),
    CONSTRAINT values_master_pkey PRIMARY KEY (id)
);

CREATE TABLE humainedev.education_master (
    id bigserial NOT NULL,
    createdon timestamp NULL,
    value varchar(64) NULL,
    CONSTRAINT education_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk_education_master_value UNIQUE (value)
);

CREATE TABLE humainedev.user_group_master (
	id bigserial NOT NULL,
	createdon timestamp NULL,
    icon varchar(255) NULL,
	modifiedon timestamp NULL,
	user_group_name varchar(64) NULL,
	state varchar(255) NULL,
	is_external_factor bool NULL,
	accountid bigserial NOT NULL,
	age_group bigserial NOT NULL,
	big_five bigserial NOT NULL,
	buying bigserial NOT NULL,
	education bigserial NOT NULL,
	ethnicity bigserial NOT NULL,
	family_size bigserial NOT NULL,
	gender bigserial NOT NULL,
	persuasive bigserial NOT NULL,
    success_match varchar(255) NULL,
	"values" bigserial NOT NULL,
	CONSTRAINT user_group_master_pkey PRIMARY KEY (id)
);


-- humainedev.user_group_master foreign keys

ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_gender_master_user_group_master FOREIGN KEY (gender) REFERENCES humainedev.gender_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_persuasive_master_user_group_master FOREIGN KEY (persuasive) REFERENCES humainedev.persuasive_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_accountmaster_user_group_master FOREIGN KEY (accountid) REFERENCES humainedev.accountmaster(accountid);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_education_master_user_group_master FOREIGN KEY (education) REFERENCES humainedev.education_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_age_group_master_user_group_master FOREIGN KEY (age_group) REFERENCES humainedev.age_group_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_buying_master_master_user_group_master FOREIGN KEY (buying) REFERENCES humainedev.buying_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_big_five_master_user_group_master FOREIGN KEY (big_five) REFERENCES humainedev.big_five_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_ethnicity_master_user_group_master FOREIGN KEY (ethnicity) REFERENCES humainedev.ethnicity_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_values_master_user_group_master FOREIGN KEY ("values") REFERENCES humainedev.values_master(id);
ALTER TABLE humainedev.user_group_master ADD CONSTRAINT FK_family_size_master_user_group_master FOREIGN KEY (family_size) REFERENCES humainedev.family_size_master(id);


CREATE TABLE humainedev.big_five_buying (
	big_five_id bigserial NOT NULL,
	buying_id bigserial NOT NULL,
	CONSTRAINT big_five_buying_pkey PRIMARY KEY (big_five_id, buying_id)
);


-- humainedev.big_five_buying foreign keys

ALTER TABLE humainedev.big_five_buying ADD CONSTRAINT FK_big_five_master_big_five_buying FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);
ALTER TABLE humainedev.big_five_buying ADD CONSTRAINT FK_buying_master_big_five_buying FOREIGN KEY (buying_id) REFERENCES humainedev.buying_master(id);



CREATE TABLE humainedev.big_five_persuasive (
	big_five_id bigserial NOT NULL,
	persuasive_id bigserial NOT NULL,
	CONSTRAINT big_five_persuasive_pkey PRIMARY KEY (big_five_id, persuasive_id)
);


-- humainedev.big_five_persuasive foreign keys

ALTER TABLE humainedev.big_five_persuasive ADD CONSTRAINT FK_persuasive_master_big_five_persuasive FOREIGN KEY (persuasive_id) REFERENCES humainedev.persuasive_master(id);
ALTER TABLE humainedev.big_five_persuasive ADD CONSTRAINT FK_big_five_master_big_five_persuasive FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);



CREATE TABLE humainedev.big_five_values (
	big_five_id bigserial NOT NULL,
	values_id bigserial NOT NULL,
	CONSTRAINT big_five_values_pkey PRIMARY KEY (big_five_id, values_id)
);


-- humainedev.big_five_values foreign keys

ALTER TABLE humainedev.big_five_values ADD CONSTRAINT FK_values_master_big_five_values FOREIGN KEY (values_id) REFERENCES humainedev.values_master(id);
ALTER TABLE humainedev.big_five_values ADD CONSTRAINT FK_big_five_master_big_five_values FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);




CREATE TABLE humainedev.user_group_master_deleted (
	id bigserial NOT NULL,
	account_id int8 NULL,
	age_group int8 NULL,
	big_five int8 NULL,
	buying int8 NULL,
	createdon timestamp NULL,
	education int8 NULL,
	ethnicity int8 NULL,
	family_size int8 NULL,
	gender int8 NULL,
	icon varchar(255) NULL,
	is_external_factor bool NULL,
	modifiedon timestamp NULL,
	user_group_name varchar(64) NULL,
	persuasive int8 NULL,
	state varchar(255) NULL,
	success_match varchar(255) NULL,
	user_group_id int8 NULL,
	"values" int8 NULL,
	CONSTRAINT user_group_master_deleted_pkey PRIMARY KEY (id)
);


CREATE TABLE humainedev.batch_job_instance (
	job_instance_id int8 NOT NULL,
	"version" int8 NULL,
	job_name varchar(100) NOT NULL,
	job_key varchar(32) NOT NULL,
	CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id),
	CONSTRAINT job_inst_un UNIQUE (job_name, job_key)
);



CREATE TABLE humainedev.batch_job_execution (
	job_execution_id int8 NOT NULL,
	"version" int8 NULL,
	job_instance_id int8 NOT NULL,
	create_time timestamp NOT NULL,
	start_time timestamp NULL,
	end_time timestamp NULL,
	status varchar(10) NULL,
	exit_code varchar(2500) NULL,
	exit_message varchar(2500) NULL,
	last_updated timestamp NULL,
	job_configuration_location varchar(2500) NULL,
	CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id)
);


-- humainedev.batch_job_execution foreign keys

ALTER TABLE humainedev.batch_job_execution ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES humainedev.batch_job_instance(job_instance_id);



CREATE TABLE humainedev.batch_job_execution_context (
	job_execution_id int8 NOT NULL,
	short_context varchar(2500) NOT NULL,
	serialized_context text NULL,
	CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id)
);


-- humainedev.batch_job_execution_context foreign keys

ALTER TABLE humainedev.batch_job_execution_context ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES humainedev.batch_job_execution(job_execution_id);




CREATE TABLE humainedev.batch_job_execution_params (
	job_execution_id int8 NOT NULL,
	type_cd varchar(6) NOT NULL,
	key_name varchar(100) NOT NULL,
	string_val varchar(250) NULL,
	date_val timestamp NULL,
	long_val int8 NULL,
	double_val float8 NULL,
	identifying bpchar(1) NOT NULL
);


-- humainedev.batch_job_execution_params foreign keys

ALTER TABLE humainedev.batch_job_execution_params ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES humainedev.batch_job_execution(job_execution_id);




CREATE TABLE humainedev.batch_step_execution (
	step_execution_id int8 NOT NULL,
	"version" int8 NOT NULL,
	step_name varchar(100) NOT NULL,
	job_execution_id int8 NOT NULL,
	start_time timestamp NOT NULL,
	end_time timestamp NULL,
	status varchar(10) NULL,
	commit_count int8 NULL,
	read_count int8 NULL,
	filter_count int8 NULL,
	write_count int8 NULL,
	read_skip_count int8 NULL,
	write_skip_count int8 NULL,
	process_skip_count int8 NULL,
	rollback_count int8 NULL,
	exit_code varchar(2500) NULL,
	exit_message varchar(2500) NULL,
	last_updated timestamp NULL,
	CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id)
);


-- humainedev.batch_step_execution foreign keys

ALTER TABLE humainedev.batch_step_execution ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES humainedev.batch_job_execution(job_execution_id);





CREATE TABLE humainedev.batch_step_execution_context (
	step_execution_id int8 NOT NULL,
	short_context varchar(2500) NOT NULL,
	serialized_context text NULL,
	CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id)
);


-- humainedev.batch_step_execution_context foreign keys

ALTER TABLE humainedev.batch_step_execution_context ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES humainedev.batch_step_execution(step_execution_id);


CREATE TABLE humainedev.big_five_personalities (
	big_five_id bigserial NOT NULL,
	personalities text NULL
);


ALTER TABLE humainedev.big_five_personalities ADD CONSTRAINT FK_big_five_master_big_five_personalities FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);


CREATE TABLE humainedev.big_five_goals (
	big_five_id bigserial NOT NULL,
	goals text NULL
);

ALTER TABLE humainedev.big_five_goals ADD CONSTRAINT FK_big_five_master_big_five_goals FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);


CREATE TABLE humainedev.big_five_frustrations (
	big_five_id bigserial NOT NULL,
	frustrations text NULL
);

ALTER TABLE humainedev.big_five_frustrations ADD CONSTRAINT FK_big_five_master_big_five_frustrations FOREIGN KEY (big_five_id) REFERENCES humainedev.big_five_master(id);


CREATE TABLE humainedev.journey_element_master (
	id bigserial NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT journey_element_master_pkey PRIMARY KEY (id)
);


CREATE TABLE humainedev.journey_element_values (
	id bigserial NOT NULL,
	value varchar(255) NULL,
	element_id bigint NOT NULL,
	event_id varchar references humainedev.eventmaster(eventid)
	CONSTRAINT journey_element_values_pkey PRIMARY KEY (id)
);


ALTER TABLE humainedev.journey_element_values ADD CONSTRAINT FK_journey_element_master_journey_element_values FOREIGN KEY (element_id) REFERENCES humainedev.journey_element_master(id);


CREATE TABLE humainedev.test_journey_master (
	id bigserial NOT NULL,
	created_on timestamp NULL,
	decison varchar(255) NULL,
	first_interest varchar(255) NULL,
	group_id int8 NULL,
	journey_steps int8 NULL,
	purchase_add_cart varchar(255) NULL,
	purchase_buy varchar(255) NULL,
	purchase_ownership varchar(255) NULL,
	accountid bigserial NOT NULL,
	CONSTRAINT test_journey_master_pkey PRIMARY KEY (id)
);


ALTER TABLE humainedev.test_journey_master ADD CONSTRAINT FK_journey_element_master_journey_element_values FOREIGN KEY (accountid) REFERENCES humainedev.accountmaster(accountid);


CREATE TABLE humainedev.test_journey_master_deleted (
	id bigserial NOT NULL,
	accountid int8 NOT NULL,
	journey_created_on timestamp NULL,
	decison varchar(255) NULL,
	first_interest varchar(255) NULL,
	group_id int8 NULL,
	journey_id int8 NULL,
	journey_steps int8 NULL,
	purchase_add_cart varchar(255) NULL,
	purchase_buy varchar(255) NULL,
	purchase_ownership varchar(255) NULL,
	created_on timestamp NULL,
	CONSTRAINT test_journey_master_deleted_pkey PRIMARY KEY (id)
);

CREATE TABLE humainedev.my_journey_deleted (
	id bigserial NOT NULL,
	account_id int8 NOT NULL,
	journey_created_on timestamp NULL,
	decison varchar(255) NULL,
	first_interest varchar(255) NULL,
	group_id int8 NULL,
	journey_id int8 NULL,
	journey_steps int8 NULL,
	purchase_add_cart varchar(255) NULL,
	purchase_buy varchar(255) NULL,
	purchase_ownership varchar(255) NULL,
	created_on timestamp NULL,
	journey_success float4 NULL,
	journey_time float4 NULL,
	CONSTRAINT my_journey_deleted_pkey PRIMARY KEY (id)
);
ALTER TABLE humainedev.my_journey_deleted ADD journey_journey_id bigint NULL;


create table humainedev.journey_element_event_mapping ( 
	id bigserial NOT NULL,
	journey_element_value_id bigint references humainedev.journey_element_values(id),
	event_id varchar references humainedev.eventmaster(eventid)
);

CREATE TABLE humainedev.pageload_data (
	id bigserial NOT NULL,
	accountid int8 NULL,
	sessionid varchar(255) NULL,
	userid varchar(255) NULL,
	pageurl text NULL,
	performance_data jsonb NULL,
	loadtime int8 NULL,
	"timestamp" timestamp NULL,
	page_source text NULL,
	CONSTRAINT pageload_data_pkey PRIMARY KEY (id)
);


CREATE TABLE humainedev.account_heatmap (
	id bigserial NOT NULL,
	accountid int8 NULL,
	category varchar(255) NULL,
	device varchar(255) NULL,
	pageurl text NULL,
	file varchar(255) NULL,
	localpath varchar(255) NULL,
	createdon timestamp NOT NULL DEFAULT timezone('utc'::text, now()),
	awspath varchar NULL,
	aws_upload_fail bool NULL,
	upload_error text NULL,
	CONSTRAINT account_heatmap_pkey PRIMARY KEY (id)
);


-- humainedev.account_heatmap foreign keys

ALTER TABLE humainedev.account_heatmap ADD CONSTRAINT account_heatmap_accountid_fkey FOREIGN KEY (accountid) REFERENCES humainedev.accountmaster(accountid);


CREATE TABLE humainedev.account_webpage_url (
	id bigserial NOT NULL,
	accountid int8 NULL,
	url text NULL,
	"type" varchar(255) NULL,
	events jsonb NULL,
	CONSTRAINT account_webpage_url_pkey PRIMARY KEY (id)
);


-- humainedev.account_webpage_url foreign keys

ALTER TABLE humainedev.account_webpage_url ADD CONSTRAINT account_webpage_url_accountid_fkey FOREIGN KEY (accountid) REFERENCES humainedev.accountmaster(accountid);


CREATE TABLE humainedev.ai_user_group (
	id bigserial NOT NULL,
	account_id int8 NOT NULL,
	big_five varchar(255) NULL,
	icon varchar(255) NULL,
	motivation varchar(255) NULL,
	group_name varchar(255) NULL,
	persuasive varchar(255) NULL,
	success_rate float8 NULL,
	time_stamp timestamp NULL,
	user_group_id varchar(255) NOT NULL,
	value varchar(255) NULL,
	female_percent float4 NULL,
	male_percent float4 NULL,
	maximum_age int8 NULL,
	maximum_education int8 NULL,
	maximum_family_size int8 NULL,
	minimum_age int8 NULL,
	minimum_education int8 NULL,
	minimum_family_size int8 NULL,
	number_of_user int8 NULL,
	other_percent float4 NULL,
	CONSTRAINT ai_user_group_pkey PRIMARY KEY (id)
);


CREATE TABLE humainedev.ai_user_group_deleted (
	id bigserial NOT NULL,
	account_id int8 NOT NULL,
	ai_user_group_id int8 NOT NULL,
	big_five varchar(255) NULL,
	icon varchar(255) NULL,
	motivation varchar(255) NULL,
	group_name varchar(255) NULL,
	persuasive varchar(255) NULL,
	success_rate float8 NULL,
	time_stamp timestamp NULL,
	user_group_id varchar(255) NOT NULL,
	value varchar(255) NULL,
	female_percent float4 NULL,
	male_percent float4 NULL,
	maximum_age int8 NULL,
	maximum_education int8 NULL,
	maximum_family_size int8 NULL,
	minimum_age int8 NULL,
	minimum_education int8 NULL,
	minimum_family_size int8 NULL,
	number_of_user int8 NULL,
	other_percent float4 NULL,
	CONSTRAINT ai_user_group_deleted_pkey PRIMARY KEY (id)
);


CREATE TABLE humainedev.persona_details_master (
	id bigserial NOT NULL,
	big_five bigserial NOT NULL,
	buy bigserial NOT NULL,
	strategies bigserial NOT NULL,
	"values" bigserial NOT NULL,
	CONSTRAINT persona_details_master_pkey PRIMARY KEY (id)
);

ALTER TABLE humainedev.persona_details_master ADD CONSTRAINT fk_big_five_master_persona_details_master FOREIGN KEY (big_five) REFERENCES humainedev.big_five_master(id);
ALTER TABLE humainedev.persona_details_master ADD CONSTRAINT fk_buying_master_persona_details_master FOREIGN KEY (buy) REFERENCES humainedev.buying_master(id);
ALTER TABLE humainedev.persona_details_master ADD CONSTRAINT fk_persuasive_master_details_master FOREIGN KEY (strategies) REFERENCES humainedev.persuasive_master(id);
ALTER TABLE humainedev.persona_details_master ADD CONSTRAINT fk_values_master_details_master FOREIGN KEY ("values") REFERENCES humainedev.values_master(id);



CREATE TABLE humainedev.persona_frustrations (
	persona_details_id bigserial NOT NULL,
	frustrations text NULL
);

ALTER TABLE humainedev.persona_frustrations ADD CONSTRAINT fk_persona_details_master_persona_frustrations FOREIGN KEY (persona_details_id) REFERENCES humainedev.persona_details_master(id);


CREATE TABLE humainedev.persona_goals (
	persona_details_id bigserial NOT NULL,
	goals text NULL
);

ALTER TABLE humainedev.persona_goals ADD CONSTRAINT fk_persona_details_master_persona_goals FOREIGN KEY (persona_details_id) REFERENCES humainedev.persona_details_master(id);

CREATE TABLE humainedev.persona_personality (
	persona_details_id bigserial NOT NULL,
	personalities text NULL
);

ALTER TABLE humainedev.persona_personality ADD CONSTRAINT fk_persona_details_master_persona_personality FOREIGN KEY (persona_details_id) REFERENCES humainedev.persona_details_master(id);


CREATE TABLE humainedev.master_category (
	id bigserial NOT NULL,
	cat_id varchar(255) NULL,
	cat_name varchar(255) NULL,
	account_accountid bigserial NOT NULL,
	parent_id bigint NULL,
	CONSTRAINT master_category_pkey PRIMARY KEY (id)
);

ALTER TABLE humainedev.master_category ADD CONSTRAINT fk_accountmaster_master_category FOREIGN KEY (account_accountid) REFERENCES humainedev.accountmaster(accountid);
ALTER TABLE humainedev.master_category ADD CONSTRAINT fk_master_category_master_category FOREIGN KEY (parent_id) REFERENCES humainedev.master_category(id);


CREATE TABLE humainedev.master_product (
	id bigserial NOT NULL,
	p_desc varchar(255) NULL,
	p_id varchar(255) NULL,
	p_image varchar(255) NULL,
	p_name varchar(255) NULL,
	p_sku varchar(255) NULL,
	account_id bigserial NOT NULL,
	p_cat_id bigint NULL,
	parent_product_id bigint NULL,
	CONSTRAINT master_product_pkey PRIMARY KEY (id)
);

ALTER TABLE humainedev.master_product ADD CONSTRAINT fk_master_category_master_product FOREIGN KEY (p_cat_id) REFERENCES humainedev.master_category(id);
ALTER TABLE humainedev.master_product ADD CONSTRAINT fk_master_product_master_product FOREIGN KEY (parent_product_id) REFERENCES humainedev.master_product(id);
ALTER TABLE humainedev.master_product ADD CONSTRAINT fk_accountmaster_master_product FOREIGN KEY (account_id) REFERENCES humainedev.accountmaster(accountid);


CREATE TABLE humainedev.product_price (
	id bigserial NOT NULL,
	created_date timestamp NULL,
	price varchar(255) NULL,
	special_price varchar(255) NULL,
	account_id bigserial NOT NULL,
	product_id bigserial NOT NULL,
	CONSTRAINT product_price_pkey PRIMARY KEY (id)
);

ALTER TABLE humainedev.product_price ADD CONSTRAINT fk_master_product_product_price FOREIGN KEY (product_id) REFERENCES humainedev.master_product(id);
ALTER TABLE humainedev.product_price ADD CONSTRAINT fk_accountmaster_product_price FOREIGN KEY (account_id) REFERENCES humainedev.accountmaster(accountid);


CREATE TABLE humainedev.master_product_prices (
	product_id bigserial NOT NULL,
	prices_id bigserial NOT NULL,
	CONSTRAINT master_product_prices_pkey PRIMARY KEY (product_id, prices_id),
	CONSTRAINT uk_master_product_prices UNIQUE (prices_id)
);

ALTER TABLE humainedev.master_product_prices ADD CONSTRAINT fk_master_product_master_product_prices FOREIGN KEY (product_id) REFERENCES humainedev.master_product(id);
ALTER TABLE humainedev.master_product_prices ADD CONSTRAINT fk_product_price_master_product_prices FOREIGN KEY (prices_id) REFERENCES humainedev.product_price(id);


CREATE TABLE humainedev.product_attributes (
	product_id bigserial NOT NULL,
	attr_name varchar(255) NULL,
	attr_value varchar(255) NULL
);

ALTER TABLE humainedev.product_attributes ADD CONSTRAINT fk_master_product_product_attributes FOREIGN KEY (product_id) REFERENCES humainedev.master_product(id);

CREATE TABLE humainedev.giftcard (
	id bigserial NOT NULL ,
	created_on timestamp with time zone NULL,
	modified_on timestamp without time zone NULL,
	uuid varchar(255) NOT NULL,
	session_id varchar(255) NULL,
	user_id varchar(255) NULL,
	account_id bigint NULL,
	CONSTRAINT giftcard_pk PRIMARY KEY (id)
);

create table humainedev.mouse_event_recording(
	eventid BIGSERIAL primary key,
	accountid int8 NOT NULL,
	pageurl text not null,
	cursorx varchar(255),
	cursory varchar(255),
	userid varchar,
	sessionid varchar(255),
	window_size jsonb,
	createdon timestamp with time zone not null,
	modifiedon timestamp with time zone not null
);

CREATE TABLE humainedev.g_form_status (
	id int8 NOT NULL,
	created_on timestamptz NULL,
	submitted_on timestamp NULL,
	session_id varchar(255) NULL,
	user_id varchar(255) NULL,
	account_id int8 NULL,
	CONSTRAINT giftcard_pk PRIMARY KEY (id)
);


CREATE TABLE humainedev.coupon_code_info (
	id bigserial NOT NULL,
	apply_date date NULL,
	coupon_code varchar(255) NULL,
	CONSTRAINT coupon_code_info_pkey PRIMARY KEY (id)
);