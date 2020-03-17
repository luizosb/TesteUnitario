package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {		
	
		@Rule
		public ErrorCollector error = new ErrorCollector();  
	
		@Test
		public void testeLocacao() {
		
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Luiz");
		Filme filme = new Filme("Star Wars", 0, 10.0);
	 
		Locacao locacao;
		try {
			locacao = service.alugarFilme(usuario, filme);

			//verificação
			//verifique q o valor da locação é 5
			error.checkThat(locacao.getValor(), is(equalTo(6.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(false));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Não deveria lançar exceção!");
		}
		
		
	}

}
