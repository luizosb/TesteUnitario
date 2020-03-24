package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import junit.framework.Assert;

public class CalculadoraTest {
	
	
	private Calculadora calc;
	
	@Before
	public void antes() {
		calc = new Calculadora();
		//System.out.println("paralelizando antes....");
	}
	
	@After
	public void depois() {
		calc = new Calculadora();
		//System.out.println("paralelizando depois....");
	}
	
	@Test
	public void somaDeValores() {
		int a = 3;
		int b = 7;
		
		int resultado = calc.somar(a, b);
		
		Assert.assertEquals(10, resultado);
	}
	
	@Test
	public void subtrairValores() {
		int a = 5;
		int b = 2;
		
		int resultado = calc.subtrair(a, b);
		
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void dividirValores() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 5;
		
		int resultado = calc.dividir(a, b);
				
		Assert.assertEquals(2, resultado);
	}
	
	@Test
	public void multiplicarValores() {
		int a = 5;
		int b = 2;
		
		int resultado = calc.multiplicar(a, b);
		
		Assert.assertEquals(10, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 0;
 		
		calc.dividir(a, b);
		
	}

}
