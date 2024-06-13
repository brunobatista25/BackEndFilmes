package com.brunao.brunao.controller;

import com.brunao.brunao.model.Filme;
import com.brunao.brunao.repository.FilmeRepository;
import com.brunao.brunao.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;

    @ApiOperation(value = "Retorna os dados de um filme")
    @GetMapping(path = "/filme/{codigo}")
    public ResponseEntity consultarFilmePorId(@PathVariable("codigo") @ApiParam(name = "codigo", value = "codigo do filme", example = "1") Integer codigo) {
        return filmeRepository.findById(codigo)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Retorna todos os filmes")
    @GetMapping(path = "/filmes")
    public ResponseEntity<Iterable<Filme>> consultarTodosOsFilmes() {
        return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.findAll());
    }

    @ApiOperation(value = "Cadastrar filmes")
    @PostMapping(path = "/salvar")
    public ResponseEntity criarFilme(@RequestBody Filme filme) {

        if (filmeRepository.existsById(filme.getCodigo())) {
            return ResponseHandler.generateResponse("Filme já cadastrado!", HttpStatus.CONFLICT);
        }
        var objetoFilme = new Filme();

        if (filme.getFaixaEtaria().equals(null) || filme.getFaixaEtaria().isEmpty() || filme.getFaixaEtaria().isBlank()) {
            return ResponseHandler.generateResponse("Faixa etária é obrigatória!", HttpStatus.BAD_REQUEST);

        }
        else if (filme.getGenero().equals(null) || filme.getGenero().isEmpty() || filme.getGenero().isBlank()) {
            return ResponseHandler.generateResponse("Genêro é obrigatório!", HttpStatus.BAD_REQUEST);

        } else if (filme.getNome().equals(null) || filme.getNome().isEmpty() || filme.getNome().isBlank()) {
            return ResponseHandler.generateResponse("Nome é obrigatório!", HttpStatus.BAD_REQUEST);

        } else if (filme.getSinopse().equals(null) || filme.getSinopse().isEmpty() || filme.getSinopse().isBlank()) {
            return ResponseHandler.generateResponse("Sinopse é obrigatório!", HttpStatus.BAD_REQUEST);

        } else  {
            objetoFilme.setCodigo(filme.getCodigo());
            objetoFilme.setFaixaEtaria(filme.getFaixaEtaria());
            objetoFilme.setSinopse(filme.getSinopse());
            objetoFilme.setGenero(filme.getGenero());
            objetoFilme.setNome(filme.getNome());

            return ResponseEntity.status(HttpStatus.CREATED).body(filmeRepository.save(objetoFilme));
        }
    }

    @ApiOperation(value = "Deletar filmes")
    @DeleteMapping(path = "/filme/{codigo}")
    public void deletarFilmePorId(@PathVariable("codigo") Integer codigo) {
        filmeRepository.findById(codigo)
                .map(record -> {
                    filmeRepository.deleteById(codigo);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "{\n" +
                        "    \"message\": \"Filme não encontrado\",\n" +
                        "}"));
    }

    @PutMapping(path = "/filme/{codigo}")
    @JsonIgnoreProperties("codigo")
    public ResponseEntity editarFilme(@PathVariable("codigo") Integer codigo, @RequestBody @Validated Filme filme) {
        Optional<Filme> filmeObject = filmeRepository.findById(filme.getCodigo());
        if (!filmeObject.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\n" +
                    "    \"message\": \"Filme não encontrado\",\n" +
                    "}");
        }

        if (filme.getFaixaEtaria() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Faixa etária é obrigatória\",\n" +
                    "}");
        }
        else if (filme.getGenero() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Genêro é obrigatório\",\n" +
                    "}");
        } else if (filme.getNome() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Nome é obrigatório\",\n" +
                    "}");
        } else if (filme.getSinopse() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Sinopse é obrigatório\",\n" +
                    "}");
        } else  {
            var filmeEditado = filmeObject.get();
            filmeEditado.getCodigo();
            filmeEditado.setNome(filme.getNome());
            filmeEditado.setSinopse(filme.getSinopse());
            filmeEditado.setGenero(filme.getGenero());
            filmeEditado.setFaixaEtaria(filme.getFaixaEtaria());
            return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.save(filmeEditado));
        }

    }

    @PatchMapping(path = "/api/usuario/{codigo}")
    @JsonIgnoreProperties({"codigo", "nome", "login", "senha"})
    public ResponseEntity editarFilmeComPatch(@PathVariable("codigo") Integer codigo, @RequestBody Filme partialFilmeUpdate) {
        Optional<Filme> filme = filmeRepository.findById(codigo);
        var filmeEditado = filme.get();
        filmeEditado.getCodigo();
        if (partialFilmeUpdate.getNome() == null) {
            filmeEditado.getNome();
        } else {
            filmeEditado.setNome(partialFilmeUpdate.getNome());
        }

        if (partialFilmeUpdate.getGenero() == null) {
            filmeEditado.getGenero();
        } else {
            filmeEditado.setGenero(partialFilmeUpdate.getGenero());
        }

        if (partialFilmeUpdate.getSinopse() == null) {
            filmeEditado.getSinopse();
        } else {
            filmeEditado.setSinopse(partialFilmeUpdate.getSinopse());
        }

        if (partialFilmeUpdate.getFaixaEtaria() == null) {
            filmeEditado.getFaixaEtaria();
        } else {
            filmeEditado.setFaixaEtaria(partialFilmeUpdate.getFaixaEtaria());
        }

        return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.save(filmeEditado));
    }
}
