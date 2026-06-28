INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users (username, email, password)
VALUES('admin', 'admin@admin.com', '$2a$10$cYUXXNkDYAyxNqvdoXwapelF12LpWQsLRA9Bwy1kg4uQxfwNvFxGG'); -- password

INSERT INTO users_roles (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'admin'),
           (SELECT id FROM roles WHERE name = 'ADMIN')
       );
INSERT INTO users_roles (user_id, role_id)
VALUES (
           (SELECT id FROM users WHERE username = 'admin'),
           (SELECT id FROM roles WHERE name = 'USER')
       );

INSERT INTO payment_method (name) VALUES  ('Cash on delivery');
INSERT INTO payment_method (name) VALUES  ('PayPal');

INSERT INTO category (name) VALUES  ('Electronics');
INSERT INTO category (name) VALUES  ('Fashion');
INSERT INTO category (name) VALUES  ('Books');
INSERT INTO category (name) VALUES  ('Gaming') ;
INSERT INTO category (name) VALUES  ('Home & Lifestyle');

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Smartphone Pro X', 'High-end smartphone', 999.99, 10, 'phone.jpg',
        (SELECT id FROM category WHERE name='Electronics'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Wireless Headphones', 'Noise cancelling headphones', 199.99, 20, 'headphones.jpg',
        (SELECT id FROM category WHERE name='Electronics'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Running Shoes', 'Lightweight running shoes', 89.99, 15, 'shoes1.jpg',
        (SELECT id FROM category WHERE name='Fashion'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Leather Boots', 'Premium leather boots', 149.99, 8, 'boots.jpg',
        (SELECT id FROM category WHERE name='Fashion'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Java Programming', 'Learn Java from scratch', 39.99, 30, 'book1.jpg',
        (SELECT id FROM category WHERE name='Books'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Clean Code', 'Software engineering best practices', 45.00, 25, 'book2.jpg',
        (SELECT id FROM category WHERE name='Books'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Gaming Console Pro', 'Next-gen console', 499.99, 12, 'console.jpg',
        (SELECT id FROM category WHERE name='Gaming'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Gaming Chair', 'Ergonomic chair for gamers', 199.99, 10, 'chair.jpg',
        (SELECT id FROM category WHERE name='Gaming'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Desk Lamp', 'LED adjustable lamp', 29.99, 50, 'lamp.jpg',
        (SELECT id FROM category WHERE name='Home & Lifestyle'));

INSERT INTO product (name, description, price, quantity, image, category_id)
VALUES ('Coffee Maker', 'Automatic coffee machine', 79.99, 18, 'coffee.jpg',
        (SELECT id FROM category WHERE name='Home & Lifestyle'));