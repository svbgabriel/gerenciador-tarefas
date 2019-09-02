package br.com.svbgabriel.gerenciadortarefas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.svbgabriel.gerenciadortarefas.models.Tarefa;

public interface RepositoryTarefa extends JpaRepository<Tarefa, Long> {

	@Query("SELECT t FROM Tarefa t WHERE t.usuario.email = :emailUsuario")
	List<Tarefa> carregarTarefasPorUsuario(@Param("emailUsuario") String email);

}
