-- SCRIPT TO LOAD TABLES
INSERT INTO exhibitions(name, description, init_date, end_date, active) VALUES
    ('TecnoFest', 'Novedades informáticas', '2026-03-02', '2026-06-30', TRUE),
    ('RetroGamer', 'Solo juegos de catálogo antiguo', '2026-01-01', '2026-05-23', TRUE),
    ('Phone Event', 'Evento de SmartPhones', '2026-04-01', '2026-07-15', TRUE),
    ('Winter E-Sale', 'Catálogo tecnológico de invierno', '2025-12-21', '2026-03-20', FALSE);


INSERT INTO categories(name, description) VALUES
    ('Smartphones', 'Teléfonos inteligentes'),
    ('Videojuegos', 'Entretenimiento virtual'),
    ('Componentes PC', 'Hardware para tu equipo'),
    ('Videoconsolas', 'Equipo conectado a pantalla'),
    ('Periféricos', 'Accesorios de PC');


INSERT INTO products(id_product, name, description, base_price, url_image, available, id_category) VALUES
    (1, 'Samsung Galaxy A56', '5G, Grey, Móvil Android, 256 GB, 8 GB RAM, 6,7 LCD, Processor, 5000 mAh', 280.50, '/img/products/SamsungGalaxyA56.jpg', TRUE, 1),
    (2, 'XIAOMI Redmi Note 14', 'Negro medianoche, Móvil Android, 128 GB, 6 GB RAM, 6,67 AMOLED FHD+, MediaTek Helio G99-Ultra, 5510 mAh', 123.49, '/img/products/XIAOMIRedmiNote14.jpg', TRUE, 1),
    (3, 'iPhone 17 Pro Max', '256 GB, 5G, 6.3 OLED Super Retina XDR, Chip A19 Pro, iOS', 1220.23, '/img/products/iPhone17ProMax.jpg', TRUE, 1),
    (4, 'Samsung Galaxy A36 5G', 'Negro, 256 GB, 8 GB RAM, 6.7 FHD+ Super AMOLED, Qualcomm Snapdragon 6 Gen 3, 5000 mAh, Android 15', 200.00, '/img/products/SamsungGalaxyA365G.jpg', TRUE, 1),
    (5, 'MY ARCADE DGUNL-3283', 'Maquina Arcade con el fantástico juego Street Fighter II', 60.89, '/img/products/MYARCADEDGUNL-3283.jpg', TRUE, 4),
    (6, 'Forspoken', 'Videojuego de consola PS5', 10.45, '/img/products/PS5Forspoken.jpg', FALSE, 2),
    (7, 'Zombie Cure Lab', 'Videojuego de consola PS5', 12.90, '/img/products/ZombieCureLab.jpg', TRUE, 2),
    (8, 'Final Fantasy VII', 'Remake Intergrade & Rebirth PS5', 28.45, '/img/products/FinalFantasyVII.jpg', FALSE, 2),
    (9, 'Leyendas Pokémon: Z-A', 'Videojuego de la consola Nintendo Switch 2', 28.45, '/img/products/LeyendasPokemonZA.jpg', TRUE, 2),
    (10, 'MARS GAMING MPB1000SIM', 'Fuente de alimentación PC MPB1000SIM MARS GAMING, Negro', 65.34, '/img/products/FuentedealimentacionPC.jpg', TRUE, 3),
    (11, 'MARS GAMING MF3AR', 'Ventilador PC', 10.00, '/img/products/VentiladorPc.jpg', TRUE, 3),
    (12, 'TP-Link ARCHER TX50E', 'Tarjeta de red, AX3000, Wi-Fi 6, Bluetooth 5.0 PCIe, Negro', 23.57, '/img/products/TarjetaRed.jpg', FALSE, 3),
    (13, 'KINGSTON ADATA', 'Memoria RAM', 100.50, '/img/products/MemoriaRam.jpg', TRUE, 3),
    (14, 'Consola PS5 Pro', 'PlayStation 5 Pro 2TB + Juego EA Sports FC 26 Digital, 2 TB, blanco', 550.45, '/img/products/PS5PRO.jpg', TRUE, 4),
    (15, 'RETROBOX 33K', 'Consola MANALEX RETROBOX33K, 64 GB, Azul', 50.03, '/img/products/retrobox.jpg', TRUE, 4),
    (16, 'Nintendo Switch 2', '7.9 Full HD HDR 120 Hz, 256 GB, Magnetic Joy-Con 2 con modo ratón, Azul y Rojo Neón', 350.44, '/img/products/switch2.jpg', TRUE, 4),
    (17, 'Xbox Series S', 'MICROSOFT EP2-00644, 1 TB, Blanco', 350.44, '/img/products/xboxS.jpg', FALSE, 4),
    (18, 'MARS GAMING MSAURA', 'Altavoces para PC - 15 W, 2.0 canales, Bluetooth, Negro', 10.12, '/img/products/altavocesPc.jpg', TRUE, 5),
    (19, 'Epson EcoTank ET-2865', 'Impresora multifunción tinta - Color, Tinta recargable, Negro', 133.65, '/img/products/impresoraEpson.jpg', TRUE, 5),
    (20, 'Xiaomi G34 34', 'Monitor gaming - Xiaomi G34, WQHD, 1 ms, 180 Hz, FreeSync, DisplayPort x2, HDMI x2, Negro', 200.12, '/img/products/monitorXiaomi.jpg', TRUE, 5);


INSERT INTO new_products(id_product, stock, release_date) VALUES
    (1, 20, '2026-01-23'),
    (2, 13, '2025-10-20'),
    (3, 5, '2026-03-05'),
    (5, 23, '2026-01-10'),
    (6, 0, '2025-03-12'),
    (9, 12, '2026-02-15'),
    (10, 7, '2025-01-10'),
    (12, 0, '2025-08-13'),
    (13, 8, '2025-06-03'),
    (14, 3, '2025-09-13'),
    (15, 5, '2023-02-12'),
    (16, 9, '2025-11-03'),
    (18, 2, '2026-01-08'),
    (19, 7, '2025-05-03'),
    (20, 5, '2026-01-03');


INSERT INTO used_products(id_product, discount, remark) VALUES
    (4, 20, 'Como nuevo, sin daños'),
    (7, 50, 'Bastante uso'),
    (8, 15, 'Prácticamente sin usar'),
    (11, 35, '3 meses de uso diario'),
    (17, 10, 'Aún en garantía');


INSERT INTO products_exhibitions(id_product, id_exhibition, quantity) VALUES
    (1, 1, 10),
    (2, 1, 8),
    (3, 3, 3),
    (4, 1, 2),
    (5, 2, 6),
    (6, 2, 4),
    (7, 2, 2),
    (8, 2, 1),
    (9, 2, 7),
    (10, 1, 5),
    (11, 1, 2),
    (12, 1, 3),
    (13, 1, 4),
    (14, 1, 2),
    (14, 4, 1),
    (15, 2, 3),
    (16, 3, 4),
    (17, 4, 1),
    (18, 1, 2),
    (19, 1, 3),
    (20, 1, 4);


INSERT INTO users(id_user, name, last_name, email, password, phone, rol, email_activate, credit, created_date) VALUES
    (1, 'Lucia', 'Morales Ruiz', 'admin1@virtualtechseller.com', 'admin123', '600111111', 'ADMIN', TRUE, 0.00, '2026-01-10'),
    (2, 'Carlos', 'Jimenez Soto', 'admin2@virtualtechseller.com', 'admin123', '600111112', 'ADMIN', TRUE, 0.00, '2026-01-11'),
    (3, 'Marta', 'Serrano Gil', 'moderator1@virtualtechseller.com', 'moderator123', '600111113', 'MODERATOR', TRUE, 0.00, '2026-01-12'),
    (4, 'Ezequiel', 'Tamayo Lopez', 'client1@virtualtechseller.com', 'client123', '600111114', 'CLIENT', TRUE, 300.00, '2026-01-15'),
    (5, 'Laura', 'Navarro Perez', 'client2@virtualtechseller.com', 'client123', '600111115', 'CLIENT', TRUE, 180.00, '2026-01-18'),
    (6, 'David', 'Romero Cruz', 'client3@virtualtechseller.com', 'client123', '600111116', 'CLIENT', TRUE, 520.00, '2026-01-20'),
    (7, 'Paula', 'Mendez Lara', 'client4@virtualtechseller.com', 'client123', '600111117', 'CLIENT', TRUE, 95.00, '2026-01-21'),
    (8, 'Sergio', 'Vega Martin', 'client5@virtualtechseller.com', 'client123', '600111118', 'CLIENT', TRUE, 410.00, '2026-01-23'),
    (9, 'Andrea', 'Castro Ruiz', 'client6@virtualtechseller.com', 'client123', '600111119', 'CLIENT', FALSE, 150.00, '2026-01-25'),
    (10, 'Javier', 'Ortega Leon', 'client7@virtualtechseller.com', 'client123', '600111120', 'CLIENT', TRUE, 260.00, '2026-01-28');


INSERT INTO user_visits(id_user, id_exhibition, last_visit, visit_counter) VALUES
    (4, 1, '2026-03-18', 5),
    (4, 3, '2026-03-19', 2),
    (5, 1, '2026-03-16', 3),
    (5, 2, '2026-03-12', 4),
    (6, 3, '2026-03-20', 6),
    (6, 1, '2026-03-17', 2),
    (7, 2, '2026-03-15', 3),
    (8, 1, '2026-03-20', 7),
    (8, 4, '2026-03-10', 1),
    (9, 3, '2026-03-19', 2),
    (9, 1, '2026-03-14', 2),
    (10, 2, '2026-03-11', 5),
    (10, 1, '2026-03-18', 1);


INSERT INTO sales(id_sale, id_user, id_exhibition, id_product, quantity, total_price, purchase_date) VALUES
    (1, 4, 1, 1, 1, 280.50, '2026-03-05'),
    (2, 4, 3, 3, 1, 1220.23, '2026-03-08'),
    (3, 5, 2, 5, 1, 60.89, '2026-03-02'),
    (4, 5, 2, 7, 1, 6.45, '2026-03-04'),
    (5, 6, 1, 10, 2, 130.68, '2026-03-09'),
    (6, 6, 1, 13, 1, 100.50, '2026-03-10'),
    (7, 7, 2, 9, 1, 28.45, '2026-03-06'),
    (8, 8, 1, 19, 1, 133.65, '2026-03-11'),
    (9, 8, 1, 20, 1, 200.12, '2026-03-12'),
    (10, 9, 3, 16, 1, 350.44, '2026-03-13'),
    (11, 9, 1, 18, 2, 20.24, '2026-03-15'),
    (12, 10, 2, 15, 1, 50.03, '2026-03-07'),
    (13, 10, 1, 11, 1, 6.50, '2026-03-16');


INSERT INTO cart_items(id_user, id_product, quantity) VALUES
    (4, 14, 1),
    (5, 2, 1),
    (6, 16, 1),
    (7, 1, 1),
    (8, 10, 1),
    (8, 18, 1),
    (9, 20, 1),
    (10, 19, 1);
