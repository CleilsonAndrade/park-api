insert into tb_user (id, username, password, role) values (100, 'ana@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_ADMIN');
insert into tb_user (id, username, password, role) values (101, 'bia@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');
insert into tb_user (id, username, password, role) values (102, 'bob@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');
insert into tb_user (id, username, password, role) values (103, 'toby@email.com', '$2a$12$WriOJicfh1CvUdEVdYdK/e1YokGlWV4zcTXctTQyW8uCtbrZauWTu', 'ROLE_CLIENT');

insert into tb_client (id, nome, cpf, id_user) values (10, 'Bianca Silva', '22381495037', 101);
insert into tb_client (id, nome, cpf, id_user) values (20, 'Roberto Gomes', '24122251095', 102);