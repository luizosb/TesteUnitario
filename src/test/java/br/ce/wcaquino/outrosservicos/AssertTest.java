package br.ce.wcaquino.outrosservicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals("Erro de comparação.", 1, 1);
		Assert.assertEquals(0.51, 0.51, 0.01);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario usu1 = new Usuario("Luiz");
		Usuario usu2 = new Usuario("Otávio");
		Usuario usu3 = null;
		
		Assert.assertEquals(usu1,usu2);
		
		Assert.assertSame(usu2, usu2);
		Assert.assertNotSame(usu3, usu2);
		
		Assert.assertNull(usu3);
		Assert.assertNotNull(usu3);
		 
		
	}
	
	
}
