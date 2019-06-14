package br.eic.sca.modules.sie.filters;

public class CursoFilter
{
    private String nome;
    private String sigla;

    public CursoFilter(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }
}
