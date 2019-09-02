package br.com.svbgabriel.gerenciadortarefas.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.svbgabriel.gerenciadortarefas.models.Tarefa;
import br.com.svbgabriel.gerenciadortarefas.models.Usuario;
import br.com.svbgabriel.gerenciadortarefas.repositories.RepositoryTarefa;
import br.com.svbgabriel.gerenciadortarefas.services.ServiceUsuario;

@Controller
@RequestMapping("/tarefas")
public class TarefasController {

	@Autowired
	private RepositoryTarefa repositoryTarefa;

	@Autowired
	private ServiceUsuario serviceUsuario;

	@GetMapping("/listar")
	public ModelAndView listar(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/listar");
		String emailUsuario = request.getUserPrincipal().getName();
		mv.addObject("tarefas", repositoryTarefa.carregarTarefasPorUsuario(emailUsuario));
		return mv;
	}

	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/inserir");
		mv.addObject("tarefa", new Tarefa());
		return mv;
	}

	@PostMapping("/inserir")
	public ModelAndView inserir(@Valid Tarefa tarefa, BindingResult result, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		if (tarefa.getDataExpiracao() == null) {
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoVazia", "A data de expiração é obrigatória.");
		} else {
			if (tarefa.getDataExpiracao().before(new Date())) {
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida",
						"A data de expiração não pode ser anterior a data atual.");
			}
		}
		if (result.hasErrors()) {
			mv.setViewName("tarefas/inserir");
			mv.addObject(tarefa);
		} else {
			String emailUsuario = request.getUserPrincipal().getName();
			Usuario usuarioLogado = serviceUsuario.encontrarPorEmail(emailUsuario);
			tarefa.setUsuario(usuarioLogado);
			repositoryTarefa.save(tarefa);
			mv.setViewName("redirect:/tarefas/listar");
		}
		return mv;
	}

	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView();
		Tarefa tarefa = repositoryTarefa.getOne(id);
		mv.addObject("tarefa", tarefa);
		mv.setViewName("tarefas/alterar");
		return mv;
	}

	@PostMapping("/alterar")
	public ModelAndView alterar(@Valid Tarefa tarefa, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		if (tarefa.getDataExpiracao() == null) {
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoVazia", "A data de expiração é obrigatória.");
		} else {
			if (tarefa.getDataExpiracao().before(new Date())) {
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida",
						"A data de expiração não pode ser anterior a data atual.");
			}
		}
		if (result.hasErrors()) {
			mv.setViewName("tarefas/alterar");
			mv.addObject(tarefa);
		} else {
			mv.setViewName("redirect:/tarefas/listar");
			repositoryTarefa.save(tarefa);
		}
		return mv;
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id) {
		repositoryTarefa.deleteById(id);
		return "redirect:/tarefas/listar";
	}

	@GetMapping("/concluir/{id}")
	public String concluir(@PathVariable("id") Long id) {
		Tarefa tarefa = repositoryTarefa.getOne(id);
		tarefa.setConcluida(true);
		repositoryTarefa.save(tarefa);
		return "redirect:/tarefas/listar";
	}

}
