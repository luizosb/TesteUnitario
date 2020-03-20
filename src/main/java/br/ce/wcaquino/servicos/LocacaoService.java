package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	
	private LocacaoDAO dao;
	private SPCService spcServ;
	private EmailServices emailServ;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		
		for (Filme filme: filmes) { 
			if(filme.getEstoque()==0) {
				throw new FilmeSemEstoqueException();
			}
		}
		
		if(spcServ.possuiNegativacao(usuario)) {
			throw new LocadoraException("Usuario negativado!");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0.0;
		for(int i = 0; i < filmes.size(); i++) {
			Double valor = filmes.get(i).getPrecoLocacao();
			switch(i) {
				case 2:	valor = valor * 0.75; break;
				case 3: valor = valor * 0.50; break;
				case 4: valor = valor * 0.25; break;
				case 5: valor = valor * 0.0; break;
			}
			valorTotal += valor;
		}
		locacao.setValor(valorTotal);
			
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.MONDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();  
		for(Locacao locacao : locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailServ.notificarAtraso(locacao.getUsuario());				
			}
		}
	}

	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSPCService(SPCService spc) {
		spcServ = spc;
	}
	
	public void setEmailService(EmailServices email) {
		emailServ = email;
	}
}
