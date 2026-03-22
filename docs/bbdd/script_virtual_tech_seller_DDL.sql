-- CREATE DATABASE
-- ==========================================
CREATE DATABASE virtual_tech_seller_db;
-- ==========================================
USE virtual_tech_seller_db;
-- ==========================================

-- CREATE TABLES

-- USERS
CREATE TABLE
    users (
        id_user INT AUTO_INCREMENTAL PRIMARY KEY,
        name VARCHAR(30) NOT NULL,
        last_name VARCHAR(60) NOT NULL,
        phone VARCHAR(15),
        email VARCHAR(150) NOT NULL UNIQUE,
        password VARCHAR(150) NOT NULL,
        rol VARCHAR(20) NOT NULL ENUM('CLIENT', 'ADMIN', 'MODERATOR'),
        created_date DATE NOT NULL DEFAULT CURRENT_DATE,
        credit DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (credit >= 0),
        email_activate BOOLEAN NOT NULL DEFAULT FALSE
    );

-- EXHIBITIONS
CREATE TABLE
    exhibitions (
        id_exhibition INT AUTO_INCREMENTAL PRIMARY KEY,
        name VARCHAR(30) NOT NULL UNIQUE,
        description VARCHAR(150) NOT NULL,
        init_date DATE NOT NULL,
        end_date DATE NOT NULL,
        active BOOLEAN DEFAULT FALSE
    );

-- CATEGORIES
CREATE TABLE
    categories (
        id_category INT AUTO_INCREMENTAL PRIMARY KEY,
        name VARCHAR(30) NOT NULL,
        description VARCHAR(150) NOT NULL
    );

-- PRODUCTS
CREATE TABLE
    products (
        id_product INT AUTO_INCREMENTAL PRIMARY KEY,
        id_category INT NOT NULL,
        name VARCHAR(60) NOT NULL UNIQUE,
        description VARCHAR(250) NOT NULL,
        url_image VARCHAR(150) NOT NULL UNIQUE,
        base_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (base_price >= 0),
        available BOOLEAN NOT NULL DEFAULT TRUE,
        CONSTRAINT fk_product_category FOREIGN KEY (id_category) REFERENCES categories (id_category) ON DELETE RESTRICT
    );

-- USER_VISITS
CREATE TABLE
    user_visits (
        id_user INT NOT NULL,
        id_exhibition INT NOT NULL,
        last_visit DATE NOT NULL DEFAULT CURRENT_DATE,
        visit_counter INT NOT NULL DEFAULT 1,
        PRIMARY KEY (id_user, id_exhibition),
        CONSTRAINT fk_visit_user FOREIGN KEY (id_user) REFERENCES users (id_user) ON DELETE CASCADE,
        CONSTRAINT fk_visit_exhibition FOREIGN KEY (id_exhibition) REFERENCES exhibitions (id_exhibition) ON DELETE CASCADE
    );

-- PRODUCTS_EXHIBITIONS
CREATE TABLE
    products_exhibitions (
        id_product INT NOT NULL,
        id_exhibition INT NOT NULL,
        PRIMARY KEY (id_product, id_exhibition),
        CONSTRAINT fk_pe_product FOREIGN KEY (id_product) REFERENCES products (id_product) ON DELETE CASCADE,
        CONSTRAINT fk_pe_exhibition FOREIGN KEY (id_exhibition) REFERENCES exhibitions (id_exhibition) ON DELETE CASCADE
    );

-- NEW_PRODUCTS
CREATE TABLE
    new_products (
        id_product INT PRIMARY KEY NOT NULL,
        stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
        release_date DATE NOT NULL,
        CONSTRAINT fk_new_product_parent FOREIGN KEY (id_product) REFERENCES products (id_product) ON DELETE CASCADE
    );

-- USED_PRODUCTS
CREATE TABLE
    used_products (
        id_product INT PRIMARY KEY NOT NULL,
        discount DECIMAL(5, 2) NOT NULL DEFAULT 0.00 CHECK (
            discount >= 0
            AND discount <= 100
        ),
        remark VARCHAR(200) NOT NULL,
        CONSTRAINT fk_used_product_parent FOREIGN KEY (id_product) REFERENCES products (id_product) ON DELETE CASCADE
    );

-- SALES
CREATE TABLE
    sales (
        id_sale SERIAL PRIMARY KEY,
        id_product INT NOT NULL,
        id_user INT NOT NULL,
        id_exhibition INT NOT NULL,
        quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
        total_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (total_price >= 0),
        purchase_date DATE NOT NULL DEFAULT CURRENT_DATE,
        CONSTRAINT fk_sale_product FOREIGN KEY (id_product) REFERENCES products (id_product),
        CONSTRAINT fk_sale_user FOREIGN KEY (id_user) REFERENCES users (id_user),
        CONSTRAINT fk_sale_exhibition FOREIGN KEY (id_exhibition) REFERENCES exhibitions (id_exhibition)
    );

-- CART_ITEMS
CREATE TABLE
    cart_items (
        id_user INT NOT NULL,
        id_product INT NOT NULL,
        quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
        PRIMARY KEY(id_user, id_product),
        CONSTRAINT fk_cart_user FOREIGN KEY (id_user) REFERENCES users (id_user) ON DELETE CASCADE,
        CONSTRAINT fk_cart_product FOREIGN KEY (id_product) REFERENCES products (id_product) ON DELETE CASCADE
    );