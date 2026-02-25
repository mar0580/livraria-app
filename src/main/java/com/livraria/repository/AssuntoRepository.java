package com.livraria.repository;

import com.livraria.entity.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {
}
