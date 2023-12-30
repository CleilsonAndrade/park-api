insert into tb_user (id, username, password, role) values (100, 'ana@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_ADMIN');
insert into tb_user (id, username, password, role) values (101, 'bia@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');
insert into tb_user (id, username, password, role) values (102, 'bob@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');

insert into tb_client (id, nome, cpf, id_user) values (21, 'Bianca Silva', '22381495037', 101);
insert into tb_client (id, nome, cpf, id_user) values (22, 'Roberto Gomes', '24122251095', 102);

insert into tb_parking (id, code, status) values (100, 'A-01', 'BUSY');
insert into tb_parking (id, code, status) values (200, 'A-02', 'BUSY');
insert into tb_parking (id, code, status) values (300, 'A-03', 'BUSY');
insert into tb_parking (id, code, status) values (400, 'A-04', 'AVAILABLE');
insert into tb_parking (id, code, status) values (500, 'A-04', 'AVAILABLE');

insert into tb_client_has_parking (number_receipt, plate, brand, model, color, date_entry, id_client, id_parking)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 22, 100);
insert into tb_client_has_parking (number_receipt, plate, brand, model, color, date_entry, id_client, id_parking)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 21, 200);
insert into tb_client_has_parking (number_receipt, plate, brand, model, color, date_entry, id_client, id_parking)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 22, 300);