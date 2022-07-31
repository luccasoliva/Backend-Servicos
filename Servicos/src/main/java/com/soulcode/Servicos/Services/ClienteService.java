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
    //aqui fazemos a injeção de depência
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ChamadoService chamadoService;

    // findAll (método da Spring Data) - busca todos os registros
    @Cacheable("clientesCache") // só chama o return se o cache expirar clientesCache::[]
    public List<Cliente> mostrarTodosClientes() {
        return clienteRepository.findAll();
    }

    // findById - busca um cliente específico pelo seu id
    @Cacheable(value = "clientesCache", key = "#idCliente") // clientesCache::1
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
        //por precaução vamos limpar o campo de id do cliente
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    // editar um cliente já cadastrado
    // atualiza(substitui) a info no cache de acordo com a key
    @CachePut(value = "clientesCache", key = "#cliente.idCliente") //SPEL
    public Cliente editarCliente(Cliente cliente) {
        mostrarUmCliente(cliente.getIdCliente());
        return clienteRepository.save(cliente); // FAZ O CACHE DESSE RETORNO
    }

    // deleteById  - excluir um cliente pelo seu id
    @CacheEvict(value = "clientesCache", key = "#idCliente", allEntries = true)
    public void excluirCliente(Integer idCliente) {
        mostrarUmCliente(idCliente);
        clienteRepository.deleteById(idCliente);
    }
}
