package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @GetMapping("/funcionarios")
    public List<Funcionario> mostrarTodosFuncionarios(){
        List<Funcionario> funcionarios = funcionarioService.mostrarTodosFuncionarios();
        return funcionarios;
    }

    @GetMapping("/funcionarios/ativos")
    public List<Funcionario> mostrarTodosFuncionariosAtivos(){
        List<Funcionario> funcionarios = funcionarioService.mostrarTodosFuncionariosAtivos();
        return funcionarios;
    }

    @GetMapping("/funcionarios/{idFuncionario}")
    public ResponseEntity<Funcionario> mostrarUmFuncionarioPeloId(@PathVariable Integer idFuncionario){
        Funcionario funcionario = funcionarioService.mostrarUmFuncionarioPeloId(idFuncionario);
        return ResponseEntity.ok().body(funcionario);
    }

    @GetMapping("/funcionariosEmail/{email}")
    public ResponseEntity<Funcionario> mostrarUmFuncionarioPeloEmail(@PathVariable String email){
        Funcionario funcionario = funcionarioService.mostrarUmFuncionarioPeloEmail(email);
        return ResponseEntity.ok().body(funcionario);
    }

    @GetMapping("funcionariosDoCargo/{idCargo}")
    public List<Funcionario> mostrarTodosFuncionariosDeUmCargo(@PathVariable Integer idCargo){
        List<Funcionario> funcionarios = funcionarioService.mostrarTodosFuncionariosDeUmCargo(idCargo);
        return funcionarios;
    }


    @GetMapping("/funcionarios/totalRecebido/{idFuncionario}")
    public Double findTotalPagamento(@PathVariable Integer idFuncionario){
        Double total = funcionarioService.findTotalPagamento(idFuncionario);
        return Objects.requireNonNullElse(total, 0.0);
    }

    @PostMapping("/funcionarios/{idCargo}")
    public ResponseEntity<Funcionario> cadastrarFuncionario(@PathVariable Integer idCargo, @RequestBody Funcionario funcionario){

        funcionario = funcionarioService.cadastrarFuncionario(funcionario,idCargo);
        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("id")
                .buildAndExpand(funcionario.getIdFuncionario()).toUri();
        return ResponseEntity.created(novaUri).body(funcionario);

    }

    @DeleteMapping("/funcionarios/{idFuncionario}")
    public ResponseEntity<Void> excluirFuncionario(@PathVariable Integer idFuncionario){
        funcionarioService.excluirFuncionario(idFuncionario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/funcionarios/{idFuncionario}")
    public ResponseEntity<Funcionario> editarFuncionario(@PathVariable Integer idFuncionario,
                                                         @RequestBody Funcionario funcionario){
        funcionario.setIdFuncionario(idFuncionario);
        funcionarioService.editarFuncionario(funcionario);
        return ResponseEntity.ok().body(funcionario);
    }


    @GetMapping("/qtdFuncionariosPorCargo")
    public int totalFuncionariosPeloCargo(@RequestParam("cargo") String nomeCargo){
        return funcionarioService.totalFuncionariosPeloCargo(nomeCargo).size();
    }


    @GetMapping("/funcionariosSemFoto")
    public List<Funcionario> mostrarTodosFuncionariosSemFoto(){
        List<Funcionario> funcionarios = funcionarioService.buscarFuncionariosSemFoto();
        return funcionarios;
    }

    @GetMapping("/funcionariosSemChamados")
    public List<Funcionario> buscarFuncionariosSemChamado(){
        List<Funcionario> funcionarios = funcionarioService.buscarFuncionariosSemChamado();
        return funcionarios;
    }


}
