package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	private FilmeBuilder() {
	}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme("Filme 1", 2, 4.0);
		return builder;
	}
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme("Filme 1", 0, 4.0);
		return builder;
	}
	
	public FilmeBuilder comValor(Double valor) {
		this.filme.setPrecoLocacao(valor);
		return this;
	}
	
	public FilmeBuilder comNome(String nome) {
		this.filme.setNome(nome);
		return this;
	}

	public Filme getFilme() {
		return filme;
	}

}
