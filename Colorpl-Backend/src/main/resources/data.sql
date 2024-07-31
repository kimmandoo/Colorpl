-- member 테이블 데이터 삽입
INSERT INTO member (email, nickname, password, type) VALUES
                                                         ('member1@example.com', 'nick1', 'password1', 'USER'),
                                                         ('member2@example.com', 'nick2', 'password2', 'USER'),
                                                         ('member3@example.com', 'nick3', 'password3', 'ADMIN'),
                                                         ('member4@example.com', 'nick4', 'password4', 'USER'),
                                                         ('member5@example.com', 'nick5', 'password5', 'USER'),
                                                         ('member6@example.com', 'nick6', 'password6', 'ADMIN'),
                                                         ('member7@example.com', 'nick7', 'password7', 'USER'),
                                                         ('member8@example.com', 'nick8', 'password8', 'USER'),
                                                         ('member9@example.com', 'nick9', 'password9', 'ADMIN'),
                                                         ('member10@example.com', 'nick10', 'password10', 'USER');

-- show_detail 테이블 데이터 삽입
INSERT INTO show_detail (show_detail_api_id, show_detail_name, show_detail_cast, show_detail_runtime, show_detail_poster_image_path, show_detail_area, show_detail_state)
VALUES
    ('API001', 'Show A', 'Cast A', '2h', '/images/show_a.jpg', 'Area A', 'SCHEDULED'),
    ('API002', 'Show B', 'Cast B', '1.5h', '/images/show_b.jpg', 'Area B', 'SHOWING'),
    ('API003', 'Show C', 'Cast C', '2h 15m', '/images/show_c.jpg', 'Area C', 'COMPLETED'),
    ('API004', 'Show D', 'Cast D', '2h 30m', '/images/show_d.jpg', 'Area D', 'SCHEDULED'),
    ('API005', 'Show E', 'Cast E', '1h 45m', '/images/show_e.jpg', 'Area E', 'SCHEDULED'),
    ('API006', 'Show F', 'Cast F', '1h 30m', '/images/show_f.jpg', 'Area F', 'SCHEDULED'),
    ('API007', 'Show G', 'Cast G', '1h 45m', '/images/show_g.jpg', 'Area G', 'SCHEDULED');  -- Ensure this ID is used in show_schedule

-- seat 테이블 데이터 삽입
INSERT INTO seat (show_detail_id, seat_row, seat_col, seat_class) VALUES
                                                                      (1, 1, 1, 'VIP'),
                                                                      (1, 1, 2, 'VIP'),
                                                                      (1, 2, 1, 'Regular'),
                                                                      (1, 2, 2, 'Regular'),
                                                                      (1, 3, 1, 'VIP'),
                                                                      (1, 3, 2, 'VIP'),
                                                                      (2, 1, 1, 'VIP'),
                                                                      (2, 1, 2, 'VIP'),
                                                                      (2, 2, 1, 'Regular'),
                                                                      (2, 2, 2, 'Regular'),
                                                                      (3, 1, 1, 'Regular'),
                                                                      (3, 1, 2, 'Regular'),
                                                                      (3, 2, 1, 'Regular'),
                                                                      (3, 2, 2, 'Regular'),
                                                                      (4, 1, 1, 'VIP'),
                                                                      (4, 1, 2, 'VIP'),
                                                                      (4, 2, 1, 'Regular'),
                                                                      (4, 2, 2, 'Regular'),
                                                                      (5, 1, 1, 'VIP'),
                                                                      (5, 1, 2, 'VIP'),
                                                                      (6, 1, 1, 'Regular'),
                                                                      (6, 1, 2, 'Regular'),
                                                                      (7, 1, 1, 'VIP'),
                                                                      (7, 1, 2, 'VIP');

-- show_schedule 테이블 데이터 삽입
INSERT INTO show_schedule (show_detail_id, show_schedule_date_time) VALUES
                                                                        (1, '2024-08-01 20:00:00'),
                                                                        (1, '2024-08-02 19:00:00'),
                                                                        (2, '2024-08-03 21:00:00'),
                                                                        (2, '2024-08-04 18:00:00'),
                                                                        (3, '2024-08-05 20:00:00'),
                                                                        (3, '2024-08-06 21:00:00'),
                                                                        (4, '2024-08-07 19:00:00'),
                                                                        (4, '2024-08-08 20:00:00'),
                                                                        (5, '2024-08-09 21:00:00'),
                                                                        (5, '2024-08-10 18:00:00'),
                                                                        (6, '2024-08-11 20:00:00'),
                                                                        (6, '2024-08-12 19:00:00'),
                                                                        (7, '2024-08-13 21:00:00'),
                                                                        (7, '2024-08-14 20:00:00');


-- reservation 테이블 데이터 삽입
INSERT INTO reservation (member_id, reserve_date, reserve_amount, reserve_comment, is_refunded) VALUES
                                                                                                    (1, '2024-08-01 20:00:00', '100', 'First reservation', FALSE),
                                                                                                    (2, '2024-08-02 19:00:00', '150', 'Second reservation', TRUE),
                                                                                                    (3, '2024-08-03 21:00:00', '200', 'Third reservation', FALSE),
                                                                                                    (4, '2024-08-04 18:00:00', '250', 'Fourth reservation', TRUE),
                                                                                                    (5, '2024-08-05 20:00:00', '300', 'Fifth reservation', FALSE),
                                                                                                    (6, '2024-08-06 21:00:00', '350', 'Sixth reservation', TRUE),
                                                                                                    (7, '2024-08-07 19:00:00', '400', 'Seventh reservation', FALSE),
                                                                                                    (8, '2024-08-08 20:00:00', '450', 'Eighth reservation', TRUE),
                                                                                                    (9, '2024-08-09 21:00:00', '500', 'Ninth reservation', FALSE),
                                                                                                    (10, '2024-08-10 18:00:00', '550', 'Tenth reservation', TRUE);

-- reservation_detail 테이블 데이터 삽입
INSERT INTO reservation_detail (seat_row, seat_col, reserve_id, show_schedule_id) VALUES
                                                                                      (1, 1, 1, 1),
                                                                                      (1, 2, 1, 1),
                                                                                      (2, 1, 2, 2),
                                                                                      (2, 2, 2, 2),
                                                                                      (3, 1, 3, 3),
                                                                                      (3, 2, 3, 3),
                                                                                      (4, 1, 4, 4),
                                                                                      (4, 2, 4, 4),
                                                                                      (5, 1, 5, 5),
                                                                                      (5, 2, 5, 5),
                                                                                      (6, 1, 6, 6),
                                                                                      (6, 2, 6, 6),
                                                                                      (7, 1, 7, 7),
                                                                                      (7, 2, 7, 7),
                                                                                      (8, 1, 8, 8),
                                                                                      (8, 2, 8, 8),
                                                                                      (9, 1, 9, 9),
                                                                                      (9, 2, 9, 9),
                                                                                      (10, 1, 10, 10),
                                                                                      (10, 2, 10, 10);
