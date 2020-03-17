package br.ce.wcaquino.servicos;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Test
	public void teste() {
		
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Luiz");
		Filme filme = new Filme("Star Wars", 1, 10.0);
	
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		Assert.assertTrue(locacao.getValor() == 10.0);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

}
