package br.ce.wcaquino.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Locacao {

	private Usuario usuario;
	private List<Filme> filmes;
	private Date dataLocacao;
	private Date dataRetorno;
	private Double valor;
	
	public Locacao() {
		super();
	}

	public Locacao(Locacao locacao) {
		this.usuario = new Usuario(locacao.getUsuario().getNome());
		this.dataLocacao = locacao.getDataLocacao();
		this.dataRetorno = locacao.getDataRetorno();
		this.filmes = new ArrayList<>(0);
		locacao.getFilmes().stream().forEach(f -> this.filmes.add(new Filme(f.getNome(), f.getEstoque(), f.getPrecoLocacao())));
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataLocacao() {
		return dataLocacao;
	}

	public void setDataLocacao(Date dataLocacao) {
		this.dataLocacao = dataLocacao;
	}

	public Date getDataRetorno() {
		return dataRetorno;
	}

	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public List<Filme> getFilmes() {
		return filmes;
	}

	public void setFilmes(List<Filme> filmes) {
		this.filmes = filmes;
	}

}