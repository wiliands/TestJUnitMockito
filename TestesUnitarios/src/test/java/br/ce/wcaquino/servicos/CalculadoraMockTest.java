package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	Calculadora calcMock;
	
	@Spy
	Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		CalculadoraTest.ordem.append("3");
	}
	
	@AfterClass
	public static void imprimirOrdem() {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	
	@Test
	public void testeMockSpy() {
		when(calcMock.somar(1, 2)).thenReturn(8);
		when(calcSpy.somar(1, 2)).thenReturn(8);
		doReturn(8).when(calcSpy).somar(1, 2);
		doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock: " + calcMock.somar(1, 2));
		System.out.println("Spy: " + calcSpy.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();
	}
	
	@Test
	public void teste() {
		Calculadora calc = mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);
		when(calc.somar(argCaptor.capture(), argCaptor.capture())).thenReturn(5);
		
		assertEquals(5, calc.somar(1, 10000));
		System.out.println(argCaptor.getAllValues());
	}

}
