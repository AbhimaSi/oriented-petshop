CREATE TABLE IF NOT EXISTS cliente (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	nome VARCHAR(50) NOT NULL,
	telefone VARCHAR(20),
	endereco VARCHAR(50),
	PRIMARY KEY(id, uuid)
);

CREATE TABLE IF NOT EXISTS animal (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	idcliente INT,
	nome VARCHAR(50),
	especie VARCHAR(30),
	raca VARCHAR(30) DEFAULT 'Vira-lata',
	FOREIGN KEY (idcliente) REFERENCES cliente (id),
	PRIMARY KEY(id, uuid)
);

CREATE TABLE IF NOT EXISTS funcionario (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	nome VARCHAR(50),
	cargo VARCHAR(50),
	PRIMARY KEY(id, uuid)
);

CREATE TABLE IF NOT EXISTS atendimento (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	data_atendimento DATE DEFAULT CURRENT_DATE,
	hora_atendimento TIME DEFAULT CURRENT_TIME,
	status VARCHAR(30),
	PRIMARY KEY(id, uuid)
);


CREATE TABLE IF NOT EXISTS produto (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	nome VARCHAR(30),
	preco DECIMAL(5,2),
	PRIMARY KEY(id, uuid)
);

CREATE TABLE IF NOT EXISTS servico (
	id SERIAL UNIQUE,
	uuid UUID UNIQUE DEFAULT gen_random_uuid(),
	nome VARCHAR(30),
	preco DECIMAL(5,2),
	duracao INT,
	PRIMARY KEY(id, uuid)
);

CREATE TABLE IF NOT EXISTS animal_atendimento (
	id SERIAL UNIQUE PRIMARY KEY,
	idatendimento INT NOT NULL,
	idservico INT NOT NULL,
	idanimal INT NOT NULL,
	idfuncionario INT NOT NULL,
	FOREIGN KEY (idatendimento) REFERENCES atendimento (id),
	FOREIGN KEY (idservico) REFERENCES servico (id),
	FOREIGN KEY (idanimal) REFERENCES animal (id),
	FOREIGN KEY (idfuncionario) REFERENCES funcionario (id)
);

CREATE TABLE IF NOT EXISTS produto_atendimento (
	id SERIAL UNIQUE PRIMARY KEY,
	idatendimento INT NOT NULL,
	idfuncionario INT NOT NULL,
	idproduto INT NOT NULL,
	qtd INT NOT NULL DEFAULT 1,
	FOREIGN KEY (idatendimento) REFERENCES atendimento (id),
	FOREIGN KEY (idfuncionario) REFERENCES funcionario (id),
	FOREIGN KEY (idproduto) REFERENCES produto (id)
);