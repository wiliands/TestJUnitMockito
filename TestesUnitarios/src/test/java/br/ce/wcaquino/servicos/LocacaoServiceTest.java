package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

 public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exceptions = ExpectedException.none();
	
	private LocacaoService service;
	
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	private int diasDiferenca;
	
	@Before
	public void setup() {
		service = new LocacaoService();
		
		dao = mock(LocacaoDAO.class);
		spcService = mock(SPCService.class);
		emailService = mock(EmailService.class);
		
		service.setLocacaoDAO(dao);
		service.setSPCService(spcService);
		service.setEmailService(emailService);
		
		diasDiferenca = 1;
		if(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)) {
			diasDiferenca++;
		}
		
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).getFilme());
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(equalTo(true)));
		//error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(diasDiferenca)), is(equalTo(true)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(diasDiferenca));
		
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().getFilme());
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//cenario
		//Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		//acao
		try {
			service.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario nao informado"));
		}
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		//List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		exceptions.expect(LocadoraException.class);
		exceptions.expectMessage("Filme nao informado");
		
		//acao
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void deveDevolverNaSegundaQuandoAlugadoNoSabado() throws LocadoraException, FilmeSemEstoqueException {
		//define uma condicao para executar o teste
		//assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(resultado.getDataRetorno(), caiNaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		when(spcService.possuiNegativacao(any(Usuario.class))).thenReturn(true);
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado"));
		}
		
		//verificacao
		verify(spcService).possuiNegativacao(usuario);
		
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario1 = umUsuario().getUsuario();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").getUsuario();
		Usuario usuario3 = umUsuario().comNome("Usuario 3").getUsuario();
		List<Locacao> locacoes = Arrays.asList(umLocacao().atrasado().comUsuario(usuario1).getLocacao(),
											   umLocacao().comUsuario(usuario2).getLocacao(),
											   umLocacao().comUsuario(usuario3).atrasado().getLocacao(),
											   umLocacao().comUsuario(usuario3).atrasado().getLocacao());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		verify(emailService, atLeast(2)).notificarAtraso(any(Usuario.class));
		verify(emailService).notificarAtraso(usuario1);
		verify(emailService, never()).notificarAtraso(usuario2);
		verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
		verifyNoMoreInteractions(emailService);

	}
	
//	public static void main(String[] args) {
//		new BuilderMaster().gerarCodigoClasse(Locacao.class);
//	}
	
}