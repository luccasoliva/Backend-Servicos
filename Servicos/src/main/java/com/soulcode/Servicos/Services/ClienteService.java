package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.*;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.PagamentoRepository;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ChamadoService chamadoService;


    @Cacheable("clientesCache")
    public List<Cliente> mostrarTodosClientes() {
        return clienteRepository.findAll();
    }


    @Cacheable(value = "clientesCache", key = "#idCliente")
    public Cliente mostrarUmCliente(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow();
    }

    public Double mostrarValorPago(Integer idCliente) {
         if (clienteRepository.findById(idCliente).isPresent()) {
           return chamadoRepository.findChamadosByIdCliente(idCliente)
                     .stream().
                     filter(result -> result.getStatus() == StatusChamado.CONCLUIDO)
                     .map(result -> pagamentoRepository.findByIdPagamento(result.getIdChamado()))
                   .filter(result -> result.getStatus() == StatusPagamento.QUITADO)
                     .mapToDouble(Pagamento::getValor).sum();
         } else {
             throw new EntityNotFoundException("Cliente não cadastrado: " + idCliente);
         }

    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente inserirCliente(Cliente cliente) {

        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }


    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente editarCliente(Cliente cliente) {
        mostrarUmCliente(cliente.getIdCliente());
        return clienteRepository.save(cliente);
    }


    @CacheEvict(value = "clientesCache", key = "#idCliente", allEntries = true)
    public void excluirCliente(Integer idCliente) {
        mostrarUmCliente(idCliente);
        clienteRepository.deleteById(idCliente);
    }
}
