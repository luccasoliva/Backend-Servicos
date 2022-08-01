package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.*;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import com.soulcode.Servicos.Repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;


    @Cacheable("chamadosCache")
    public List<Chamado> mostrarTodosChamados(){
        return chamadoRepository.findAll();	}


    @Cacheable(value = "chamadosCache", key = "#idChamado")
    public Chamado mostrarUmChamado(Integer idChamado) {
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);
        return chamado.orElseThrow();
    }


    @Cacheable(value = "chamadosCache", key = "idCliente")
    public List<Chamado> buscarChamadosPeloCliente(Integer idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return chamadoRepository.findByCliente(cliente);

    }
    @Cacheable(value = "chamadosCache", key = "#idFuncionario")
    public List<Chamado> buscarChamadosPeloFuncionario(Integer idFuncionario){
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return chamadoRepository.findByFuncionario(funcionario);
    }

    @Cacheable(value = "chamadosCache", key = "#status")
    public List<Chamado> buscarChamadosPeloStatus(String status){
        return chamadoRepository.findByStatus(status);
    }


    @Cacheable(value = "chamadosCache", key = "T(java.util.Objects).hash(#data1, # data2)")
    public List<Chamado> buscarPorIntervaloData(Date data1, Date data2){
        return chamadoRepository.findByIntervaloData(data1,data2);
    }



    public List<Chamado> buscarChamadosPeloStatusPagamento(String status){
        return chamadoRepository.findChamadoByStatusPagamento(status);
    }


    public Integer buscarNumeroChamadosPeloStatus(String status){
        return chamadoRepository.findByStatus(status).size();
    }


    @CachePut(value = "chamadosCache", key = "#chamados.idChamado")
    public Chamado cadastrarChamado(Chamado chamado, Integer idCliente){

        chamado.setStatus(StatusChamado.RECEBIDO);

        chamado.setFuncionario(null);

        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        chamado.setCliente(cliente.get());
        return chamadoRepository.save(chamado);
    }


    public void excluirChamado(Integer idChamado){
        chamadoRepository.deleteById(idChamado);
    }

    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamado editarChamado(Chamado chamado, Integer idChamado){

        Chamado chamadoSemAsNovasAlteracoes = mostrarUmChamado(idChamado);
        Funcionario funcionario = chamadoSemAsNovasAlteracoes.getFuncionario();
        Cliente cliente = chamadoSemAsNovasAlteracoes.getCliente();

        chamado.setCliente(cliente);
        chamado.setFuncionario(funcionario);
        return chamadoRepository.save(chamado);
    }



    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamado atribuirFuncionario(Integer idChamado, Integer idFuncionario){

        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);

        Chamado chamado = mostrarUmChamado(idChamado);
        chamado.setFuncionario(funcionario.get());
        chamado.setStatus(StatusChamado.ATRIBUIDO);

        return chamadoRepository.save(chamado);
    }



    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamado modificarStatus(Integer idChamado,String status){
        Chamado chamado = mostrarUmChamado(idChamado);
        switch (status){
            case "ATRIBUIDO":
            {
                chamado.setStatus(StatusChamado.ATRIBUIDO);
                break;
            }
            case "CONCLUIDO":
            {
                chamado.setStatus(StatusChamado.CONCLUIDO);
                break;
            }
            case "ARQUIVADO":
            {
                chamado.setStatus(StatusChamado.ARQUIVADO);
                break;
            }
            case "RECEBIDO":
            {
                chamado.setStatus(StatusChamado.RECEBIDO);
                break;
            }
        }
        return chamadoRepository.save(chamado);
    }



}
