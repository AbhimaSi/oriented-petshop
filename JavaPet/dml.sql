INSERT INTO cliente (nome, telefone, endereco) VALUES ('Abhimael', '4399999999', 'Santos Dumont, 312');
INSERT INTO cliente (nome, telefone, endereco) VALUES ('Romanelo', '4399999999', 'XV. Novembro, 123');
INSERT INTO cliente (nome, telefone, endereco) VALUES ('Matheus', '4399999999', 'Alberto Carazzai, 321');


INSERT INTO animal (idcliente, nome, especie, raca) VALUES (2, 'Ghost', 'Cachorro', 'Husky');
INSERT INTO animal (idcliente, nome, especie, raca) VALUES (3, 'Caramelo', 'Cachorro', 'Vira-lata');
INSERT INTO animal (idcliente, nome, especie, raca) VALUES (2, 'Gota DAgua', 'Gato', 'Frajola');
INSERT INTO animal (idcliente, nome, especie, raca) VALUES (1, 'Frido', 'Gato', 'Siames');
INSERT INTO animal (idcliente, nome, especie, raca) VALUES (3, 'Ghost', 'Cachorro', 'Husky');
INSERT INTO animal (nome, especie, raca) VALUES ('Neguinho', 'Cachorro', 'Salsicha');


INSERT INTO funcionario (nome, cargo, email, senha) VALUES ('Alisson', 'Veterinário', 'alisson@javapet.com', '1234');
INSERT INTO funcionario (nome, cargo, email, senha) VALUES ('Richard', 'Veterinário', 'rick@javapet.com', '1234');
INSERT INTO funcionario (nome, cargo, email, senha) VALUES ('Ronaldo', 'Afazeres gerais', 'ronaldinho@outlook.com', '1234');
INSERT INTO funcionario (nome, cargo, email, senha) VALUES ('Samuel', 'Atendente', 'samu@javapet.com', '1234');


INSERT INTO atendimento (data_atendimento, hora_atendimento, status) VALUES ('2026-06-01', '20:30:00','EM ANDAMENTO');
INSERT INTO atendimento (data_atendimento, hora_atendimento, status) VALUES ('2026-06-02', '12:00:00','CONCLUIDO');
INSERT INTO atendimento (status) VALUES ('EM ANDAMENTO');


INSERT INTO servico (nome, preco, duracao) VALUES ('BANHO', 10.00, 10);
INSERT INTO servico (nome, preco, duracao) VALUES ('TOSA', 15.00, 10);
INSERT INTO servico (nome, preco, duracao) VALUES ('BANHO E TOSA', 20.00, 20);
INSERT INTO servico (nome, preco, duracao) VALUES ('BUSCA DE ANIMAL', 10.00, 10);
INSERT INTO produto (nome, preco) VALUES ('whiskas sache', 5.00);


INSERT INTO animal_atendimento (idatendimento, idservico, idanimal, idfuncionario) VALUES (1, 4, 2, 3);
INSERT INTO animal_atendimento (idatendimento, idservico, idanimal, idfuncionario) VALUES (1, 4, 3, 3);


INSERT INTO produto_atendimento (idatendimento, idfuncionario, idproduto) VALUES (2, 4, 1);


