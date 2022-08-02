package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Services.ChamadoService;
import com.soulcode.Servicos.Services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class ChamadoController {

    @Autowired
    ChamadoService chamadoService;

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping("/chamados")
    public List<Chamado> mostrarTodosChamados(){
        List<Chamado> chamados = chamadoService.mostrarTodosChamados();
        return chamados;
    } //

    @GetMapping("/chamados/{idChamado}")
    public ResponseEntity<Chamado> buscarUmChamado(@PathVariable Integer idChamado){
        Chamado chamado  = chamadoService.mostrarUmChamado(idChamado);
        return ResponseEntity.ok().body(chamado);

    } //

    @GetMapping("/chamadosPeloCliente/{idCliente}")
    public List<Chamado> buscarChamadosPeloCliente(@PathVariable Integer idCliente){
        List<Chamado> chamados = chamadoService.buscarChamadosPeloCliente(idCliente);
        return chamados;
    }

    @GetMapping("/chamadosPeloFuncionario/{idFuncionario}")
    public List<Chamado> buscarChamadosPeloFuncionario(@PathVariable Integer idFuncionario){
        List<Chamado> chamados = chamadoService.buscarChamadosPeloFuncionario(idFuncionario);
        return chamados;


    }

    @GetMapping("/chamadosPeloStatus")
    public List<Chamado> buscarChamadosPeloStatus(@RequestParam("status") String status){
        List<Chamado> chamados = chamadoService.buscarChamadosPeloStatus(status);
        return chamados;
    }

    @GetMapping("/chamadosPorIntervaloData")
    public List<Chamado> buscarPorIntervaloData(@RequestParam("data1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data1,
                                                @RequestParam("data2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date data2){
        List<Chamado> chamados = chamadoService.buscarPorIntervaloData(data1,data2);
        return chamados;
    }

    @GetMapping("/chamados/porStatusPagamento")
    public List<Chamado> buscarChamadosPorStatusPagamento(@RequestParam("status") String status){
        List<Chamado> chamados = chamadoService.buscarChamadosPeloStatusPagamento(status);
        return chamados;
    }


    @GetMapping("/chamados/quantidadePorStatus")
    public Integer buscarQuantidadeChamadosPorStatus(@RequestParam("status") String status){
        Integer quantidade = chamadoService.buscarNumeroChamadosPeloStatus(status);
        return quantidade;
    }


    @PostMapping("/chamados/{idCliente}/{idFuncionario}")
    public ResponseEntity<Chamado> cadastrarChamado(@PathVariable Integer idCliente,
                                                    @PathVariable Integer idFuncionario,
                                                    @RequestBody Chamado chamado){
        chamado = chamadoService.cadastrarChamado(chamado,idCliente, idFuncionario);
        URI novaUri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}")
                .buildAndExpand(chamado.getIdChamado()).toUri();
        return ResponseEntity.created(novaUri).body(chamado);
    }


    @DeleteMapping("/chamados/{idChamado}")
       public ResponseEntity<Void> excluirChamado(@PathVariable Integer idChamado){
        chamadoService.excluirChamado(idChamado);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/chamados/{idChamado}")
    public ResponseEntity<Chamado> editarChamado(@PathVariable Integer idChamado,
                                                 @RequestBody Chamado chamado){
        chamado.setIdChamado(idChamado);
        chamado = chamadoService.editarChamado(chamado, idChamado);
        return ResponseEntity.ok().body(chamado);
    }


    @PutMapping("/chamadosAtribuirFuncionario/{idChamado}/{idFuncionario}")
    public ResponseEntity<Chamado> atribuirFuncionario(@PathVariable Integer idChamado,
                                                       @PathVariable Integer idFuncionario){
        chamadoService.atribuirFuncionario(idChamado, idFuncionario);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/chamadosModificarStatus/{idChamado}")
    public ResponseEntity<Chamado> modificarStatus(@PathVariable Integer idChamado,
                                                   @RequestParam("status") String status){
        chamadoService.modificarStatus(idChamado,status);
        return ResponseEntity.noContent().build();
    }

}
