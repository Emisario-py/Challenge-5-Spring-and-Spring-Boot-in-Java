-- ========================================
-- Insert Clients
-- ========================================
INSERT INTO clients (id_client, client_name, email, address, age) VALUES (1, 'Juan Perez', 'juan.perez@email.com', 'Av. Revolucion 123, CDMX', 35);
INSERT INTO clients (id_client, client_name, email, address, age) VALUES (2, 'María Gonzalez', 'maria.gonzalez@email.com', 'Calle Morelos 456, Guadalajara', 28);
INSERT INTO clients (id_client, client_name, email, address, age) VALUES (3, 'Carlos Ramirez', 'carlos.ramirez@email.com', 'Blvd. Independencia 789, Monterrey', 42);
INSERT INTO clients (id_client, client_name, email, address, age) VALUES (4, 'Ana Lopez', 'ana.lopez@email.com', 'Av. Juarez 321, Puebla', 31);
INSERT INTO clients (id_client, client_name, email, address, age) VALUES (5, 'Pedro Martínez', 'pedro.martinez@email.com', 'Calle Hidalgo 654, Querétaro', 38);

-- ========================================
-- Insert Orders
-- ========================================
INSERT INTO orders (id, client_id, created_at) VALUES (1, 1, '2025-10-01T10:30:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (2, 2, '2025-10-02T14:15:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (3, 1, '2025-10-03T09:45:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (4, 3, '2025-10-04T16:20:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (5, 4, '2025-10-05T11:00:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (6, 2, '2025-10-06T13:30:00Z');
INSERT INTO orders (id, client_id, created_at) VALUES (7, 5, '2025-10-07T15:45:00Z');

-- ========================================
-- Insert Items
-- ========================================

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (1, 1, 'PROD-001', 'Laptop Dell XPS 13', 1, 2500.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (2, 1, 'PROD-002', 'Mouse Logitech MX Master', 2, 500.00);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (3, 2, 'PROD-003', 'Teclado Mecánico RGB', 1, 800.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (4, 2, 'PROD-004', 'Webcam HD 1080p', 1, 400.00);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (5, 3, 'PROD-005', 'Monitor 24" Full HD', 1, 500);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (6, 4, 'PROD-006', 'MacBook Pro 14"', 1, 3500.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (7, 4, 'PROD-007', 'USB-C Hub', 2, 500.00);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (8, 5, 'PROD-008', 'iPad Air', 1, 1800.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (9, 5, 'PROD-009', 'Apple Pencil', 1, 500.0);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (10, 6, 'PROD-010', 'Auriculares Sony WH-1000XM5', 1, 1250.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (11, 6, 'PROD-011', 'Cable USB-C', 5, 100.0);

INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (12, 7, 'PROD-012', 'iPhone 15 Pro', 1, 2800.00);
INSERT INTO items (id, order_id, product_id, name, quantity, unit_price) VALUES (13, 7, 'PROD-013', 'Case Protector', 2, 200.0);