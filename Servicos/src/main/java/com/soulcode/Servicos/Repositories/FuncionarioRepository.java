package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Models.StatusFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByEmail(String email);
    // Optional<Funcionario> findByNome

    //Optional<Funcionario> findByNomeAndEmailAndFoto(String nome, String email, String foto);
    List<Funcionario> findByCargo(Optional<Cargo> cargo);

    @Query(value = "SELECT * FROM funcionario WHERE status = :status", nativeQuery = true)
    List<Funcionario> findByStatus(String status);

    //findByNomeDoCargo
    List<Funcionario> findByCargo_Nome(String nome);

    List<Funcionario> findByFotoIsNull();

    List<Funcionario> findByChamadosIsNull();

    @Query(value = "  SELECT SUM(pagamento.valor) AS \"TOTAL\" FROM pagamento, chamado, funcionario WHERE chamado.status = \"CONCLUIDO\" AND funcionario.id_funcionario = :idFuncionario AND chamado.id_funcionario = funcionario.id_funcionario AND pagamento.id_pagamento = chamado.id_chamado AND pagamento.status = \"QUITADO\";\n" , nativeQuery = true)
    Double findTotalPagamento(Optional<Funcionario> idFuncionario);
}
