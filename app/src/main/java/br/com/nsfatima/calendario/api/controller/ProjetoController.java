package br.com.nsfatima.calendario.api.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import br.com.nsfatima.calendario.application.usecase.projeto.CreateProjetoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projetos")
public class ProjetoController {

    private final CreateProjetoUseCase createProjetoUseCase;

    public ProjetoController(CreateProjetoUseCase createProjetoUseCase) {
        this.createProjetoUseCase = createProjetoUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody Map<String, String> request) {
        return createProjetoUseCase.create(request.getOrDefault("nome", "Projeto"), request.getOrDefault("descricao", ""));
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return List.of(Map.of(
                "id", UUID.randomUUID().toString(),
                "nome", "Projeto Pastoral",
                "descricao", "Planejamento"));
    }

    @PatchMapping("/{projetoId}")
    public Map<String, Object> patch(@PathVariable UUID projetoId, @RequestBody Map<String, Object> payload) {
        return Map.of(
                "id", projetoId.toString(),
                "updated", true,
                "payload", payload);
    }
}
