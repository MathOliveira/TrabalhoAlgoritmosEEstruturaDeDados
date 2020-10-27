package br.ucs.trabalho.modelo;

import twitter4j.Status;

public class ArquivoIndice {
	public int Indice;
	public long IdTwitter;
	
	public ArquivoIndice(Status tweet, int indice) {
		this.setIdTwitter(tweet.getId());
		this.setIndice(indice);
	}
	
	public int getIndice() {
		return Indice;
	}
	public void setIndice(int indice) {
		Indice = indice;
	}
	public long getIdTwitter() {
		return IdTwitter;
	}
	public void setIdTwitter(long idTwitter) {
		IdTwitter = idTwitter;
	}
	
	
}
