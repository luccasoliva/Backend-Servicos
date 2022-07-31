package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Models.StatusFuncionario;
import com.soulcode.Servicos.Repositories.CargoRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import com.soulcode.Servicos.Services.Exceptions.DataIntegrityViolationException;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// quando se fala em serviços, estamos falando dos métodos do crud da tabela

@Service
public class FuncionarioService {

    // aqui se faz a injeção de dependência
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    CargoRepository cargoRepository;


    private CacheManager cacheManager;

    //primeiro serviço na tabela de funcionário vai ser a leitura de todos
    //os funcionários cadastrados
    //findAll -> método do spring Data JPA -> busca todos os registros de uma tabela
    @Cacheable("funcionariosCache") // só chama o return se o cache expirar funcionarios::[]
    public List<Funcionario> mostrarTodosFuncionarios(){

        return funcionarioRepository.findAll();
    }
    public List<Funcionario> mostrarTodosFuncionariosAtivos(){

        return funcionarioRepository.findByStatus(StatusFuncionario.ATIVO.getConteudo());
    }



    //vamos mais um serviço relacionado ao funcionário
    //criar um serviço de buscar apenas um funcionário pelo seu id(chave primária)

    @Cacheable(value = "funcionariosCache", key = "#idFuncionario") // funcionarios::1
    public Funcionario mostrarUmFuncionarioPeloId(Integer idFuncionario)
    {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow(
                () -> new EntityNotFoundException("Funcionário não cadastrado: " + idFuncionario)
        );
    }

    //vamos criar mais um serviço pra buscar um funcionário pelo seu email
    @Cacheable(value = "funcionariosCache", key = "#email") // funcionarios::
    public Funcionario mostrarUmFuncionarioPeloEmail(String email){
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);
        return funcionario.orElseThrow();
    }


    @Cacheable(value = "funcionariosCache", key = "#idCargo") // funcionarios::1
    public List<Funcionario> mostrarTodosFuncionariosDeUmCargo(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return funcionarioRepository.findByCargo(cargo);
    }

    //query findTotalPagamento
    public Double findTotalPagamento(Integer idFuncionario){
        Optional<Funcionario> funcionario = Optional.ofNullable(funcionarioRepository.findById(idFuncionario).orElseThrow(
                () -> new EntityNotFoundException("Funcionário não cadastrado: " + idFuncionario)
        ));
        return funcionarioRepository.findTotalPagamento(funcionario);
    }

    //vamos criar um serviço para cadastrar um novo funcionário
    @CachePut(value = "funcionariosCache", key = "#funcionario.idFuncionario") // funcionarios::1
    public Funcionario cadastrarFuncionario(Funcionario funcionario, Integer idCargo) throws DataIntegrityViolationException {


        funcionario.setIdFuncionario(null);
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        funcionario.setCargo(cargo.get());

        return funcionarioRepository.save(funcionario);
    }

    @CacheEvict(value = "funcionariosCache", key = "#idFuncionario") // funcionarios::1
    public void excluirFuncionario(Integer idFuncionario){
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setStatus(StatusFuncionario.INATIVO);
        funcionarioRepository.save(funcionario);
    }
    @CachePut(value = "funcionariosCache", key = "#funcionario.idFuncionario")
    public Funcionario editarFuncionario(Funcionario funcionario){
        return funcionarioRepository.save(funcionario);
    }


    @CachePut(value = "funcionariosCache", key = "#idFuncionario") // funcionarios::1
    public Funcionario salvarFoto(Integer idFuncionario, String caminhoFoto){
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);

        return funcionarioRepository.save(funcionario);
    }

    //Número total de funcionarios pelo findByCargo_Nome

    public List<Funcionario> totalFuncionariosPeloCargo(String nomeCargo){
        return funcionarioRepository.findByCargo_Nome(nomeCargo);
    }


    public List<Funcionario> buscarFuncionariosSemFoto(){
        return funcionarioRepository.findByFotoIsNull();
    }


    public List<Funcionario> buscarFuncionariosSemChamado(){
        return funcionarioRepository.findByChamadosIsNull();
    }
}
