/* USERS ACCESS TO DB */
CREATE USER 'default_user'@'localhost' IDENTIFIED BY 'default123';
CREATE USER 'client_user'@'localhost' IDENTIFIED BY 'client123';
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'admin123';
CREATE USER 'moderator_user'@'localhost' IDENTIFIED BY 'moderator123';

/* USERS ROLES */
CREATE ROLE 'role_default';
CREATE ROLE 'role_client';
CREATE ROLE 'role_admin';
CREATE ROLE 'role_analyst';

/* ASSIGN ROLE TO USERS */
GRANT role_default TO 'default_user'@'localhost';
GRANT role_client TO 'client_user'@'localhost';
GRANT role_admin TO 'admin_user'@'localhost';
GRANT role_analyst TO 'moderator_user'@'localhost';

/* SET DEFAULT ROLE TO START DB */
SET DEFAULT ROLE 'role_default' FOR 'default_user'@'localhost';
SET DEFAULT ROLE 'role_client' FOR 'client_user'@'localhost';
SET DEFAULT ROLE 'role_admin' FOR 'admin_user'@'localhost';
SET DEFAULT ROLE 'role_analyst' FOR 'moderator_user'@'localhost';

/* PERMISSIONS ROLE */

/* DEFAULT */
GRANT SELECT, INSERT ON virtual_tech_seller_db.users TO 'role_default';

/* CLIENT */
GRANT SELECT, UPDATE, DELETE, INSERT ON virtual_tech_seller_db.cart_items TO 'role_client';
GRANT SELECT ON virtual_tech_seller_db.categories TO 'role_client';
GRANT SELECT ON virtual_tech_seller_db.exhibitions TO 'role_client';
GRANT SELECT, UPDATE ON virtual_tech_seller_db.new_products TO 'role_client';
GRANT SELECT, UPDATE ON virtual_tech_seller_db.used_products TO 'role_client';
GRANT SELECT, UPDATE ON virtual_tech_seller_db.products TO 'role_client';
GRANT SELECT, UPDATE ON virtual_tech_seller_db.products_exhibitions TO 'role_client';
GRANT SELECT, UPDATE ON virtual_tech_seller_db.users TO 'role_client';
GRANT SELECT, INSERT ON virtual_tech_seller_db.sales TO 'role_client';
GRANT SELECT, INSERT, UPDATE ON virtual_tech_seller_db.user_visits TO 'role_client';

/* ADMIN */
GRANT SELECT, UPDATE ON virtual_tech_seller_db.users TO 'role_admin';
GRANT SELECT, UPDATE, INSERT ON virtual_tech_seller_db.exhibitions TO 'role_admin';
GRANT SELECT, INSERT ON virtual_tech_seller_db.categories TO 'role_admin';
GRANT SELECT, UPDATE, INSERT ON virtual_tech_seller_db.products TO 'role_admin';
GRANT SELECT, UPDATE, INSERT ON virtual_tech_seller_db.new_products TO 'role_admin';
GRANT SELECT, UPDATE, INSERT ON virtual_tech_seller_db.used_products TO 'role_admin';
GRANT SELECT, UPDATE, INSERT, DELETE ON virtual_tech_seller_db.products_exhibitions TO 'role_admin';

/* MODERATOR */
GRANT SELECT ON virtual_tech_seller_db.users TO 'role_analyst';
GRANT SELECT ON virtual_tech_seller_db.user_visits TO 'role_analyst';
GRANT SELECT ON virtual_tech_seller_db.exhibitions TO 'role_analyst';
GRANT SELECT ON virtual_tech_seller_db.sales TO 'role_analyst';
GRANT SELECT ON virtual_tech_seller_db.products TO 'role_analyst';
