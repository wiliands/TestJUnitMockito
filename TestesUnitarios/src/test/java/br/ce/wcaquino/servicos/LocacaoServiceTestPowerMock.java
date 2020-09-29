package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNaSegunda;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(value = {LocacaoService.class})
public class LocacaoServiceTestPowerMock {
	
	//@Rule
	public ErrorCollector error = new ErrorCollector();
	
	//@Rule
	public ExpectedException exceptions = ExpectedException.none();

	//@InjectMocks
	private LocacaoService service;
	
	//@Mock
	private LocacaoDAO dao;
	//@Mock
	private SPCService spcService;
	//@Mock
	private EmailService emailService;
	
	private int diasDiferenca;
	
	//@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		//service = PowerMockito.spy(service);
		
		diasDiferenca = 1;
		if(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY)) {
			diasDiferenca++;
		}
		
	}
	
	//@After
	public void tearDown() {
	}

	//@Test
	public void deveDevolverNaSegundaQuandoAlugadoNoSabado() throws Exception {
		//define uma condicao para executar o teste
		assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//whenNew(Date.class).withAnyArguments().thenReturn(DataUtils.obterData(26, 9, 2020));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 26);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.YEAR, 2020);
		//mockStatic(Calendar.class);
		when(Calendar.getInstance()).thenReturn(calendar);
		
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(resultado.getDataRetorno(), caiNaSegunda());
		
		//PowerMockito.verifyStatic(Calendar.class, times(2));
		Calendar.getInstance();
	}
	
	//@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		//cenario
		Usuario usuario = umUsuario().getUsuario();
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		//doReturn(1.0).when(service, "calcularPrecoLocacao", filmes);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(4.0));
		//verifyPrivate(service).invoke("calcularPrecoLocacao", filmes);
	}
	
	//@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().getFilme());
		
		//acao
		//Double valorLocacao = (Double) Whitebox.invokeMethod(service, "calcularPrecoLocacao", filmes);
		
		//verificaco
		//assertThat(valorLocacao, is(4.0));
		
	}
	
}