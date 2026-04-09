/* ==============
    USERS QUERIES 
=================*/

-- EXISTS EMAIL IN USERS?
SELECT EXISTS(SELECT 1 FROM users WHERE email = 'client1@virtualtechseller.com');

-- USER CREDENTIAL MATCH?
SELECT EXISTS(SELECT 1 FROM users WHERE email = 'client1@virtualtechseller.com' AND password = '$2a$12$1hrlxwUGJgoYtWIWjLpY2eJZTPtYLMgf2vVUUvadaTl3WX20YibhO');

-- USER EMAIL IS ACTIVATED?
SELECT email_activate FROM users WHERE email = 'client1@virtualtechseller.com';

-- GET USER ROLE WHEN LOGIN BY EMAIL
SELECT rol FROM users WHERE email = 'client1@virtualtechseller.com';

-- REGISTER NEW USER (CLIENT)
INSERT INTO users(name, last_name, phone, email, password, rol, created_date, credit, email_activate) VALUES 
('Sergio', 'Benitez Leon', '600111121', 'client10@virtualtechseller.com', '$2a$12$1hrlxwUGJgoYtWIWjLpY2eJZTPtYLMgf2vVUUvadaTl3WX20YibhO', 'CLIENT', CURRENT_DATE, 0, 0);

-- GET NECESARY USER DATA BY EMAIL
SELECT id_user, name, last_name, phone, credit FROM users WHERE email = 'client1@virtualtechseller.com';

-- UPDATE USER CREDIT SENDING CREDIT TO ADD
UPDATE users SET credit = credit + 20.33 WHERE id_user = 7 AND rol = 'CLIENT';

-- CHECK CONSTRAINT TO AVOID ADD CREDIT TO ADMIN OR MODERATOR
UPDATE users SET credit = 100 WHERE rol = 'ADMIN' OR rol = 'MODERATOR';

-- CHECK USER HAS ENOUGH CREDIT
SELECT credit >= 245.34 FROM users WHERE id_user = 8;

-- GRANT ACCESS TO USER
UPDATE users SET email_activate = true WHERE email = 'client6@virtualtechseller.com';

-- UPDATE USER DATA
UPDATE users SET name = 'Paco', last_name = 'Benitez Garcia', phone = '600111222', email = 'client20@virtualtechseller.com' , password = '$2a$12$1hrlxwUGJgoYtWIWjLpY2eJZTPtYLMgf2vVUUvadaTl3WX20YibhO', rol = 'CLIENT' WHERE id_user = 9;

/* =================
    PRODUCTS QUERIES 
====================*/

-- UPDATE QUANTITY STOCK ITEM AFTER A SALE
UPDATE products_exhibitions SET quantity = quantity - 2 WHERE id_exhibition = 1 AND id_product = 10 AND quantity >= 2;

-- CHECK IF STOCK IS 0
SELECT stock = 0 FROM new_products WHERE id_product = 7;

-- SET A PRODUCT NOT AVAILABLE
UPDATE products SET available = 0 WHERE id_product = 4 AND available = 1;

-- DECREASE NEW PRODUCT STOCK
UPDATE new_products SET stock = stock - 2 WHERE id_product = 1 AND stock >= 2;

-- GET ALL PRODUCTS ASSOCIATED OR NOT TO EXHIBITION
SELECT p.id_product, p.name AS product_name, p.description, np.stock, c.name AS category_name, e.id_exhibition, e.name AS exhibition_name, pe.quantity 
FROM products p INNER JOIN categories c ON c.id_category = p.id_category 
LEFT JOIN new_products np ON np.id_product = p.id_product 
LEFT JOIN products_exhibitions pe ON pe.id_product = p.id_product 
LEFT JOIN exhibitions e ON e.id_exhibition = pe.id_exhibition;


/* =================
    SALES QUERIES 
====================*/

-- INSERT A NEW SALE
INSERT INTO sales(id_user, id_product, id_exhibition, quantity, total_price, purchase_date) VALUES (8,10,1,2,158.12,'2026-05-03');

-- GET DATA SALES(TOTAL PRICE, PURCHASE_DATE, QUANTITY) AND DETAILS PRODUCTS FROM SINGLE USER BY ID
SELECT s.quantity, s.total_price, s.purchase_date, p.name AS product_name, p.description, p.id_product, p.url_image, 
c.name AS category_name, c.id_category, np.id_product AS new_id, up.id_product AS used_id, e.name AS event_name 
FROM sales s INNER JOIN products p ON p.id_product = s.id_product 
LEFT JOIN new_products np ON np.id_product = p.id_product 
LEFT JOIN used_products up ON up.id_product = p.id_product 
INNER JOIN categories c ON c.id_category = p.id_category 
INNER JOIN exhibitions e ON e.id_exhibition = s.id_exhibition 
WHERE s.id_user = 5;

-- GET ALL USERS SALES FOR CHECK DATA ANALYST
SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, s.quantity, s.total_price, s.purchase_date, p.id_product, p.name AS prod_name 
FROM sales s INNER JOIN users u ON s.id_user = u.id_user 
INNER JOIN exhibitions e ON s.id_exhibition = e.id_exhibition 
INNER JOIN products p ON p.id_product = s.id_product;

-- GET SALE BETWEEN DATES ORDER BY USER ALPHA
SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, 
s.quantity, s.total_price, s.purchase_date, p.id_product, p.name AS prod_name 
FROM sales s INNER JOIN users u ON s.id_user = u.id_user 
INNER JOIN exhibitions e ON s.id_exhibition = e.id_exhibition 
INNER JOIN products p ON p.id_product = s.id_product 
WHERE s.purchase_date BETWEEN '2026-03-10' AND '2026-03-20' ORDER BY user_name ASC;

/* ===================
    EXHIBITION QUERIES 
======================*/

-- INSERT NEW EXHIBITION
INSERT INTO exhibitions(name, description, init_date, end_date) VALUES ('evento1', 'descripcion1', '2026-5-25',  '2026-7-30');

--UPDATE DATA EXHIBITION
UPDATE exhibitions SET name = 'evento1', description = 'evento de prueba', init_date = '2026-6-12', end_date = '2026-8-20' WHERE id_exhibition = 3;

-- GRANT ACCESS TO EXHIBITION
UPDATE exhibitions SET active = 1 WHERE id_exhibition = 4;

-- DECREASE PRODUCT EXHIBITION WHEN A CLIENT BUY ITEM
UPDATE products_exhibitions SET quantity = quantity - 1 WHERE id_exhibition = 1 AND id_product = 1 AND quantity >= 1;

-- GET ALL EXHIBITIONS(AFTER THIS, FRONT-END ASSIGN A PLACE)
SELECT id_exhibition, name, description, init_date, end_date, active FROM exhibitions;

-- GET STOCK PRODUCT ASSIGNED TO SPECIFIC EXHIBITION
SELECT np.stock, p.available, ph.quantity FROM products p LEFT JOIN new_products np ON p.id_product = np.id_product 
INNER JOIN products_exhibitions ph ON ph.id_product = p.id_product AND ph.id_exhibition = 1 
WHERE p.id_product = 1;

/* ====================
    CART-ITEMS-PRODUCT
=======================*/

-- GET PRODUCTS DETAILS(NAME, DESCRIPTION, URL_IMAGE, CATEGORY, QUANTITY...) THAT USER  HAS IN CART, QUANTITY, AND KNOW IF IT'S NEW OR USED BY ID
SELECT p.id_product, p.name AS product_name, p.description AS product_description, p.base_price, p.url_image, c.id_category, c.name AS category_name, c.description AS category_description, 
np.id_product AS new_id, np.stock, np.release_date, up.id_product AS used_id, up.discount, up.remark, cart.quantity 
FROM cart_items cart INNER JOIN products p ON p.id_product = cart.id_product 
LEFT JOIN new_products np ON np.id_product = p.id_product 
LEFT JOIN used_products up ON up.id_product = p.id_product 
INNER JOIN categories c ON c.id_category = p.id_category 
WHERE cart.id_user = 6;

-- REMOVE CART ITEM WHEN USER REQUEST
DELETE FROM cart_items WHERE id_user = 8 AND id_product = 10;

-- REMOVE ALL USER CART ITEMS (CLEAR CART)
DELETE FROM cart_items WHERE id_user = 8;

-- ADD NEW PRODUCT TO CART ITEM (IF EXISTS INCREASE QUANTITY)
INSERT INTO cart_items(id_user, id_product, id_exhibition, quantity) VALUES (8, 5, 2, 2) ON DUPLICATE KEY UPDATE quantity = quantity + 2;

/* ========
    VISITS
===========*/

-- SET A NEW VISIT TO EXHIBITION BUT IF EXISTS UPDATE IT + 1 
INSERT INTO user_visits(id_user, id_exhibition, last_visit, visit_counter) VALUES (8, 1, CURRENT_DATE, 1) ON DUPLICATE KEY UPDATE visit_counter = visit_counter + 1, last_visit = CURRENT_DATE;

-- GET ALL USER VISITS
SELECT e.id_exhibition, e.name, e.description, e.init_date, e.end_date, e.active, uv.last_visit, uv.visit_counter 
FROM user_visits uv INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition 
WHERE uv.id_user = 5;

-- GET ALL VISITS EACH USER TO EXHIBITION
SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, uv.last_visit, uv.visit_counter 
FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user 
INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition;

-- GET AMOUNT VISITS TO DETERMINATE EXHIBITION
SELECT SUM(visit_counter) AS amount_visits FROM user_visits WHERE id_exhibition = 1;

-- GET VISITS BETWEEN TWO SELECTED VALUES
SELECT u.id_user, u.name AS user_name, u.last_name, e.id_exhibition, e.name AS event_name, uv.last_visit, uv.visit_counter 
FROM user_visits uv INNER JOIN users u ON uv.id_user = u.id_user 
INNER JOIN exhibitions e ON uv.id_exhibition = e.id_exhibition 
WHERE uv.visit_counter BETWEEN 2 AND 5 ORDER BY uv.visit_counter DESC;

-- GET ALL VISITS GROUP BY NAME WITHOUT USE JOIN
SELECT e.name, (SELECT SUM(uv.visit_counter) FROM user_visits uv WHERE uv.id_exhibition = e.id_exhibition) AS amount_visit FROM exhibitions e 
WHERE e.id_exhibition IN ( SELECT uv.id_exhibition FROM user_visits uv ) ORDER BY amount_visit DESC;