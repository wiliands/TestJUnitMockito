package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
	//CalculadoraTest.class,
	LocacaoServiceTest.class,
	CalculoValorLocacaoTest.class
})
public class SuiteExecucao {

	@BeforeClass
	public static void beforeClass() {
		System.out.println(":: Inicio Testes ::");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println(":: Fim Testes ::");
	}
}
