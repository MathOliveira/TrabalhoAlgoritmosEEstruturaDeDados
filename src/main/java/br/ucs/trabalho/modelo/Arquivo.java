package br.ucs.trabalho.modelo;

import br.ucs.trabalho.util.StringTransformer;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Arquivo {
	
	private long idTwitter;
	private String usuario;
	private String mensagem;
	private Date data;
	private String pais;
	private String hashtags;
	
	
	public Arquivo(Status tweet) {
		this.setIdtwitter(tweet.getId());
		this.setUsuario(tweet.getUser().getScreenName());
		this.setMensagem(tweet.getText());
		this.setData(tweet.getCreatedAt());
		this.setPais(tweet.getUser().getLocation());
		this.setHashtags(tweet.getHashtagEntities());
		System.out.println("id: " + this.getIdTwitter());
		System.out.println("usuario: " + this.getUsuario());
		System.out.println("mensagem: " + this.getMensagem());
		System.out.println("data: " + this.getDataFormatada());
		System.out.println("pais: " + this.getPais());
		System.out.println("Hashtags: " + this.getHashtags());
	}
	
	
	public long getIdTwitter() {
		return idTwitter;
	}

	public String getUsuario() {
		return usuario;
	}

	public Date getData() {
		return data;
	}

	public String getPais() {
		return pais;
	}

	public void setIdtwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}
	
	public String getMensagem() {
		return String.format("%-280.280s",mensagem.replaceAll("\r", "").replaceAll("\n", ""));
	}

	public String getDataFormatada() {
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		return fmt.format(data);
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getHashtags() {
		return hashtags;
	}
	public void setHashtags(HashtagEntity[] hashtag) {
		ArrayList<String> lista = new ArrayList<String>();
		for(HashtagEntity hash : hashtag) {
			lista.add(hash.getText());
		}
		this.hashtags =String.format("%-200.200s",String.join(",", lista));
	}

	public void setIdTwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = StringTransformer.removerCaracteresInvalidos(mensagem);
	}

	public void setUsuario(String usuario) {
		this.usuario = StringTransformer.removerCaracteresInvalidos(usuario);
	}

	public void setPais(String pais) {
		this.pais = StringTransformer.removerCaracteresInvalidos(pais);
	}

	public void setHashtags(String hashtags) {
		this.hashtags = StringTransformer.removerCaracteresInvalidos(hashtags);
	}

	public String retornarLinhaFormatada(){
		String saida = StringTransformer.padLeft(String.valueOf(getIdTwitter()),15).concat(
				StringTransformer.padRight(getUsuario(),20).concat(
						StringTransformer.padRight(getMensagem(),280).concat(
								StringTransformer.padRight(getDataFormatada(),8).concat(
										StringTransformer.padRight(getPais(),20).concat(
												StringTransformer.padRight(getHashtags(),200)
										)
								)
						)
				)
		);
		return saida;
	}
}
