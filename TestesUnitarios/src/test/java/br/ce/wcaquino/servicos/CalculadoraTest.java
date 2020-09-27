package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void somar() {
		int a = 5;
		int b = 3;
		int result = calc.somar(a, b);
		assertEquals(8, result);
	}
	
	@Test
	public void subtrair() {
		int a = 5;
		int b = 3;
		int result = calc.subtrair(a, b);
		assertEquals(2, result);
	}
	
	@Test
	public void dividir() throws NaoPodeDividirPorZeroException {
		int a = 6;
		int b = 3;
		int result = calc.dividr(a, b);
		assertEquals(2, result);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void dividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 6;
		int b = 0;
		int result = calc.dividr(a, b);
		assertEquals(2, result);
	}
	
	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";
		
		int resultado = calc.dividr(a, b);
		assertEquals(2, resultado);
	}
	
	@Test
	public void testeMock() {
		calc = mock(Calculadora.class);
		when(calc.somar(eq(1), anyInt())).thenReturn(4);
		
		System.out.println(calc.somar(1, 8));
	}

}
