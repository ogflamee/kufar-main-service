INSERT INTO users (id, email, username, password, phone_number, rating, created_at) VALUES
(100, 'test@mail.com', 'test_user', 'test', '+100000000', 0, NOW());

INSERT INTO categories (id, name) VALUES
(100, 'TestCategory');

INSERT INTO ads (id, title, description, price, city, views_count, status, user_id, category_id, created_at) VALUES
(100,'Test Ad 1','desc',10,'TestCity',0,'ACTIVE',100,100,NOW()),
(101,'Test Ad 2','desc',11,'TestCity',0,'ACTIVE',100,100,NOW()),
(102,'Test Ad 3','desc',12,'TestCity',0,'ACTIVE',100,100,NOW()),
(103,'Test Ad 4','desc',13,'TestCity',0,'ACTIVE',100,100,NOW()),
(104,'Test Ad 5','desc',14,'TestCity',0,'ACTIVE',100,100,NOW()),
(105,'Test Ad 6','desc',15,'TestCity',0,'ACTIVE',100,100,NOW()),
(106,'Test Ad 7','desc',16,'TestCity',0,'ACTIVE',100,100,NOW()),
(107,'Test Ad 8','desc',17,'TestCity',0,'ACTIVE',100,100,NOW()),
(108,'Test Ad 9','desc',18,'TestCity',0,'ACTIVE',100,100,NOW()),
(109,'Test Ad 10','desc',19,'TestCity',0,'ACTIVE',100,100,NOW()),
(110,'Test Ad 11','desc',20,'TestCity',0,'ACTIVE',100,100,NOW()),
(111,'Test Ad 12','desc',21,'TestCity',0,'ACTIVE',100,100,NOW());

INSERT INTO favorites (id, user_id, ad_id) VALUES
(100, 100, 100),
(101, 100, 101),
(102, 100, 102);

SELECT setval('ads_id_seq', (SELECT MAX(id) FROM ads));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));
SELECT setval('favorites_id_seq', (SELECT MAX(id) FROM favorites));