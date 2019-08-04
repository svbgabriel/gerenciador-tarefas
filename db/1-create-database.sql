CREATE DATABASE IF NOT EXISTS gerenciador_tarefas;

USE gerenciador_tarefas;

CREATE TABLE tar_tarefas
(
	tar_id INT PRIMARY KEY AUTO_INCREMENT,
    tar_titulo VARCHAR(50) NOT NULL,
    tar_descricao VARCHAR(100) DEFAULT NULL,
    tar_data_expiracao DATE NOT NULL,
    tar_concluida BIT DEFAULT FALSE
);
