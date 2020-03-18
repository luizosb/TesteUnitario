package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

public class LocacaoServiceTest {

	private LocacaoService service;
	
//	private static int cont = 0;	

	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Before
	public void antes() {
		service = new LocacaoService();
	//	cont = cont + 1;
	//	System.out.println(cont);
	}
	
	/*@After
	public void depois() {
	//	System.out.println();
	}
	
	@BeforeClass
	public static void antesClass() {
		System.out.println("antes");
	}
	
	@AfterClass
	public static void depoisClass() {
		System.out.println("depois");
	}*/

	@Test
	public void deveAlugarFilme() throws Exception {
		Usuario usuario = new Usuario("Luiz");
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars", 2, 10.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// verifique q o valor da locação é 5
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}

	// metodo elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmesSemEstoque() throws Exception {
		Usuario usuario = new Usuario("Luiz");
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars", 2, 10.0));

		service.alugarFilme(usuario, filmes);
	}
	
	//metodo robusta
	@Test
	public void naoDeveAlugarFilmesSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars", 2, 10.0));

		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}
	
	//metodo nova
	@Test
	public void naoDeveAlugarFilmesSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Luiz");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		service.alugarFilme(usuario, null);		
	}
	
	@Test
	public void devePagar75PctFilme3() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Luiz");
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars", 2, 4.0), 
										   new Filme("Senhor dos Aneis", 3, 6.0),
										   new Filme("Bee Movie", 7, 8.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(16.0));
					
	}
	
	@Test
	public void devePagar50PctFilme4() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Luiz");
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars", 2, 4.0), 
										   new Filme("Senhor dos Aneis", 3, 6.0),
										   new Filme("Bee Movie", 7, 8.0),
										   new Filme("Lagoa Azul", 9, 6.0));
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(16.0));
					
	}
	

	/* metodo robusto
	@Test
	public void testLocacao_filmeSemEstoque2() {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Luiz");
		Filme filme = new Filme("Star Wars", 0, 10.0);

		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma exceção");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	// metodo novo
	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Luiz");
		Filme filme = new Filme("Star Wars", 0, 10.0);

		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		service.alugarFilme(usuario, filme);
	}
	*/
}