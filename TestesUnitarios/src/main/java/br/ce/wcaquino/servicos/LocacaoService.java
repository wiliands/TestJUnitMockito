package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {

		validaAlugarFilme(usuario, filmes);
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(calcularPrecoLocacao(filmes));

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}

	private Double calcularPrecoLocacao(List<Filme> filmes) {
		Double totalLocacao = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Double valorFilme = filmes.get(i).getPrecoLocacao();
			
			switch (i) {
				case 2:
					valorFilme = valorFilme * 0.75;
					break;
				case 3:
					valorFilme = valorFilme * 0.5;
					break;
				case 4:
					valorFilme = valorFilme * 0.25;
					break;
				case 5:
					valorFilme = 0d;
					break;
				default:
					break;
			}
			totalLocacao += valorFilme;
		}
		return totalLocacao;
	}

	private void validaAlugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException {
		if(usuario == null) {
			throw new LocadoraException("Usuario nao informado");
		}
		
		if(spcService.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuario negativado");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme nao informado");
		}
		
		for (Filme filme : filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");
			}
		}
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		locacoes.forEach(loc -> {
			if(loc.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(loc.getUsuario());
			}
		});
	}
	
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSPCService(SPCService spcService) {
		this.spcService = spcService;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

}