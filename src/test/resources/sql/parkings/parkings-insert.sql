insert into tb_user (id, username, password, role) values (100, 'ana@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_ADMIN');
insert into tb_user (id, username, password, role) values (101, 'bia@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');
insert into tb_user (id, username, password, role) values (102, 'bob@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');

insert into tb_parking (id, code, status) values (10, 'A-01', 'AVAILABLE');
insert into tb_parking (id, code, status) values (20, 'A-02', 'AVAILABLE');
insert into tb_parking (id, code, status) values (30, 'A-03', 'BUSY');
insert into tb_parking (id, code, status) values (40, 'A-04', 'AVAILABLE');