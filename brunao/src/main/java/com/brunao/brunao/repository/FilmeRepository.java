package com.brunao.brunao.repository;

import com.brunao.brunao.model.Filme;
import org.springframework.data.repository.CrudRepository;
// isso serve para fazer as consultas basicas no banco
// ja vem com comandos prontos pra fazer coisas no banco
public interface FilmeRepository extends CrudRepository<Filme, Integer> {
}
