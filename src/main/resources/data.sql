
--role
insert into role(role_id, role_name) values (1, 'ROLE_ADMIN');
insert into role(role_id, role_name) values (2, 'ROLE_USER');
--member

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (1, 2, 'admin', 'admin@admin.com', '010-0000-1111', 'Tester',  '1234', true, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (2, 2, 'tester1', 'tester1@user.com', '010-1234-1234', 'Tester1',  '1234', true, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (3, 2, 'tester2', 'tester2@user.com', '010-9911-3344', 'Tester2',  '1234', false, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (4, 2, 'tester3', 'tester3@user.com', '010-5932-1927', 'Tester3',  '1234', true, false, true);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (5, 2, 'tester4', 'tester4@user.com', '010-5122-1727', 'Tester4',  '1234', true, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (6, 2, 'tester5', 'tester5@user.com', '010-5932-1527', 'Tester5',  '1234', true, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (7, 2, 'tester6', 'tester6@user.com', '010-3232-1447', 'Tester6',  '1234', true, false, false);

insert into member(member_id, role_id, account, email, tel, member_name, password, enabled, banned, off_cd)
values (8, 2, 'tester7', 'tester7@user.com', '010-5442-1127', 'Tester7',  '1234', true, false, false);

--product

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

--code

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

--paymentMethod

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


--paymentMethodList

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

--member_groups(group)
insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (1, 1, '하리보맨', true);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (2, 1, '나는 오예스가 먹고싶다', true);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (3, 1, '오예스그룹', true);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (4, 1, '점심그룹', false);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (5, 2, '쌀과자 사주세요', true);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (6, 3, '빵먹자', false);

insert into member_groups(group_id, group_leader_id, group_name, group_active)
values (7, 4, '저녁그룹', false);


--groupStatus
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (1, 1, 1, 1, false, false, true, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (2, 1, 1, 2, true, false, true, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (3, 1, 1, 2, true, false, true, true, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (4, 1, 1, 3, false, true, false, false, true);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (5, 1, 1, 4, false, true, false, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (6, 2, 1, 1, false, true, true, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (7, 2, 1, 2, true, false, false, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (8, 3, 2, 2, false, false, true, false, false);
insert into group_status(group_status_id, group_id, group_leader_id, group_member_id, invited, requested, accepted, exited, banned)
values (9, 4, 3, 3, false, false, true, false, false);


--friend

--friendStatus

--funding


--orderList

--payment