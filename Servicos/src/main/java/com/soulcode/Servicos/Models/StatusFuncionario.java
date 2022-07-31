package com.soulcode.Servicos.Models;

public enum StatusFuncionario {

    ATIVO("Ativo"),
    INATIVO("Inativo");

    private String conteudo;

    private StatusFuncionario(String conteudo){
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }
}
