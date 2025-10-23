INSERT INTO orders (customer_name, customer_email, total, created_at)
VALUES
    ('Ada Lovelace', 'ada@analytical.engine', 1799.98, CURRENT_TIMESTAMP()),
    ('Alan Turing', 'alan@enigma.uk' , 2499.50, CURRENT_TIMESTAMP()),
    ('Grace Hopper', 'grace@navy.mil', 899.99, CURRENT_TIMESTAMP());

INSERT INTO order_items (product_id, name, quantity, unit_price, line_total, order_id)
VALUES
    ('SKU-001', 'Teclado Mecánico', 2, 899.99, 1799.98, 1),
    ('SKU-002', 'Monitor 27"', 1, 2499.50, 2499.50, 2),
    ('SKU-003', 'Mouse Inalámbrico', 1, 899.99, 899.99, 3);
