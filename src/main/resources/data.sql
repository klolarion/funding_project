

insert into role(role_id, role_name) values (1, 'ROLE_ADMIN');
insert into role(role_id, role_name) values (2, 'ROLE_USER');


insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (1, 2, 'admin', 'admin@admin.com', 'Tester', '010-1211-2252', true, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (2, 2, 'tester1', 'tester1@user.com', 'Tester1', '010-1000-2222', true, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (3, 2, 'tester2', 'tester2@user.com', 'Tester2', '010-1311-9999', false, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (4, 2, 'tester3', 'tester3@user.com', 'Tester3', '010-1761-2222', true, false, true);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (5, 2, 'tester4', 'tester4@user.com', 'Tester4', '010-1121-2222', true, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (6, 2, 'tester5', 'tester5@user.com', 'Tester5', '010-1331-2222', true, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (7, 2, 'tester6', 'tester6@user.com', 'Tester6', '010-4441-2222', true, false, false);

insert into member(member_id, role_id, account, email, member_name, tel, enabled, banned, off_cd)
values (8, 2, 'tester7', 'tester7@user.com', 'Tester7', '010-1115-2222', true, false, false);


insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (1, '쌀과자', 5000, 20, false, false);

insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (2, '오예스', 8000, 10, true, false);

insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (3, '마가렛트', 4200, 660, true, false);

insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (4, '하리보', 1500, 0, false, true);

insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (5, '버터와플', 15000, 20, false, false);

insert into product(product_id, product_name, price, stock, restock, sale_finished)
values (6, '커피사탕', 300, 3000, false, false);


insert into code_master(code_id, code, description, reference)
values (1, 101, 'KB은행', '은행');

insert into code_master(code_id, code, description, reference)
values (2, 102, '신한은행', '은행');

insert into code_master(code_id, code, description, reference)
values (3, 105, '하나은행', '은행');

insert into code_master(code_id, code, description, reference)
values (4, 201, '네이버페이', 'pay');

insert into code_master(code_id, code, description, reference)
values (5, 203, '카카오페이', 'pay');

insert into code_master(code_id, code, description, reference)
values (6, 901, '선물', '그룹분류');

insert into code_master(code_id, code, description, reference)
values (7, 902, '여행', '그룹분류');

insert into code_master(code_id, code, description, reference)
values (8, 903, '핑크', '그룹분류');

insert into code_master(code_id, code, description, reference)
values (9, 904, '실버', '그룹분류');


insert into payment_method(payment_method_id, payment_name, payment_code, account_number, available_amount)
values (1, 'KB은행', 101, '123-12345-20', 594835);

insert into payment_method(payment_method_id, payment_name, payment_code, account_number, available_amount)
values (2, '신한은행', 102, '382-47262-84', 2841892947);

insert into payment_method(payment_method_id, payment_name, payment_code, account_number, available_amount)
values (3, '하나은행', 105, '377-01023-61', 28376);

insert into payment_method(payment_method_id, payment_name, payment_code, account_number, available_amount)
values (4, '네이버페이', 201, '003-00328-98', 3000);

insert into payment_method(payment_method_id, payment_name, payment_code, account_number, available_amount)
values (5, '카카오페이', 203, '333-33213-77', 0);



insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (1, 1, 1, true, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (2, 3, 1, false, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (3, 4, 1, false, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (4, 1, 3, false, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (5, 2, 3, false, true);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (6, 5, 3, true, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (7, 2, 4, false, false);

insert into payment_method_list(payment_method_list_id, payment_method_id, member_id, main_payment, off_cd)
values (8, 3, 4, false, false);


INSERT INTO member_groups (group_active, group_id, group_leader_id, group_name, group_category_code) VALUES (true, 1, 1, '마가렛트 사줘', 901);
INSERT INTO member_groups (group_active, group_id, group_leader_id, group_name, group_category_code) VALUES (true, 2, 1, '나는 오예스가 먹고싶다', 901);
INSERT INTO member_groups (group_active, group_id, group_leader_id, group_name, group_category_code) VALUES (true, 3, 1, '부산호캉스갈래', 902);


INSERT INTO group_status (accepted, banned, exited, invited, requested, group_id, group_leader_id, group_member_id, group_status_id) VALUES (true, false, false, false, false, 1, 1, 1, 1);
INSERT INTO group_status (accepted, banned, exited, invited, requested, group_id, group_leader_id, group_member_id, group_status_id) VALUES (true, false, false, false, false, 2, 1, 1, 2);
INSERT INTO group_status (accepted, banned, exited, invited, requested, group_id, group_leader_id, group_member_id, group_status_id) VALUES (true, false, false, false, false, 3, 1, 1, 3);


INSERT INTO funding (closed, completed, deleted, current_funding_amount, funding_id, travel_id, group_id, member_id, product_id, total_funding_amount, created_date, funding_account, last_modified_date, funding_category_code) VALUES (false, false, false, 1000, 1, null, 1, 1, 3, 4200, '24. 8. 16. 오후 5:11', '620-65697-37', '24. 8. 16. 오후 5:11', 901);
INSERT INTO funding (closed, completed, deleted, current_funding_amount, funding_id, travel_id, group_id, member_id, product_id, total_funding_amount, created_date, funding_account, last_modified_date, funding_category_code) VALUES (false, false, false, 500, 2, null, null, 1, 5, 15000, '24. 8. 16. 오후 5:14', '540-75235-09', '24. 8. 16. 오후 5:14', 901);
INSERT INTO funding (closed, completed, deleted, current_funding_amount, funding_id, travel_id, group_id, member_id, product_id, total_funding_amount, created_date, funding_account, last_modified_date, funding_category_code) VALUES (false, false, false, 0, 3, null, 2, 1, 2, 8000, '24. 8. 16. 오후 5:14', '946-49182-93', '24. 8. 16. 오후 5:14', 901);


INSERT INTO travel (travel_name, end_date, group_id, member_id, price, start_date, travel_id, description, city) VALUES ('부산호캉스', '2024-10-24 21:11:29.000000', 3, 1, 1500000, '2024-10-20 12:00:00.000000', 1, '부산호캉스 돼지국밥냠냠', '부산');

-- INSERT INTO friend_status (accepted, denied, accepter_id, friend_status_id, requester_id, created_date, last_modified_date) VALUES (true, false, 9, 1, 1, null, null);
-- INSERT INTO friend_status (accepted, denied, accepter_id, friend_status_id, requester_id, created_date, last_modified_date) VALUES (false, false, 9, 2, 2, null, null);
--
-- INSERT INTO friend (banned, deleted, accepter_id, friend_id, requester_id, created_date, last_modified_date) VALUES (false, false, 9, 1, 1, null, null);
