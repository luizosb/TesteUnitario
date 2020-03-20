package br.ce.wcaquino.servicos;

import static br.ce.waquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.waquino.matchers.MatchersProprios.ehHoje;
import static br.ce.waquino.matchers.MatchersProprios.ehHojeComDiferencaDeDias;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builder.FilmeBuilder.umfilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.ce.wcaquino.builder.FilmeBuilder;
import br.ce.wcaquino.builder.LocacaoBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import junit.framework.Assert;

public class LocacaoServiceTest {

	private LocacaoService service;
	
	private SPCService spc;
	
	private LocacaoDAO dao;
	
	private EmailServices email;
	
//	private static int cont = 0;	

	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Before
	public void antes() {
		service = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		spc = Mockito.mock(SPCService.class);
		service.setSPCService(spc);
		email = Mockito.mock(EmailServices.class);
		service.setEmailService(email);
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
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().comValor(5.0).agora());

		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// verifique q o valor da locação é 5
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		//error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHoje());
		//error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
	}

	// metodo elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmesSemEstoque() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		service.alugarFilme(usuario, filmes);
	}
	
	//metodo robusta
	@Test
	public void naoDeveAlugarFilmesSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(umfilme().agora());

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
		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		service.alugarFilme(usuario, null);		
	}
	
	@Test
	public void devePagar75PctFilme3() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(4.0));
					
	}
	
	@Test
	public void devePagar50PctFilme4() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(4.0));
					
	}
	
	@Test
	public void devePagar25PctFilme5() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(4.0));
	}
	
	@Test
	public void devePagar0PctFilme6() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());
		
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		assertThat(resultado.getValor(), is(4.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoLugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umfilme().agora());	
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException	 {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umfilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado!"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Otavio").agora();
		Usuario usuario3 = umUsuario().comNome("Silva").agora();
		List<Locacao> locacoes = 
				Arrays.asList(
					umLocacao().atrasado().comUsuario(usuario).agora(),
					umLocacao().comUsuario(usuario2).agora(),
					umLocacao().atrasado().comUsuario(usuario3).agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		service.notificarAtrasos();
		
		verify(email).notificarAtraso(usuario);
		verify(email).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		verifyZeroInteractions(spc);
	}
	
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
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