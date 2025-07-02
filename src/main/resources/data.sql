MERGE INTO brand (id, name) KEY(id) VALUES (1, 'A'), (2, 'B'), (3, 'C'), (4, 'D'), (5, 'E'), (6, 'F'), (7, 'G'), (8, 'H'), (9, 'I');

MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (1, 'TOP', 11200, 1), (2, 'OUTER', 5500, 1), (3, 'PANTS', 4200, 1), (4, 'SNEAKERS', 9000, 1), (5, 'BAG', 2000, 1), (6, 'HAT', 1700, 1), (7, 'SOCKS', 1800, 1), (8, 'ACCESSORIES', 2300, 1);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (9, 'TOP', 10500, 2), (10, 'OUTER', 5900, 2), (11, 'PANTS', 3800, 2), (12, 'SNEAKERS', 9100, 2), (13, 'BAG', 2100, 2), (14, 'HAT', 2000, 2), (15, 'SOCKS', 2000, 2), (16, 'ACCESSORIES', 2200, 2);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (17, 'TOP', 10000, 3), (18, 'OUTER', 6200, 3), (19, 'PANTS', 3300, 3), (20, 'SNEAKERS', 9200, 3), (21, 'BAG', 2200, 3), (22, 'HAT', 1900, 3), (23, 'SOCKS', 2200, 3), (24, 'ACCESSORIES', 2100, 3);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (25, 'TOP', 10100, 4), (26, 'OUTER', 5100, 4), (27, 'PANTS', 3000, 4), (28, 'SNEAKERS', 9500, 4), (29, 'BAG', 2500, 4), (30, 'HAT', 1500, 4), (31, 'SOCKS', 2400, 4), (32, 'ACCESSORIES', 2000, 4);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (33, 'TOP', 10700, 5), (34, 'OUTER', 5000, 5), (35, 'PANTS', 3800, 5), (36, 'SNEAKERS', 9900, 5), (37, 'BAG', 2300, 5), (38, 'HAT', 1800, 5), (39, 'SOCKS', 2100, 5), (40, 'ACCESSORIES', 2100, 5);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (41, 'TOP', 11200, 6), (42, 'OUTER', 7200, 6), (43, 'PANTS', 4000, 6), (44, 'SNEAKERS', 9300, 6), (45, 'BAG', 2100, 6), (46, 'HAT', 1600, 6), (47, 'SOCKS', 2300, 6), (48, 'ACCESSORIES', 1900, 6);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (49, 'TOP', 10500, 7), (50, 'OUTER', 5800, 7), (51, 'PANTS', 3900, 7), (52, 'SNEAKERS', 9000, 7), (53, 'BAG', 2200, 7), (54, 'HAT', 1700, 7), (55, 'SOCKS', 2100, 7), (56, 'ACCESSORIES', 2000, 7);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (57, 'TOP', 10800, 8), (58, 'OUTER', 6300, 8), (59, 'PANTS', 3100, 8), (60, 'SNEAKERS', 9700, 8), (61, 'BAG', 2100, 8), (62, 'HAT', 1600, 8), (63, 'SOCKS', 2000, 8), (64, 'ACCESSORIES', 2000, 8);
MERGE INTO product (id, category, price, brand_id) KEY(id) VALUES
    (65, 'TOP', 11400, 9), (66, 'OUTER', 6700, 9), (67, 'PANTS', 3200, 9), (68, 'SNEAKERS', 9500, 9), (69, 'BAG', 2400, 9), (70, 'HAT', 1700, 9), (71, 'SOCKS', 1700, 9), (72, 'ACCESSORIES', 2400, 9);