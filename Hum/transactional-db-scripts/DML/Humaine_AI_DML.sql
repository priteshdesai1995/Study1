INSERT INTO humainedev.accountmaster
(username, siteurl, status, email, createdon, modifiedon, sessiontimeout)
VALUES('Test1', 'http://www.google.com', 'confirmed', 'test1@mail.com','2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 7200);

INSERT INTO humainedev.accountmaster
(username, siteurl, status, email, createdon, modifiedon, sessiontimeout)
VALUES('Test2', 'http://www.google.com', 'confirmed', 'test2@mail.com','2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 7200);

INSERT INTO humainedev.accountmaster
(username, siteurl, status, email, createdon, modifiedon, sessiontimeout)
VALUES('Test3', 'http://www.google.com', 'confirmed', 'test3@mail.com','2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 7200);

INSERT INTO humainedev.accountmaster
(username, siteurl, status, email, createdon, modifiedon, sessiontimeout)
VALUES('Test4', 'http://www.google.com', 'unconfirmed', 'test4@mail.com','2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 7200);


INSERT INTO humainedev.accountmaster
(username, siteurl, status, email, createdon, modifiedon, sessiontimeout)
VALUES('Test5', 'http://www.google.com', 'unconfirmed', 'test5@mail.com','2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 7200);

-- ---------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.accountdetails(
	accountid, address, expectation_comment, consumers_from, current_analytics_solution, e_shop_hosted_on, headquarter_location, highest_product_price, business_name, no_of_employees, no_of_products, tracking_data, tracking_data_type, url, city, country, full_name, phonenumber, state)
	VALUES (1, 'new street road', 'track user events', 'e-commerce', 'i dont have one', 'shopify', 'USA', '300.1$', 'Flipkart', '20-50', '500', true, 'Demographic data','https://www.flipkart.com', 'Chicago', 'USA', 'Flipkart', '1234567890', 'Illinois');

-- ---------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.data_tracking_providers(accountid, provider) VALUES (1, 'cookie');
INSERT INTO humainedev.data_tracking_providers(accountid, provider) VALUES (1, 'Provider 1');
-- ---------------------------------------------------------------------------------------------------------------------------------------
INSERT INTO humainedev.account_industries(accountid, industry) VALUES (1, 'Health and Beauty');
INSERT INTO humainedev.account_industries(accountid, industry) VALUES (1, 'Toys and Baby Equipment');
INSERT INTO humainedev.account_industries(accountid, industry) VALUES (1, 'Home and Furniture');

-- ---------------------------------------------------------------------------------------------------------------------------------------

-- Removed Location Master Table
-- ---------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.user
(userid, externaluserid,accountid, deviceid, bodegaid, devicetype, pageloadtime, createdon)
VALUES('USER001', 'EXTUSER001',1, 'DEVICE001', 'BODEGA001', 'PC', 100, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.user
(userid, externaluserid, accountid, deviceid, bodegaid, devicetype, pageloadtime, createdon)
VALUES('USER002', 'EXTUSER002', 1, 'DEVICE002', 'BODEGA002', 'PC', 100, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.user
(userid, externaluserid, accountid, deviceid, bodegaid, devicetype, pageloadtime, createdon)
VALUES('USER003', 'EXTUSER003', 1, 'DEVICE003', 'BODEGA003', 'PC', 100, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.user
(userid, externaluserid, accountid, deviceid, bodegaid, devicetype, pageloadtime, createdon)
VALUES('USER004', 'EXTUSER004', 1, 'DEVICE004', 'BODEGA004', 'PC', 100, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.user
(userid, externaluserid, accountid, deviceid, bodegaid, devicetype, pageloadtime, createdon)
VALUES('USER005', 'EXTUSER005', 1, 'DEVICE005', 'BODEGA005', 'PC', 100, '2021-05-01 14:00:00 +00:00');


-- -----------------------------------------------------------------------------------------------------------------
INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD001', 1, 'PROD_001', 'testing product 001', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD002', 1, 'PROD_002', 'testing product 002', 'test', 20.10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD003', 1, 'PROD_003', 'testing product 003', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD004', 1, 'PROD_004', 'testing product 004', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD005', 1, 'PROD_005', 'testing product 005', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD006', 1, 'PROD_006', 'testing product 006', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD007', 1, 'PROD_007', 'testing product 007', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD008', 1, 'PROD_009', 'testing product 008', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD009', 1, 'PROD_009', 'testing product 009', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD010', 1, 'PROD_010', 'testing product 010', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD011', 1, 'PROD_011', 'testing product 011', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD012', 1, 'PROD_012', 'testing product 012', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD013', 1, 'PROD_013', 'testing product 013', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD014', 1, 'PROD_014', 'testing product 014', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.inventorymaster
(productid, accountid, "name", description, category, price, relatedimages, addedon)
VALUES('PROD015', 1, 'PROD_015', 'testing product 015', 'test', 10, null, '2021-05-01 14:00:00 +00:00');

-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('START', 'Session Start', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('NAV', 'Page Navigation', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('PRODVIEW', 'Product View', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('ADDCART', 'Add to Cart', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('REMCART', 'Remove from Cart', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('ADDLIST', 'Add to wishlist', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('REMLIST', 'Remove from wishlist', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('BUY', 'Buy Product', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('REVIEW', 'Review Product', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('RATE', 'Rate Product', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES('END', 'Session end', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.eventmaster
(eventid, description, createdon, modifiedon)
VALUES ('DISCOVER', 'Discover', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('NEWSLETTER_SUBSCRIBE', 'Subscribe to newsletter', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('SEARCH', 'Search activity', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('MENU', 'Menu Event', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('BACK_NAV', 'Back Navigation', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('SAVE_FOR_LATER', 'Save for later', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('PROD_RETURN', 'Product Return request', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('VISIT_BLOG_POST', 'Blog Post Visits', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('VISIT_SOCIAL_MEDIA', 'Social Media link', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00'),
('DELETE', 'Delete saved product', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

-- -----------------------------------------------------------------------------------------------------------------

-- Comment from Prithviraj
-- Remove all DMLs for Metrics Master. We will enter this data when we are building the Dashboard pages

-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.userdemographics
(userid, accountid, age, gender, education, income, familysize, rece)
VALUES('USER001', 1, 29, 'male','POST GRADUATE', 1000, 5, 'test');

INSERT INTO humainedev.userdemographics
(userid, accountid, age, gender, education, income, familysize, rece)
VALUES('USER002', 1, 29, 'female','POST GRADUATE', 1000, 5, 'test');

INSERT INTO humainedev.userdemographics
(userid, accountid, age, gender, education, income, familysize, rece)
VALUES('USER003', 1, 29, 'female','POST GRADUATE', 1000, 5, 'test');

INSERT INTO humainedev.userdemographics
(userid, accountid, age, gender, education, income, familysize, rece)
VALUES('USER004', 1, 29, 'female','POST GRADUATE', 1000, 5, 'test');

INSERT INTO humainedev.userdemographics
(userid, accountid, age, gender, education, income, familysize, rece)
VALUES('USER005', 1, 29, 'female','POST GRADUATE', 1000, 5, 'test');
-- -----------------------------------------------------------------------------------------------------------------
-- Comment from Prithviraj
-- Remove all DMLs for Dashboard Metrics. We will enter this data when we are building the Dashboard pages

-- -----------------------------------------------------------------------------------------------------------------
INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER001', 1, '2021-05-01 14:00:00 +00:00', 'SES001', 'PROD001', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER002', 1, '2021-05-01 14:00:00 +00:00', 'SES002', 'PROD002', 20, 20);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER003', 1, '2021-05-01 14:00:00 +00:00', 'SES003', 'PROD003', 10, 30);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER004', 1, '2021-05-01 14:00:00 +00:00', 'SES004', 'PROD004', 10, 40);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER005', 1, '2021-05-01 14:00:00 +00:00', 'SES005', 'PROD005', 10, 50);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER002', 1, '2021-05-01 14:00:00 +00:00', 'SES006', 'PROD001', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER003', 1, '2021-05-01 14:00:00 +00:00', 'SES007', 'PROD002', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER004', 1, '2021-05-01 14:00:00 +00:00', 'SES008', 'PROD003', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER005', 1, '2021-05-01 14:00:00 +00:00', 'SES009', 'PROD004', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER002', 1, '2021-05-01 14:00:00 +00:00', 'SES010', 'PROD005', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER003', 1, '2021-05-01 14:00:00 +00:00', 'SES011', 'PROD001', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER004', 1, '2021-05-01 14:00:00 +00:00', 'SES012', 'PROD003', 10, 10);

INSERT INTO humainedev.saledata
(userid, accountid, saleon, sessionid, productid, saleamount, productquantity)
VALUES('USER005', 1, '2021-05-01 14:00:00 +00:00', 'SES013', 'PROD002', 10, 10);

-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid,pageurl, "timestamp")
VALUES('USER001', 1, 'START', 'SES001', 'PROD001', 'https://unsplash.com/photos/LxVxPA1LOVM', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'NAV', 'SES002', 'PROD002', 'https://unsplash.com/photos/2cFZ_FB08UM', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'PRODVIEW', 'SES003', 'PROD003', 'https://unsplash.com/photos/KStSiM1UvPw', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'ADDCART', 'SES004', 'PROD004', 'https://unsplash.com/photos/ElfJDs4LAQk', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'REMCART', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'ADDLIST', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid,pageurl, "timestamp")
VALUES('USER001', 1, 'REMLIST', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'BUY', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'REVIEW', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'RATE', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');


INSERT INTO humainedev.userevent
(userid, accountid, eventid, sessionid, productid, pageurl, "timestamp")
VALUES('USER001', 1, 'END', 'SES005', 'PROD005', 'https://unsplash.com/photos/KsLPTsYaqIQ', '2021-05-01 14:00:00 +00:00');
-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.usersession
(sessionid, userid, accountid, deviceid, starttime, endtime, city, state, country, lat, long)
VALUES('SES001', 'USER001', 1, '001', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 'New York', 'New York', 'USA', 0, 0 );

INSERT INTO humainedev.usersession
(sessionid, userid, accountid, deviceid, starttime, endtime, city, state, country, lat, long)
VALUES('SES002', 'USER002', 1, '002', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 'Los Angeles', 'California', 'USA', 0 ,0);

INSERT INTO humainedev.usersession
(sessionid, userid, accountid, deviceid, starttime, endtime, city, state, country, lat, long)
VALUES('SES003', 'USER003', 1, '003', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 'Chicago', 'illinois', 'USA', 0 , 0);

INSERT INTO humainedev.usersession
(sessionid, userid, accountid, deviceid, starttime, endtime, city, state, country, lat, long)
VALUES('SES004', 'USER004', 1, '004', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 'Phoenix', 'Arizona', 'USA' , 0 , 0);

INSERT INTO humainedev.usersession
(sessionid, userid, accountid, deviceid, starttime, endtime, city, state, country, lat, long)
VALUES('SES005', 'USER005', 1, '005', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00', 'Philadelphia','Pennsylvania', 'USA', 0 , 0 );

-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO humainedev.pagemaster
(accountid, pageurl, pagename, createdon, modifiedon)
VALUES(1, 'https://unsplash.com/photos/LxVxPA1LOVM', 'google', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.pagemaster
(accountid, pageurl, pagename, createdon, modifiedon)
VALUES(1, 'https://unsplash.com/photos/2cFZ_FB08UM', 'google', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.pagemaster
(accountid, pageurl, pagename, createdon, modifiedon)
VALUES(1, 'https://unsplash.com/photos/KStSiM1UvPw', 'google', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.pagemaster
(accountid, pageurl, pagename, createdon, modifiedon)
VALUES(1, 'https://unsplash.com/photos/ElfJDs4LAQk', 'google', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

INSERT INTO humainedev.pagemaster
(accountid, pageurl, pagename, createdon, modifiedon)
VALUES(1, 'https://unsplash.com/photos/KsLPTsYaqIQ', 'google', '2021-05-01 14:00:00 +00:00', '2021-05-01 14:00:00 +00:00');

-- -----------------------------------------------------------------------------------------------------------------

-- Removed Archived table DML as per Prithviraj Comment.


--------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------
insert into humainedev.big_five_master (id, value, createdon)
values (1, 'Agreeableness', '2021-07-09 18:00:00'),
    (2, 'Conscientiousness', '2021-07-09 18:00:00'),
    (3, 'Extraversion', '2021-07-09 18:00:00'),
    (4, 'Neuroticism', '2021-07-09 18:00:00'),
    (5, 'Openness To Experience', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.age_group_master (id, value, createdon)
values (1, 'Under 17', '2021-07-09 18:00:00'),
    (2, '18 - 24', '2021-07-09 18:00:00'),
    (3, '25 - 34', '2021-07-09 18:00:00'),
    (4, '35 - 44', '2021-07-09 18:00:00'),
    (5, '45 - 54', '2021-07-09 18:00:00'),
    (6, '55 - 64', '2021-07-09 18:00:00'),
    (7, '65+', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.gender_master (id, createdon, value, is_other)
VALUES (1, '2021-07-09 18:00:00', 'N/A', false),
(2, '2021-07-09 18:00:00', 'Male', false),
(3, '2021-07-09 18:00:00', 'Female', false),
(4, '2021-07-09 18:00:00', 'Androgyne', true),
(5, '2021-07-09 18:00:00', 'Androgynous', true),
(6, '2021-07-09 18:00:00', 'Bigender', true),
(7, '2021-07-09 18:00:00', 'Cisgender Female', true),
(8, '2021-07-09 18:00:00', 'Cisgender Male', true),
(9, '2021-07-09 18:00:00', 'Female to Male', true),
(10, '2021-07-09 18:00:00', 'FTM', true),
(11, '2021-07-09 18:00:00', 'Gender Fluid', true),
(12, '2021-07-09 18:00:00', 'Gender Nonconforming', true),
(13, '2021-07-09 18:00:00', 'Gender Questioning', true),
(14, '2021-07-09 18:00:00', 'Gender Variant', true),
(15, '2021-07-09 18:00:00', 'Genderqueer', true),
(16, '2021-07-09 18:00:00', 'Intersex', true),
(17, '2021-07-09 18:00:00', 'Male to Female',true),
(18, '2021-07-09 18:00:00', 'MTF', true),
(19, '2021-07-09 18:00:00', 'Neither', true),
(20, '2021-07-09 18:00:00', 'Neutrois', true),
(21, '2021-07-09 18:00:00', 'Non-binary', true),
(22, '2021-07-09 18:00:00', 'Other', true),
(23, '2021-07-09 18:00:00', 'Pangender', true),
(24, '2021-07-09 18:00:00', 'Trans', true),
(25, '2021-07-09 18:00:00', 'Trans*', true),
(26, '2021-07-09 18:00:00', 'Trans Female', true),
(27, '2021-07-09 18:00:00', 'Trans* Female', true),
(28, '2021-07-09 18:00:00', 'Trans Male ', true),
(29, '2021-07-09 18:00:00', 'Trans* Male', true),
(30, '2021-07-09 18:00:00', 'Trans Man', true),
(31, '2021-07-09 18:00:00', 'Trans* Man', true),
(32, '2021-07-09 18:00:00', 'Trans Person', true),
(33, '2021-07-09 18:00:00', 'Trans* Person', true),
(34, '2021-07-09 18:00:00', 'Trans Woman', true),
(35, '2021-07-09 18:00:00', 'Trans* Woman', true),
(36, '2021-07-09 18:00:00', 'Transfeminine', true),
(37, '2021-07-09 18:00:00', 'Transgender', true),
(38, '2021-07-09 18:00:00', 'Transgender Female', true),
(39, '2021-07-09 18:00:00', 'Transgender Male', true),
(40, '2021-07-09 18:00:00', 'Transgender Man', true),
(41, '2021-07-09 18:00:00', 'Transgender Person', true),
(42, '2021-07-09 18:00:00', 'Transgender Woman', true),
(43, '2021-07-09 18:00:00', 'Transmasculine', true),
(44, '2021-07-09 18:00:00', 'Transsexual', true),
(45, '2021-07-09 18:00:00', 'Transsexual Female', true),
(46, '2021-07-09 18:00:00', 'Transsexual Male', true),
(47, '2021-07-09 18:00:00', 'Transsexual Man', true),
(48, '2021-07-09 18:00:00', 'Transsexual Person', true),
(49, '2021-07-09 18:00:00', 'Transsexual Woman', true),
(50, '2021-07-09 18:00:00', 'Two-Spirit', true),
(51, '2021-07-09 18:00:00', 'Not Specify', true);
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.family_size_master (id, value, createdon)
values (1, '0 Sib', '2021-07-09 18:00:00'),
    (2, '1 Sib', '2021-07-09 18:00:00'),
    (3, '2 Sib', '2021-07-09 18:00:00'),
    (4, '3 Sib', '2021-07-09 18:00:00'),
    (5, '4 Sib', '2021-07-09 18:00:00'),
    (6, '5 Sib', '2021-07-09 18:00:00'),
    (7, '6+ Sib', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.ethnicity_master (id, value, createdon)
values (1, 'N/A', '2021-07-09 18:00:00'),
(2, 'Afghanistan', '2021-07-09 18:00:00'),
(3, 'Albania', '2021-07-09 18:00:00'),
(4, 'Algeria', '2021-07-09 18:00:00'),
(5, 'Andorra', '2021-07-09 18:00:00'),
(6, 'Angola', '2021-07-09 18:00:00'),
(7, 'Antigua and Barbuda', '2021-07-09 18:00:00'),
(8, 'Argentina', '2021-07-09 18:00:00'),
(9, 'Armenia', '2021-07-09 18:00:00'),
(10, 'Australia', '2021-07-09 18:00:00'),
(11, 'Austria', '2021-07-09 18:00:00'),
(12, 'Azerbaijan', '2021-07-09 18:00:00'),
(13, 'The Bahamas', '2021-07-09 18:00:00'),
(14, 'Bahrain', '2021-07-09 18:00:00'),
(15, 'Bangladesh', '2021-07-09 18:00:00'),
(16, 'Barbados', '2021-07-09 18:00:00'),
(17, 'Belarus', '2021-07-09 18:00:00'),
(18, 'Belgium', '2021-07-09 18:00:00'),
(19, 'Belize', '2021-07-09 18:00:00'),
(20, 'Benin', '2021-07-09 18:00:00'),
(21, 'Bhutan', '2021-07-09 18:00:00'),
(22, 'Bolivia', '2021-07-09 18:00:00'),
(23,'Bosnia and Herzegovina','2021-07-09 18:00:00'),
(24, 'Botswana', '2021-07-09 18:00:00'),
(25, 'Brazil', '2021-07-09 18:00:00'),
(26, 'Brunei', '2021-07-09 18:00:00'),
(27, 'Bulgaria', '2021-07-09 18:00:00'),
(28, 'Burkina Faso', '2021-07-09 18:00:00'),
(29, 'Burundi', '2021-07-09 18:00:00'),
(30, 'Cabo Verde', '2021-07-09 18:00:00'),
(31, 'Cambodia', '2021-07-09 18:00:00'),
(32, 'Cameroon', '2021-07-09 18:00:00'),
(33, 'Canada', '2021-07-09 18:00:00'),
(34,'Central African Republic','2021-07-09 18:00:00'),
(35, 'Chad', '2021-07-09 18:00:00'),
(36, 'Chile', '2021-07-09 18:00:00'),
(37, 'China', '2021-07-09 18:00:00'),
(38, 'Colombia', '2021-07-09 18:00:00'),
(39, 'Comoros', '2021-07-09 18:00:00'),
(40, 'Congo, Democratic  Republic of the', '2021-07-09 18:00:00'),
(41, 'Congo, Republic of the', '2021-07-09 18:00:00'),
(42, 'Costa Rica', '2021-07-09 18:00:00'),
(43, 'Côte d’Ivoire', '2021-07-09 18:00:00'),
(44, 'Croatia', '2021-07-09 18:00:00'),
(45, 'Cuba', '2021-07-09 18:00:00'),
(46, 'Cyprus', '2021-07-09 18:00:00'),
(47, 'Czech Republic', '2021-07-09 18:00:00'),
(48, 'Denmark', '2021-07-09 18:00:00'),
(49, 'Djibouti', '2021-07-09 18:00:00'),
(50, 'Dominica', '2021-07-09 18:00:00'),
(51, 'Dominican Republic', '2021-07-09 18:00:00'),
(52, 'East Timor', '2021-07-09 18:00:00'),
(53, 'Ecuador', '2021-07-09 18:00:00'),
(54, 'Egypt', '2021-07-09 18:00:00'),
(55, 'El Salvador', '2021-07-09 18:00:00'),
(56, 'Equatorial Guinea', '2021-07-09 18:00:00'),
(57, 'Eritrea', '2021-07-09 18:00:00'),
(58, 'Estonia', '2021-07-09 18:00:00'),
(59, 'Eswatini', '2021-07-09 18:00:00'),
(60, 'Ethiopia', '2021-07-09 18:00:00'),
(61, 'Fiji', '2021-07-09 18:00:00'),
(62, 'Finland', '2021-07-09 18:00:00'),
(63, 'France', '2021-07-09 18:00:00'),
(64, 'Gabon', '2021-07-09 18:00:00'),
(65, 'The Gambia', '2021-07-09 18:00:00'),
(66, 'Georgia', '2021-07-09 18:00:00'),
(67, 'Germany', '2021-07-09 18:00:00'),
(68, 'Ghana', '2021-07-09 18:00:00'),
(69, 'Greece', '2021-07-09 18:00:00'),
(70, 'Grenada', '2021-07-09 18:00:00'),
(71, 'Guatemala', '2021-07-09 18:00:00'),
(72, 'Guinea', '2021-07-09 18:00:00'),
(73, 'Guinea-Bissau', '2021-07-09 18:00:00'),
(74, 'Guyana', '2021-07-09 18:00:00'),
(75, 'Haiti', '2021-07-09 18:00:00'),
(76, 'Honduras', '2021-07-09 18:00:00'),
(77, 'Hungary', '2021-07-09 18:00:00'),
(78, 'Iceland', '2021-07-09 18:00:00'),
(79, 'India', '2021-07-09 18:00:00'),
(80, 'Indonesia', '2021-07-09 18:00:00'),
(81, 'Iran', '2021-07-09 18:00:00'),
(82, 'Iraq', '2021-07-09 18:00:00'),
(83, 'Ireland', '2021-07-09 18:00:00'),
(84, 'Israel', '2021-07-09 18:00:00'),
(85, 'Italy', '2021-07-09 18:00:00'),
(86, 'Jamaica', '2021-07-09 18:00:00'),
(87, 'Japan', '2021-07-09 18:00:00'),
(88, 'Jordan', '2021-07-09 18:00:00'),
(89, 'Kazakhstan', '2021-07-09 18:00:00'),
(90, 'Kenya', '2021-07-09 18:00:00'),
(91, 'Kiribati', '2021-07-09 18:00:00'),
(92, 'Korea, North', '2021-07-09 18:00:00'),
(93, 'Korea, South', '2021-07-09 18:00:00'),
(94, 'Kosovo', '2021-07-09 18:00:00'),
(95, 'Kuwait', '2021-07-09 18:00:00'),
(96, 'Kyrgyzstan', '2021-07-09 18:00:00'),
(97, 'Laos', '2021-07-09 18:00:00'),
(98, 'Latvia', '2021-07-09 18:00:00'),
(99, 'Lebanon', '2021-07-09 18:00:00'),
(100, 'Lesotho', '2021-07-09 18:00:00'),
(101, 'Liberia', '2021-07-09 18:00:00'),
(102, 'Libya', '2021-07-09 18:00:00'),
(103, 'Liechtenstein', '2021-07-09 18:00:00'),
(104, 'Lithuania', '2021-07-09 18:00:00'),
(105, 'Luxembourg', '2021-07-09 18:00:00'),
(106, 'Madagascar', '2021-07-09 18:00:00'),
(107, 'Malawi', '2021-07-09 18:00:00'),
(108, 'Malaysia', '2021-07-09 18:00:00'),
(109, 'Maldives', '2021-07-09 18:00:00'),
(110, 'Mali', '2021-07-09 18:00:00'),
(111, 'Malta', '2021-07-09 18:00:00'),
(112, 'Marshall Islands', '2021-07-09 18:00:00'),
(113, 'Mauritania', '2021-07-09 18:00:00'),
(114, 'Mauritius', '2021-07-09 18:00:00'),
(115, 'Mexico', '2021-07-09 18:00:00'),
(116, 'Micronesia,  Federated States of', '2021-07-09 18:00:00'),
(117, 'Moldova', '2021-07-09 18:00:00'),
(118, 'Monaco', '2021-07-09 18:00:00'),
(119, 'Mongolia', '2021-07-09 18:00:00'),
(120, 'Montenegro', '2021-07-09 18:00:00'),
(121, 'Morocco', '2021-07-09 18:00:00'),
(122, 'Mozambique', '2021-07-09 18:00:00'),
(123, 'Myanmar', '2021-07-09 18:00:00'),
(124, 'Namibia', '2021-07-09 18:00:00'),
(125, 'Nauru', '2021-07-09 18:00:00'),
(126, 'Nepal', '2021-07-09 18:00:00'),
(127, 'Netherlands', '2021-07-09 18:00:00'),
(128, 'New Zealand', '2021-07-09 18:00:00'),
(129, 'Nicaragua', '2021-07-09 18:00:00'),
(130, 'Niger', '2021-07-09 18:00:00'),
(131, 'Nigeria', '2021-07-09 18:00:00'),
(132, 'North Macedonia', '2021-07-09 18:00:00'),
(133, 'Norway', '2021-07-09 18:00:00'),
(134, 'Oman', '2021-07-09 18:00:00'),
(135, 'Pakistan', '2021-07-09 18:00:00'),
(136, 'Palau', '2021-07-09 18:00:00'),
(137, 'Panama', '2021-07-09 18:00:00'),
(138, 'Papua New Guinea', '2021-07-09 18:00:00'),
(139, 'Paraguay', '2021-07-09 18:00:00'),
(140, 'Peru', '2021-07-09 18:00:00'),
(141, 'Philippines', '2021-07-09 18:00:00'),
(142, 'Poland', '2021-07-09 18:00:00'),
(143, 'Portugal', '2021-07-09 18:00:00'),
(144, 'Qatar', '2021-07-09 18:00:00'),
(145, 'Romania', '2021-07-09 18:00:00'),
(146, 'Russia', '2021-07-09 18:00:00'),
(147, 'Rwanda', '2021-07-09 18:00:00'),
(148, 'Saint Kitts and Nevis', '2021-07-09 18:00:00'),
(149, 'Saint Lucia', '2021-07-09 18:00:00'),
(150, 'Saint Vincent and  the Grenadines', '2021-07-09 18:00:00'),
(151, 'Samoa', '2021-07-09 18:00:00'),
(152, 'San Marino', '2021-07-09 18:00:00'),
(153, 'Sao Tome and Principe', '2021-07-09 18:00:00'),
(154, 'Saudi Arabia', '2021-07-09 18:00:00'),
(155, 'Senegal', '2021-07-09 18:00:00'),
(156, 'Serbia', '2021-07-09 18:00:00'),
(157, 'Seychelles', '2021-07-09 18:00:00'),
(158, 'Sierra Leone', '2021-07-09 18:00:00'),
(159, 'Singapore', '2021-07-09 18:00:00'),
(160, 'Slovakia', '2021-07-09 18:00:00'),
(161, 'Slovenia', '2021-07-09 18:00:00'),
(162, 'Solomon Islands', '2021-07-09 18:00:00'),
(163, 'Somalia', '2021-07-09 18:00:00'),
(164, 'South Africa', '2021-07-09 18:00:00'),
(165, 'Spain', '2021-07-09 18:00:00'),
(166, 'Sri Lanka', '2021-07-09 18:00:00'),
(167, 'Sudan', '2021-07-09 18:00:00'),
(168, 'Sudan, South', '2021-07-09 18:00:00'),
(169, 'Suriname', '2021-07-09 18:00:00'),
(170, 'Sweden', '2021-07-09 18:00:00'),
(171, 'Switzerland', '2021-07-09 18:00:00'),
(172, 'Syria', '2021-07-09 18:00:00'),
(173, 'Taiwan', '2021-07-09 18:00:00'),
(174, 'Tajikistan', '2021-07-09 18:00:00'),
(175, 'Tanzania', '2021-07-09 18:00:00'),
(176, 'Thailand', '2021-07-09 18:00:00'),
(177, 'Togo', '2021-07-09 18:00:00'),
(178, 'Tonga', '2021-07-09 18:00:00'),
(179, 'Trinidad and Tobago', '2021-07-09 18:00:00'),
(180, 'Tunisia', '2021-07-09 18:00:00'),
(181, 'Turkey', '2021-07-09 18:00:00'),
(182, 'Turkmenistan', '2021-07-09 18:00:00'),
(183, 'Tuvalu', '2021-07-09 18:00:00'),
(184, 'Uganda', '2021-07-09 18:00:00'),
(185, 'Ukraine', '2021-07-09 18:00:00'),
(186, 'United Arab Emirates', '2021-07-09 18:00:00'),
(187, 'United Kingdom', '2021-07-09 18:00:00'),
(188, 'United States', '2021-07-09 18:00:00'),
(189, 'Uruguay', '2021-07-09 18:00:00'),
(190, 'Uzbekistan', '2021-07-09 18:00:00'),
(191, 'Vanuatu', '2021-07-09 18:00:00'),
(192, 'Vatican City', '2021-07-09 18:00:00'),
(193, 'Venezuela', '2021-07-09 18:00:00'),
(194, 'Vietnam', '2021-07-09 18:00:00'),
(195, 'Yemen', '2021-07-09 18:00:00'),
(196, 'Zambia', '2021-07-09 18:00:00'),
(197, 'Zimbabwe', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.buying_master (id, value, createdon)
values (1, 'Compulsive', '2021-07-09 18:00:00'),
    (2, 'Impulsive', '2021-07-09 18:00:00'),
    (3, 'Hedonic', '2021-07-09 18:00:00'),
    (4, 'Utilitarian', '2021-07-09 18:00:00'),
    (5, 'N/A', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.persuasive_master (id, value, createdon)
VALUES(1, 'Authority', '2021-07-09 18:00:00'),
    (2, 'Commitment', '2021-07-09 18:00:00'),
    (3, 'Consensus', '2021-07-09 18:00:00'),
    (4, 'Liking', '2021-07-09 18:00:00'),
    (5, 'Reciprocity', '2021-07-09 18:00:00'),
    (6, 'Scarcity', '2021-07-09 18:00:00'),
    (7, 'N/A', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.values_master (id, value, createdon)
VALUES(1, 'Achievement', '2021-07-09 18:00:00'),
    (2, 'Benevolence', '2021-07-09 18:00:00'),
    (3, 'Conformity', '2021-07-09 18:00:00'),
    (4, 'Hedonism', '2021-07-09 18:00:00'),
    (5, 'Power', '2021-07-09 18:00:00'),
    (6, 'Stimulation', '2021-07-09 18:00:00'),
    (7, 'Self-Direction', '2021-07-09 18:00:00'),
    (8, 'Security', '2021-07-09 18:00:00'),
    (9, 'Tradition', '2021-07-09 18:00:00'),
    (10, 'Universalism', '2021-07-09 18:00:00'),
    (11, 'N/A', '2021-07-09 18:00:00');
--------------------------------------------------------------------------------------------

INSERT INTO humainedev.education_master
(id, value, createdon)
values
(1, 'NA', '2021-07-09 18:00:00'),
(2, 'Less than high school', '2021-07-09 18:00:00'),
(3, 'High school', '2021-07-09 18:00:00'),
(4, 'University degree', '2021-07-09 18:00:00'),
(5, 'Graduate degree', '2021-07-09 18:00:00');

--------------------------------------------------------------------------------------------

INSERT INTO humainedev.big_five_buying (big_five_id, buying_id)
values (2, 1),
(2, 2),
(2, 4),
(3, 4),
(4, 2),
(4, 4),
(5, 4);
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.big_five_persuasive (big_five_id, persuasive_id)
VALUES(1, 1),
(1, 2),
(1, 4),
(2, 2),
(2, 4),
(2, 5),
(2, 6),
(4, 3),
(4, 6),
(5, 1),
(5, 3),
(5, 4);
--------------------------------------------------------------------------------------------
INSERT INTO humainedev.big_five_values (big_five_id, values_id)
VALUES(1, 1),
(1, 7),
(1, 2),
(1, 3),
(1, 8),
(2, 5),
(2, 1),
(2, 4),
(2, 6),
(2, 7),
(2, 2),
(2, 9),
(3, 5),
(3, 1),
(3, 10),
(3, 2),
(3, 9),
(3, 3),
(4, 6),
(5, 5),
(5, 4),
(5, 6),
(5, 7),
(5, 10),
(5, 2),
(5, 9),
(5, 3);

-----------------------------------------------------------------------------------------

insert
	into
	humainedev.user_group_master (user_group_name,
	icon,
	state,
	is_external_factor,
	accountid,
	age_group,
	big_five,
	buying,
	education,
	ethnicity,
	family_size,
	gender,
	persuasive,
	"values",
	createdon, 
	modifiedon)
VALUES

('Test User Group 2', '2.png', 'California', false, 2, 3, 2, 1, 2, 18, 3, 5, 4, 5, '2021-07-09 18:00:00', '2021-07-09 18:00:00'),
('Test User Group 3', '3.png', 'Texas', true, 3, 5, 5, 4, 3, 26, 7, 9, 3, 10, '2021-07-09 18:00:00', '2021-07-09 18:00:00'),
('Test User Group 4', '4.png', 'Hawaii', false, 4, 7, 4, 2, 5, 35, 5,12, 3, 6, '2021-07-09 18:00:00', '2021-07-09 18:00:00'),
('Test User Group 1', '1.png', 'Floida', false, 1, 1, 4, 4, 1, 1, 1, 1, 6, 6, '2021-07-09 18:00:00', '2021-07-09 18:00:00');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.big_five_goals
(big_five_id, goals)
values
(5, 'Duis porta, ligula rhoncus euismod pretium.'),
(5, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(5, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');



INSERT INTO humainedev.big_five_frustrations
(big_five_id, frustrations)
values
(5, 'Duis porta, ligula rhoncus euismod pretium.'),
(5, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(5, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean'),
(5, 'Duis porta, ligula rhoncus euismod pretium.'),
(5, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');


INSERT INTO humainedev.big_five_personalities (big_five_id, personalities) VALUES
	 (5, 'People who are “open to experience” tend to be intellectually curious, creative and imaginative. Personality researchers have shown that such people literally see the world differently.<a href="https://www.scientificamerican.com/article/openness-to-experience-the-gates-of-the-mind/" target="_blank">LINK</a>'),
	 (5, 'Because their perception allows more information to flow into their visual system, more open people tend to see things that others block out. Researchers also found that open people can feel very complex emotional states because seemingly incompatible feelings break through into their consciousness simultaneously.'),
	 (5, 'They are interested in art and are voracious consumers of music, books and other fruits of culture. They also tend to be politically liberal. According to personality theorists, openness reflects a greater “breadth, depth, and permeability of consciousness” and propensity to “cognitively explore” both abstract information (ideas and arguments) and sensory information (sights and sounds). In other words, open people engage with the various percepts, patterns and perspectives that clamor for space in our mind—information is like catnip for their brain.');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.big_five_goals
(big_five_id, goals)
values
(4, 'Avoid caos'),
(4, 'Avoid stress'),
(4, 'Help others'),
(4, 'Provide for others');


INSERT INTO humainedev.big_five_frustrations
(big_five_id, frustrations)
values
(4, 'Failure'),
(4, 'Negative feedback'),
(4, 'Not finding what they want'),
(4, 'Load time'),
(4, 'Self-criticism');

INSERT INTO humainedev.big_five_personalities (big_five_id, personalities) VALUES
	 (4, 'Individuals high on neuroticism tend to be anxious, depressed, angry, and insecure. Bilsky and Schwartz (1994, p. 171) reasoned, “The depression characteristic of people high on neuroticism might result from failure to attain the desired level of any one of the ten values.”'),
	 (4, 'Neuroticism is the trait disposition to experience negative affects, including anger, anxiety, self‐consciousness, irritability, emotional instability, and depression1. Persons with elevated levels of neuroticism respond poorly to environmental stress, interpret ordinary situations as threatening, and can experience minor frustrations as hopelessly overwhelming. Neuroticism is one of the more well established and empirically validated personality trait domains, with a substantial body of research to support its heritability, childhood antecedents, temporal stability across the life span, and universal presence (<a href="https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5428182/" target="_blank">https://www.ncbi.nlm.nih.gov/pmc/articles/PMC5428182/</a>)');
	
-----------------------------------------------------------------------------------------
	

INSERT INTO humainedev.big_five_goals
(big_five_id, goals)
values
(3, 'Duis porta, ligula rhoncus euismod pretium.'),
(3, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(3, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');



INSERT INTO humainedev.big_five_frustrations
(big_five_id, frustrations)
values
(3, 'Duis porta, ligula rhoncus euismod pretium.'),
(3, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(3, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean'),
(3, 'Duis porta, ligula rhoncus euismod pretium.'),
(3, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');


INSERT INTO humainedev.big_five_personalities (big_five_id, personalities) VALUES
	 (3, 'Phasellus dignissim, tellus in pellentesque mollis, mauris orci dignissim nisl, id gravida nunc enim quis nibh. Maecenas convallis eros a ante dignissim, vitae elementum metus facilisis. Cras in maximus sem. Praesent libero augue, ornare eget quam sed, volutpat suscipit arcu.');

-----------------------------------------------------------------------------------------

	INSERT INTO humainedev.big_five_goals
(big_five_id, goals)
values
(2, 'Duis porta, ligula rhoncus euismod pretium.'),
(2, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(2, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');



INSERT INTO humainedev.big_five_frustrations
(big_five_id, frustrations)
values
(2, 'Duis porta, ligula rhoncus euismod pretium.'),
(2, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(2, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean'),
(2, 'Duis porta, ligula rhoncus euismod pretium.'),
(2, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');


INSERT INTO humainedev.big_five_personalities (big_five_id, personalities) VALUES
	 (2, 'Phasellus dignissim, tellus in pellentesque mollis, mauris orci dignissim nisl, id gravida nunc enim quis nibh. Maecenas convallis eros a ante dignissim, vitae elementum metus facilisis. Cras in maximus sem. Praesent libero augue, ornare eget quam sed, volutpat suscipit arcu.');

	
-----------------------------------------------------------------------------------------
	
INSERT INTO humainedev.big_five_goals
(big_five_id, goals)
values
(1, 'Duis porta, ligula rhoncus euismod pretium.'),
(1, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(1, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');



INSERT INTO humainedev.big_five_frustrations
(big_five_id, frustrations)
values
(1, 'Duis porta, ligula rhoncus euismod pretium.'),
(1, 'Pellentesque tincidunt tristique neque, eget venenatis enim gravida quis. Fusce at egestas libero.'),
(1, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean'),
(1, 'Duis porta, ligula rhoncus euismod pretium.'),
(1, 'Duis mauris augue, efficitur eu arcu sit amet, posuere dignissim neque. Aenean');


INSERT INTO humainedev.big_five_personalities (big_five_id, personalities) VALUES
	 (1, 'Phasellus dignissim, tellus in pellentesque mollis, mauris orci dignissim nisl, id gravida nunc enim quis nibh. Maecenas convallis eros a ante dignissim, vitae elementum metus facilisis. Cras in maximus sem. Praesent libero augue, ornare eget quam sed, volutpat suscipit arcu.');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.journey_element_master
(id ,"name")
values (1, 'First Interest'),
(2, 'Decision'),
(3, 'Purchase: Add to Cart'),
(4, 'Purchase: Buy'),
(5, 'Purchase: Rate Product');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.journey_element_values
(id, value, element_id,event_id)
values
(1, 'Blog Post', 1,'VISIT_BLOG_POST'),
(2, 'Products', 1,'PRODVIEW'),
(3, 'Social Media Link', 1,'VISIT_SOCIAL_MEDIA'),
(4, 'Subscribe to Newsletter', 1,'NEWSLETTER_SUBSCRIBE'),
(5, 'Menu Item', 2,'MENU'),
(6, 'Direct Search Product', 2, 'SEARCH'),
(7, 'Add to Cart', 3, 'ADDCART'),
(8, 'Remove from Cart', 3, 'REMCART'),
(9, 'Add to Favorites', 3, 'ADDLIST'),
(10, 'Save for Later', 4, 'SAVE_FOR_LATER'),
(11, 'Buy', 4, 'BUY'),
(12, 'Remove', 4, 'REMCART'),
(13, 'Comment on Product', 5, 'REVIEW'),
(14, 'Rate Product', 5, 'RATE');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.test_journey_master
(group_id, first_interest, decison, purchase_add_cart, purchase_buy, purchase_ownership, journey_steps, created_on)
values
(1, 'Blog Post', 'Women', 'Add To Cart', 'Buy', 'Rate Product', 3, '2021-02-06 10:19:39'),
(2, 'Products', 'Collections', 'Add to Favorites', 'Remove', 'Comment on Product', 3, '2021-02-06 10:19:39'),
(3, 'Social Media Link', 'Men', 'Add To Cart', 'Save for Later', 'Comment on Product', 3, '2021-02-06 10:19:39'),
(4, 'Products', 'New Arrivals', 'Remove From Cart', 'Remove', 'Rate Product', 3, '2021-02-06 10:19:39');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.journey_element_event_mapping
(journey_element_value_id, event_id)
VALUES (1, 'VISIT_BLOG_POST'),
(2, 'PRODVIEW'),
(3, 'VISIT_SOCIAL_MEDIA'),
(4, 'NEWSLETTER_SUBSCRIBE'),
(5, 'MENU'),
(6, 'SEARCH'),
(7, 'ADDCART'),
(8, 'REMCART'),
(9, 'ADDLIST'),
(10, 'SAVE_FOR_LATER'),
(11, 'BUY'),
(12, 'REMCART'),
(13, 'REVIEW'),
(14, 'RATE');

-----------------------------------------------------------------------------------------

INSERT INTO humainedev.persona_details_master VALUES (1, 5, 4, 1, 5);
INSERT INTO humainedev.persona_details_master VALUES (2, 5, 4, 1, 4);
INSERT INTO humainedev.persona_details_master VALUES (3, 5, 4, 1, 6);
INSERT INTO humainedev.persona_details_master VALUES (4, 5, 4, 1, 7);
INSERT INTO humainedev.persona_details_master VALUES (5, 5, 4, 1, 10);
INSERT INTO humainedev.persona_details_master VALUES (6, 5, 4, 1, 2);
INSERT INTO humainedev.persona_details_master VALUES (7, 5, 4, 1, 9);
INSERT INTO humainedev.persona_details_master VALUES (8, 5, 4, 1, 3);
INSERT INTO humainedev.persona_details_master VALUES (9, 5, 4, 3, 5);
INSERT INTO humainedev.persona_details_master VALUES (10, 5, 4, 3, 4);
INSERT INTO humainedev.persona_details_master VALUES (11, 5, 4, 3, 6);
INSERT INTO humainedev.persona_details_master VALUES (12, 5, 4, 3, 7);
INSERT INTO humainedev.persona_details_master VALUES (13, 5, 4, 3, 10);
INSERT INTO humainedev.persona_details_master VALUES (14, 5, 4, 3, 2);
INSERT INTO humainedev.persona_details_master VALUES (15, 5, 4, 3, 9);
INSERT INTO humainedev.persona_details_master VALUES (16, 5, 4, 3, 3);
INSERT INTO humainedev.persona_details_master VALUES (17, 5, 4, 4, 5);
INSERT INTO humainedev.persona_details_master VALUES (18, 5, 4, 4, 4);
INSERT INTO humainedev.persona_details_master VALUES (19, 5, 4, 4, 6);
INSERT INTO humainedev.persona_details_master VALUES (20, 5, 4, 4, 7);
INSERT INTO humainedev.persona_details_master VALUES (21, 5, 4, 4, 10);
INSERT INTO humainedev.persona_details_master VALUES (22, 5, 4, 4, 2);
INSERT INTO humainedev.persona_details_master VALUES (23, 5, 4, 4, 9);
INSERT INTO humainedev.persona_details_master VALUES (24, 5, 4, 4, 3);
INSERT INTO humainedev.persona_details_master VALUES (25, 2, 1, 2, 5);
INSERT INTO humainedev.persona_details_master VALUES (26, 2, 1, 2, 1);
INSERT INTO humainedev.persona_details_master VALUES (27, 2, 1, 2, 4);
INSERT INTO humainedev.persona_details_master VALUES (28, 2, 1, 2, 6);
INSERT INTO humainedev.persona_details_master VALUES (29, 2, 1, 2, 7);
INSERT INTO humainedev.persona_details_master VALUES (30, 2, 1, 2, 2);
INSERT INTO humainedev.persona_details_master VALUES (31, 2, 1, 2, 9);
INSERT INTO humainedev.persona_details_master VALUES (32, 2, 1, 4, 5);
INSERT INTO humainedev.persona_details_master VALUES (33, 2, 1, 4, 1);
INSERT INTO humainedev.persona_details_master VALUES (34, 2, 1, 4, 4);
INSERT INTO humainedev.persona_details_master VALUES (35, 2, 1, 4, 6);
INSERT INTO humainedev.persona_details_master VALUES (36, 2, 1, 4, 7);
INSERT INTO humainedev.persona_details_master VALUES (37, 2, 1, 4, 2);
INSERT INTO humainedev.persona_details_master VALUES (38, 2, 1, 4, 9);
INSERT INTO humainedev.persona_details_master VALUES (39, 2, 1, 5, 5);
INSERT INTO humainedev.persona_details_master VALUES (40, 2, 1, 5, 1);
INSERT INTO humainedev.persona_details_master VALUES (41, 2, 1, 5, 4);
INSERT INTO humainedev.persona_details_master VALUES (42, 2, 1, 5, 6);
INSERT INTO humainedev.persona_details_master VALUES (43, 2, 1, 5, 7);
INSERT INTO humainedev.persona_details_master VALUES (44, 2, 1, 5, 2);
INSERT INTO humainedev.persona_details_master VALUES (45, 2, 1, 5, 9);
INSERT INTO humainedev.persona_details_master VALUES (46, 2, 1, 6, 5);
INSERT INTO humainedev.persona_details_master VALUES (47, 2, 1, 6, 1);
INSERT INTO humainedev.persona_details_master VALUES (48, 2, 1, 6, 4);
INSERT INTO humainedev.persona_details_master VALUES (49, 2, 1, 6, 6);
INSERT INTO humainedev.persona_details_master VALUES (50, 2, 1, 6, 7);
INSERT INTO humainedev.persona_details_master VALUES (51, 2, 1, 6, 2);
INSERT INTO humainedev.persona_details_master VALUES (52, 2, 1, 6, 9);
INSERT INTO humainedev.persona_details_master VALUES (53, 2, 2, 2, 5);
INSERT INTO humainedev.persona_details_master VALUES (54, 2, 2, 2, 1);
INSERT INTO humainedev.persona_details_master VALUES (55, 2, 2, 2, 4);
INSERT INTO humainedev.persona_details_master VALUES (56, 2, 2, 2, 6);
INSERT INTO humainedev.persona_details_master VALUES (57, 2, 2, 2, 7);
INSERT INTO humainedev.persona_details_master VALUES (58, 2, 2, 2, 2);
INSERT INTO humainedev.persona_details_master VALUES (59, 2, 2, 2, 9);
INSERT INTO humainedev.persona_details_master VALUES (60, 2, 2, 4, 5);
INSERT INTO humainedev.persona_details_master VALUES (61, 2, 2, 4, 1);
INSERT INTO humainedev.persona_details_master VALUES (62, 2, 2, 4, 4);
INSERT INTO humainedev.persona_details_master VALUES (63, 2, 2, 4, 6);
INSERT INTO humainedev.persona_details_master VALUES (64, 2, 2, 4, 7);
INSERT INTO humainedev.persona_details_master VALUES (65, 2, 2, 4, 2);
INSERT INTO humainedev.persona_details_master VALUES (66, 2, 2, 4, 9);
INSERT INTO humainedev.persona_details_master VALUES (67, 2, 2, 5, 5);
INSERT INTO humainedev.persona_details_master VALUES (68, 2, 2, 5, 1);
INSERT INTO humainedev.persona_details_master VALUES (69, 2, 2, 5, 4);
INSERT INTO humainedev.persona_details_master VALUES (70, 2, 2, 5, 6);
INSERT INTO humainedev.persona_details_master VALUES (71, 2, 2, 5, 7);
INSERT INTO humainedev.persona_details_master VALUES (72, 2, 2, 5, 2);
INSERT INTO humainedev.persona_details_master VALUES (73, 2, 2, 5, 9);
INSERT INTO humainedev.persona_details_master VALUES (74, 2, 2, 6, 5);
INSERT INTO humainedev.persona_details_master VALUES (75, 2, 2, 6, 1);
INSERT INTO humainedev.persona_details_master VALUES (76, 2, 2, 6, 4);
INSERT INTO humainedev.persona_details_master VALUES (77, 2, 2, 6, 6);
INSERT INTO humainedev.persona_details_master VALUES (78, 2, 2, 6, 7);
INSERT INTO humainedev.persona_details_master VALUES (79, 2, 2, 6, 2);
INSERT INTO humainedev.persona_details_master VALUES (80, 2, 2, 6, 9);
INSERT INTO humainedev.persona_details_master VALUES (81, 2, 4, 2, 5);
INSERT INTO humainedev.persona_details_master VALUES (82, 2, 4, 2, 1);
INSERT INTO humainedev.persona_details_master VALUES (83, 2, 4, 2, 4);
INSERT INTO humainedev.persona_details_master VALUES (84, 2, 4, 2, 6);
INSERT INTO humainedev.persona_details_master VALUES (85, 2, 4, 2, 7);
INSERT INTO humainedev.persona_details_master VALUES (86, 2, 4, 2, 2);
INSERT INTO humainedev.persona_details_master VALUES (87, 2, 4, 2, 9);
INSERT INTO humainedev.persona_details_master VALUES (88, 2, 4, 4, 5);
INSERT INTO humainedev.persona_details_master VALUES (89, 2, 4, 4, 1);
INSERT INTO humainedev.persona_details_master VALUES (90, 2, 4, 4, 4);
INSERT INTO humainedev.persona_details_master VALUES (91, 2, 4, 4, 6);
INSERT INTO humainedev.persona_details_master VALUES (92, 2, 4, 4, 7);
INSERT INTO humainedev.persona_details_master VALUES (93, 2, 4, 4, 2);
INSERT INTO humainedev.persona_details_master VALUES (94, 2, 4, 4, 9);
INSERT INTO humainedev.persona_details_master VALUES (95, 2, 4, 5, 5);
INSERT INTO humainedev.persona_details_master VALUES (96, 2, 4, 5, 1);
INSERT INTO humainedev.persona_details_master VALUES (97, 2, 4, 5, 4);
INSERT INTO humainedev.persona_details_master VALUES (98, 2, 4, 5, 6);
INSERT INTO humainedev.persona_details_master VALUES (99, 2, 4, 5, 7);
INSERT INTO humainedev.persona_details_master VALUES (100, 2, 4, 5, 2);
INSERT INTO humainedev.persona_details_master VALUES (101, 2, 4, 5, 9);
INSERT INTO humainedev.persona_details_master VALUES (102, 2, 4, 6, 5);
INSERT INTO humainedev.persona_details_master VALUES (103, 2, 4, 6, 1);
INSERT INTO humainedev.persona_details_master VALUES (104, 2, 4, 6, 4);
INSERT INTO humainedev.persona_details_master VALUES (105, 2, 4, 6, 6);
INSERT INTO humainedev.persona_details_master VALUES (106, 2, 4, 6, 7);
INSERT INTO humainedev.persona_details_master VALUES (107, 2, 4, 6, 2);
INSERT INTO humainedev.persona_details_master VALUES (108, 2, 4, 6, 9);
INSERT INTO humainedev.persona_details_master VALUES (109, 3, 4, 7, 5);
INSERT INTO humainedev.persona_details_master VALUES (110, 3, 4, 7, 1);
INSERT INTO humainedev.persona_details_master VALUES (111, 3, 4, 7, 10);
INSERT INTO humainedev.persona_details_master VALUES (112, 3, 4, 7, 2);
INSERT INTO humainedev.persona_details_master VALUES (113, 3, 4, 7, 9);
INSERT INTO humainedev.persona_details_master VALUES (114, 3, 4, 7, 3);
INSERT INTO humainedev.persona_details_master VALUES (115, 1, 5, 1, 1);
INSERT INTO humainedev.persona_details_master VALUES (116, 1, 5, 1, 7);
INSERT INTO humainedev.persona_details_master VALUES (117, 1, 5, 1, 2);
INSERT INTO humainedev.persona_details_master VALUES (118, 1, 5, 1, 3);
INSERT INTO humainedev.persona_details_master VALUES (119, 1, 5, 1, 8);
INSERT INTO humainedev.persona_details_master VALUES (120, 1, 5, 2, 1);
INSERT INTO humainedev.persona_details_master VALUES (121, 1, 5, 2, 7);
INSERT INTO humainedev.persona_details_master VALUES (122, 1, 5, 2, 2);
INSERT INTO humainedev.persona_details_master VALUES (123, 1, 5, 2, 3);
INSERT INTO humainedev.persona_details_master VALUES (124, 1, 5, 2, 8);
INSERT INTO humainedev.persona_details_master VALUES (125, 1, 5, 4, 1);
INSERT INTO humainedev.persona_details_master VALUES (126, 1, 5, 4, 7);
INSERT INTO humainedev.persona_details_master VALUES (127, 1, 5, 4, 2);
INSERT INTO humainedev.persona_details_master VALUES (128, 1, 5, 4, 3);
INSERT INTO humainedev.persona_details_master VALUES (129, 1, 5, 4, 8);
INSERT INTO humainedev.persona_details_master VALUES (130, 4, 2, 3, 6);
INSERT INTO humainedev.persona_details_master VALUES (131, 4, 2, 6, 6);
INSERT INTO humainedev.persona_details_master VALUES (132, 4, 4, 3, 6);
INSERT INTO humainedev.persona_details_master VALUES (133, 4, 4, 6, 6);
INSERT INTO humainedev.persona_details_master VALUES (134, 4, 5, 7, 11);
INSERT INTO humainedev.persona_details_master VALUES (135, 1, 5, 7, 11);
INSERT INTO humainedev.persona_details_master VALUES (136, 3, 5, 7, 11);
INSERT INTO humainedev.persona_details_master VALUES (137, 2, 5, 7, 11);
INSERT INTO humainedev.persona_details_master VALUES (138, 5, 5, 7, 11);

-----------------------------------------------------------------------------------------


INSERT INTO humainedev.persona_frustrations VALUES (1, 'The fabric of the suits in this showroom doesn’t seem high quality. I’m afraid I will not be able to use them more than once. 

90% of the suits I am looking at are models I already have or seen on my coworkers/ friends. I want uniquely designed suits that make me stand out from the crowd.
This designer doesn’t offer custom made suits. As public figure, I need to have perfect fitting clothing in order to make a great example for my supporters.');
INSERT INTO humainedev.persona_frustrations VALUES (2, 'The assistant buyers are not suggesting anything that fits me, and they’re not giving any options that I could wear for the gala night.');
INSERT INTO humainedev.persona_frustrations VALUES (2, 'I came to this shop because of its popularity, but the quality of clothes doesn’t look good.');
INSERT INTO humainedev.persona_frustrations VALUES (2, 'The dress designs seem very casual and they don’t have anything that makes me look elegant and unique.');
INSERT INTO humainedev.persona_frustrations VALUES (3, 'The items in this shop are all pastel-colored;');
INSERT INTO humainedev.persona_frustrations VALUES (3, 'All the items with interesting designs have bad quality and don’t seem like they will last long;');
INSERT INTO humainedev.persona_frustrations VALUES (3, 'The online shop doesn’t suggest ideas to match pieces of clothing.');
INSERT INTO humainedev.persona_frustrations VALUES (4, 'I keep looking but I cannot find anything unique and creative.');
INSERT INTO humainedev.persona_frustrations VALUES (4, 'This shopping mall has only a few shops in it, and they all have similar items.');
INSERT INTO humainedev.persona_frustrations VALUES (4, 'I am not allowed to make combinations between gift sets.');
INSERT INTO humainedev.persona_frustrations VALUES (4, 'I don’t like the way the gift sets are created and I want to make combinations to match the birthday person’s taste.');
INSERT INTO humainedev.persona_frustrations VALUES (5, 'I keep asking for an assistant to help me choose a shirt that makes me stand out but the x section is the only section with assistant buyers.

There are no shirts with unique designs or colors. This shop only has ordinary pastel-colored shirts.');
INSERT INTO humainedev.persona_frustrations VALUES (6, 'The quality of the clothes on sale is very bad. I cannot use them in an everyday basis;The size numbers in the sales section are very small and they don’t fit me. 

I keep looking but I cannot find anything that my kids will like.');
INSERT INTO humainedev.persona_frustrations VALUES (7, 'I keep looking but these items are all colorful and don’t look comfortable to use on a daily basis.');
INSERT INTO humainedev.persona_frustrations VALUES (7, 'I really thought this shop had what I needed but I guess I will have to look for another shop that has similar clothes to Y shop.');
INSERT INTO humainedev.persona_frustrations VALUES (7, 'The assistants are all busy with other clients and are not helping me find some casual clothes I could try on.');
INSERT INTO humainedev.persona_frustrations VALUES (8, 'I don’t know why everyone is buying in this store, the clothes don’t look good quality at all;');
INSERT INTO humainedev.persona_frustrations VALUES (8, 'I am afraid that I won’t be able to wear these clothes more than 2-3 times;');
INSERT INTO humainedev.persona_frustrations VALUES (9, 'Although my friend is pleased with the clothes of this shop, it doesn’t feel like their clothes fit my style.');
INSERT INTO humainedev.persona_frustrations VALUES (9, 'The clothes in the shop look all quite casual and clothes I’ve seen other people wear.');
INSERT INTO humainedev.persona_frustrations VALUES (9, 'There are no unique designs or colors of items in this shop. Everything seems ordinary and that is not what I am looking for.');
INSERT INTO humainedev.persona_frustrations VALUES (10, 'The products I saved in the online shop are all out of stock. I really liked those items.');
INSERT INTO humainedev.persona_frustrations VALUES (10, 'The items in the shop are so simple and ordinary. I want to buy things that are unique and define me.');
INSERT INTO humainedev.persona_frustrations VALUES (10, 'This new collection is very similar to every other shop in the mall. I wanted something that not every store has.');
INSERT INTO humainedev.persona_frustrations VALUES (11, 'I love the clothes in this shop but the numbers are very small for my size;');
INSERT INTO humainedev.persona_frustrations VALUES (11, 'Maybe this style isn’t for me, as they don’t fit as good as I thought.');
INSERT INTO humainedev.persona_frustrations VALUES (11, 'The items from the influencer’s Instagram page are all sold.');
INSERT INTO humainedev.persona_frustrations VALUES (12, 'There are only a few items on sale and I cannot explore enough to find unique items.');
INSERT INTO humainedev.persona_frustrations VALUES (12, 'All the clothes seem to have similar designs to the other shops and I don’t know if I can find something that is rare and unique.');
INSERT INTO humainedev.persona_frustrations VALUES (12, 'Although it’s my favorite shop and all my friends buy here, this sale doesn’t have anything worth buying.');
INSERT INTO humainedev.persona_frustrations VALUES (13, 'It says that this item is on sale, but it has the same price that it had before the sale? I have added it on my shopping list last week, and now although on sale, it has the same price!');
INSERT INTO humainedev.persona_frustrations VALUES (13, 'I don’t think this shop deserves to be this popular. The clothes quality isn’t good at all.');
INSERT INTO humainedev.persona_frustrations VALUES (13, 'The clothes on sale are so casual, I cannot wear them to an office party.');
INSERT INTO humainedev.persona_frustrations VALUES (14, 'I keep looking but nothing looks good on me;');
INSERT INTO humainedev.persona_frustrations VALUES (14, 'My friend suggested this shop to buy elegant clothes, but everything here looks casual and similar to my current style.');
INSERT INTO humainedev.persona_frustrations VALUES (14, 'There’s nothing good for my friend as well. They have a very unique style, and the clothes in this shop don’t match with  their style.');
INSERT INTO humainedev.persona_frustrations VALUES (15, 'The website is taking too long to load.');
INSERT INTO humainedev.persona_frustrations VALUES (15, 'There’s no size guide for me to know if the number of the shoe will fit me.');
INSERT INTO humainedev.persona_frustrations VALUES (15, 'The shoes all seem very uncomfortable and I don’t know if I will be able to wear them on a daily basis.');
INSERT INTO humainedev.persona_frustrations VALUES (15, 'I wish I had gone to the store instead.');
INSERT INTO humainedev.persona_frustrations VALUES (16, 'The shop has very few clothes for Autumn, everything else is for summer;');
INSERT INTO humainedev.persona_frustrations VALUES (16, 'This shop doesn’t have the kind of clothes that are very in right now;');
INSERT INTO humainedev.persona_frustrations VALUES (16, 'I am afraid that I won’t be able to wear the clothes that I buy in this shop because summer’s almost over and I will have to take them away as soon as the weather starts to cool down.');
INSERT INTO humainedev.persona_frustrations VALUES (17, 'The dresses in my friend’s salon seem casual and I want something elegant for the event;');
INSERT INTO humainedev.persona_frustrations VALUES (17, 'Although I am open to try out different styles, the ones in this collection aren’t my taste;');
INSERT INTO humainedev.persona_frustrations VALUES (17, 'My friend’s salon doesn’t offer custom made clothes, so I am afraid I will look just like everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (18, 'The items I saw on the influencers’ Instagram pages are all out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (18, 'The items are all for winter, only a few of them are for spring.');
INSERT INTO humainedev.persona_frustrations VALUES (18, 'The products looked much better in their photos. But the fabrics doesn’t look like it’s good quality.');
INSERT INTO humainedev.persona_frustrations VALUES (19, 'This item looks great on the model but I am not sure it will look good on me;');
INSERT INTO humainedev.persona_frustrations VALUES (19, 'I like this shirt but it has a bad quality, I am afraid I will use it once and throw it away.');
INSERT INTO humainedev.persona_frustrations VALUES (19, 'The products my friends recommended are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (20, 'This item is good but I’ve seen it on a lot of influencers. I want something more unique.');
INSERT INTO humainedev.persona_frustrations VALUES (20, 'The quality of the shirts isn’t as good as I thought. I need items for daily use, I cannot buy items that last 1 week only.');
INSERT INTO humainedev.persona_frustrations VALUES (20, 'All the products are similar to one another and there are not many choices for me to pick from.');
INSERT INTO humainedev.persona_frustrations VALUES (21, 'All the items I like are small sized only. Nothing fits me!');
INSERT INTO humainedev.persona_frustrations VALUES (21, 'Most of the clothes in this shop are elegant and made for gala nights.');
INSERT INTO humainedev.persona_frustrations VALUES (21, 'There are not enough products to explore.');
INSERT INTO humainedev.persona_frustrations VALUES (22, 'I keep looking but nothing seems like my style;');
INSERT INTO humainedev.persona_frustrations VALUES (22, 'The quality of the clothes is okay, but they’re not comfortable at all. I will not be able to wear them all day long.');
INSERT INTO humainedev.persona_frustrations VALUES (22, 'The clothes my sister picked up for me are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (23, 'There are not many products on sale. It seems that they are sold out.');
INSERT INTO humainedev.persona_frustrations VALUES (23, 'All the products I like are not on sale, and I don’t want to buy them at the regular price.');
INSERT INTO humainedev.persona_frustrations VALUES (23, 'The products my friend bought are all either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (23, 'I thought that coming to the store would be better as I would see the product’s quality with my own eyes, but there are very few clothes to check out.');
INSERT INTO humainedev.persona_frustrations VALUES (24, 'The clothes look very beautiful on the model, but I am afraid they won’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (24, 'I found a shirt the model wore but the material looked much better in her pictures.');
INSERT INTO humainedev.persona_frustrations VALUES (24, 'This website is a little difficult. I added an item to the cart and suddenly the product isn’t there anymore?');
INSERT INTO humainedev.persona_frustrations VALUES (25, 'Most of the suits seem ordinary and  I’ve seen them to other men.');
INSERT INTO humainedev.persona_frustrations VALUES (25, 'There aren’t a lot of suits for me to choose from.');
INSERT INTO humainedev.persona_frustrations VALUES (25, 'The suits that are unique/ with good quality are all out of stock and the shop doesn’t offer custom made designs.');
INSERT INTO humainedev.persona_frustrations VALUES (26, 'The clothes in the new collection are great, but I’ve seen them in a lot of influencers’ lately.');
INSERT INTO humainedev.persona_frustrations VALUES (26, 'The new collection has only a few products and they’re all designed for every day use. I want to buy some items for everyday use, but I need clothes for different events as well.');
INSERT INTO humainedev.persona_frustrations VALUES (26, 'The items I saved on the online shop are all out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (27, 'All the clothes seem for every day use. I want to buy something special that grabs attention.');
INSERT INTO humainedev.persona_frustrations VALUES (27, 'I always buy at this shop but they never send discounts.');
INSERT INTO humainedev.persona_frustrations VALUES (27, 'The clothes look a little cheap and don’t look like they have good quality.');
INSERT INTO humainedev.persona_frustrations VALUES (28, 'The items on sale are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (28, 'This shop doesn’t have anything on sale. I am a regular costumer but they never offer me any discount although the items aren’t on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (28, 'I keep looking but these clothes are not my style at all. I will probably end up buying items that aren’t my style.');
INSERT INTO humainedev.persona_frustrations VALUES (29, 'I like the way this item looks on the model but I don’t know if it will fit me as well.');
INSERT INTO humainedev.persona_frustrations VALUES (29, 'I like x item, but one of my friends bought it a couple weeks ago and I don’t want to have the same items as him.');
INSERT INTO humainedev.persona_frustrations VALUES (29, 'There are not enough items for me to explore.');
INSERT INTO humainedev.persona_frustrations VALUES (30, 'I keep looking but nothing looks good on me;');
INSERT INTO humainedev.persona_frustrations VALUES (30, 'I don’t like the new collection, all the items are so extra and I will not be able to wear them on a daily basis.');
INSERT INTO humainedev.persona_frustrations VALUES (30, 'I like this item but I don’t like the quality of it.');
INSERT INTO humainedev.persona_frustrations VALUES (30, 'I wanted to buy something for my husband but I don’t like anything from the men’s collection.');
INSERT INTO humainedev.persona_frustrations VALUES (30, 'There are a lot of people waiting in line before me, and I don’t have time to wait until my turn to try on the clothes comes.');
INSERT INTO humainedev.persona_frustrations VALUES (31, 'The clothes look good on the model but I don’t know if they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (31, 'The website is complicated. How do I know if the size they recommend is my size?');
INSERT INTO humainedev.persona_frustrations VALUES (31, 'Everything seems very extravagant. I want something more casual that I am able to wear on a daily basis as well.');
INSERT INTO humainedev.persona_frustrations VALUES (32, 'Only a few items are shown on the website.');
INSERT INTO humainedev.persona_frustrations VALUES (32, 'There are no new collection items, only items from the past season.');
INSERT INTO humainedev.persona_frustrations VALUES (32, 'The suits that are unique/ with good quality are all out of stock and the shop doesn’t offer custom made designs.');
INSERT INTO humainedev.persona_frustrations VALUES (33, 'The clothes I like are all out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (33, 'There aren’t enough products for me to look through.');
INSERT INTO humainedev.persona_frustrations VALUES (33, 'The clothes style doesn’t look as good on me as on my friend.');
INSERT INTO humainedev.persona_frustrations VALUES (34, 'This shop has mostly casual clothes. I want something extra for my birthday party.');
INSERT INTO humainedev.persona_frustrations VALUES (34, 'The clothes look good on the models but I am not sure they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (34, 'This shop has very few clothes for me to explore through.');
INSERT INTO humainedev.persona_frustrations VALUES (35, 'The clothes look a little bit casual for my style. I want to show off my personality using my clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (35, 'The clothes I liked from the influencer are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (35, 'All the clothes in the shop are similar. There’s nothing unique in it.');
INSERT INTO humainedev.persona_frustrations VALUES (36, 'I like this bag but a lot of people are buying this vintage model lately.');
INSERT INTO humainedev.persona_frustrations VALUES (36, 'There are only a few new findings in the shop.');
INSERT INTO humainedev.persona_frustrations VALUES (36, 'The prices are very expensive, even though all these clothes are pre-owned.');
INSERT INTO humainedev.persona_frustrations VALUES (36, 'This shop doesn’t offer customized clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (37, 'There are not plain white shirts for me to look for.');
INSERT INTO humainedev.persona_frustrations VALUES (37, 'The quality of the shirts doesn’t look good.');
INSERT INTO humainedev.persona_frustrations VALUES (37, 'This new shop has good items but it has a very complicated structure. I cannot find what I’m looking for.');
INSERT INTO humainedev.persona_frustrations VALUES (37, 'There are not plain white shirts for me to look for.');
INSERT INTO humainedev.persona_frustrations VALUES (38, 'These items look great on the models but I don’t know if they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (38, 'The website is quite complicated and slow!!');
INSERT INTO humainedev.persona_frustrations VALUES (38, 'There are only a few dresses that I could wear to the wedding. All the other clothes are for everyday use.');
INSERT INTO humainedev.persona_frustrations VALUES (38, 'All the items here are for grown women only, my daughter cannot wear any of these items.');
INSERT INTO humainedev.persona_frustrations VALUES (39, 'Only a few items are shown in the website.');
INSERT INTO humainedev.persona_frustrations VALUES (39, 'There are no new collection items, only items from the past season.');
INSERT INTO humainedev.persona_frustrations VALUES (39, 'The suits that are unique/ with good quality are all out of stock and the shop doesn’t offer custom made designs.');
INSERT INTO humainedev.persona_frustrations VALUES (40, 'That offer isn’t available anymore?');
INSERT INTO humainedev.persona_frustrations VALUES (40, 'There are only a few dresses in this shop.');
INSERT INTO humainedev.persona_frustrations VALUES (40, 'All the ones that are already in the shop are all very simple and not my style.');
INSERT INTO humainedev.persona_frustrations VALUES (41, 'I can’t find anything that’s like my style in this new collection. Everything is either for young girls or older women.');
INSERT INTO humainedev.persona_frustrations VALUES (41, 'I don’t want to waste all my day looking for these items. Plus I am wasting my friend’s day as well.');
INSERT INTO humainedev.persona_frustrations VALUES (41, 'There are only a few dresses that I can try on.');
INSERT INTO humainedev.persona_frustrations VALUES (42, 'The items are a little bit too much for me. I don’t know if they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (42, 'Apparently the shop is only giving discounts if you buy for more than $100. I don’t want to buy just to get the discount. I want to buy something that fits my new style.');
INSERT INTO humainedev.persona_frustrations VALUES (43, 'The items of this collection seem too casual.');
INSERT INTO humainedev.persona_frustrations VALUES (43, 'Everyone is buying from this shop lately so I have to dig in in order to find something unique');
INSERT INTO humainedev.persona_frustrations VALUES (43, 'This shop has very few clothes for me to explore through.');
INSERT INTO humainedev.persona_frustrations VALUES (44, 'The items are very beautiful but most of them are all small sizes.');
INSERT INTO humainedev.persona_frustrations VALUES (44, 'There are not enough items in sale, there are only a few products.');
INSERT INTO humainedev.persona_frustrations VALUES (44, 'All the items I like are either from the new collection, or not the right size for me.');
INSERT INTO humainedev.persona_frustrations VALUES (45, 'The shirts that are on sale have a bad quality and they don’t look comfortable at all. I will not be able to wear them to work.');
INSERT INTO humainedev.persona_frustrations VALUES (45, 'I keep looking but these items are not my style at all.');
INSERT INTO humainedev.persona_frustrations VALUES (45, 'All the beautiful shirts are not on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (45, 'Most of the clothes are for special occasions only, there are only a few clothes for everyday use.');
INSERT INTO humainedev.persona_frustrations VALUES (46, 'There are a lot of people waiting in line for the fitting room and I don’t know if I will have time to wait until my turn comes.');
INSERT INTO humainedev.persona_frustrations VALUES (46, 'I cannot find the items I saved from the website. They’re either out of stock, or the assistants can’t find them.');
INSERT INTO humainedev.persona_frustrations VALUES (46, 'The shop is very crowded and the clothes are all super messy. There’s no way to find what I am looking for in this crowd.');
INSERT INTO humainedev.persona_frustrations VALUES (46, 'All the clothes that are here are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (47, 'There are a lot of people waiting in line to get the product.');
INSERT INTO humainedev.persona_frustrations VALUES (47, 'There are only a few products and I HAVE to buy one of them.');
INSERT INTO humainedev.persona_frustrations VALUES (47, 'I am a regular costumer in this shop but they’re not doing any exceptions for me.');
INSERT INTO humainedev.persona_frustrations VALUES (48, 'Most of the items on sale don’t look comfortable at all.');
INSERT INTO humainedev.persona_frustrations VALUES (48, 'The designs are for younger people, and those colors don’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (48, 'All the items I saved on the website are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (49, 'All the items look too casual for me.');
INSERT INTO humainedev.persona_frustrations VALUES (49, 'The items in sale are all either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (49, 'There are only a few items that fit my style.');
INSERT INTO humainedev.persona_frustrations VALUES (50, 'This item looked much better in picture than in real life. It seems older than it looked like before.');
INSERT INTO humainedev.persona_frustrations VALUES (50, 'The only items left on sale are items that are quite in right now and a lot of people are buying.');
INSERT INTO humainedev.persona_frustrations VALUES (50, 'There are not a lot of products for me to explore through.');
INSERT INTO humainedev.persona_frustrations VALUES (51, 'These items look good on the models but I don’t know if they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (51, 'Most of the items on sale don’t seem comfortable at all. It seems that they are all dedicated for events and not every day use.');
INSERT INTO humainedev.persona_frustrations VALUES (51, 'There are a lot of similar items. I don’t know which of these to choose.');
INSERT INTO humainedev.persona_frustrations VALUES (52, 'X shop brought the products from x brand but apparently they only brought the limited edition specials? I want to buy the products they usually sell!');
INSERT INTO humainedev.persona_frustrations VALUES (52, 'I like this bag but I m afraid that I will not be able to use it because it is very small.');
INSERT INTO humainedev.persona_frustrations VALUES (52, 'They increased their prices by 3%. This is way off my limit for clothes!');
INSERT INTO humainedev.persona_frustrations VALUES (53, 'The clothes look good on the models, but I don’t know if they will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (53, 'All the items I like are out of stock?');
INSERT INTO humainedev.persona_frustrations VALUES (53, 'I bought a couple of items but there weren’t enough products to explore in the store. The website had more options.');
INSERT INTO humainedev.persona_frustrations VALUES (53, 'They never give me any discounts.');
INSERT INTO humainedev.persona_frustrations VALUES (54, 'The dresses in the new collection seem so casual and not fit for my birthday party.');
INSERT INTO humainedev.persona_frustrations VALUES (54, 'There are only a few dresses that are more fancy but I am afraid none of them will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (54, 'If I can’t find anything I might have to check the other shops and I am not sure where to go.');
INSERT INTO humainedev.persona_frustrations VALUES (55, 'The items look good on the models but I’m not sure f they will look good on me as well.');
INSERT INTO humainedev.persona_frustrations VALUES (55, 'All the items are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (55, 'Everyday items are all basics and I don’t want to look ordinary. I want to look unique everyday.');
INSERT INTO humainedev.persona_frustrations VALUES (55, 'I like this item but I already have another one that looks exactly like this one.');
INSERT INTO humainedev.persona_frustrations VALUES (56, 'Most of the items in this shop are sneakers. Only a few of them are elegant ones.');
INSERT INTO humainedev.persona_frustrations VALUES (56, 'I like this pair, but the leather of them doesn’t look very good quality. I want to use them for a longer period and if the quality is not good I won’t be able to.');
INSERT INTO humainedev.persona_frustrations VALUES (56, 'None of the stores have many pairs of elegant shoes. There are not many choices for me to look through.');
INSERT INTO humainedev.persona_frustrations VALUES (57, 'All the items on sale are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (57, 'I like this item but I saw 3 other people going to the fitting room to try it on.');
INSERT INTO humainedev.persona_frustrations VALUES (57, 'All the items I like are either bad quality or not on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (57, 'There are not a lot of products for me to explore through.');
INSERT INTO humainedev.persona_frustrations VALUES (58, 'There were more items n the website. I think they still haven’t brought every item to the store.');
INSERT INTO humainedev.persona_frustrations VALUES (58, 'Most of the items are on sale and they’re from the summer season, so I don’t need to buy them.');
INSERT INTO humainedev.persona_frustrations VALUES (58, 'I like this item but the shop doesn’t offer the option to save the items and come collect them later.');
INSERT INTO humainedev.persona_frustrations VALUES (59, 'Most items are in sale but they are not good quality. I want to leave an impression with my clothes as well.');
INSERT INTO humainedev.persona_frustrations VALUES (59, 'There are only a few items I could try on from the new collection because most of them are either for younger or older people.');
INSERT INTO humainedev.persona_frustrations VALUES (59, 'I am not sure buying from this shop will make me look professional and stylish. The style just doesn’t look like my own and I don’t feel comfortable in it.');
INSERT INTO humainedev.persona_frustrations VALUES (60, 'This shop is so crowded. I don’t have space to try things on.');
INSERT INTO humainedev.persona_frustrations VALUES (60, 'All the things I like are larger numbers than my size.');
INSERT INTO humainedev.persona_frustrations VALUES (60, 'All the items left in stock are bad quality. I cannot wear them.');
INSERT INTO humainedev.persona_frustrations VALUES (61, 'I like how this item looks on the model in store, but I don’t know if it will look good on me as well.');
INSERT INTO humainedev.persona_frustrations VALUES (61, 'There are a lot of people waiting in line to try the clothes on. I have to find a way to pass them in order to try the clothes I like on.');
INSERT INTO humainedev.persona_frustrations VALUES (61, 'Most of the clothes in the new collection are not my style and I don’t know if I will use them.');
INSERT INTO humainedev.persona_frustrations VALUES (62, 'All items look casual and I want something more extra.');
INSERT INTO humainedev.persona_frustrations VALUES (62, 'I like the items’ designs but the quality of the clothes is not good. I don’t know if the clothes will last and I might have to buy new ones.');
INSERT INTO humainedev.persona_frustrations VALUES (62, 'All the items she promoted are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (63, 'The shop is too crowded, I don’t know when it will be my turn to try the clothes on.');
INSERT INTO humainedev.persona_frustrations VALUES (63, 'The clothes looked good on the actress but they don’t fit me at all.');
INSERT INTO humainedev.persona_frustrations VALUES (63, 'There’s no variety of colors in this shop. Everything has the nuances of beige and it looks a little boring for my style.');
INSERT INTO humainedev.persona_frustrations VALUES (64, 'I like this item, but my friend has this exact same one and I don’t want to have identical clothes with anyone.');
INSERT INTO humainedev.persona_frustrations VALUES (64, 'This shop is so crowded, there’s no space for me to try things on in peace.');
INSERT INTO humainedev.persona_frustrations VALUES (64, 'This shirt looks good on me but it doesn’t have a good quality and I’m afraid I won’t be able to wear it more than twice.');
INSERT INTO humainedev.persona_frustrations VALUES (65, 'The website is loading slowly, I don’t have all day.');
INSERT INTO humainedev.persona_frustrations VALUES (65, 'Nothing in here seems comfortable. Although I want to support this campaign, I just can’t find anything that is comfortable for every day use.');
INSERT INTO humainedev.persona_frustrations VALUES (65, 'This campaign only lasts this weekend? I wonder why they didn’t make it at least a week long, because a lot more people would see the campaign and buy clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (66, 'There are only a few clothes that are casual in this shop. All the other ones are fancy and for events only.');
INSERT INTO humainedev.persona_frustrations VALUES (66, 'I like this white shirt but it looks tight and I don’t feel comfortable in tight shirts.');
INSERT INTO humainedev.persona_frustrations VALUES (66, 'The items I like are all out of stock??');
INSERT INTO humainedev.persona_frustrations VALUES (67, 'Their new collection doesn’t have plain shirts for everyday use. I need items that I can wear multiple times.');
INSERT INTO humainedev.persona_frustrations VALUES (67, 'Most of the suits the shop has are shiny or with complex designs, I need something more casual.');
INSERT INTO humainedev.persona_frustrations VALUES (67, 'I like this item but It doesn’t fit me and they don’t make custom made clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (68, 'I like this item but they don’t have it in my size.');
INSERT INTO humainedev.persona_frustrations VALUES (68, 'This shirt is cool but the color is too bright and it will be noticeable when I sweat.');
INSERT INTO humainedev.persona_frustrations VALUES (68, 'There are only a few thermos bottles left. I hope I can decide what to buy before they run out of them.');
INSERT INTO humainedev.persona_frustrations VALUES (69, 'I keep looking but I cannot find anything that fits my style.');
INSERT INTO humainedev.persona_frustrations VALUES (69, 'I like this dress but it has a very uncomfortable fabrics. I don’t know how I will stay in it all night.');
INSERT INTO humainedev.persona_frustrations VALUES (69, 'The shop has perfect service but it doesn’t offer custom made clothes. I like x item but it doesn’t fit me.');
INSERT INTO humainedev.persona_frustrations VALUES (70, 'Their new collection has only clothes for younger ages, there are only a few products from the style I want to have.');
INSERT INTO humainedev.persona_frustrations VALUES (70, 'I keep trying clothes on but nothing looks good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (70, 'The clothes the assistant is offering don’t look good on me at all.');
INSERT INTO humainedev.persona_frustrations VALUES (71, 'I like this shirt but I’ve seen it in many influencers lately.');
INSERT INTO humainedev.persona_frustrations VALUES (71, 'The items look too casual and ordinary. I don’t want to look like everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (71, 'There aren’t a lot of items to explore through.');
INSERT INTO humainedev.persona_frustrations VALUES (72, 'I keep looking but nothing fits my style.');
INSERT INTO humainedev.persona_frustrations VALUES (72, 'The colors are quite bright and the designs are too complicated. Although I wanted to buy colorful items, I don’t want to look like i try too hard.');
INSERT INTO humainedev.persona_frustrations VALUES (72, 'Apparently the shop now only gives free gifts if we buy for more than $50. I don’t know if I will buy that much.');
INSERT INTO humainedev.persona_frustrations VALUES (73, 'I keep looking but I cannot find what I like.');
INSERT INTO humainedev.persona_frustrations VALUES (73, 'I cannot find any blazer that looks good on me. My voucher expires next week so I need to spend it before it does.');
INSERT INTO humainedev.persona_frustrations VALUES (73, 'All the blazers I like are either too big or too small.');
INSERT INTO humainedev.persona_frustrations VALUES (73, 'There are not enough blazers in the shop, maybe i need to look for something else.');
INSERT INTO humainedev.persona_frustrations VALUES (74, 'I went to the shop but it’s too crowded and I cannot find what I need.');
INSERT INTO humainedev.persona_frustrations VALUES (74, 'None of the assistants is helping me find the coat.');
INSERT INTO humainedev.persona_frustrations VALUES (74, 'I keep looking but there are only a few models of coats. I cannot find the one I like :(.');
INSERT INTO humainedev.persona_frustrations VALUES (74, 'I see this shirt that is too beautiful not to take it. I know I didn’t come for the shirt but I am afraid It will get sold until I come here again.');
INSERT INTO humainedev.persona_frustrations VALUES (75, 'All the items on sale are from the last collection?');
INSERT INTO humainedev.persona_frustrations VALUES (75, 'The items are all too extra and not for everyday use.');
INSERT INTO humainedev.persona_frustrations VALUES (75, 'The shop is too crowded. I cannot find what I need.');
INSERT INTO humainedev.persona_frustrations VALUES (75, 'I like this shirt but I have one similar at home. I don’t know if I should buy it or not.');
INSERT INTO humainedev.persona_frustrations VALUES (76, 'The shop is too crowded and I cannot find what I am looking for.');
INSERT INTO humainedev.persona_frustrations VALUES (76, 'None of the assistants are helping me find the item.');
INSERT INTO humainedev.persona_frustrations VALUES (76, 'They brought the item but it is not in my size. I really wanted to buy it.');
INSERT INTO humainedev.persona_frustrations VALUES (77, 'I keep looking but all the items seem like they’re in the last trend. This shop hasn’t updated their items to the latest fashion trends.');
INSERT INTO humainedev.persona_frustrations VALUES (77, 'Most of the items are for vacations only and not for work days. I am not allowed to wear no sleeves shirts at work.');
INSERT INTO humainedev.persona_frustrations VALUES (77, 'I like this item but it doesn’t look high quality. I don’t know if I will be able to use it more than once.');
INSERT INTO humainedev.persona_frustrations VALUES (78, 'I keep looking but everything I saved from their page is out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (78, 'The fabrics of this item is good but the color is pastel. I want to grab attention with my style.');
INSERT INTO humainedev.persona_frustrations VALUES (78, 'I like this item but i’ve seen it on a lot of influencers lately. I don’t want to look like everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (79, 'The shop is too crowded and I don’t know if I will manage to finish shopping in time.');
INSERT INTO humainedev.persona_frustrations VALUES (79, 'All the items are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (79, 'I like this shirt’s color but it is quite tight in the waist and that style doesn’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (79, 'This item is pretty but I don’t have any clothes to match it with.');
INSERT INTO humainedev.persona_frustrations VALUES (80, 'The website is quite complicated. I just saved an item but it got removed?');
INSERT INTO humainedev.persona_frustrations VALUES (80, 'I like this item but I don’t like it on the model.');
INSERT INTO humainedev.persona_frustrations VALUES (80, 'This item is nice but it’s too ordinary and I want to look more elegant.');
INSERT INTO humainedev.persona_frustrations VALUES (80, 'What if the clothes don’t fit me? Can I return them? How long am I allowed until I return them?');
INSERT INTO humainedev.persona_frustrations VALUES (81, 'This shirt has a perfect color for my style but it is not cotton and I will not be able to wear it.');
INSERT INTO humainedev.persona_frustrations VALUES (81, 'The shoes in this collection don’t look comfortable at all. How can I be active all day when my feet are not comfortable?');
INSERT INTO humainedev.persona_frustrations VALUES (81, 'I don’t see anything in this shop that has anything that reflects my personality.');
INSERT INTO humainedev.persona_frustrations VALUES (81, 'This shop is so crowded. I don’t have space to try things on.');
INSERT INTO humainedev.persona_frustrations VALUES (82, 'The suits I like are too colorful and attention grabbing. I don’t want to be center of attention at the event.');
INSERT INTO humainedev.persona_frustrations VALUES (82, 'There are no plain suits that I can use in different events.');
INSERT INTO humainedev.persona_frustrations VALUES (82, 'The items on sale have bad quality and I don’t want to even try anything on from that section. However, the new collection only has a few pieces and there are not enough suits for me to try on.');
INSERT INTO humainedev.persona_frustrations VALUES (83, 'The shop is too crowded, no one has time to help me decide what to buy.');
INSERT INTO humainedev.persona_frustrations VALUES (83, 'The items that have good quality are too casual, and the fancy clothes are not comfortable at all… I don’t know what to do because I don’t want to buy items that I only wear once.');
INSERT INTO humainedev.persona_frustrations VALUES (84, 'There are no plain one colored shirts in this shop? Only multicolored shirts that I can only use once or twice in the office.');
INSERT INTO humainedev.persona_frustrations VALUES (84, 'The items in the shop don’t look comfortable at all. I cannot work all day and be active if I am not wearing comfortable clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (84, 'I like this jacket for x conference, but I’m not sure if I will still like it after 6 months.');
INSERT INTO humainedev.persona_frustrations VALUES (85, 'They haven’t bought anything new from the last time I went to the shop.');
INSERT INTO humainedev.persona_frustrations VALUES (85, 'I like this shirt but I saw other people trying it on and I don’t want to have the same clothes as everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (85, 'I always buy at this shop but they never offer any discount.');
INSERT INTO humainedev.persona_frustrations VALUES (86, 'The shop is too crowded and messy. I cannot find items from my size.');
INSERT INTO humainedev.persona_frustrations VALUES (86, 'I like this shirt but it’s too casual for the event. This is not an everyday thing.');
INSERT INTO humainedev.persona_frustrations VALUES (86, 'The items I see here are mostly bad quality clothes that people only wear once and never again.');
INSERT INTO humainedev.persona_frustrations VALUES (87, 'The only items left in the shop are the ones on sale, and the new collection isn’t here yet.');
INSERT INTO humainedev.persona_frustrations VALUES (87, 'I like this shirt but size xs is the only size left in stock and that’s too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (87, 'There are no products I could wear to the Thanksgiving dinner. Everything is either too casual or too fancy for me.');
INSERT INTO humainedev.persona_frustrations VALUES (88, 'All the dresses in this shop are casual. They only have a few elegant dresses.');
INSERT INTO humainedev.persona_frustrations VALUES (88, 'I don’t want to look simple. I need to look perfect because I will be giving an important speech to the event.');
INSERT INTO humainedev.persona_frustrations VALUES (88, 'All the ones I like are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (88, 'I see an item that my friend has, but it’s out of stock!!');
INSERT INTO humainedev.persona_frustrations VALUES (89, 'This item looked very good on her Instagram post but it doesn’t seem good quality at all live.');
INSERT INTO humainedev.persona_frustrations VALUES (89, 'All her items are much smaller sizes, there are not enough items that are my size.');
INSERT INTO humainedev.persona_frustrations VALUES (89, 'She is only selling a few items, there are not enough clothes to explore.');
INSERT INTO humainedev.persona_frustrations VALUES (89, 'The only items that look good on me are suitable for fancy events only. I wanted to buy items for daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (90, 'I like this shirt but I don’t know if I should buy it because I already have 2 similar ones.');
INSERT INTO humainedev.persona_frustrations VALUES (90, 'There’s no sale section in the shop, everything is from the new collection only.');
INSERT INTO humainedev.persona_frustrations VALUES (90, 'Most of the clothes are bad quality and I don’t want to spend money on them if I cannot wear them more than once.');
INSERT INTO humainedev.persona_frustrations VALUES (90, 'My friend just bought a couple of clothes and wants to go home but I want to explore more items.');
INSERT INTO humainedev.persona_frustrations VALUES (91, 'I went to x shop because I saw that x influencer usually buys there, but I don’t see anything that fits my style.');
INSERT INTO humainedev.persona_frustrations VALUES (91, 'All the items are all plain or super complicated. I want to have clothes that are both casual and attention grabbing.');
INSERT INTO humainedev.persona_frustrations VALUES (92, 'The new collection has mostly casual clothes with pastel colors.');
INSERT INTO humainedev.persona_frustrations VALUES (92, 'I like this item but it says that it’s the last one in stock, so I believe a lot of people bought it already.');
INSERT INTO humainedev.persona_frustrations VALUES (92, 'There are only a few items in this collection. I guess they still haven’t brought all the items to the shop.');
INSERT INTO humainedev.persona_frustrations VALUES (93, 'The clothes look good on the models but I don’t know if they will look good on me as well.');
INSERT INTO humainedev.persona_frustrations VALUES (93, 'I like this shirt but it doesn’t look like its quality is good. I cannot buy something I won’t use more than once.');
INSERT INTO humainedev.persona_frustrations VALUES (93, 'I cannot find the items my friend suggested me to buy. I wrote the code of those items but it seems that those items are no longer available.');
INSERT INTO humainedev.persona_frustrations VALUES (93, 'The website is taking quite long to load. I don’t have all day.');
INSERT INTO humainedev.persona_frustrations VALUES (94, 'The items on sale look so uncomfortable. I don’t understand how people wear them!!');
INSERT INTO humainedev.persona_frustrations VALUES (94, 'I like this item but it is not my preferred style. I don’t know if I will wear it often.');
INSERT INTO humainedev.persona_frustrations VALUES (94, 'The website is a little complicated. How do I put this item in the bag?');
INSERT INTO humainedev.persona_frustrations VALUES (94, 'There are not enough items that fit my style. They’re either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (95, 'There are a lot of people in this shop. I need to get to the office ASAP to prepare my speech.');
INSERT INTO humainedev.persona_frustrations VALUES (95, 'I like this item but its color is too bright and I will not be able to wear it often.');
INSERT INTO humainedev.persona_frustrations VALUES (95, 'The voucher only offers discount on some of the items?? How do I know if what I like has discount as well?');
INSERT INTO humainedev.persona_frustrations VALUES (96, 'The suits I see are all for events and gatherings, there is nothing suitable for everyday use.');
INSERT INTO humainedev.persona_frustrations VALUES (96, 'There are a lot of people in the shop. The assistants don’t have time to help me pick the clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (96, 'I like this item but they don’t have it in my size. We are checking in with other branches to see if they have it.');
INSERT INTO humainedev.persona_frustrations VALUES (97, 'The shop is too crowded I cannot find the items I need to buy.');
INSERT INTO humainedev.persona_frustrations VALUES (97, 'The clothes look too ordinary and not unique. I don’t want to just buy items and leave them in the closet.');
INSERT INTO humainedev.persona_frustrations VALUES (97, 'All the items I like are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (98, 'There are not enough items in the shop. I don’t know if I will find all the products I need.');
INSERT INTO humainedev.persona_frustrations VALUES (98, 'We are not allowed to try on the products. How will I know that they look good on me if I don’t try them on?');
INSERT INTO humainedev.persona_frustrations VALUES (98, 'I like this product but it’s the only item I found so far and I don’t know if I will get any gifts if i don’t buy much products.');
INSERT INTO humainedev.persona_frustrations VALUES (99, 'I like this item but it’s from the most bought list today.');
INSERT INTO humainedev.persona_frustrations VALUES (99, 'Most of the clothes are casual, ordinary clothes. I want to have clothes that make me stand out from the crowd.');
INSERT INTO humainedev.persona_frustrations VALUES (99, 'There are not enough products in the new collection, so I don’t have many choices to spend my voucher for.');
INSERT INTO humainedev.persona_frustrations VALUES (100, 'I keep looking but all the fancy items look uncomfortable.');
INSERT INTO humainedev.persona_frustrations VALUES (100, 'I don’t want to stay frozen in a dress just to look good. I want to be comfortable.');
INSERT INTO humainedev.persona_frustrations VALUES (100, 'I hope I can find some items in less than 1 hour. I have my volunteer in an hour.');
INSERT INTO humainedev.persona_frustrations VALUES (101, 'All items are colorful and with complicated designs. I won’t be able to use them on a daily basis.');
INSERT INTO humainedev.persona_frustrations VALUES (101, 'I like the color of this shirt but it isn’t cotton and I cannot wear other fabrics than cotton.');
INSERT INTO humainedev.persona_frustrations VALUES (101, 'I didn’t buy enough products to fill the $100. I don’t want to buy something I don’t need just because I want to win the free item.');
INSERT INTO humainedev.persona_frustrations VALUES (102, 'The shop is so crowded  with people. I cannot find any of the clothes I liked.');
INSERT INTO humainedev.persona_frustrations VALUES (102, 'I like this item but its material isn’t that good. I don’t know if it’s worth buying.');
INSERT INTO humainedev.persona_frustrations VALUES (102, 'All the items on sale are either too big or too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (102, 'None of the items I saved on the online website isn’t in stock!');
INSERT INTO humainedev.persona_frustrations VALUES (103, 'They brought similar items but not that specific one.');
INSERT INTO humainedev.persona_frustrations VALUES (103, 'They found the exact same blazer in the online shop but it only had larger sizes.');
INSERT INTO humainedev.persona_frustrations VALUES (103, 'I don’t like any other blazer because they’re all with different colors and I won’t know how to match them.');
INSERT INTO humainedev.persona_frustrations VALUES (104, 'Most of the shoes in sale are sneakers and not fit for a birthday party.');
INSERT INTO humainedev.persona_frustrations VALUES (104, 'The place is too crowded and I cannot find anything.');
INSERT INTO humainedev.persona_frustrations VALUES (104, 'All the shoes I like are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (104, 'I like this pair but they are too high and I am not sure I will be able to wear them for the whole night.');
INSERT INTO humainedev.persona_frustrations VALUES (105, 'There are a lot of people waiting in line before me.');
INSERT INTO humainedev.persona_frustrations VALUES (105, 'I like this shirt’s design but I wish it had different colors. I don’t know with what to match it.');
INSERT INTO humainedev.persona_frustrations VALUES (105, 'I like a lot of items but I am afraid I won’t use them because I already have similar items at home.');
INSERT INTO humainedev.persona_frustrations VALUES (105, 'All the colorful clothes I like are in small sizes only?? They are not my size.');
INSERT INTO humainedev.persona_frustrations VALUES (105, 'I saved a jacket in the website but it’s out of stock now.');
INSERT INTO humainedev.persona_frustrations VALUES (106, 'I need to buy some body products because my bottles are almost finished.');
INSERT INTO humainedev.persona_frustrations VALUES (106, 'I always buy items from limited edition in shops so I am going to check x shop and see what they have for this summer.');
INSERT INTO humainedev.persona_frustrations VALUES (106, 'I like this shop because it constantly brings new unique brands and I get to buy items that not a lot of people have.');
INSERT INTO humainedev.persona_frustrations VALUES (107, 'I keep looking but I cannot find the limited edition section.');
INSERT INTO humainedev.persona_frustrations VALUES (107, 'I found the perfumes but this one isn’t there.');
INSERT INTO humainedev.persona_frustrations VALUES (107, 'I wanted to buy this perfume for my mom as well because she has asked for it as well, so now I won’t be able to buy it for her.');
INSERT INTO humainedev.persona_frustrations VALUES (107, 'We just spoke to the manager of the other branch in the city, and they said that they don’t sell this perfume anymore.');
INSERT INTO humainedev.persona_frustrations VALUES (108, 'Only some of the products are on sale, not all of them. I thought every product is on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (108, 'I am not sure how to use the website. How to know which size is good for me?');
INSERT INTO humainedev.persona_frustrations VALUES (108, 'The website is taking too long to load.');
INSERT INTO humainedev.persona_frustrations VALUES (108, 'No item from the basics collection is on sale. I saved a lot of products in that collection and I thought they would go on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (109, 'I keep on looking but the clothes are either too simple or too complex. I want to have a combination of that.');
INSERT INTO humainedev.persona_frustrations VALUES (109, 'I don’t want to buy something and only wear it once. I want the clothes to be high quality and useful for different events.');
INSERT INTO humainedev.persona_frustrations VALUES (109, 'I like this shirt but it’s a pastel color. I would like to try a more colorful shirt, that is eye grabbing,');
INSERT INTO humainedev.persona_frustrations VALUES (110, 'There are only a few shops that have elegant shoes, most of them have sneakers and boots only.');
INSERT INTO humainedev.persona_frustrations VALUES (110, 'I like this pair of shoes but I don’t feel comfortable in them. I cannot buy them if I cannot wear them all night.');
INSERT INTO humainedev.persona_frustrations VALUES (110, 'I found a pair of shoes that seem comfortable and high quality, but they are not in stock.');
INSERT INTO humainedev.persona_frustrations VALUES (111, 'No one is free to come shopping with me.');
INSERT INTO humainedev.persona_frustrations VALUES (111, 'All the items I like are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (111, 'I like this item but it is not something I’d wear everyday.');
INSERT INTO humainedev.persona_frustrations VALUES (111, 'There are not enough products for me to explore for, and I want to buy everything I need here, so I can contribute on the cause.');
INSERT INTO humainedev.persona_frustrations VALUES (112, 'I like this item but I don’t like how it looks on the model. I don’t know if it will look good on me as well?The internet is too slow. I need to get find something faster. I like a lot of dresses but they don’t look comfortable at all. I don’t know how I will stay all night wearing any of them.');
INSERT INTO humainedev.persona_frustrations VALUES (113, 'The suits are all too colorful. I need clothes with more ordinary colors for daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (113, 'Most of the items in the new collection are casual items such as jeans and shirts. I cannot buy any of them because I need items for work.');
INSERT INTO humainedev.persona_frustrations VALUES (113, 'I like this item but they don’t have it in my size, and they don’t do custom made blazers.');
INSERT INTO humainedev.persona_frustrations VALUES (114, 'All the items I like are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (114, 'X item looked so good on x influencer but it doesn’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (114, 'I like this item but it doesn’t look good quality.');
INSERT INTO humainedev.persona_frustrations VALUES (115, 'I cannot find the items I saved from the website, and no one is helping me find them in the shop. The assistants are all helping other people but not me.');
INSERT INTO humainedev.persona_frustrations VALUES (115, 'Although this brand is quite famous, only a few of the items are good quality.');
INSERT INTO humainedev.persona_frustrations VALUES (115, 'There are not enough products to explore.');
INSERT INTO humainedev.persona_frustrations VALUES (116, 'I like this item but 3 people before me went to try it on. I don’t want to buy stuff everyone is buying.');
INSERT INTO humainedev.persona_frustrations VALUES (116, 'Most of the clothes seem ordinary clothes everyone wears.');
INSERT INTO humainedev.persona_frustrations VALUES (116, 'This item looked nice but it’s the last one in my size and a girl wants to buy it. I feel bad not letting her get it.');
INSERT INTO humainedev.persona_frustrations VALUES (117, 'I keep looking but nothing fits my style.');
INSERT INTO humainedev.persona_frustrations VALUES (117, 'The shop is too crowded. I cannot find anything that I need.');
INSERT INTO humainedev.persona_frustrations VALUES (117, 'I like this item but it doesn’t look high quality and I don’t want to buy something that I can only wear once or twice.');
INSERT INTO humainedev.persona_frustrations VALUES (118, 'All the items I am trying on are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (118, 'I like the way this item looked on x influencer, but I don’t like the way it looks on me.');
INSERT INTO humainedev.persona_frustrations VALUES (118, 'The shop is too crowded and there is a very big waiting line for the fitting room. I cannot try anything on.');
INSERT INTO humainedev.persona_frustrations VALUES (119, 'I keep looking but I cannot find anything that matches my style.');
INSERT INTO humainedev.persona_frustrations VALUES (119, 'I don’t know why the clothes of this new collection seem uncomfortable and I would not be able to use them every day.');
INSERT INTO humainedev.persona_frustrations VALUES (119, 'Most of the items in the shop are for big events and gatherings. Only a few items are casual and for daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (119, 'The shop is too crowded and people keep passing me on to pay before me.');
INSERT INTO humainedev.persona_frustrations VALUES (120, 'I keep looking but nothing seems fit for this event.');
INSERT INTO humainedev.persona_frustrations VALUES (120, 'I like x item but it is more for everyday use and not for parties. I don’t know if it’s too simple for the event.');
INSERT INTO humainedev.persona_frustrations VALUES (120, 'I always buy at this shop but they never send any discounts.');
INSERT INTO humainedev.persona_frustrations VALUES (121, 'I like this item but I’ve seen this design on a lot of people lately. I don’t want to look just like everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (121, 'All the items on this collection are too ordinary and items that a lot of people wear.');
INSERT INTO humainedev.persona_frustrations VALUES (121, 'I like this item but they don’t have it in my size. I wish I could leave my info and get informed if they bring my size.');
INSERT INTO humainedev.persona_frustrations VALUES (122, 'The website is taking too long to load. I don’t have time to wait for it.');
INSERT INTO humainedev.persona_frustrations VALUES (122, 'This item looks perfect on the model but I don’t know if it will look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (122, 'Most of the items are casual and for daily use. I want to look more elegant for the event.');
INSERT INTO humainedev.persona_frustrations VALUES (123, 'All the items I like are out of stock.');
INSERT INTO humainedev.persona_frustrations VALUES (123, 'X influencer has this item but it doesn’t match with my style at all. I don’t want to buy items and never wear them.');
INSERT INTO humainedev.persona_frustrations VALUES (123, 'The shop only bought a few items. I hope they bring the others next week.');
INSERT INTO humainedev.persona_frustrations VALUES (124, 'I keep looking but nothing looks good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (124, 'All the items in the shop are either too casual or too complicated that I cannot wear for my party.');
INSERT INTO humainedev.persona_frustrations VALUES (124, 'I like this item but they don’t have it in my size. I wish they could ask the other stores in the city and see if they have my size. This way I wouldn’t have to go on my own.');
INSERT INTO humainedev.persona_frustrations VALUES (125, 'The shop is very crowded. I cannot find what I need from this mess.');
INSERT INTO humainedev.persona_frustrations VALUES (125, 'I saved some items from the website and I cannot find them. I asked the assistants to help but they were too busy with other people and weren’t able to help me.');
INSERT INTO humainedev.persona_frustrations VALUES (125, 'All the items in the new collection are either too casual or too extra. I need to buy clothes for everyday use.');
INSERT INTO humainedev.persona_frustrations VALUES (125, 'There are not enough items that I like in this shop and I need to buy all the needed items for the new season.');
INSERT INTO humainedev.persona_frustrations VALUES (126, 'I like this item but it got sold as soon as they put it on sale.');
INSERT INTO humainedev.persona_frustrations VALUES (126, 'I like how this item looks on the influencer but it isn’t my style at all and I don’t know if I will wear it at all.');
INSERT INTO humainedev.persona_frustrations VALUES (126, 'This item is a very beautiful and unique vintage item, but I’ve seen it on a lot of influencers lately. I don’t want to be just like everyone else.');
INSERT INTO humainedev.persona_frustrations VALUES (126, 'Most of the items are too small for me.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'I keep looking but nothing looks good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'This item looked much better in picture. I don’t like it in real life.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'This item looked very good on my sister but it doesn’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'All the clothes in this collection seem for events and gatherings only. I cannot wear them on a daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'I keep looking but nothing looks good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'This item looked much better in picture. I don’t like it in real life.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'This item looked very good on my sister but it doesn’t look good on me.');
INSERT INTO humainedev.persona_frustrations VALUES (127, 'All the clothes in this collection seem for events and gatherings only. I cannot wear them on a daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (128, 'This item looked very good on the influencer but it doesn’t look good on me at all.');
INSERT INTO humainedev.persona_frustrations VALUES (128, 'All the items I saw on x influencer are out of stock??');
INSERT INTO humainedev.persona_frustrations VALUES (128, 'I keep asking for an assistant to help me find x item but no one is available right now.');
INSERT INTO humainedev.persona_frustrations VALUES (128, 'All the items in this new collection are very extravagant and not for daily use.');
INSERT INTO humainedev.persona_frustrations VALUES (129, 'The clothes are too fancy and not for everyday use. I don’t want to spend money on items I won’t wear.');
INSERT INTO humainedev.persona_frustrations VALUES (129, 'I like this item but it doesn’t look good quality. I will only get to wear it once or twice and throw it away.');
INSERT INTO humainedev.persona_frustrations VALUES (129, 'All the items I like are either out of stock, or they don’t have my size at all.');
INSERT INTO humainedev.persona_frustrations VALUES (130, 'The boots I like are all out of stock. I cannot wait for them to bring them back because of the rain.');
INSERT INTO humainedev.persona_frustrations VALUES (130, 'There are a lot of people waiting in line before me.');
INSERT INTO humainedev.persona_frustrations VALUES (130, 'The shop still hasn’t brought the complete autumn collection. Most of the items are for summer and I don’t want to buy clothes for next summer.');
INSERT INTO humainedev.persona_frustrations VALUES (131, 'I keep looking but I cannot find the item.');
INSERT INTO humainedev.persona_frustrations VALUES (131, 'The sales assistants are so rude. They are not helping me find the item!');
INSERT INTO humainedev.persona_frustrations VALUES (131, 'I like this shirt but it’s from the past season’s collection. I really like it and I want to buy it although I have a lot of similar shirts in my closet.');
INSERT INTO humainedev.persona_frustrations VALUES (131, 'Why haven’t they brought the new collection yet? It’s already summer!');
INSERT INTO humainedev.persona_frustrations VALUES (132, 'All the clothes in this shop are for elegant gatherings. Don’t they have anything for daily use?');
INSERT INTO humainedev.persona_frustrations VALUES (132, 'I like this item’s color but the quality is awful. I cannot buy something this expensive and never wear it!');
INSERT INTO humainedev.persona_frustrations VALUES (132, 'The shop is too crowded. I cannot find any of the items I saved from the shop.');
INSERT INTO humainedev.persona_frustrations VALUES (133, 'There was too much traffic on my way to the shop. I am afraid there’s nothing left in stock for me.');
INSERT INTO humainedev.persona_frustrations VALUES (133, 'I like this item because it’s casual and comfortable but they don’t have it in my size.');
INSERT INTO humainedev.persona_frustrations VALUES (133, 'The shop is too crowded. I cannot find the items that I saved in the online website.');
INSERT INTO humainedev.persona_frustrations VALUES (133, 'I love this item but it’s not on sale? I cannot buy something for this price!');
INSERT INTO humainedev.persona_frustrations VALUES (134, 'I keep looking but all the sweaters are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (134, 'There are only a few sweaters in this shop.');
INSERT INTO humainedev.persona_frustrations VALUES (134, 'I like this one but I don’t know if I should buy it.. what if I buy a better one at the other shop?');
INSERT INTO humainedev.persona_frustrations VALUES (134, 'I saved x item in the website but none of the assistants are helping me find it.');
INSERT INTO humainedev.persona_frustrations VALUES (135, 'I keep looking but I cannot find anything that fits my style.');
INSERT INTO humainedev.persona_frustrations VALUES (135, 'I am trying to find x item that I saved in the website, but all the sales assistants are busy and cannot help me find it.');
INSERT INTO humainedev.persona_frustrations VALUES (135, 'All the items I like are either too small or too big for me.');
INSERT INTO humainedev.persona_frustrations VALUES (136, 'All the bags in x shop are for daily use only. I need something more extravagant.');
INSERT INTO humainedev.persona_frustrations VALUES (136, 'I saw this purse in the website but they don’t have it in stock.');
INSERT INTO humainedev.persona_frustrations VALUES (136, 'The purses in the other shops all have very hot colors that only match with 1-2 dresses of mine, and I don’t want to buy a purse to only use it once.');
INSERT INTO humainedev.persona_frustrations VALUES (137, 'I always buy at x shop and they never give me any discounts.');
INSERT INTO humainedev.persona_frustrations VALUES (137, 'The items from the new collection are all too futuristic and fancy. I cannot wear them daily.');
INSERT INTO humainedev.persona_frustrations VALUES (137, 'I like the fabrics of this shirt but they only have it in orange. I don’t have any trousers that match with this color.');
INSERT INTO humainedev.persona_frustrations VALUES (138, 'There are only a few shops that have elegant clothes for me to buy.');
INSERT INTO humainedev.persona_frustrations VALUES (138, 'I like this item but it is not my size and they don’t do customized clothes.');
INSERT INTO humainedev.persona_frustrations VALUES (138, 'I like the color of this item but the quality doesn’t seem good. I am afraid I will buy it and only use it for this event.');

-----------------------------------------------------------------------------------------


INSERT INTO humainedev.persona_goals VALUES (1, 'I need new items for daily use, and I want to buy suits with different designs and colors for the Summer.');
INSERT INTO humainedev.persona_goals VALUES (1, 'My wardrobe consists of unique and high quality clothes, that’s why I only look for popular brands that I can trust when making a purchase.');
INSERT INTO humainedev.persona_goals VALUES (1, 'I have some designers I usually buy my clothes from, but I want to check out a new designer that has recently become famous with the suits he’s designing.');
INSERT INTO humainedev.persona_goals VALUES (2, 'I need some fresh items for a gala night organized by the organization I volunteer at.');
INSERT INTO humainedev.persona_goals VALUES (2, 'I want to buy items that I can use for other events as well, because this organization holds several events per year.');
INSERT INTO humainedev.persona_goals VALUES (2, 'I am looking to buy something more elegant than usual and my friends recommended x shop.');
INSERT INTO humainedev.persona_goals VALUES (2, 'I don’t care about the price of the item, I just want to feel comfortable and beautiful in it.');
INSERT INTO humainedev.persona_goals VALUES (3, 'I need to buy some fresh items for my new job;');
INSERT INTO humainedev.persona_goals VALUES (3, 'I want to change my style and buy items that I wouldn’t normally buy;');
INSERT INTO humainedev.persona_goals VALUES (3, 'I have 1 hour to buy them before my meeting for a business idea.');
INSERT INTO humainedev.persona_goals VALUES (4, 'I am getting a present for my friend’s birthday party;I want to buy something that can be used for different purposes; I don’t want to buy a casual gift that anyone can get for them, I want to  to find a unique gift that shows my care for this person;');
INSERT INTO humainedev.persona_goals VALUES (5, 'I am attending a concert next week and I want to buy a shirt that is unique but also useful for other different events such as gala nights or work dinners.

There’s a newly opened shop in the city mall that I want to check out. If it doesn’t have anything that suits me I will check out the other stores in the mall.');
INSERT INTO humainedev.persona_goals VALUES (6, 'I need to buy some new items that are comfortable for everyday use;
I usually buy in x shop because it is my friend’s shop and I like their taste in choosing items.

The shop is having a huge sale and I want to check out those items;If I find anything good on sale, I would love to buy some items for my kids as well.
I have 30 minutes before I pick up my kids from school.');
INSERT INTO humainedev.persona_goals VALUES (7, 'I need to buy some new items from x shop; I usually buy at Y shop, but last time I went there I didn’t find anything for my style.');
INSERT INTO humainedev.persona_goals VALUES (7, 'I hope I can find something that has good quality in this shop. I’ve never bought anything here but I will buy 1-2 items and if I am pleased with the fabric quality, I will probably buy here again.');
INSERT INTO humainedev.persona_goals VALUES (7, 'I usually buy soft color items as they fit better with my looks.');
INSERT INTO humainedev.persona_goals VALUES (8, 'I want to go to x shop as my friends told me they have a lot of new items;');
INSERT INTO humainedev.persona_goals VALUES (8, 'Clothes in this shop aren’t much of my style, but I would like to check if I can find something since everyone is buying there;');
INSERT INTO humainedev.persona_goals VALUES (8, 'I want to find items that are trendy but that I can use for several ocassions.');
INSERT INTO humainedev.persona_goals VALUES (9, 'I want to buy something for my new job as a sales representative for a 6-Figure company, and I want to look sharp and professional.');
INSERT INTO humainedev.persona_goals VALUES (9, 'I am looking to change my style a little bit and experiment with different colors.');
INSERT INTO humainedev.persona_goals VALUES (9, 'I want to buy clothes that are unique and make me stand out.');
INSERT INTO humainedev.persona_goals VALUES (9, 'My best friend recommended x shop, and said that they have the best quality of clothes.');
INSERT INTO humainedev.persona_goals VALUES (10, 'I want to always look cool and unique.');
INSERT INTO humainedev.persona_goals VALUES (10, 'X shop’s new collection is out and I want to check it out and see if It has anything I could wear on a daily basis.');
INSERT INTO humainedev.persona_goals VALUES (10, 'Everyone is buying from this shop these days and I want to see if I can find myself in their items as well.');
INSERT INTO humainedev.persona_goals VALUES (11, 'I want to change my style from elegant to cool and casual;');
INSERT INTO humainedev.persona_goals VALUES (11, 'I have changed the industry of work and I want to suit it, although I have never had this style before;');
INSERT INTO humainedev.persona_goals VALUES (11, 'I found a shop in an influencer’s Instagram story, and I want to buy items from that shop since it is popular;');
INSERT INTO humainedev.persona_goals VALUES (12, 'I want some unique clothes for daily use;');
INSERT INTO humainedev.persona_goals VALUES (12, 'I want my clothes to represent my personality; an independent person who loves challenge and adventure.');
INSERT INTO humainedev.persona_goals VALUES (12, 'My favorite shop is having a sale and I want to dig in and find some items that no one has;');
INSERT INTO humainedev.persona_goals VALUES (13, 'I am attending my first office party, and I want to make an impression to everyone in the office.');
INSERT INTO humainedev.persona_goals VALUES (13, 'My friend told me that X shop has a sale and I want to check out their products.');
INSERT INTO humainedev.persona_goals VALUES (13, 'I hope I can find items that are simple and elegant.');
INSERT INTO humainedev.persona_goals VALUES (14, 'I need to buy some new items for my new job;');
INSERT INTO humainedev.persona_goals VALUES (14, 'I want to change my style from casual to more elegant;');
INSERT INTO humainedev.persona_goals VALUES (14, 'I don’t know in which shop to buy, so I need to ask x friend to let me know about a shop I could check out.');
INSERT INTO humainedev.persona_goals VALUES (14, 'It’s my best friend’s birthday next week and I would like to buy something for them as well.');
INSERT INTO humainedev.persona_goals VALUES (15, 'I need to buy some comfortable shoes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (15, 'I don’t have a favorite shop, but I saw that x shop is quite popular and a lot of people buy there, so I will check it out and see if I can find anything for my style.');
INSERT INTO humainedev.persona_goals VALUES (15, 'I usually buy in stores but because I only have 30 minutes until my guests come for dinner,  I have to buy what I need online.');
INSERT INTO humainedev.persona_goals VALUES (16, 'I need to buy new clothes for this autumn;');
INSERT INTO humainedev.persona_goals VALUES (16, 'I want to find clothes that are in style;');
INSERT INTO humainedev.persona_goals VALUES (16, 'My friend bought some very good items in x shop so I will go to x shop and see if they have similar items for my size.');
INSERT INTO humainedev.persona_goals VALUES (17, 'I want to buy something for an event I am attending;');
INSERT INTO humainedev.persona_goals VALUES (17, 'Quality is very important for me;');
INSERT INTO humainedev.persona_goals VALUES (17, 'My friend has a clothing store and I will go to his salon and try on some dresses;');
INSERT INTO humainedev.persona_goals VALUES (17, 'I hope he can help me find something that makes me look unique and reflects my personality.');
INSERT INTO humainedev.persona_goals VALUES (18, 'I want to change my style and buy items for the new season;');
INSERT INTO humainedev.persona_goals VALUES (18, 'I follow different influencers on Instagram and a lot of them recommended x shop for clothes.');
INSERT INTO humainedev.persona_goals VALUES (18, 'I really love their style and I believe I will find items in that shop that look good on me as well.');
INSERT INTO humainedev.persona_goals VALUES (19, 'I am going on a hike and I want to buy some comfortable items but also ones that look good on me.');
INSERT INTO humainedev.persona_goals VALUES (19, 'My friends who frequently hike buy their clothes from x shop so I want to check out their products.');
INSERT INTO humainedev.persona_goals VALUES (19, 'I am willing to spend hours searching until I find the items I want to wear.');
INSERT INTO humainedev.persona_goals VALUES (20, 'I need to buy some fresh shirts for daily use.');
INSERT INTO humainedev.persona_goals VALUES (20, 'I would like to check out x shop. I saw that they have a 5 star rating and I want to check out their clothes.');
INSERT INTO humainedev.persona_goals VALUES (20, 'I want to find items that nobody has. I’m willing to search all day in order to find unique items.');
INSERT INTO humainedev.persona_goals VALUES (21, 'I need casual clothes for my job that are comfortable and good quality;');
INSERT INTO humainedev.persona_goals VALUES (21, 'There’s a newly opened shop in the mall, and I want to check it out. My friend already bought a couple of items there.');
INSERT INTO humainedev.persona_goals VALUES (21, 'I hope I can find something that looks good on me.');
INSERT INTO humainedev.persona_goals VALUES (22, 'I need to buy some new items because I am moving to another country.');
INSERT INTO humainedev.persona_goals VALUES (22, 'I want to buy clothes from my favorite shop. I hope they have comfortable and good quality clothes.');
INSERT INTO humainedev.persona_goals VALUES (22, 'I am taking my sister with me and she will help me choose some items that look good on me.');
INSERT INTO humainedev.persona_goals VALUES (23, 'I need clothes for every day use.');
INSERT INTO humainedev.persona_goals VALUES (23, 'My favorite shop has a sale and I want to check out their products.');
INSERT INTO humainedev.persona_goals VALUES (23, 'My friend bought a lot of items from the sale and I hope I can find something that looks good on me.');
INSERT INTO humainedev.persona_goals VALUES (23, 'They have the option to order online but I like to go to the store myself and pick up my favorite items.');
INSERT INTO humainedev.persona_goals VALUES (24, 'I need to buy new clothes for my new job.');
INSERT INTO humainedev.persona_goals VALUES (24, 'My favorite model has a clothing store and I want to check it out.');
INSERT INTO humainedev.persona_goals VALUES (24, 'I hope I can find something that looks good on me as they look on her.');
INSERT INTO humainedev.persona_goals VALUES (24, 'Online shopping is very in right now and I want to see if I can learn to order online.');
INSERT INTO humainedev.persona_goals VALUES (25, 'The election campaign is near and I want to update my wardrobe as I will be on TV.');
INSERT INTO humainedev.persona_goals VALUES (25, 'I want to buy high quality clothes that fit me best and represent my personality.');
INSERT INTO humainedev.persona_goals VALUES (25, 'I want to buy the clothes from my favorite shop. They always have items I like.');
INSERT INTO humainedev.persona_goals VALUES (25, 'I want to have unique suits that no one wears.');
INSERT INTO humainedev.persona_goals VALUES (26, 'The new collection of my favorite shop is out and i want to check their clothes.');
INSERT INTO humainedev.persona_goals VALUES (26, 'I want to buy clothes for different occasions.');
INSERT INTO humainedev.persona_goals VALUES (26, 'I am willing to search all day until i find what I want.');
INSERT INTO humainedev.persona_goals VALUES (26, 'I want the clothes to have good quality and be unique.');
INSERT INTO humainedev.persona_goals VALUES (27, 'I have a date night and I want to buy something that looks very good on me.');
INSERT INTO humainedev.persona_goals VALUES (27, 'I usually buy at x shop because they’re clothes fit my style.');
INSERT INTO humainedev.persona_goals VALUES (27, 'I hope I can find some good items that make me look unique and make me stand out.');
INSERT INTO humainedev.persona_goals VALUES (28, 'It’s the sale season and I want to buy some cool clothes.');
INSERT INTO humainedev.persona_goals VALUES (28, 'I hope x shop has a sale. I always buy in that shop.');
INSERT INTO humainedev.persona_goals VALUES (28, 'I would like to buy things that are exactly my style as the other styles don’t look good on me.');
INSERT INTO humainedev.persona_goals VALUES (29, 'I want to always look cool and young.');
INSERT INTO humainedev.persona_goals VALUES (29, 'My clothes are one of a kind clothes that you rarely see anyone else wearing.');
INSERT INTO humainedev.persona_goals VALUES (29, 'I always buy from x shop as they have good quality, unique clothes.');
INSERT INTO humainedev.persona_goals VALUES (29, 'They recently added the option to order online, so I am going to try out the website. =');
INSERT INTO humainedev.persona_goals VALUES (30, 'I need some new items for my daily use.');
INSERT INTO humainedev.persona_goals VALUES (30, 'I will go to my favorite shop to check their new collection.');
INSERT INTO humainedev.persona_goals VALUES (30, 'I am looking for something comfortable but unique that suits my style only.');
INSERT INTO humainedev.persona_goals VALUES (30, 'I only have 1 hour before I go pick up the cake I bought for my husband’s birthday.');
INSERT INTO humainedev.persona_goals VALUES (30, 'I would like to see if they have any shirts that I could get for my husband’s birthday');
INSERT INTO humainedev.persona_goals VALUES (31, 'I need clothes for a birthday party');
INSERT INTO humainedev.persona_goals VALUES (31, 'I am not able to order online from my favorite shop.');
INSERT INTO humainedev.persona_goals VALUES (31, 'I only have 30 minutes to find a dress before I drive my kids to school.');
INSERT INTO humainedev.persona_goals VALUES (32, 'I want to buy new clothes for the new season.');
INSERT INTO humainedev.persona_goals VALUES (32, 'My favorite designer has now his online shop and I want to order online');
INSERT INTO humainedev.persona_goals VALUES (32, 'I want to buy high quality clothes that are unique');
INSERT INTO humainedev.persona_goals VALUES (32, 'I will search for hours until I find what I need.');
INSERT INTO humainedev.persona_goals VALUES (33, 'I want to buy clothes from a shop my friend recommended.');
INSERT INTO humainedev.persona_goals VALUES (33, 'I want to update my wardrobe and buy items for the new season.');
INSERT INTO humainedev.persona_goals VALUES (33, 'I hope I can find clothes like my friend’s. She is always well-dressed.');
INSERT INTO humainedev.persona_goals VALUES (33, 'I am willing to search all day until I find what I am looking for.');
INSERT INTO humainedev.persona_goals VALUES (34, 'I am celebrating my birthday next week and I want to buy something fancy.');
INSERT INTO humainedev.persona_goals VALUES (34, 'We are going to a restaurant that is frequented by famous people and although I’ve never eaten there, I want to give it a try.');
INSERT INTO humainedev.persona_goals VALUES (34, 'I am willing to look for hours until I find the item I want to buy.');
INSERT INTO humainedev.persona_goals VALUES (35, 'I just got a raise and I want to celebrate it by buying some new clothes.');
INSERT INTO humainedev.persona_goals VALUES (35, 'I want to look super cool and young.');
INSERT INTO humainedev.persona_goals VALUES (35, 'My favorite influencer buys in this shop so I want to check out their products.');
INSERT INTO humainedev.persona_goals VALUES (35, 'I want to find hidden gems and I am willing to look for hours until I find something unique for me.');
INSERT INTO humainedev.persona_goals VALUES (36, 'I want to always look stylish and unique.');
INSERT INTO humainedev.persona_goals VALUES (36, 'I constantly update my wardrobe with one of a kind clothes.');
INSERT INTO humainedev.persona_goals VALUES (36, 'I hate looking like other people, that’s why I always shop vintage.');
INSERT INTO humainedev.persona_goals VALUES (36, 'My friends know this about me, so they always suggest vintage shops for me to explore.');
INSERT INTO humainedev.persona_goals VALUES (37, 'I need white shirts for daily use.');
INSERT INTO humainedev.persona_goals VALUES (37, 'A new shop has opened in the mall and everyone’s buying items there.');
INSERT INTO humainedev.persona_goals VALUES (37, 'I need comfortable and good quality clothes that I can use every day.');
INSERT INTO humainedev.persona_goals VALUES (38, 'My favorite shop has now the online shopping option, and although I don’t usually buy clothes online, I want to try and find some things I like.');
INSERT INTO humainedev.persona_goals VALUES (38, 'I have a wedding to attend to, and I need to look fancy.');
INSERT INTO humainedev.persona_goals VALUES (38, 'I have to find something for my daughter as well.');
INSERT INTO humainedev.persona_goals VALUES (39, 'I want to buy perfect clothes for the new season.');
INSERT INTO humainedev.persona_goals VALUES (39, 'A friend of mine who I’ve lended some money to has opened a shop and I wanted to check out their clothes.');
INSERT INTO humainedev.persona_goals VALUES (39, 'I am looking for comfortable every day clothes, but ones that look unique and beautiful.');
INSERT INTO humainedev.persona_goals VALUES (39, 'I will search for hours until I find what I need.');
INSERT INTO humainedev.persona_goals VALUES (40, 'I need new a dress for an event I will be attending next week.');
INSERT INTO humainedev.persona_goals VALUES (40, 'X shop has an offer of giving a perfume if you buy for more than $50, and I would like to go.');
INSERT INTO humainedev.persona_goals VALUES (40, 'I need to look fancy and stand out from the crowd.');
INSERT INTO humainedev.persona_goals VALUES (41, 'My birthday is coming soon and my best friend has offered me to take me to a shop and buy whatever I want for my birthday.');
INSERT INTO humainedev.persona_goals VALUES (41, 'I am so glad she offered this, because I always buy beautiful and unique gifts for her.');
INSERT INTO humainedev.persona_goals VALUES (41, 'I am looking to buy something fancy that I can wear for my birthday party.');
INSERT INTO humainedev.persona_goals VALUES (41, 'I usually buy at x shop as they always have products I like.');
INSERT INTO humainedev.persona_goals VALUES (42, 'I want to change my style to look more chic.');
INSERT INTO humainedev.persona_goals VALUES (42, 'I want to try out x shop because they give 10% discount for their regulars and I usually buy there.');
INSERT INTO humainedev.persona_goals VALUES (42, 'I am so excited to see what clothes will look good on me.');
INSERT INTO humainedev.persona_goals VALUES (43, 'I have an office party this upcoming Friday and I want to look extravagant for it.');
INSERT INTO humainedev.persona_goals VALUES (43, 'I like items that are unique and that no one has.');
INSERT INTO humainedev.persona_goals VALUES (43, 'X shop always gives me 10% discounts and I want to go check out their new collection.');
INSERT INTO humainedev.persona_goals VALUES (44, 'I need some fresh items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (44, 'X shop has a sale of 30% on their “basic” collection and I want to buy in that store.');
INSERT INTO humainedev.persona_goals VALUES (44, 'I am using my lunch break to shop for these items as I have to go to the animal shelter after work.');
INSERT INTO humainedev.persona_goals VALUES (45, 'I need to buy new items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (45, 'I would to buy some plain white shirts that are comfortable.');
INSERT INTO humainedev.persona_goals VALUES (45, 'My favorite store is having a huge sale and I want to check if they have the shirts I am looking for.');
INSERT INTO humainedev.persona_goals VALUES (45, 'This shop has the online shopping option as well, but I don’t like to order online as I want to try on the clothes that I will buy.');
INSERT INTO humainedev.persona_goals VALUES (46, 'I want to buy some items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (46, 'X shop has everything 80% off and this sale only lasts this weekend.');
INSERT INTO humainedev.persona_goals VALUES (46, 'This shop always has good items that’s why I believe I will find great items that fit my style.');
INSERT INTO humainedev.persona_goals VALUES (47, 'I want to buy the new x item.');
INSERT INTO humainedev.persona_goals VALUES (47, 'Only x shop has it and they only brought 5-6 pieces, so I want to check if I can get one.');
INSERT INTO humainedev.persona_goals VALUES (47, 'I hope I can find the product and buy it');
INSERT INTO humainedev.persona_goals VALUES (48, 'I need to buy new items for a work trip.');
INSERT INTO humainedev.persona_goals VALUES (48, 'I want to look good but also be comfortable because we will be attending lectures during the day.');
INSERT INTO humainedev.persona_goals VALUES (48, 'My favorite shop has a sale on the beach collection, and I want to check if they have anything that looks good on me.');
INSERT INTO humainedev.persona_goals VALUES (49, 'I want to buy some items for my birthday party.');
INSERT INTO humainedev.persona_goals VALUES (49, 'I want to look extravagant for the event so I need the items to be unique and beautiful.');
INSERT INTO humainedev.persona_goals VALUES (49, 'X shop is having a sale this month and I want to check out their items.');
INSERT INTO humainedev.persona_goals VALUES (50, 'I want to buy some items that look good on me.');
INSERT INTO humainedev.persona_goals VALUES (50, 'I want to stand out from the crowd and always look cool .');
INSERT INTO humainedev.persona_goals VALUES (50, 'I am willing to search all day to find the products I like.');
INSERT INTO humainedev.persona_goals VALUES (50, 'I always shop vintage because they have one of a kind clothes, and a shop I buy items from has now a sale and I want to check out the products in sale.');
INSERT INTO humainedev.persona_goals VALUES (51, 'I need some items for daily use.');
INSERT INTO humainedev.persona_goals VALUES (51, 'I need to have comfortable and good quality clothes because I am active all day.');
INSERT INTO humainedev.persona_goals VALUES (51, 'I only have 30 min before I take my kids from school.');
INSERT INTO humainedev.persona_goals VALUES (51, 'My favorite shop is having a huge style so I  want to check their online website and see if I can find something I love.');
INSERT INTO humainedev.persona_goals VALUES (52, 'I’ve been looking to buy products from x brand for a while now and x shop finally brought their items so I want to go check the shop out and see what products they brought.');
INSERT INTO humainedev.persona_goals VALUES (52, 'I am very conscious about buying clothes and I don’t like to have items that I never wear in my wardrobe.');
INSERT INTO humainedev.persona_goals VALUES (52, 'Their products are a little higher but they’re worth buying because they have very good quality.');
INSERT INTO humainedev.persona_goals VALUES (53, 'My favorite shop has brought the new collection and it’s perfect!!!');
INSERT INTO humainedev.persona_goals VALUES (53, 'They brought suits for the autumn season and I just have to buy them.');
INSERT INTO humainedev.persona_goals VALUES (53, 'I hope they give me any discounts because I’m a regular costumer.');
INSERT INTO humainedev.persona_goals VALUES (54, 'I need a new dress for my birthday party next week.');
INSERT INTO humainedev.persona_goals VALUES (54, 'I want to look so extra, so the dress has to be perfect.');
INSERT INTO humainedev.persona_goals VALUES (54, 'I’ve organized everything to the last detail and the last thing I need is my dress.');
INSERT INTO humainedev.persona_goals VALUES (54, 'I want to buy from my favorite shop and I am willing to search all day to find my ideal dress.');
INSERT INTO humainedev.persona_goals VALUES (55, 'I want to buy new items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (55, 'I want clothes that I can wear daily, but I need them to fit me perfectly.');
INSERT INTO humainedev.persona_goals VALUES (55, 'I am not in need of anything specific because I already did my Fall haul, I just saw that a shop I usually buy from is now offering the online shopping and II want to try it out.');
INSERT INTO humainedev.persona_goals VALUES (56, 'I need to buy new shoes and I need them to look perfect on me.');
INSERT INTO humainedev.persona_goals VALUES (56, 'I need to look elegant so I need to find the right shoes for me.');
INSERT INTO humainedev.persona_goals VALUES (56, 'I have saved the whole weekend just to shop for shoes, so I better find them.');
INSERT INTO humainedev.persona_goals VALUES (57, 'My favorite shop is having a huge sale and I want to check out their clothes.');
INSERT INTO humainedev.persona_goals VALUES (57, 'I want to look perfect every day so I need to find clothes that fit me perfectly.');
INSERT INTO humainedev.persona_goals VALUES (57, 'I want to find items that no one has so I will stand out of the crowd.');
INSERT INTO humainedev.persona_goals VALUES (58, 'I need new items for the Fall season.');
INSERT INTO humainedev.persona_goals VALUES (58, 'I want to go to my favorite shop and see their new collection items.');
INSERT INTO humainedev.persona_goals VALUES (58, 'I don’t have more than 1 hour so even if I just save the items that I like it’s enough.');
INSERT INTO humainedev.persona_goals VALUES (59, 'I want to buy some items for my new job.');
INSERT INTO humainedev.persona_goals VALUES (59, 'I need to look professional and stylish so I need the clothes to be perfect.');
INSERT INTO humainedev.persona_goals VALUES (59, 'I hope I can find anything to buy because this job is very important to me and I want to be well-dressed up for it.');
INSERT INTO humainedev.persona_goals VALUES (59, 'I am looking at x shop because my friends told me that it has casual everyday clothes.');
INSERT INTO humainedev.persona_goals VALUES (60, 'I want to always look well dressed and elegant.');
INSERT INTO humainedev.persona_goals VALUES (60, 'Good quality is important for me because I want my clothes to look good and expensive.');
INSERT INTO humainedev.persona_goals VALUES (60, 'My boss always buys at x shop and I love his style.');
INSERT INTO humainedev.persona_goals VALUES (60, 'I want to check out their items and see if I can find anything for myself.');
INSERT INTO humainedev.persona_goals VALUES (61, 'I am at x shop and I see that they have a new collection. I have to check out their items!!');
INSERT INTO humainedev.persona_goals VALUES (61, 'My friend usually clothes there and I want to check If I can find something that looks good on me as well.');
INSERT INTO humainedev.persona_goals VALUES (61, 'I wasn’t planning to buy anything, but now that I’m here I at least want to check what they have.');
INSERT INTO humainedev.persona_goals VALUES (62, 'I want to buy something for my birthday party next week.');
INSERT INTO humainedev.persona_goals VALUES (62, 'I want to look fancy so the items need to be perfect.');
INSERT INTO humainedev.persona_goals VALUES (62, 'My favorite influencer just opened her own line and I want to see if it has items I like.');
INSERT INTO humainedev.persona_goals VALUES (62, 'I love her style and I am sure I will like the items she brought to the shop.');
INSERT INTO humainedev.persona_goals VALUES (63, 'I want to buy new clothes in x shop that has just opened in town.');
INSERT INTO humainedev.persona_goals VALUES (63, 'My favorite actress is the main model of the shop and everything looks very good on her.');
INSERT INTO humainedev.persona_goals VALUES (63, 'I am looking to buy clothes for different occasions. I’m willing to look all day to find clothes that are for me.');
INSERT INTO humainedev.persona_goals VALUES (64, 'I am going to x shop because my friend wants to buy some clothes, and I am looking some things for myself as well.');
INSERT INTO humainedev.persona_goals VALUES (64, 'We’re going to his favorite shop because he said that they have a new collection.');
INSERT INTO humainedev.persona_goals VALUES (64, 'I hope I can find something unique, because although I really love the items in this shop, a lot of people buy and I don’t want to wear the clothes everyone is wearing.');
INSERT INTO humainedev.persona_goals VALUES (65, 'X shop has made a campaign that for every sale, 50% of the earnings will go to the Autism Speaks Organization.');
INSERT INTO humainedev.persona_goals VALUES (65, 'I love how that shop often makes these campaigns because I really believe we need to help these organizations socially and financially.');
INSERT INTO humainedev.persona_goals VALUES (65, 'I want to buy comfortable items for this season, but I don’t really know what I want to buy as I haven’t planned to buy anything beforeh');
INSERT INTO humainedev.persona_goals VALUES (66, 'I want to buy some items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (66, 'x shop has a sale and I want to check their items.');
INSERT INTO humainedev.persona_goals VALUES (66, 'I want to buy casual office clothes that are comfortable and good quality.');
INSERT INTO humainedev.persona_goals VALUES (66, 'I want to feel comfortable in my clothes and not wear extravagant styles.');
INSERT INTO humainedev.persona_goals VALUES (66, 'I don’t usually shop online but my daughter said that it is very easy and fast so I want to give it a try.');
INSERT INTO humainedev.persona_goals VALUES (67, 'I need to buy items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (67, 'I need the items to be high quality because I need to wear them more than once.');
INSERT INTO humainedev.persona_goals VALUES (67, 'Because I am active all day I need to always be comfortable, that’s why I am looking for some items that are both unique and comfortable.');
INSERT INTO humainedev.persona_goals VALUES (67, 'I am going to x shop because they brought the new collection and I want to see what items they brought.');
INSERT INTO humainedev.persona_goals VALUES (67, 'I always shop in that shop because their assistants are very helpful and kind.');
INSERT INTO humainedev.persona_goals VALUES (68, 'I need to buy some sport items for gym.');
INSERT INTO humainedev.persona_goals VALUES (68, 'The items need to be comfortable and specifically made for gym.');
INSERT INTO humainedev.persona_goals VALUES (68, 'I want to buy to x shop because they just made an offer where we get a thermos bottle for every purchase of a combination of training sneakers and sports shirt.');
INSERT INTO humainedev.persona_goals VALUES (68, 'I hope I can find both of them so I can win the thermos bottle.');
INSERT INTO humainedev.persona_goals VALUES (69, 'I want to buy clothes for an event I am attending.');
INSERT INTO humainedev.persona_goals VALUES (69, 'I want to look fancy and grab attention so I need to wear perfect clothes.');
INSERT INTO humainedev.persona_goals VALUES (69, 'I always shop for fancy items at x shop because they make you feel like home and are always helpful for picking items.');
INSERT INTO humainedev.persona_goals VALUES (69, 'Although this event is for one night only, I would like to be able to use the items for other events in the future as well, so I need the items to be high quality.');
INSERT INTO humainedev.persona_goals VALUES (70, 'I want to change my style from younger to look more mature.');
INSERT INTO humainedev.persona_goals VALUES (70, 'I am willing to try out clothes all day until I find what looks good on me.');
INSERT INTO humainedev.persona_goals VALUES (70, 'I will go to x shop because they have buyer assistants who help us match clothes and help us pick what to buy.');
INSERT INTO humainedev.persona_goals VALUES (71, 'I always buy one of a kind items.');
INSERT INTO humainedev.persona_goals VALUES (71, 'I like to be comfortable but also grab attention.');
INSERT INTO humainedev.persona_goals VALUES (71, 'I always explore new shops because I get to find rare items no one has.');
INSERT INTO humainedev.persona_goals VALUES (72, 'I want to buy some items that are comfortable and look good on me.');
INSERT INTO humainedev.persona_goals VALUES (72, 'My sister says that I wear dark colors only so I want to buy some colorful items.');
INSERT INTO humainedev.persona_goals VALUES (72, 'X shop always gives free body lotions if you buy items there, so I want to buy items there.');
INSERT INTO humainedev.persona_goals VALUES (73, 'My favorite shop is giving out 50$ vouchers for regular costumers, so I want to go and see what I can buy with it.');
INSERT INTO humainedev.persona_goals VALUES (73, 'I hope I can find a blazer because I’ve been planning to buy one for so long.');
INSERT INTO humainedev.persona_goals VALUES (73, 'I need to buy it for everyday use so It needs to be high quality and a pastel color to match with everything.');
INSERT INTO humainedev.persona_goals VALUES (74, 'I need to buy a coat for the winter season.');
INSERT INTO humainedev.persona_goals VALUES (74, 'I’ve been looking for the perfect coat for a long time now, and I cannot find it.');
INSERT INTO humainedev.persona_goals VALUES (74, 'Last year x shop had a coat that looked good on me, but it was too small for me and I couldn’t buy it.');
INSERT INTO humainedev.persona_goals VALUES (74, 'I hope they brought it back!!');
INSERT INTO humainedev.persona_goals VALUES (74, 'I will look at other shops to find a similar coat.');
INSERT INTO humainedev.persona_goals VALUES (75, 'I need to buy some items for my new job.');
INSERT INTO humainedev.persona_goals VALUES (75, 'My favorite shop is having a weekend sale and I want to buy what I need while they’re on sale.');
INSERT INTO humainedev.persona_goals VALUES (75, 'I am willing to look all day to find what I need.');
INSERT INTO humainedev.persona_goals VALUES (76, 'I want to buy x item.');
INSERT INTO humainedev.persona_goals VALUES (76, 'I’ve wanted it for so long but I didn’t know if they would bring it back.');
INSERT INTO humainedev.persona_goals VALUES (76, 'I hope they have it in my size!!');
INSERT INTO humainedev.persona_goals VALUES (77, 'I need some items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (77, 'I need the items to be comfortable because I am active during the most part of the day.');
INSERT INTO humainedev.persona_goals VALUES (77, 'Quality is very important for me.');
INSERT INTO humainedev.persona_goals VALUES (77, 'x shop has a 50% sale on the summer collection and I want to go check their items before they run out of stock.');
INSERT INTO humainedev.persona_goals VALUES (78, 'I want to always unique and well-dressed.');
INSERT INTO humainedev.persona_goals VALUES (78, 'My favorite just added a few items on sale for today only and I need to check what they added because they rarely put anything on sale.');
INSERT INTO humainedev.persona_goals VALUES (78, 'I hope I can find some items for a 3 day training I am attending at x country.');
INSERT INTO humainedev.persona_goals VALUES (78, 'The clothes need to be high quality and to represent my style.');
INSERT INTO humainedev.persona_goals VALUES (79, 'I need clothes for a family gathering.');
INSERT INTO humainedev.persona_goals VALUES (79, 'I usually buy at x shop because they have high quality clothes, and usually only have a limited number of items. This makes me feel like not a lot of people buy what I wear and it makes me look more unique.');
INSERT INTO humainedev.persona_goals VALUES (79, 'I hope I can find some items that suit me perfectly. I don’t want to buy items I won’t wear.');
INSERT INTO humainedev.persona_goals VALUES (79, 'I only have 1 hour before I go get my kids from school so I need to find something to deliver quickly. I cannot spend all my day doing shopping.');
INSERT INTO humainedev.persona_goals VALUES (80, 'I need to buy some items for the Thanksgiving dinner I am organizing.');
INSERT INTO humainedev.persona_goals VALUES (80, 'I want the items to be elegant but also comfortable because I will be active all the time.');
INSERT INTO humainedev.persona_goals VALUES (80, 'I don’t usually shop online but my favorite is having a sale this weekend and it can only be applied if we buy from the online shop, so I want to try it.');
INSERT INTO humainedev.persona_goals VALUES (81, 'I need clothes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (81, 'My clothes need to have high quality because I want to look rich in them.');
INSERT INTO humainedev.persona_goals VALUES (81, 'I need to always be comfortable in my clothes because I’m at my office all day every day.');
INSERT INTO humainedev.persona_goals VALUES (81, 'I only have 2 hours to buy what I need so I am going to my favorite shop to see what I can buy.');
INSERT INTO humainedev.persona_goals VALUES (82, 'I need items for an event I will be attending.');
INSERT INTO humainedev.persona_goals VALUES (82, 'I need the clothes to be high quality so I can use them on different events as well.');
INSERT INTO humainedev.persona_goals VALUES (82, 'I usually buy at x shop because they always have something for me, but now the clothes are on sale and I don’t know if anything good is left.');
INSERT INTO humainedev.persona_goals VALUES (83, 'I have a birthday party and I need to buy some items.');
INSERT INTO humainedev.persona_goals VALUES (83, 'I want to be able to use these items for other birthdays as well.');
INSERT INTO humainedev.persona_goals VALUES (83, 'The quality is very important to me.');
INSERT INTO humainedev.persona_goals VALUES (83, 'I usually buy at x shop because they always treat me super nice and it’s always a pleasure to shop here.');
INSERT INTO humainedev.persona_goals VALUES (84, 'I need to buy clothes for my new office job, but I don’t want to change my style much because this style I have matches me perfectly.');
INSERT INTO humainedev.persona_goals VALUES (84, 'I would like to buy shirts that I can match with different colored trousers, and I want the shirts to be comfortable for daily use.');
INSERT INTO humainedev.persona_goals VALUES (84, 'If I could find some clothes that I could use for conferences as well, that would be perfect.');
INSERT INTO humainedev.persona_goals VALUES (85, 'I want to buy some items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (85, 'I am going to the shop I usually go in because they’re the only shop that brings one of a kind clothes.');
INSERT INTO humainedev.persona_goals VALUES (85, 'I want to always look unique and stand out from the crowd.');
INSERT INTO humainedev.persona_goals VALUES (85, 'Quality is very important, so the clothes need to be high quality in order for me to wear them as I wish.');
INSERT INTO humainedev.persona_goals VALUES (86, 'My son’s birthday party is coming next week and I need to buy something to wear for the event.');
INSERT INTO humainedev.persona_goals VALUES (86, 'I will be active the whole time of the party so I need to have something comfortable to wear.');
INSERT INTO humainedev.persona_goals VALUES (86, 'I don’t want to change my style for the event, just something a bit more fancy than usual will do.');
INSERT INTO humainedev.persona_goals VALUES (86, 'I only have 30 minutes before I go pick my kids from school.');
INSERT INTO humainedev.persona_goals VALUES (87, 'I need to buy items for Thanksgiving.');
INSERT INTO humainedev.persona_goals VALUES (87, 'I want to look good but I also don’t want to go out of my style.');
INSERT INTO humainedev.persona_goals VALUES (87, 'I want to check out x shop because they always have items for my style.');
INSERT INTO humainedev.persona_goals VALUES (88, 'I need a dress for a work event.');
INSERT INTO humainedev.persona_goals VALUES (88, 'The dress needs to be elegant and I want to look extravagant for the event.');
INSERT INTO humainedev.persona_goals VALUES (88, 'I don’t know where to buy it because I just moved to this town, but my friend who is quite stylish buys in x shop and I want to buy my items there.');
INSERT INTO humainedev.persona_goals VALUES (88, 'I hope I can find something that looks good on me.');
INSERT INTO humainedev.persona_goals VALUES (89, 'I want to always look good and trendy.');
INSERT INTO humainedev.persona_goals VALUES (89, 'I love fashion so I follow a lot of influencers on Instagram.');
INSERT INTO humainedev.persona_goals VALUES (89, 'X influencer is selling her clothes to the flea market and I have to at least see what clothes she has.');
INSERT INTO humainedev.persona_goals VALUES (89, 'I hope I can find something beautiful and comfortable.');
INSERT INTO humainedev.persona_goals VALUES (90, 'My friend needs to buy some items so I am going with them to help them choose what they want.');
INSERT INTO humainedev.persona_goals VALUES (90, 'I don’t need anything for myself, but if I find something I like, I will buy it.');
INSERT INTO humainedev.persona_goals VALUES (90, 'I love shopping and I can’t wait to see what x shop has.');
INSERT INTO humainedev.persona_goals VALUES (90, 'I hope they have items on sale so I don’t spend much money buying clothes.');
INSERT INTO humainedev.persona_goals VALUES (91, 'I want to change my style and I want to look cooler.');
INSERT INTO humainedev.persona_goals VALUES (91, 'I see a lot of influencers on Instagram and I want to look like them.');
INSERT INTO humainedev.persona_goals VALUES (91, 'I want my clothes to look chic but also comfortable because I need to wear them on a daily basis.');
INSERT INTO humainedev.persona_goals VALUES (92, 'I need clothes for my everyday use, but I want them to be unique clothes that not many people have.');
INSERT INTO humainedev.persona_goals VALUES (92, 'I don’t want to look like everyone else, so I want to find hidden gems.');
INSERT INTO humainedev.persona_goals VALUES (92, 'I am willing to search all day to find items I like.');
INSERT INTO humainedev.persona_goals VALUES (93, 'I need new clothes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (93, 'I need the clothes to be comfortable and plain so I can use for different outfits and events.');
INSERT INTO humainedev.persona_goals VALUES (93, 'My friend always buys at x shop and they recommended me to buy some items there.');
INSERT INTO humainedev.persona_goals VALUES (93, 'I only have 30 min before I run to get a shirt from the laundry so I am checking the online website of the shop.');
INSERT INTO humainedev.persona_goals VALUES (94, 'My friend bought some very good items on x shop and I want to check if they have something for me as well.');
INSERT INTO humainedev.persona_goals VALUES (94, 'That friend always looks good and well dressed, and yesterday they said that they bought a lot of items on this season’s sale.');
INSERT INTO humainedev.persona_goals VALUES (94, 'I hope I can buy some items that are high quality and comfortable.');
INSERT INTO humainedev.persona_goals VALUES (94, 'I don’t usually buy online but the friend said that there are only a few items left in store.');
INSERT INTO humainedev.persona_goals VALUES (95, 'I need to buy something elegant for a work event.');
INSERT INTO humainedev.persona_goals VALUES (95, 'I want the item to be fancy and grab attention because I will be the center of attention as I am now the head of department.');
INSERT INTO humainedev.persona_goals VALUES (95, 'I want to buy from x shop because I have a voucher and I need to spend it before the month ends.');
INSERT INTO humainedev.persona_goals VALUES (95, 'I hope I can find something that I can wear for multiple events.');
INSERT INTO humainedev.persona_goals VALUES (96, 'I need clothes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (96, 'Clothes need to be casual and comfortable, but I also want to look stylish on them .');
INSERT INTO humainedev.persona_goals VALUES (96, 'I usually buy at x shop because I love how they treat me when I go there. The assistants always bring me coffee and they help me pick my suits.');
INSERT INTO humainedev.persona_goals VALUES (96, 'It’s the summer so the have a lot of costumers but I hope there aren’t a lot of people today.');
INSERT INTO humainedev.persona_goals VALUES (97, 'I need to buy some items for my summer vacation.');
INSERT INTO humainedev.persona_goals VALUES (97, 'I am going to the Maldives and I want to look perfect.');
INSERT INTO humainedev.persona_goals VALUES (97, 'I would like the items to be practical for my other summer vacations, so I need the clothes to be exactly my style.');
INSERT INTO humainedev.persona_goals VALUES (97, 'My favorite shop has an offer that if you buy for more than $100, you get 50% off on the next item.');
INSERT INTO humainedev.persona_goals VALUES (98, 'I need to update my makeup because I’ve ran out of my products.');
INSERT INTO humainedev.persona_goals VALUES (98, 'I will need quite a lot of items because I’ve waited until all my products are finished.');
INSERT INTO humainedev.persona_goals VALUES (98, 'X shop always gives free products after buying a couple of items, and I want to buy my makeup there.');
INSERT INTO humainedev.persona_goals VALUES (98, 'I am willing to search all day to buy all the items I need.');
INSERT INTO humainedev.persona_goals VALUES (99, 'I need to buy some items that suit my style perfectly.');
INSERT INTO humainedev.persona_goals VALUES (99, 'I love to find hidden gems to buy and wear because I don’t want to look like everyone else.');
INSERT INTO humainedev.persona_goals VALUES (99, 'I explore a lot of shops in my city, so I can find one of a kind clothes no one has.');
INSERT INTO humainedev.persona_goals VALUES (99, 'I am going to this shop because I have a voucher and I waited for their new collection so I can find unique items.');
INSERT INTO humainedev.persona_goals VALUES (100, 'I want to buy something fancy for a family gathering.');
INSERT INTO humainedev.persona_goals VALUES (100, 'I would like to be comfortable in it, but also look good.');
INSERT INTO humainedev.persona_goals VALUES (100, 'I always buy at x shop because they’re super helpful. They usually write blogs with tips to be stylish, or “how to match x item” etc. I love how friendly they are.');
INSERT INTO humainedev.persona_goals VALUES (101, 'I need some plain shirts and trousers for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (101, 'I need the clothes to be high quality because I will be wearing them daily.');
INSERT INTO humainedev.persona_goals VALUES (101, 'X shop is offering different rewards if we buy for more than $100. They have x item that I really like and I would like to get it since I already have to buy the items I need.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I need clothes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I waited so long for the Christmas sale, so I will be going to x shop to see what they have on sale.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I saved a lot of items from their online website, so I hope I can find them in the store.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I need the items to have good quality and look expensive.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I need clothes for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I waited so long for the Christmas sale, so I will be going to x shop to see what they have on sale.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I saved a lot of items from their online website, so I hope I can find them in the store.');
INSERT INTO humainedev.persona_goals VALUES (102, 'I need the items to have good quality and look expensive.');
INSERT INTO humainedev.persona_goals VALUES (103, 'I want to buy this blazer from x brand, but when I went last month they didn’t have it on my size. So, I am going to see if they brought the item with the new collection.');
INSERT INTO humainedev.persona_goals VALUES (103, 'I hope I can find the blazer in my size!');
INSERT INTO humainedev.persona_goals VALUES (103, 'The reason I liked it that much was because it was super comfortable and it had a color I can match with everything.');
INSERT INTO humainedev.persona_goals VALUES (103, 'I am willing to check in every branch they have in the city just to find my size.');
INSERT INTO humainedev.persona_goals VALUES (104, 'I want to buy some shoes for my birthday party.');
INSERT INTO humainedev.persona_goals VALUES (104, 'I want to look perfect and grab attention everywhere I go.');
INSERT INTO humainedev.persona_goals VALUES (104, 'I would like the shoes to be comfortable so I can stay in them all night without having to take them off.');
INSERT INTO humainedev.persona_goals VALUES (104, 'X shop is having a big sale this weekend so I want to check what they have.');
INSERT INTO humainedev.persona_goals VALUES (105, 'I need to buy everyday clothes that are comfortable but attention grabbing as well.');
INSERT INTO humainedev.persona_goals VALUES (105, 'My favorite has a 50% sale until tomorrow so I need to go ASAP and buy what I need.');
INSERT INTO humainedev.persona_goals VALUES (105, 'I hope I can find items for my size.');
INSERT INTO humainedev.persona_goals VALUES (106, 'The shop is too crowded and I cannot find the limited edition products.');
INSERT INTO humainedev.persona_goals VALUES (106, 'They only brought limited edition perfumes, but not body lotions or makeup products.');
INSERT INTO humainedev.persona_goals VALUES (106, 'I like this bottle but they only have the 20ml one. I need to buy a bigger perfume so I can use it for a longer period.');
INSERT INTO humainedev.persona_goals VALUES (107, 'I am searching for a perfume that I bought on x shop, but it was limited edition and I don’t know if they still have it.');
INSERT INTO humainedev.persona_goals VALUES (107, 'I have been looking for that perfume for months now, but no shop is bringing it.');
INSERT INTO humainedev.persona_goals VALUES (107, 'The perfume has a soft flowery smell, and I first bought it when the brand just got started.');
INSERT INTO humainedev.persona_goals VALUES (107, 'I am going to look to all their branches to find it.');
INSERT INTO humainedev.persona_goals VALUES (108, 'I want to buy some plain shirts for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (108, 'I need the shirts to be comfortable and good quality.');
INSERT INTO humainedev.persona_goals VALUES (108, 'X shop is having a 50% discount if you buy from the website and I want to check out what they have.');
INSERT INTO humainedev.persona_goals VALUES (108, 'I only have 30 min to order what I need because I need to cook dinner for my children.');
INSERT INTO humainedev.persona_goals VALUES (109, 'I need to buy items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (109, 'I want the items to be attention grabbing and beautiful so that I catch the eye of people.');
INSERT INTO humainedev.persona_goals VALUES (109, 'Because they are clothes for daily use, I need them to be comfortable and to have colors I can match with different other items.');
INSERT INTO humainedev.persona_goals VALUES (110, 'I need to buy some elegant shoes for an event I am organizing.');
INSERT INTO humainedev.persona_goals VALUES (110, 'The shoes need to be comfortable because I will be active all night and I need to be able to do that without any problems.');
INSERT INTO humainedev.persona_goals VALUES (110, 'I don’t have any favorite shop so I am going to check all the shops in the mall to find what I need.');
INSERT INTO humainedev.persona_goals VALUES (110, 'I am willing to search all day until I find some items I can use.');
INSERT INTO humainedev.persona_goals VALUES (111, 'I need to buy some items from x shop.');
INSERT INTO humainedev.persona_goals VALUES (111, 'They have a campaign where 50% of sales goes to the campaign for offering scholarships for people of rural areas, that don’t have the financial matters to attend college.');
INSERT INTO humainedev.persona_goals VALUES (111, 'I need items for the new season so I will use this to do something good for the community.');
INSERT INTO humainedev.persona_goals VALUES (111, 'The items need to be high quality because I will use them all season.');
INSERT INTO humainedev.persona_goals VALUES (111, 'I am asking my friends to come with me and help with shopping.');
INSERT INTO humainedev.persona_goals VALUES (112, 'I need to buy some items for a birthday party I am attending.I only have 30 min before I drive my kids to school so I need to find what I need quick.I want to look chic and feel comfortable as well.');
INSERT INTO humainedev.persona_goals VALUES (113, 'I want to always look well dressed and elegant.');
INSERT INTO humainedev.persona_goals VALUES (113, 'My new job requires suits and ties only so I want to buy some items today.');
INSERT INTO humainedev.persona_goals VALUES (113, 'I usually buy from x shop because they have a lot of products for men and I have many products to pick.');
INSERT INTO humainedev.persona_goals VALUES (114, 'I need items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (114, 'I want to buy x items because they are so in right now and I want to try them on.');
INSERT INTO humainedev.persona_goals VALUES (114, 'They need to be comfortable because I need to be active all day.');
INSERT INTO humainedev.persona_goals VALUES (114, 'I hope that style looks good on me because I don’t usually dress like that.');
INSERT INTO humainedev.persona_goals VALUES (115, 'I want to change my style from younger to more mature.');
INSERT INTO humainedev.persona_goals VALUES (115, 'I want to go to x shop because they have become very popular in the recent years because of their quality of clothes.');
INSERT INTO humainedev.persona_goals VALUES (115, 'Quality is important for me.');
INSERT INTO humainedev.persona_goals VALUES (115, 'I am willing to search all day to find items that look good on me.');
INSERT INTO humainedev.persona_goals VALUES (116, 'I need to buy items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (116, 'I always want to look well dressed and unique.');
INSERT INTO humainedev.persona_goals VALUES (116, 'X shop has just opened and I want to see what items they have. My friend told me that they have one of a kind items that I would like, so I want to check them out.');
INSERT INTO humainedev.persona_goals VALUES (116, 'I hope I can find items that not everyone wears.');
INSERT INTO humainedev.persona_goals VALUES (117, 'I want to buy something for a family birthday party I am attending.');
INSERT INTO humainedev.persona_goals VALUES (117, 'I want the items to be casual and comfortable because I want to be able to help them with organizing the party.');
INSERT INTO humainedev.persona_goals VALUES (117, 'I want to buy from x brand although they are quite expensive. But I want to have high quality clothes.');
INSERT INTO humainedev.persona_goals VALUES (118, 'I need new items for the new season.');
INSERT INTO humainedev.persona_goals VALUES (118, 'I want to buy clothes that are in the trend and what the influencers are wearing.');
INSERT INTO humainedev.persona_goals VALUES (118, 'I hope I can find items that look good on me but are also in at the moment.');
INSERT INTO humainedev.persona_goals VALUES (118, 'I am going to x shop because they are a very big brand and all the Instagram influencers I like buy there.');
INSERT INTO humainedev.persona_goals VALUES (119, 'I need some items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (119, 'The clothes need to be high quality and comfortable because I am active all day.');
INSERT INTO humainedev.persona_goals VALUES (119, 'I usually buy from x shop because they use very good fabrics and most of my clothes are from this brand.');
INSERT INTO humainedev.persona_goals VALUES (120, 'I need a shirt for a work event.');
INSERT INTO humainedev.persona_goals VALUES (120, 'I need to find something that is casual but also grabs attention.');
INSERT INTO humainedev.persona_goals VALUES (120, 'I am going to x shop. It’s my favorite one.');
INSERT INTO humainedev.persona_goals VALUES (121, 'I need to buy items for everyday use.I want to go to x shop because they always bring one of a kind items that I love.I want to always look casual but unique. I don’t want to grab attention when I am walking, but I want to always be well dressed.');
INSERT INTO humainedev.persona_goals VALUES (122, 'I want to buy some items for a gathering I am organizing for my family.');
INSERT INTO humainedev.persona_goals VALUES (122, 'I need to buy something more elegant, but it also has to be comfortable because I will be active all night.');
INSERT INTO humainedev.persona_goals VALUES (122, 'I only have 30 min to do it and although I don’t order clothes online, I will try to because my favorite shop has created the online shopping option and I need to deliver quickly.');
INSERT INTO humainedev.persona_goals VALUES (123, 'I want to buy some items for the new season.');
INSERT INTO humainedev.persona_goals VALUES (123, 'I want to buy x items that everyone is buying but I don’t want to change my style.');
INSERT INTO humainedev.persona_goals VALUES (123, 'I usually shop at x shop and I want to see if they brought the new collection yet.');
INSERT INTO humainedev.persona_goals VALUES (124, 'I need to buy items for my birthday party.');
INSERT INTO humainedev.persona_goals VALUES (124, 'I am gathering all my family relatives and I want everything to be great. I have already organized everything, the only thing left is my outfit.');
INSERT INTO humainedev.persona_goals VALUES (124, 'I just want to find an outfit to deliver quickly, I don’t want to search all day.');
INSERT INTO humainedev.persona_goals VALUES (124, 'I am going to my favorite shop and see if they brought any new items.');
INSERT INTO humainedev.persona_goals VALUES (125, 'I want to always be well dressed and elegant.');
INSERT INTO humainedev.persona_goals VALUES (125, 'I want to buy some items for everyday use. They need to be comfortable because I am active all day, but also unique and eye grabbing.');
INSERT INTO humainedev.persona_goals VALUES (125, 'My best friend recommended x shop and said that they brought some very good items, so I will check them out.');
INSERT INTO humainedev.persona_goals VALUES (125, 'I am willing to look all day to find what I need.');
INSERT INTO humainedev.persona_goals VALUES (126, 'I love vintage and one of a kind items.');
INSERT INTO humainedev.persona_goals VALUES (126, 'I want to look unique that’s why I rarely buy from brands that are super trendy.');
INSERT INTO humainedev.persona_goals VALUES (126, 'X influencer is doing a closet sale and they have vintage high fashion brands that have one of a kind pieces.');
INSERT INTO humainedev.persona_goals VALUES (126, 'I hope I can find something that looks good on me, but it is also something not everyone buys.');
INSERT INTO humainedev.persona_goals VALUES (127, 'I need items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (127, 'My sister always buys at x shop so I want to check what items they have.');
INSERT INTO humainedev.persona_goals VALUES (127, 'I want the clothes to be casual and comfortable for daily use.');
INSERT INTO humainedev.persona_goals VALUES (127, 'I hope I can find something that fits my style.');
INSERT INTO humainedev.persona_goals VALUES (128, 'I want to change my style to the trendy style for this season.');
INSERT INTO humainedev.persona_goals VALUES (128, 'I follow a lot of instagram influencers and I love to have items like them.');
INSERT INTO humainedev.persona_goals VALUES (128, 'My favorite influencer usually buys items from x shop so I want to go check if I can find some items for me.');
INSERT INTO humainedev.persona_goals VALUES (128, 'I would like to buy daily use items that are comfortable but also attention grabbing.');
INSERT INTO humainedev.persona_goals VALUES (129, 'I need items for everyday use.');
INSERT INTO humainedev.persona_goals VALUES (129, 'There’s a new shop in town that x actress is the ambassador for, and I want to see if any item looks good on me as well.');
INSERT INTO humainedev.persona_goals VALUES (129, 'I want the clothes to be comfortable and high quality so I can use them for longer periods.');
INSERT INTO humainedev.persona_goals VALUES (130, 'Autumn is here and I still haven’t bought any boots for the season.');
INSERT INTO humainedev.persona_goals VALUES (130, 'The news are predicting a flood in my hometown so I need to buy them ASAP.');
INSERT INTO humainedev.persona_goals VALUES (130, 'I want to go to x shop because everyone I know is buying there, and I want to just buy something to deliver quickly.');
INSERT INTO humainedev.persona_goals VALUES (130, 'I hope I can find a coat to match it with the boots. I want to look stylish and well-dressed.');
INSERT INTO humainedev.persona_goals VALUES (131, 'I want to buy x item.');
INSERT INTO humainedev.persona_goals VALUES (131, 'I have saved it for so long in x brand’s website, but it has been out of stock for a while now and they still haven’t brought it online. I want to go to the store and see if they brought it because I’ve wanted to buy it for so long.');
INSERT INTO humainedev.persona_goals VALUES (131, 'The reason I liked that item is because it is casual but also attention grabbing.');
INSERT INTO humainedev.persona_goals VALUES (131, 'If I cannot find it I will look for something similar that I like.');
INSERT INTO humainedev.persona_goals VALUES (131, 'While I am at the shop I will probably buy some shirts and trousers for work.');
INSERT INTO humainedev.persona_goals VALUES (131, 'I want to buy x item.');
INSERT INTO humainedev.persona_goals VALUES (131, 'I have saved it for so long in x brand’s website, but it has been out of stock for a while now and they still haven’t brought it online. I want to go to the store and see if they brought it because I’ve wanted to buy it for so long.');
INSERT INTO humainedev.persona_goals VALUES (131, 'The reason I liked that item is because it is casual but also attention grabbing.');
INSERT INTO humainedev.persona_goals VALUES (131, 'If I cannot find it I will look for something similar that I like.');
INSERT INTO humainedev.persona_goals VALUES (131, 'While I am at the shop I will probably buy some shirts and trousers for work.');
INSERT INTO humainedev.persona_goals VALUES (132, 'I need some clothes for every day use.');
INSERT INTO humainedev.persona_goals VALUES (132, 'I need the items to be high quality because I will use them on a daily basis.');
INSERT INTO humainedev.persona_goals VALUES (132, 'I hope I can find something comfortable because I will be active all day and I want to be able to stay in the same clothes for the day.');
INSERT INTO humainedev.persona_goals VALUES (132, 'Everyone is buying in x shop and I want to go there and see if I can find anything for my style.');
INSERT INTO humainedev.persona_goals VALUES (133, 'My favorite shop has a 50% discount this weekend.');
INSERT INTO humainedev.persona_goals VALUES (133, 'All my favorite items are on sale and I want to go try them on in the store.');
INSERT INTO humainedev.persona_goals VALUES (133, 'I hope they don’t get sold until I go there. I really liked them and they’re all high quality.');
INSERT INTO humainedev.persona_goals VALUES (134, 'I need to buy some sweaters for winter.');
INSERT INTO humainedev.persona_goals VALUES (134, 'The sweaters have to be colors that match with my coat and different pants because I want to wear them daily.');
INSERT INTO humainedev.persona_goals VALUES (134, 'The sweaters need to fit me perfectly otherwise I won’t buy them.');
INSERT INTO humainedev.persona_goals VALUES (134, 'I am willing to search all day to find the perfect sweaters.');
INSERT INTO humainedev.persona_goals VALUES (135, 'I want to buy items for my friend’s birthday party.');
INSERT INTO humainedev.persona_goals VALUES (135, 'I want to buy something fancy but that is comfortable at the same time because I want to help her with serving the cake.');
INSERT INTO humainedev.persona_goals VALUES (135, 'My friend bought her dress at x shop and I want to check if I can find something for myself as well.');
INSERT INTO humainedev.persona_goals VALUES (135, 'I hope I can find some items that look good on me and are comfortable at the same time.');
INSERT INTO humainedev.persona_goals VALUES (136, 'I want to buy a purse for an event I am attending.');
INSERT INTO humainedev.persona_goals VALUES (136, 'I would like the purse to have a pastel color that matches with different colors.');
INSERT INTO humainedev.persona_goals VALUES (136, 'My friend bought some perfect items on x shop and they suggested that they have put a lot of items on sale.');
INSERT INTO humainedev.persona_goals VALUES (136, 'I am willing to check all the shops in the mall to find what I need.');
INSERT INTO humainedev.persona_goals VALUES (137, 'I need to buy some items for daily use.');
INSERT INTO humainedev.persona_goals VALUES (137, 'The items need to be high quality because I need to wear them for longer periods.');
INSERT INTO humainedev.persona_goals VALUES (137, 'I usually buy at x shop because they have beautiful items that are comfortable and have good quality.');
INSERT INTO humainedev.persona_goals VALUES (137, 'I need to buy some items for daily use.');
INSERT INTO humainedev.persona_goals VALUES (137, 'The items need to be high quality because I need to wear them for longer periods.');
INSERT INTO humainedev.persona_goals VALUES (137, 'I usually buy at x shop because they have beautiful items that are comfortable and have good quality.');
INSERT INTO humainedev.persona_goals VALUES (138, 'I need items for a work event I am attending.');
INSERT INTO humainedev.persona_goals VALUES (138, 'I need the items to be comfortable but also attention grabbing because there will be a lot of influencers there.');
INSERT INTO humainedev.persona_goals VALUES (138, 'I don’t have any favorite shop, so I will buy the items wherever I find what I like.');
INSERT INTO humainedev.persona_goals VALUES (138, 'I am willing to search all day to find what I need.');


-----------------------------------------------------------------------------------------


INSERT INTO humainedev.persona_personality VALUES (1, 'This user has spent most of their life working to make their country a better place for the citizens. Through a combination of their powerful position and their novel projects and ideas, they enhance the quality of people’s lives and improve the community. 
Considering their authority, they’re constantly looking for new clothes that are comfortable but also speak out for their extraordinary personality.');
INSERT INTO humainedev.persona_personality VALUES (2, 'This user is a person whose mission is to live a life they love. Although they’re very hard working during work days, they enjoy traveling, trying out new things and buying beautiful items in their free time. As their activities range from gala nights to camping adventures, they’re always experimenting with their style. Considering their lifestyle, they buy clothes from different brands, always making sure that the brand has good quality clothes and is a trusted one.');
INSERT INTO humainedev.persona_personality VALUES (3, 'This user lives a life full of adventures, both in their personal and professional life. From proposing and implementing projects at work to building campfires in the forest, their life is filled with challenge. Due to their busy lives, they usually shop online and pick their clothes straight from home.');
INSERT INTO humainedev.persona_personality VALUES (4, 'This user is a creative, outgoing person who is always looking to explore opportunities. They’re the person always trying new ways of doing things, trying out new recipes and the most creative person in the room. They usually shop for necessities and prefer unique but practical items for themselves and others.');
INSERT INTO humainedev.persona_personality VALUES (5, 'This person is a hard-working person who is always challenging themselves on new endeavors. Thinking outside the box, suggesting novel ideas, and believing in each person’s potential are just some of the reasons people feel inspired when they’re around them. They attend several events per year, so they''re constantly searching for new shops to buy clothes that are unique and convenient to use at different events.');
INSERT INTO humainedev.persona_personality VALUES (6, 'This user is a family person and has spent their life working hard to bring peace and comfort in their family. In their free time they visit their relatives, help their kids with their schoolwork or spend days organizing their friends’ weddings. They usually shop for necessities or when they have to get something for their family. They prefer good quality and comfortable items that they can use for longer periods of time.');
INSERT INTO humainedev.persona_personality VALUES (7, 'This user has dedicated their life to bettering the quality of life of their close ones. Although they prefer routines, every once in a while they try new things and try to see life from a different perspective. This ability to switch from conventional to unorthodox when needed helps them adjust to their environment and make decisions based on what’s the best for the time. They usually buy clothes for everyday use and from brands they trust, but in times that they don’t find good choices from those brands, they are willing to switch to brands they haven’t bought from before.');
INSERT INTO humainedev.persona_personality VALUES (8, 'This user is a people’s person and cares deeply about their loved ones. They are out-going, friendly, and the person everyone wants to hang out with. They often shop, whether they need comfortable items or extravagant ones, and as a person who is always attending parties or visiting different places, they are always up to date with the latest trend in fashion.');
INSERT INTO humainedev.persona_personality VALUES (9, 'This user is an open-minded person, who’s always looking to improve the quality of their life. Never missing out new opportunities, they lead a busy life with little time to shop. However, when they do, they want to buy useful items they can use for longer periods. Since they’re busy and don’t have time to explore through shops, they believe that the best way to find trusted brands is by asking people who share the same values as they do.');
INSERT INTO humainedev.persona_personality VALUES (10, 'This user is a person who’s always trying new things and looking for new opportunities. Their need to change routines and make each day better than the other is reflected in their style in fashion. They’re constantly updating their wardrobe and looking other people’s choices in fashion to make better choices for themselves.');
INSERT INTO humainedev.persona_personality VALUES (11, 'This user is curious about the world and they are eager to learn new things and enjoy new experiences. They are always challenging themselves and getting out of their comfort zone. When it comes to shopping, they’re constantly updating their wardrobe to match with their lifestyle. Therefore, they follow the latest fashion trends and people in fashion to make choices for themselves.');
INSERT INTO humainedev.persona_personality VALUES (12, 'This user is an active, do-it-all person. They spend their time doing what they love, and are not afraid to show who they are. They want their style to reflect this that’s why they spend a lot of time shopping. Their mission in shopping is to buy from trusted brands and find unique pieces that make them stand out from the crowd.');
INSERT INTO humainedev.persona_personality VALUES (13, 'This user is a creative person who is always proposing and implementing ideas for the betterment of society and promoting equality for all. They believe that everyone has the right to be treated the same, and they work hard to contribute in tackling this problem. When it comes to shopping, they usually buy practical items that they can wear on events but also on a daily basis. Because they don’t have time to shop, they usually buy from shops that their friends recommend.');
INSERT INTO humainedev.persona_personality VALUES (14, 'This user is a person for whom family and friends are the most important part of life. They work hard during the day, but make sure to be home after work as they want to spend as much of their free time with them. When they’re not working on new ideas, they’re constantly looking for new places to visit with their family, or buying unique presents for them. 
They usually shop for daily use, and the quality of the clothes is very important as they plan to give away their items to other people. However, every once in a while they like to make a change in style and challenge themselves with something new.');
INSERT INTO humainedev.persona_personality VALUES (15, 'This user is a hardworking person who has dedicated their life to making their circle happy. Always being the one who takes care of their kids, organizes events and family dinners, trying on new recipes to make for their family, they have little to no time to spend shopping. When they do shop, they usually go directly to the store and pick up the items they like, however because they lead a busy life they sometimes shop online to use the time for something else.');
INSERT INTO humainedev.persona_personality VALUES (16, 'This user is a person who tries to enjoy life and not spend their whole life working. They’re the ones who are always planning trips and parties for their friends, and they’re constantly looking for ways to have fun. This user is a fashionista who is always looking to upgrade their style and get on with the latest trends. They shop as much as they work and there’s no way a new season catches them unprepared. Their sense of style helps them use the same item for different purposes, that’s why they need their clothes to have good quality in order to experiment with them.');
INSERT INTO humainedev.persona_personality VALUES (17, 'This user is a hard-working person, who is always giving new ideas and implementing projects for the betterment of her community. Because she is a person her citizens love, she works on raising awareness on different topics in order to improve the life quality of her community. Being a very busy person, she doesn’t have much time to shop.  She usually shops online, and when she has time she buys clothes at shops people she knows buy.');
INSERT INTO humainedev.persona_personality VALUES (18, 'This user is a person who is full of new ideas and one who works hard to accomplish their goals. As much as they like to work, they also like to treat themselves and do things for fun. That’s why they’re always the friend who finds the new restaurants in town, buys clothes the day the new collection comes out, and finds the tickets to the concerts of her favorite bands. They usually shop when the seasons change, as they like to update their wardrobe to current fashion trends.');
INSERT INTO humainedev.persona_personality VALUES (19, 'This user is a successful entrepreneur, who has worked hard to get where they are. As they deal with stressful situations during the week, they usually spend their weekend planning fun activities to get energized and recharge for the upcoming weeks. When it comes to shopping, they don’t have much time to buy clothes, but when they decide to buy items, they don’t stop looking until they find it.');
INSERT INTO humainedev.persona_personality VALUES (20, 'This user is a freelance marketing strategist who is always up to date with the latest news on digital marketing and everything related to it. They enjoy learning new things and challenging themselves to different tasks and responsibilities. They like to be independent in their choices and want to look and feel unique in everything they do. When it comes to clothes, they want their clothes to reflect their personality, that’s why they are willing to spend hours to find clothes that fit them perfectly. They don’t shop very often, but when they do, that is mostly because of necessities and important events they need to attend.');
INSERT INTO humainedev.persona_personality VALUES (21, 'This person is an open-minded person, working with marginalized groups to increase their quality of life and help break the discrimination towards them. They are a people’s person and are always putting others before themselves. They fight for an equal world, where everyone has the right to have a good life. They don’t shop often, as they’re always working on new projects and ideas, and when they do, they usually look for good quality and comfortable clothes for daily use. They don’t have a favorite shop, but trust the people they value to suggest brands for him to buy from.');
INSERT INTO humainedev.persona_personality VALUES (22, 'This user is an energetic person, always challenging themselves with new experiences. They use their energy to work for the betterment of their community, that’s why they advocate to improve the life of their circle and the city. When they’re not working, they’re volunteering in a soup kitchen, tutoring children for free, or helping old ladies cross the street. They usually buy for necessities and the comfort in their clothes is very important.');
INSERT INTO humainedev.persona_personality VALUES (23, 'This user is a person who has spent their life working a 9-5 job. They prefer the simple life, although every once in a while they like to try new things and look out for opportunities. Being a family person, they are always organizing family dinners, planning birthday parties or helping their kids with their homework. They don’t usually shop, and when they do they like to go directly to the store to pick their favorite items. Their wardrobe is filled with comfortable good quality clothes that they can use for longer periods.');
INSERT INTO humainedev.persona_personality VALUES (24, 'This user is a hardworking person who has dedicated their life to making their circle happening. Always being the one who takes care of their kids, organizes events and family dinners, trying on new recipes to make for their family, they have little to no time to spend doing shopping. When they do shop, they usually go directly to the store and pick up the items they like, however because they lead a busy life they sometimes shop online to use the time for something else.');
INSERT INTO humainedev.persona_personality VALUES (25, 'This person is a goal-oriented, result-driven person who has worked his way up as a politician. He spends most of his time working and gets pleasure in getting things done. He chooses high quality clothes and constantly buys items to update his wardrobe. He believes that he needs to always be dressed up in the best way possible as the clothes reflect a person’s authority and position.');
INSERT INTO humainedev.persona_personality VALUES (26, 'This person is a neuroplastic surgeon who enjoys rewarding herself with clothes and makeup. She is very hard-working, disciplined and doesn’t take no for an answr. That makes her the most committed doctor in the hospital.  She constantly updates her wardrobe and buys clothes that are in for the season. She believes that if you look good, you will feel good and that’s why she buys clothes very often.');
INSERT INTO humainedev.persona_personality VALUES (27, 'This user is a dedicated entrepreneur, who works hard to achieve their goals. They are constantly on the move and sometimes work more than they’re supposed to. However, they never miss out on fun times. They travel a lot, hang out it their friends and go to dinners with coworkers. When it comes to shopping, they have a specific shop they always check the items from, and they don’t like to buy items on other places as they don’t believe the other shops are as good as the one they like.');
INSERT INTO humainedev.persona_personality VALUES (28, 'This user is a dedicated person who is willing to go out of his way to make the work done and achieve success. They love a good challenge and don’t give up until they reach their goal. When they’re not working, they’re hanging out with their friends as they enjoy their presence. They shop quite often and know exactly what they want from shopping. They know themselves and their style and don’t like to experiment with their clothing.');
INSERT INTO humainedev.persona_personality VALUES (29, 'This user is a successful entrepreneur who has worked hard to achieve their goals. They are disciplined, results driven and able to postpone immediate gratification for the sake of long-term success. When it comes to shopping, they like to wear unique clothes that no one has. They have a good sense of style and try to always show their personality using their clothes.');
INSERT INTO humainedev.persona_personality VALUES (30, 'This user is a family person, whose mission in life is to make a good life for themselves and their family. They work hard to make their family happy and they go out of their way to make sure everyone is doing well. They work part time, but fill in their day with volunteering in the local NGO. When it comes to shopping, they usually buy in 1 particular shop that has the products they like, and they usually buy clothes for important events in their life.');
INSERT INTO humainedev.persona_personality VALUES (31, 'This user has dedicated their life to working to make their family happy. Smart, methodical, and assertive. This makes them a professional in their field, and a model for young people to look up to. Their goals however, don’t only include having a successful career. They take pride in knowing that they have a family they go home to every day. They want to spend as much time with them as possible, that’s why they reserve their nights watching movies or preparing dinner for the family. They don’t buy clothes very often, and when they do they buy comfortable clothes they can wear for longer periods.');
INSERT INTO humainedev.persona_personality VALUES (32, 'This person is a highly successful news anchor, who has dedicated their life to bettering the life of their community. They are the person who are willing to work all night in order to get the job done. When they have free time they like to spend it doing things they love and that includes shopping. They want their clothes to reflect their personality, that''s why they shop quite often. They constantly update their wardrobe with quality clothes from trusted brands.');
INSERT INTO humainedev.persona_personality VALUES (33, 'This person is a fashion model who has worked consistently for years to be where she is. She loves her job and although it might be exhausting sometimes, she always works with a smile on her face. This user spends a great deal of time and money for clothes, as she has loved clothes since a little girl. This love for clothes has made her a follower of trends, and that’s why she is always up to date with the latest fashion trends.');
INSERT INTO humainedev.persona_personality VALUES (34, 'This user is a hardworking individual, who apart from working, loves to spoil themselves with treats. She works in a 6-figure marketing agency and has had the opportunity to work with people of all fields. She loves to celebrate her life, that’s why she is always up for a drink after work, or a birthday celebration with her loved ones. Shopping is something that she highly enjoys and she regularly buys clothes not only for everyday use, but for different events she attends.');
INSERT INTO humainedev.persona_personality VALUES (35, 'This is a sales representative who doesn’t take no for an answer. They have spent their life working for the things they love and they don’t plan to ever stop. They love being challenged, that’s why they chose sales as their occupation. They believe that their ability to close deals even when it seems impossible, is their greatest strength. They celebrate their success buying things they love or by doing fun and challenging activities such as bungee jumping.When they buy clothes, they make sure to buy the best thing in the shop, no matter the price. They look for hidden gems in shops and are always the most stylish person in the room.');
INSERT INTO humainedev.persona_personality VALUES (36, 'This user is an entrepreneur who has always wanted to do things her way. Ever since she was little, she hated having the dresses all her friends wore, that’s why she always made her own clothes using her own materials. Growing up, she loved design and always used her skills to make unique clothes. That has stayed with her and now she is a successful designer. Stylish and one of a kind, she is a role model for a lot of young girls.');
INSERT INTO humainedev.persona_personality VALUES (37, 'This user has dedicated their life to making people happy and their community to have a better quality of life. They work hard and are clear on what they want, that’s why they have achieved every goal they had so far. When it comes to shopping, they try to be reasonable and only buy clothes they need, but every once in a while they buy clothes that they like although they don’t need them.');
INSERT INTO humainedev.persona_personality VALUES (38, 'This user is a person who loves to spend time with their circle. Although they work hard during the day, they want to spend their nights with their children and do things to make them feel as a family. They like their children to grow in a loving family and want to become a role model for them. They organize family dinners, visit relatives often, and go to museum parks with their children. When it comes to shopping, they have the shops they usually buy from, and they like to go there physically, but when they don’t have the time to go there themselves, they shop online.');
INSERT INTO humainedev.persona_personality VALUES (39, 'This person is a workaholic who is able to work from morning to evening and not even notice. Being this hardworking has paid him off, as he has been able to achieve his career goals. His motto is “always pay your debts” and that’s what has motivated him to work hard and live debt free.

When it comes to shopping, he doesn’t shop often. He wants to spend his time working or doing other activities that he enjoys, but that doesn’t include shopping. He usually shops for necessities, but he makes sure to buy unique clothes that never go out of style.');
INSERT INTO humainedev.persona_personality VALUES (40, 'This person is a high-achiever, results driven person who has earned the life they live. They love their job and they don’t plan to ever stop working on their dreams. They love giving gifts and returning gifts, that’s why, when they are not buying items for themselves, they shop for their loved ones.');
INSERT INTO humainedev.persona_personality VALUES (41, 'This user is a person who enjoys their life to the fullest. She is hardworking, reliable and disciplined in her work, but she never misses a chance for a cup of coffee or a glass of beer when her friends offer. She has always wanted a job where she can meet new people, explore places and travel the world. That’s why, her job as an event planner is quite fulfilling, and she has achieved a lot working in this profession. She gets to meet people of all kinds, see different cultures, and explore herself in the process. She shops quite often, and always buys presents for her friends and family when she goes back home. When it comes to her style she has a vibrant fashion style that shows her creative spirit.');
INSERT INTO humainedev.persona_personality VALUES (42, 'This user is a successful business owner who has succeeded in every aspect of life. He is very competitive and willing to go out of his way to get what he wants. Considering his hard work and dedication to achieve his goals, he is now at the top and doesn’t have any competition worth mentioning. He wants his clothes to reflect that, that’s why he is very careful with the items he buys. Only wearing high fashion and good quality clothes, he spends a reasonable time picking his style. Although money isn’t an issue, he knows how hard he has worked to achieve it, that’s why he doesn’t want to spend unreasonably. He usually buys at shops he is a regular costumer at, and expects to have discounts from those shops as well.');
INSERT INTO humainedev.persona_personality VALUES (43, 'This user is a highly independent, do-it-all kind of person, who is constantly challenging themselves to achieve more and do more. They love to celebrate their hard work and success, that’s why they are always looking for new places to visit and restaurants to try food in. They constantly update their wardrobe as well and they love to buy unique items. You never see them wearing the same item twice, and they always look well dressed up.');
INSERT INTO humainedev.persona_personality VALUES (44, 'This user is an active person who never stops working to make the world a better place. She believes that the change comes from within, and that made her spend her free time doing good for her community. She holds free tutoring classes for children from marginalized societies and volunteers at the animal shelter in her free time. She doesn’t shop often, but when she does, she usually shops when there are discounts or sales.');
INSERT INTO humainedev.persona_personality VALUES (45, 'This user has dedicated their life to making their family happy. They spend their time organizing dinners, birthday parties or family gatherings, and they’re always the person people go to when they want to organize something. They’re disciplined and organized and have worked hard to get in a position in life where money isn’t an issue. They usually shop for necessities and the quality of the product is at utmost importance when picking clothes.');
INSERT INTO humainedev.persona_personality VALUES (46, 'This user is an energetic person who spends her time working to make the community better. She’s always on the run to a meeting or an event, that’s why she always has to have comfortable clothes she can stay all day in. She loves shopping, but she makes sure to be reasonable and buy items when they are on sale. Powerful, active and always stylish, she is a person a lot of young people see as a role model.');
INSERT INTO humainedev.persona_personality VALUES (47, 'This person is a person who doesn’t take no for an answer. She is very competitive and fights hard to get what she wants. She knows who she is and what her needs are, that’s why she is always on the move to achieve her goals. When it comes to shopping, she likes unique and one of a kind clothes that’s why she constantly is in search of rare items.');
INSERT INTO humainedev.persona_personality VALUES (48, 'This person is a person who uses every opportunity to have fun. Although they work hard, they also like to enjoy themselves. That’s why they’re always organizing parties or gatherings. They usually shop for fun, as they enjoy spending time doing things for themselves.');
INSERT INTO humainedev.persona_personality VALUES (49, 'This user is a person who loves to be challenged. They enjoy working and they constantly look for ways to improve. That means that they’re always on the move, and always doing something new. They usually shop for unique items that only  a few people have, that’s why they are willing to dig deep to find what they like. They don’t like to spend much money on clothes because they know that all items will eventually go on sale. However, although they buy on sale, they always find the most unique items that everyone likes.');
INSERT INTO humainedev.persona_personality VALUES (50, 'This person is an energetic person who has worked her way up. She works hard and is very independent. She loves a good challenge and doesn’t take no as an answer. She believes that the reason she is successful in her profession is because she has never listened to other people’s words and she channeled her creativity in a productive way. She usually shops vintage because she doesn’t want to have the clothes everyone is wearing. She wants to unique not in just her profession but also in her style.');
INSERT INTO humainedev.persona_personality VALUES (51, 'This person has dedicated their lives to bettering the life quality of their loved ones. They care deeply for their family and are willing to risk their life for them. They work hard to provide for them and make a good life. When they’re not working they always spend time with their family by having movie nights or just family dinners. They don’t shop often and when they do they make sure to buy good quality clothes to match with their lifestyle. They usually wait for sales to buy the things they need and they have some shops they usually buy from.');
INSERT INTO humainedev.persona_personality VALUES (52, 'This user is a person who thinks through every decision that they make. They’re thoughtful, organized and responsible, that’s why they have never had a problem holding jobs for long periods. They work as mathematics professor and they work hard to help their students learn everything in a way that they can use their knowledge in life. Their consciousness also affects their decision to buy clothes. They don’t buy clothes often, but when they do, they love to buy products that are unique and not everyone has. That’s why their style is distinctive from the others.');
INSERT INTO humainedev.persona_personality VALUES (53, 'This person is an office worker who is quite good at her job. She handles daily tasks with certainty and isn’t afraid to ask questions. She has attained an authority at her job and everyone looks up to her for her discipline and dedication. She works hard and shops harder. Whenever she is at her favorite shop she cannot go by without buying something, and to her it is not much about whether she needs the clothes, but the way she feels better after she does.');
INSERT INTO humainedev.persona_personality VALUES (54, 'This user is a hyper energetic person who has worked hard to build a life they love. They love their job and they’re very serious when it comes to completing tasks. They’re just as serious when buying clothes for themselves. They want their clothes to look perfect on them, and they don’t stop until they’ve found what they wanted. They’re super stylish and love to look and feel good.');
INSERT INTO humainedev.persona_personality VALUES (55, 'This user is a hardworking person who spends his time working for things he loves. Although he works during the week, he usually spends his weekends doing fun activities such as hiking, swimming or camping. He likes to celebrate his success by doing things he loves, that’s why when he’s not working he’s always doing something else he enjoys. When it comes to shopping, he usually does for fun and he enjoys spending hours looking for items he loves. He shops often and is always in with the latest trends.');
INSERT INTO humainedev.persona_personality VALUES (56, 'This person is an energetic user who works hard to get what they want. They are disciplined, methodical and reliable, that’s why everyone likes them. When a task is assigned to them, they commit to it and do anything to complete it. In fact, they see their life’s responsibilities as tasks as well, so they never miss anything important. They’re there for birthday parties, family events, work parties etc. The same applies to his shopping habits, he usually plans ahead his buying and is firm on what he needs. Although sometimes buys more than expected, they are always conscious on what they’re buying and whether that item useful for them.');
INSERT INTO humainedev.persona_personality VALUES (57, 'This person is a highly independent person who has come where they are with hard work. They work in the advertising industry and are always updated with the latest trend. They are careful when they choose clothes because although they know what’s in at the moment, they want to add their personality touch in that style. They want their clothes to be a combination of the trend and their personality, that’s why they save their time for shopping.');
INSERT INTO humainedev.persona_personality VALUES (58, 'This user is a benevolent person who puts their relationships with their circle first. They understand the power of having people you love by your side and that’s why they work hard to have good relationships. They work part-time and spend the other parts of the day to take care of their children, help them with homework or help a friend organize a party. They don’t shop that much but Fall is their favorite season and they always look perfect on this season. They don’t spend that much time shopping but they make sure that when they go to a shop, they don’t go out without buying something they like.');
INSERT INTO humainedev.persona_personality VALUES (59, 'This user is a person who is very serious about their job. They are committed, hardworking and disciplined and they always leave a good impression. They like to always be dressed up perfectly, that’s why they’re always buying clothes with good quality and ones that fit them perfectly. They don’t like to buy on sale because they know that only the products with bad quality are left, and they feel that they will look “cheap” if they buy from that section.');
INSERT INTO humainedev.persona_personality VALUES (60, 'This user is a person who works in a real estate office. He is hardworking and disciplined, and he never says no when it comes to finishing tasks. He usually spends his weekends working and when he’s not working he meets with his friends where they talk mostly about business. He often shops and although he likes comfortable clothes, he also wants to grab attention with them. He believes that just the way someone dresses leaves enough impression to the others, that’s why he’s very careful of what he buys and where.');
INSERT INTO humainedev.persona_personality VALUES (61, 'This user is an organized and methodical person, who is always looking for ways to improve themselves. They surround themselves with successful people and love to talk about business and different endeavors. When it comes to shopping, they don’t usually plan to buy specific items at specific times. Usually they just go to a shop and buy items they love. They have a unique style and they’re always well dressed.');
INSERT INTO humainedev.persona_personality VALUES (74, 'This user is a highly successful person, who has worked hard to get where they are now. They are disciplined, organized and goal-oriented. They know what they want and are not afraid to pursue it, that’s why they hold a powerful position in the company they work in. This user loves to shop, and sometimes spends more than they should, but they justify those purchase with their fear of the items going out of stock. Their wardrobe contains a chic and fashionable style, and they never go out wearing simple clothes that everyone wears.');
INSERT INTO humainedev.persona_personality VALUES (62, 'This user works as an executive assistant to the CEO of a 6-figure company. They are very excited about their job and they go to work with enthusiasm. They love how no day is the same as the other, and they’re constantly being challenged with new tasks they have to find a solution for. They work hard but also love to celebrate their wins. They hang out, go to parties, do fun activities etc, just to remind themselves that they need a break and deserve to be celebrated. They shop quite often, not because they need clothes all the time, but because once they go to a shop, they can’t help themselves but buy them. However, they never buy things they won’t use and are very conscious about the decisions they make in shopping.');
INSERT INTO humainedev.persona_personality VALUES (63, 'This user is a person whose dreams constantly change and goals get bigger. They own a shop and they’re constantly looking to bring new products to the market in order to make the life of the costumers easier. They love challenges and are even more motivated to work on something when they know that it is harder to reach it. They love to shop and they usually buy colorful clothes and different designs just to see how they’re able to match it to their other items. Always unique dressed and looking cool, a lot of younger love their style and want to be just like them.');
INSERT INTO humainedev.persona_personality VALUES (64, 'This person is a business owner who is constantly on the search for something new. He works hard on his business, and as an entrepreneur he knows the importance fashion has when closing deals. That’s why he only buys unique clothes that he is able to make them custom for him. He wants his clothes to reflect his active personality, that’s why he usually chooses attention grabbing clothes, and considering that he’s active all day, those items need to be comfortable. He usually goes to a fashion designer for clothes, but every once in a while he listens to the suggestions of the people he loves and buys items from different shops. 
Although he’s a workaholic and a super independent person, he cares deeply about his circle. He loves to spend time with them and to do fun activities with them. He goes shopping with his friends and help each other decide what suits them and what not.');
INSERT INTO humainedev.persona_personality VALUES (65, 'This person is an empath, who spends all their days working to make the world a better place. They are constantly doing something for the community, no matter how small that is. They’re the person who is always helping others with their studies, the one who babysits children when the babysitter is not available, or the person who you count on when you need financial help. They’re there to help their circle with anything they need, but their need to help goes beyond that. They join campaigns for the protection of human rights, campaigns for children with special needs, or for animal welfare. They’re concerned with world problems and that’s why their days are filled with various activities. They don’t shop often, but when they do they make sure the brand they buy from is a conscious one and one that doesn’t use bad fabrics for the clothes. Usually, when there are campaigns that include human welfare, they contribute to them by buying clothes, and they use those money for two purposes: 1. to buy the items they need, and 2. to help the world be better.');
INSERT INTO humainedev.persona_personality VALUES (66, 'This user is a person who has dedicated their life to their family. They are the perfect sibling, parent and child. They take care and make sure to provide for their family, that’s why they’re working most of the time. However, after their shift is over, they go directly home and spend time with their family. Their weekends are full of family activities, dinners, or birthday parties, and that makes them happier than any career in the world. They don’t buy clothes often, and they usually do when the clothes are on sale because that’s when they remember that they need new clothes for the season. Their go to clothes are plain shirts and trousers, and don’t usually go out of their style.');
INSERT INTO humainedev.persona_personality VALUES (67, 'This user is a disciplined, detail-oriented person, who has worked hard to achieve the popularity they have now. They hold a powerful position in their community and because of their traits and characteristics, they are highly successful at it. They want to be well-dressed at all times, that’s why they buy clothes quite often. They have a weakness for luxurious brands and that is not only because of the items, but also for the way they’re treated during shopping. They want to be valued by the sellers and that’s why they don’t buy from any brand.');
INSERT INTO humainedev.persona_personality VALUES (68, 'This user is a person who is willing to do anything in order to pursue and ultimately achieve their goals. They are hard workers and don’t take no for an answer. They are constantly challenging themselves and doing things they enjoy such as hiking, swimming or playing basketball. When they put the eye on something, they cannot go without achieving what they have started. Whether it is learning a new language, or becoming fit, they don’t stop until they’ve reached their goal. They usually buy clothes for necessities, but are quite tempted when they see offers or discounts in stores.');
INSERT INTO humainedev.persona_personality VALUES (69, 'This user is a hardworking person who has worked on achieving their goals day and night. They are disciplined, organized and responsible. Each time they are given a task, they complete it with precision and in the best way possible. Although they spend most of their time working, they still manage to enjoy themselves by attending parties, hanging out with their friends, or just spending a whole day shopping. Shopping is their weakness and they are always tempted to buy items although they might not need them. They are very serious about making the process of shopping enjoyable, that’s why they love to buy items from brands they trust and ones that value them as a costumer.');
INSERT INTO humainedev.persona_personality VALUES (70, 'This user is a person who loves a good challenge, and is constantly looking for opportunities to test themselves. They’ve had multiple jobs in the past, and always look to grow more in their profession. Their drive to achieve and to be successful inspires a lot of people. When it comes to shopping, they like to plan what they need to buy before going to the shops. However, every once in a while they decide to buy whatever they’re tempted to, and they don’t feel guilty about it because they consider it a gift to themselves.');
INSERT INTO humainedev.persona_personality VALUES (71, 'This user is a high achiever, who has worked hard to come where they are now. They have never stopped believing in themselves, and didn’t give up no matter the situations. They attribute their success to their independence and their courage to show who they are. They love to shop, and they have difficulties controlling themselves when a good offer or product is before their eyes. However, because they only like unique items, they are not easily tempted to buy items often.');
INSERT INTO humainedev.persona_personality VALUES (72, 'This user is a person who is very committed to their family. They love to spend time with them and always plan to do things together. They’re kind, loving and forgiving, and they give out a positive energy that everyone feels drawn to. They don’t shop often, but they’re usually tempted to buy items when they get to have free items.');
INSERT INTO humainedev.persona_personality VALUES (73, 'This user is a person who works to build a good life for their family. They are hardworking, responsible and always there for their circle. No matter the event, they’re there to help or contribute in any matter. Although their weekdays are spent working in the office, their weekends are full of adventures with their family. They love to shop for them and for their family, but they try to be more reasonable and not buy things they don’t need.');
INSERT INTO humainedev.persona_personality VALUES (75, 'This user is a high achiever who has worked their whole life for their goals. They are highly competitive and are willing to do anything to get what they want. This attitude has helped them achieve their goals and become the person of their dreams. When it comes to shopping, they love to spend time shopping and buying items that they like. They have a chic style and they are always well-dressed.');
INSERT INTO humainedev.persona_personality VALUES (76, 'This user is a person whose mission in life is to work hard in order to have a great life. They take pleasure very seriously and are very motivated to work just to enjoy life the other part of the day. In the daytime they work, while after work they go for wine dates or parties. They love to shop, and sometimes they spend more money than they are supposed to. Their wardrobe is constantly updated and they are always well-dressed.');
INSERT INTO humainedev.persona_personality VALUES (77, 'This user is a person who always seeks excitement, novelty, and challenge in life. They don’t take no for an answer and are willing to do anything to get what they want. This user works hard during the week, but never misses an opportunity to go hiking, surfing or rock climbing with their friends. They are always looking for adventures, and cannot say no when adventure calls. When it comes to shopping, they love to buy clothes. They spend quite some time and money doing so, and because they know that, they try to control it by only buying when items are on sale.');
INSERT INTO humainedev.persona_personality VALUES (78, 'This user is an independent person, who loves exploring the world and uses every opportunity to apply their creativity to giving solutions. They are a successful business owner who has passed several challenges to get where they are. Although they had difficulties, they never gave up and never let other people change their point of view. When it comes to shopping, they are very careful of what they buy. They want their clothes to represent their personality, and don’t want to look just like everyone else, that’s why they only buy from shops that have one of a kind clothes.');
INSERT INTO humainedev.persona_personality VALUES (79, 'This user is a person whose mission is to increase the life quality of their close ones. They work hard to be provide for them and support them in every endeavor. This user is disciplined, smart and thoughtful and always ready to work with something new. Because they’re busy most of the time, they don’t have much time to shop. This is why they usually only buy when there are sales and they reserve their time to shopping only. Their wardrobe is filled with casual yet high quality clothes that help them lead an active life.');
INSERT INTO humainedev.persona_personality VALUES (80, 'This user is a conventional person who respects and accepts their tradition. They love to celebrate where they come from, and they want to transfer this love to their children. That’s why they always organize family gatherings especially for holidays as they believe that it is important for the whole family to be together in those times. They don’t shop often, and they usually do either for necessities or for gatherings that they organize. Their wardrobe consists of comfortable yet elegant clothes.');
INSERT INTO humainedev.persona_personality VALUES (81, 'This user is a person who has had an executive position for years now. They’re stable, reliable and always the person you learn from all the time. His students look up to him for his experience and his knowledge, that’s why he’s constantly working on bettering himself in his field of study. He doesn’t shop often, but when he does, he usually buys everything he needs in one haul. He wants his clothes to reflect his power and his position at his job, that’s why he is careful when he buys them.');
INSERT INTO humainedev.persona_personality VALUES (82, 'This user is a high-achiever who is willing to do anything to get what he wants. He’s competitive and along with his hard work and discipline, that has helped him to be where he is now. He tries to be conscious about shopping because he knows that the moment he goes into a shop he will want to buy something. He plans ahead his shopping trip, and this way he buys what he needs without spending money on things he doesn’t need.');
INSERT INTO humainedev.persona_personality VALUES (83, 'This user is a person who has spent their whole life trying to balance life and work. As much as they love their job, they also want to enjoy life, that’s why they love to go out on Fridays, attend parties and organize picnics with friends. They have a lot of clothes, but still find themselves looking to buy new items for new occasions. They always make sure to buy high quality material only, and they try hard to buy comfortable clothes even for events that that is nearly impossible.');
INSERT INTO humainedev.persona_personality VALUES (84, 'This user is a person who is constantly challenging themselves to be better and better. They are never content with their achievements and are always pushing themselves to do better. That’s why they often change jobs or get promoted to higher positions than before. They never say no to an opportunity and they’re always prepared for a new experience. They usually shop when they get new jobs, but they don’t move much from their style as they have found what looks good on them and don’t want to change it just because their job has changed. They look for comfortable clothes that they can use for different occasions, and clothes that are practical and useful for different seasons as well.');
INSERT INTO humainedev.persona_personality VALUES (85, 'This person is an artist who spends their time working for their creative design agency. Along with their friends, they are growing their business and slowly creating the life they love. They are a creative person who loves to show their creative soul through the physical appearance. They have a bohemian style and always wear clothes no one has. Whenever they go to a shop, they directly search for vintage items that have big value and that no one else appreciates.');
INSERT INTO humainedev.persona_personality VALUES (86, 'This person has spent their life working hard to build a healthy family where everything is discussed and no problem get bigger than it should. They’re the person everyone goes to when they need help, and the one who is always there for better or worse. Considering their busy life, they usually buy for necessities, and if they see something that they cannot resist. However, the latter doesn’t happen often, and when it does, they make sure the item is high quality that can be used for longer periods of time.');
INSERT INTO humainedev.persona_personality VALUES (87, 'This user is a family person who knows the importance of family and closed ones in one’s life. They like to build this closeness with other people to their children that’s why they always organize family gatherings, birthday parties etc. They want their children to know their roots, and know who their family is. They’re always on the move and they rarely have time to shop. And when they do, they usually buy practical items they can use for different occasions.');
INSERT INTO humainedev.persona_personality VALUES (88, 'This person is a person who has worked her way up to the be the executive director of a big organization. She spends her days in meetings with donators and her other parts of the day are spent, working to distribute the fundings the organization receives, and managing her employees. She doesn’t have much time to shop, but when she does, she likes to buy practical things that are used for multiple events. She needs to always be well dressed that’s why she spends quite some time shopping and buying items that have good quality.');
INSERT INTO humainedev.persona_personality VALUES (114, 'This user is a person who is always energetic and full of life. They love life and are always doing something to make themselves or others happy. They usually buy clothes for daily use, but they try to buy items that are trendy at the moment.');
INSERT INTO humainedev.persona_personality VALUES (89, 'This user is a highly successful person, who doesn’t stop until they get what they want. They work on their goals and finish whatever they put the eye on. They love fashion and although they want to be comfortable, they like to get the attention of people. They always follow the latest trend and love to look fresh and well dressed. They are obsessed with fashion influencers and always look to buy similar clothes, because they love their style.');
INSERT INTO humainedev.persona_personality VALUES (90, 'This user is a person whose passion is fashion. They’re always the most stylish person in the room and everyone loves going shopping with them because they are informed about the quality of clothes and the best shops in town. They always wished they had a job in fashion that’s why they always worked with fashion brands. They’re now the manager of a big brand and their job  requires that they are always elegant and well dressed. This job fits perfectly because they love to shop and follow the latest trends, so now they get to buy clothes for work and for fun.');
INSERT INTO humainedev.persona_personality VALUES (91, 'This user loves challenging themselves and is constantly looking for new opportunities to learn from. They are hardworking and don’t take no for an answer. When they’re not working, they’re doing fun activities such as hiking or skydiving. When it comes to shopping, they usually shop for necessities, or when they want to do a change of style. They hate to have the same style all the time, so they constantly change it to see which style looks better on them.');
INSERT INTO humainedev.persona_personality VALUES (92, 'This person is a digital marketer who always thinks outside the box. They’re very serious about using new strategies for marketing and that’s why they’ve always been successful in this field. This mindset of always finding a novel solution to a problem has made them among the top digital marketers, and they have followers from all over the world. They want their style to reflect their personality, that’s why they always buy unique clothes that not many people have. Their clothes are a mix of comfortable and chic, but always having a unique detail that makes the clothes look like they’ve been made specifically for this person.');
INSERT INTO humainedev.persona_personality VALUES (93, 'This person is a kind person who spent their life working for a life they love. They work for different NGOs in order to contribute to the betterment of their community. In addition to that, they love to spend time with their family, so after work they always visit their relatives, help their cousins with their studies, or take care of house chores. They usually shop for necessities, or when they have some free time and like to spend it doing things for themselves. Their style is casual and comfortable and they always look well dressed.');
INSERT INTO humainedev.persona_personality VALUES (94, 'This user is a family person who has dedicated their life to putting a smile on their circle’s face. They work hard in order to provide for the family, but makes sure they’re always home for dinner. They do movie nights every weekend, and loves to cook dinner with their family. They usually only shop when they need new clothes, but every once in a while they like to spend some time for themselves and do things they love. That includes shopping. They shop for casual and comfortable clothes, and they make sure the clothes they pick are always good quality.');
INSERT INTO humainedev.persona_personality VALUES (95, 'This user is an efficient and organized person, who takes every task they have seriously. Whether it be just a simple task, or running an entire department, they take it with enthusiasm and don’t give up until they reach the goal. They have a powerful position in their career, and they are very well respected. When it comes to shopping, they try to shop as efficiently as possible. They know that they need to always be well-dressed, so they always buy clothes that they’re able to wear for multiple events, and they use discount codes to buy them.');
INSERT INTO humainedev.persona_personality VALUES (96, 'This user is a high achiever who is always striving for excellence in everything they do. They are quite ambitious and has big goals. No matter how difficult, they always manage to achieve them. They love to celebrate their achievements and often have dinners with their friends and family. They spend quite some money for clothes and they enjoy luxurious shops where they get treated well and with respect.');
INSERT INTO humainedev.persona_personality VALUES (97, 'This user is a person who finds pleasure in everything they do. They work a lot but never miss a beer or a coffee date with their loved ones. They also enjoy shopping and they take pleasure, fun, and enjoyment from buying clothes for different occasions. Because they know that, they like to plan when to buy clothes, so they don’t spend money on unnecessary items.');
INSERT INTO humainedev.persona_personality VALUES (98, 'This user is a successful person who has complete confidence in their ability to reach their goals. They believe in themselves and their abilities and have a great deal of ambition to see things through to the end. Ever since they were young, they wanted an exciting and daring life, that’s why they’ve always looked for opportunities to grow and experience more. They love shopping, and love to spend time buying items they need. They like to look cool and stylish, that’s why they are constantly updating their wardrobe.');
INSERT INTO humainedev.persona_personality VALUES (99, 'This user is an independent person, who spends their time working for things they believe in. They have a unique personality and they love to explore ideas and opportunities. They love to shop, however they’re very conscious about it and only spend money on clothes they really need. When they buy clothes they buy one of a kind clothes and never want to look like everyone else.');
INSERT INTO humainedev.persona_personality VALUES (100, 'This user is a warm and friendly person who has a desire to do nice things. They are always the person you see helping a child with their homework, helping their family make dinner, or helping their friends rent an apartment. Their desire to help and their soft personality makes everyone love their energy, that’s why they have a lot of friends and people they care about. They don’t shop very often and they usually shop for special occasions. Their wardrobe consists of casual and comfortable clothes.');
INSERT INTO humainedev.persona_personality VALUES (101, 'This user is aa very reliable person with a strong sense of duty and moral obligation. They honor their commitments and always keep their word. Although sometimes it is difficult, this user understands their role in life and works diligently toward achieving their goals. They are the provider of the family, and are very serious about keeping their family gathered in their free time. They love to spend time with their family, and always save up their weekends for them. When it comes to shopping, they are very rational buyers. They only shop for necessary clothes and usually do that when there are sales in shops. Their wardrobe consists of casual plain clothes that are comfortable for everyday use.');
INSERT INTO humainedev.persona_personality VALUES (102, 'This user is a person who takes their job very seriously. They worked their way up to the position they’re in and they’re constantly looking to be better and better at their job. Because they have a job that requires a lot of work, they don’t have much time to shop for clothes. This has made them more conscious in buying clothes, that’s why they only buy clothes in sales. This way, they’re not tempted to buy clothes all the time, plus they save money in that process. Their wardrobe consists of casual comfortable clothes, with a hint of elegance in order to show the person’s powerful position in their job.');
INSERT INTO humainedev.persona_personality VALUES (103, 'This user is a person who doesn’t take no for an answer. When they want something, it is very hard to stop them. This behavior has helped them in so many ways in their job, as they have achieved all their goals in their career. They are highly successful in their profession and are willing to work all day and night to get things done. They don’t have time to buy clothes often, that’s why they usually buy for necessities. However, they are very passionate about fashion and although they don’t shop often, they always choose the best items to wear. When they like something they’re willing to wait for months until they get it, and they will search several shops to get them.');
INSERT INTO humainedev.persona_personality VALUES (104, 'This user is a person who takes pleasure in doing everything they do.  They are enthusiastic about life in general and see every experience as an adventure. They work hard in their job, but also love to celebrate the success of themselves and their circles. They love to organize gatherings and events and are always willing to help when someone else does. When it comes to shopping, they highly enjoy it. However, because they have high levels of self-control, they don’t let themselves buy unnecessary items. It only happens when they have events to attend that they let themselves enjoy shopping and treating themselves by buying whatever they want.');
INSERT INTO humainedev.persona_personality VALUES (105, 'This user is a reliable person who loves to work and contribute to any company they work at. They have had multiple roles in companies and they’ve succeeded in each one of them. They started small but when you look at them now, you notice how much they’ve grown not only in their profession but personally as well. This person knows their value that’s why they never hesitate to do things for themselves. Whether they just take a walk or buy expensive watches, they take time to celebrate their successes. They love shopping, but they only allow themselves to buy whatever they like when there are sales. Their wardrobe consists of casual but chic clothes and they''re always well dressed.');
INSERT INTO humainedev.persona_personality VALUES (106, 'This user is a person who works as a sales manager for x brand. They love their job and and are always working to bring costumers to the shop. Because they are exposed to a lot of people with different styles, they are able to discriminate between things they love and things they don’t. They notice how the same product comes in a large amount and they see how many people use the same products. Considering that, they love to buy items that are more unique and no one has. They usually buy from limited edition collection, and are willing to search a lot until they find something that resembles their personality.');
INSERT INTO humainedev.persona_personality VALUES (107, 'This user is a kind and loving person, who cares deeply about their loved ones. Although they work all day and have a busy life, they always manage to do things that make their family happy. They buy gifts for their family, help them with whatever they need, or organize gatherings for all the family to get together. Everyone who knows this user knows how much value their circle has for them, and everyone loves their loving spirit. This user usually shops for necessary items that they need for everyday, but once they find something they love, they keep buying it because they don’t like changing their style.');
INSERT INTO humainedev.persona_personality VALUES (108, 'This user is a person who cares deeply about their family, and is very committed to keeping the family traditions alive. They are always the ones organizing family gatherings, calling relatives for dinner, going on coffee dates with their loved ones etc. They want to keep their family together and never plan to do things on their own. They usually only shop for necessities, because they know that whenever they go to the mall they will want to buy something. That’s why they usually save items they want to buy, but wait for a long time to see if they still need it, and if they don’t, they don’t buy it at all. Their wardrobe consists of basic and comfortable clothes for everyday use.');
INSERT INTO humainedev.persona_personality VALUES (109, 'This user is a life enthusiast who is constantly looking for opportunities to engage with others. They love to hang out with their friends, organize parties or participate in artistic events. They enjoy spending time with people and are never too lazy for a cup of coffee with the people they love. Because of these traits, everyone loves to spend time with them, and they have used their skills to achieve many goals. They have a powerful position in society and use that to do good deeds for their community. Because they’re busy all the time, they don’t have time for shopping, so they usually buy only for necessities. Although needed for every day use, they like to buy clothes that represent their personality, so they are quite careful when choosing their clothes.');
INSERT INTO humainedev.persona_personality VALUES (110, 'This user is a hardworking, and energetic person who has worked all their life to achieve their goals. They are goal oriented and once they set the eye on something, they will complete it no matter what. Their best quality however is not hard work, but their closeness with other people. They have a way to make everyone feel loved and comfortable, and everyone is inspired by their empathic personality. Their whole life they worked for big projects that included helping other people, and now they are the executive director of a big NGO. They are usually in back to back meetings so they don’t have time to spend on shopping, but when they need items, they are willing to spend many hours a day until they find what they need.');
INSERT INTO humainedev.persona_personality VALUES (111, 'This user is a kind hearted and understanding person, who has dedicated their life to bettering the life of their community. They care for the wellbeing of everyone, and work hard to increase their quality of life. They believe that everyone should have the same rights, and they advocate for equity, that’s why they always take volunteering opportunities to help the vulnerable ones or contributing with donations for research etc. They are friendly, loving and most importantly, always there for their loved ones. These traits make everyone be inspired by them, and take them as role models. Because they are always on the run to doing something, they don’t have time to do shopping often, however, when they do, they try to buy from brands that fight for causes they believe in, and this way they get to buy what they need, and also help the community.');
INSERT INTO humainedev.persona_personality VALUES (112, 'This user is a family person, who spends their life enhancing the wellbeing of their family. They try to tell their kids how important family is, that’s why when they are not working they are always spending time with their relatives. They attend birthday parties, organize family gatherings and visit relatives often. They are full of life, energy, and positivity, and their energy attracts everyone who stays with them. This user loves to shop and they usually do it for everyday use items as well as events they need to attend.');
INSERT INTO humainedev.persona_personality VALUES (113, 'This user is a conventional person who has lived their life following rules and traditions. They are friendly, family oriented and they love to spend time with the people they love. They try to spend as much time as they can with their family, that’s why, although they work all day, at night they get home and watch movies with their family. They try hard to make their children connected to their relatives that’s why they often visit relatives or call them for dinners. They don’t have much time to shop and when they do, they usually do it when they get new jobs or job promotions.');
INSERT INTO humainedev.persona_personality VALUES (115, 'This user is a hardworking person, who has dedicated their life to bettering the life of their community. They are humble, honest, and kind and are always looking for opportunities to grow. Because of their personality and hard work, they have achieved most of their goals in life, and they don’t plan to stop with what they have. Their style is quite simple, usually wearing casual clothes, but they make sure to always buy from trusted brand only. They buy from the brands they know the quality is good, so that they don’t have to buy clothes often.');
INSERT INTO humainedev.persona_personality VALUES (116, 'This user is a kind hearted person who loves what they do, and doesn’t stop until they achieve what they started. Although they deeply care about others and care what others think about them, they have also learned to be dependent on themselves only, and feel more comfortable making decisions on their own. They want to hear different opinions, but at the end they decide what they think is best for everything. They like for their style to represent their personality, that’s why although they buy casual clothes, they make sure those items have some unique details that only they could be represented in.');
INSERT INTO humainedev.persona_personality VALUES (117, 'This person is a kind, considerate and helpful person. They care deeply about their circle and are willing to do anything to enhance their wellbeing. They’re always the ones compromising in order for something to happen, but they don’t mind because they take pleasure in helping others. When it comes to shopping, they usually do that when they need to buy items for daily use or family events. They only buy from trusted brands because the quality of the clothes is very important for them.');
INSERT INTO humainedev.persona_personality VALUES (118, 'This user is a well-liked person, who everyone loves to stay with. They are kind, understanding and forgiving, and they radiate a positive energy that draws everyone to stay with them. They are the person who have a lot of friends, and they rarely have conflicts with anyone. This user is willing to change their desires just to be in peace with others, and that’s why people appreciate this behavior. This user loves to shop and tries to wear clothes that are in for the season. They usually buy from clothes that famous people buy, because they believe that those brands are more trust-worthy than the others.');
INSERT INTO humainedev.persona_personality VALUES (119, 'This user is a good natured person, who takes the relationship with their loved ones very serious. They are very careful when it comes to arguments with people they love, and they try their hardest to remain calm and find a compromise for everyone to feel happy. They are easy to be with, they’re humble and they are always doing good deeds. When it comes to shopping, they usually do it for daily use items, and they try to always buy high quality items because they are always on the run for a meeting and need to have comfortable clothes.');
INSERT INTO humainedev.persona_personality VALUES (120, 'This user is a person who doesn’t take no for an answer. They worked hard all their life and once they committed to something, they did everything to achieve it. They are  cooperative, reliable and trustworthy, and that’s why people love to work with them. When it comes to shopping, they usually buy items when necessary. They have some favorite shops and they only buy there because they don’t want to spend their time looking for new brands, when they could work on something during that time.');
INSERT INTO humainedev.persona_personality VALUES (121, 'This user is a person who loves to explore and figure things out on their own. They always wanted to be independent, although they never put aside their relationships with their circle. They are caught between doing what they want and between what is expected from them, and that has made them quite logical and conscious for every decision they make. They are straight forward, honest and work hard for what they believe in. Their strong personality is what draws people in and everyone wants to be around them. They like shopping and although they buy for necessities, they always manage to grab attention and look well dressed.');
INSERT INTO humainedev.persona_personality VALUES (122, 'This user is a benevolent person, who is constantly looking for ways to help the people they love. They are kind hearted, altruistic and very loving towards their circle. They’re always the person who organizes family dinners and dates and they love to spend as much time as they can with them. This user doesn’t have much time to shop because they’re always busy doing something for their job or their family, but every once in a while they like to go shopping and buy clothes for different events and gatherings.');
INSERT INTO humainedev.persona_personality VALUES (123, 'This user is a friendly and well-liked person, who values their relationships with others. They are kind and loving and they are very supportive of their friends. They have a strong sense of belonging to their group, that’s why they are willing to do anything to be in good terms with them. They love shopping and they love to follow the latest fashion trends.');
INSERT INTO humainedev.persona_personality VALUES (124, 'This user is a kind person who takes it very seriously to maintain the family relations. They care deeply about their loved ones and want to keep the spirit of family alive. They are considerate, helpful and forgiving and they put family first no matter what happens. When it comes to shopping they usually do it for necessities, but also when they have family gatherings. They buy from brands they trust and quality is very important to them.');
INSERT INTO humainedev.persona_personality VALUES (125, 'This user is an active person who works hard to get what they want. They are very motivated and committed to being successful, that’s why they take no days off. Apart from their occupation, they often get involved with altruistic activities such as charities. Because of their energy and discipline, a lot of people see them as role models. They love to shop, but they don’t buy from every brand. To them it is quite important for the products to have high quality and that’s why they only buy from brands that are well trusted, and the people they know buy from.');
INSERT INTO humainedev.persona_personality VALUES (126, 'This user is an independent person who is always working to build a life they love. They appreciate honesty and confidence, and they believe that that helped them achieve their goals. They are not afraid to speak their minds, but they are able to do that without offending anyone. Their compassion makes them easy to hang out with, and everyone appreciates the way they speak the truth without hurting anyone. They love shopping and sometimes buy more than they should, but they cannot resist buying a good vintage item that is only available for a short period of time.');
INSERT INTO humainedev.persona_personality VALUES (127, 'This user is a kind hearted person, who has dedicated their life to building a good life for them and their family. They love to keep the family spirit alive, that’s why they always organize gatherings and events with them. After work, they always visit their relatives, stay in for movie nights with their family, or go for coffee dates with their cousins. They don’t buy clothes often, and when they do that is usually when a friend or family suggests a good brand. They usually buy for necessities and daily use items.');
INSERT INTO humainedev.persona_personality VALUES (128, 'This user is a friendly and well-liked human who is constantly spending time with their friends or family. They are humble, honest and reliable and take pleasure in helping others. Their weakness is not being able to say ‘no’ to anyone. When it comes to fashion, they follow the trend of the seasons. They are very fashionable and always look well-dressed.');
INSERT INTO humainedev.persona_personality VALUES (129, 'This user is a kind person, who has always put others’ interests before their own. They are helpful, empathic and altruistic and take pleasure in helping others achieve their goals. They care deeply about their relationships that’s why they are willing to compromise their ideas and wishes just to get an agreement with their loved ones. They don’t shop often, and when they do they usually buy necessities. They like to buy from brands they know or the ones that the people they like suggest.');
INSERT INTO humainedev.persona_personality VALUES (130, 'This user is an enthusiastic person who is constantly doing something. They are constantly working to achieve their goals, and everyone who knows them knows that they are perfectionists. They are highly competitive and always have to be the best in what they do. This tendency to be perfect in every situation makes them view minor problems as overwhelming. They need to constantly be affirmed that things are going to be alright, and sometimes that drains them. When it comes to shopping, they usually buy when the new seasons come, but they are always tempted to buy clothes when they go to a shop without planning before hand.');
INSERT INTO humainedev.persona_personality VALUES (131, 'This user is a person who loves to be challenged. They are willing to do anything to get what they want and they don’t care if it may take them too long to get that. When they’re not working they always do fun activities such as hiking, bungee jumping or taking motorcycle lessons. They don’t like the ordinary life and want to always do something that gets their adrenaline levels up. They love to shop and when they go to the mall they rarely come home with one bag only. They are very fashionable and obsessed with fashion, that every time they go shopping they buy a lot of items for their wardrobe.');
INSERT INTO humainedev.persona_personality VALUES (132, 'This user is an emotional person who feels too much and is often worried about daily situations. Their whole life they have tried to do everything the way they wanted, and they always wanted to do it in a perfect manner. This attitude however, made them feel anxious because thy weren’t able to control every situation in their life. They’re loving, supportive and they spend most of their time with their family. They usually shop for necessities and are very careful to not overspend when buying clothes.');
INSERT INTO humainedev.persona_personality VALUES (133, 'This user is a person who is interested in the wellbeing of everyone around them. They worry about every thing and they preoccupy their mind with scenarios that never happen. This concern has made them quite a passive person because they are always afraid something might happen. However, this doesn’t apply to their job position. When it comes to their work, they are very disciplined, they work hard and always achieve their goals. They are willing to work day and night just to finish the job they started, and a lot of people see them as role models because of this. They don’t often buy clothes, but they usually do when items they like have discounts and they’re afraid they might lose the opportunity to buy them. They usually buy when the brands they love have sales, and they always manage to buy beautiful and unique items.');
INSERT INTO humainedev.persona_personality VALUES (134, 'This user is a perfectionist person who has to do everything in the best manner possible. They’re hardworking and don’t stop until they achieve what they set their eye to. Although they are successful and very inspirational to the eye of the stranger, they are quite drained by their need for perfection. This has made them indecisive in many aspects in their life, and has increased the levels of criticism to themselves. When it comes to shopping, this person loves to. They thrive in the purchase of clothing. They buy often and usually plan ahead what they want to buy. They are willing to look everywhere to find the perfect item and don’t get tired to look all day until they purchase what they need.');
INSERT INTO humainedev.persona_personality VALUES (135, 'This user is a kind hearted person whose mission in life is to have harmonious relationships with their loved ones. They are friendly, helpful and considerate. They try to help everyone they can and are always the first person people think of when they need assistance on something. They’re always the one organizing family dinners or gatherings, or helping others organizing them because they love to spend their free time with their loved ones. When it comes to shopping, they usually shop for necessities, but every once in a while they like to buy items that they might not use several times.');
INSERT INTO humainedev.persona_personality VALUES (136, 'This user is an energetic person, who is constantly looking for a new challenge. They’re open, enthusiastic and doesn’t have a problem making friends. They’re always the center of attention and are very good at leadership roles. This user loves to shop but they try to control their shopping by buying necessary items. Only every once in a while they allow themselves to buy more than they need, and in those times they try to be conscious about their spendings.');
INSERT INTO humainedev.persona_personality VALUES (137, 'This user is a successful person who has worked their whole life to create a good life for themselves and their family. They are discplned, responsible and reliable, that’s why everyone looks up to them when it comes to getting things done. They are busy most of the time and don’t have time for shopping. Even when they do, they usually buy necessary items that they can use daily and for longer periods. They have a couple of shops they buy from and don’t like to change because they always find what they need in those shops.');
INSERT INTO humainedev.persona_personality VALUES (138, 'This user is an open minded individual who is eager to learn about the world and explore different opportunities and experiences. They are always the person in the group  who orders the most exotic thing on the menu, making traveling arrangements and having broad interests on many topics. They love shopping and they usually buy unique and beautiful clothes. The quality of clothes is also very important for them because they want to be able to wear them on longer periods and for different occasions.');


ALTER TABLE humainedev.master_attributes ADD CONSTRAINT fkb5j1svf0suocrxfjqtepn46u7 FOREIGN KEY (product_id) REFERENCES humainedev.master_product(id);
ALTER TABLE humainedev.master_attributes ADD CONSTRAINT fkm45wdnmcg76dirr782wjgshn2 FOREIGN KEY (account_accountid) REFERENCES humainedev.accountmaster(accountid);