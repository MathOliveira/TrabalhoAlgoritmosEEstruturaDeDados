import br.ucs.trabalho.config.Configuracao;
import br.ucs.trabalho.modelo.Arquivo;
import br.ucs.trabalho.modelo.ArquivoIndice;
import br.ucs.trabalho.util.ConverteArquivo;
import twitter4j.*;

import java.io.*;
import java.util.*;

public class Runner {

	private static List<Status> tweets = new ArrayList<Status>();
	private static List<Status> tweetsInseridos = new ArrayList<Status>();
	private static int ultimoIndice = 0;
	private static int linesize = 543;
	private RandomAccessFile arquivo;
	// Listas arquivos
	private static List<Arquivo> arquivoDadosList = new ArrayList<Arquivo>();
	private static List<ArquivoIndice> arquivoIndiceList = new ArrayList<ArquivoIndice>();

	public static void main(String[] args)  {
		Runner app = new Runner();
		app.menu();
	}

	private void menu() {
		int opcao = 0;
		Scanner scan2 = new Scanner(System.in);
		do {
			System.out.println("***************************");
			System.out.println("Digite uma opcao:");
			System.out.println("1 Coletar dados");
			System.out.println("2 Converter dados");
			System.out.println("3 Buscar de tweet por Twitter_id");
			System.out.println("5 Buscar de tweets por Hashtag");
			System.out.println("9 Buscar hashtag mais usada");
			System.out.println("0 Sair");
			opcao = scan2.nextInt();

			switch (opcao) {
				case 1:
					System.out.println("Iniciando 'Coletar dados'");
					try {
						fetchDados();
					} catch (Exception xx) {
						xx.printStackTrace();
					}
					break;
				case 2:
					System.out.println("Iniciando 'Converter dados'");
					ConverteArquivo convert = new ConverteArquivo();
					convert.serial(linesize);
					break;
				case 3:
					System.out.println("Iniciando 'Busca de tweet por Twitter_id'");
					System.out.println("Digite o indice que deseja:");
					scan2.nextLine();
					String tw_id = scan2.nextLine();
					Long twid = Long.parseLong(tw_id);
					System.out.println(twid);
					int idx1 = buscaBinaria(twid);
					buscaPorId(idx1);
					break;
				case 5:
					System.out.println("Iniciando 'Busca tweets por hashtag'");
					scan2.nextLine();
					System.out.println("Digite a hashtag desejada");
					String hst = scan2.nextLine();
					System.out.println(hst);

					try {
						buscarTweetsPorHashtag(hst);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 9:
					System.out.println("Iniciando 'Buscar hashtag mais usada'");
					try {
						mostrarHashtagMaisComentada();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			}
			scan2.nextLine();
		} while (opcao != 0);
	}

	private void fetchDados() throws IOException, InterruptedException {
		Configuracao config = new Configuracao();
		Twitter twitter = config.ObterConfiguracao();
		try {
			criarArquivoHashtags();
			while (ultimoIndice < 10000) { //10.000
					buscarTweets(twitter);
					inserirArquivoDeDados();
					inserirArquivoIndice();
					inserirArquivoHashtags();
					arquivoDadosList.clear();
					arquivoIndiceList.clear();

					Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to search tweets: " + e.getMessage());
			System.exit(-1);
		}

	}

	private static void buscarTweets(Twitter twitter) throws TwitterException {
		Query query = new Query("queimadas");
		query.setCount(100);
		QueryResult result = twitter.search(query);
		tweets = result.getTweets();
		Collections.reverse(tweets);
	}

	private static void inserirArquivoDeDados() throws IOException {

		FileWriter writer = new FileWriter("arquivo-conteudo.txt", true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		tweetsInseridos.clear();
		long ultimoIdTwitterInserido = ultimoIdTwitter();
		for (Status tweet : tweets) {
			if (!tweet.isRetweet() && tweet.getId() > ultimoIdTwitterInserido) {
				Arquivo arquivo = new Arquivo(tweet);
				System.out.println("ultimo indice twitter: " + ultimoIdTwitterInserido);
				buffWriter.write(arquivo.retornarLinhaFormatada() + "\n");
				ultimoIdTwitterInserido = arquivo.getIdTwitter();

				tweetsInseridos.add(tweet);
				arquivoDadosList.add(arquivo);
			}
		}
		writer.flush();
		buffWriter.close();
		writer.close();
	}

	public static long ultimoIdTwitter() {

		String ultimo = "0";
		try {
			InputStream file = new FileInputStream("arquivo-conteudo.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					ultimo = line;
				}
			}

			if (ultimo.equals(String.valueOf('0'))) {

			} else {
				ultimo = ultimo.substring(0, 15);
			}

			buffer.close();
			file_reader.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ultimo = ultimo.trim();
		return Long.parseLong(ultimo);
	}

	private static void inserirArquivoIndice() throws IOException {
		FileWriter writerIndc = new FileWriter("arquivo_indice_id.txt", true);
		BufferedWriter buffWriterIndc = new BufferedWriter(writerIndc);

		int indice = ultimoIndice();
		for (Status tweet : tweetsInseridos) {
			ArquivoIndice arquivoIndice = new ArquivoIndice(tweet, indice);
			String string_indice = Integer.toString(arquivoIndice.getIndice());
			buffWriterIndc.write(String.format("%6.6s", string_indice) + arquivoIndice.getIdTwitter() + "\n");
			System.out.println("indice: " + indice);
			ultimoIndice = indice;
			indice++;
			arquivoIndiceList.add(arquivoIndice);
		}
		writerIndc.flush();
		buffWriterIndc.close();
		writerIndc.close();
	}

	public static int ultimoIndice() {

		String ultimo = "0";
		int retorno = 0;
		try {
			InputStream file = new FileInputStream("arquivo_indice_id.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					ultimo = line;
				}
			}

			if (ultimo.equals(String.valueOf('0'))) {

			} else {
				ultimo = ultimo.substring(0, 6);
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ultimo = ultimo.trim();
		retorno = Integer.parseInt(ultimo);
		retorno++;
		return retorno;
	}

	public static void inserirArquivoHashtags() throws IOException {
		try {
			InputStream file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);
			StringBuffer inputBuffer = new StringBuffer();
			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					inputBuffer.append(atualizarLinhaArquivoHashtags(line));
					inputBuffer.append("\n");
				}
			}
			buffer.close();
			String inputStr = inputBuffer.toString();
			FileOutputStream fileOut = new FileOutputStream("arquivo_hashtags.txt");
			fileOut.write(inputStr.getBytes());
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String atualizarLinhaArquivoHashtags(String linha) {
		String hashtag = linha.substring(0, 14).trim();

		for (Arquivo arquivo : arquivoDadosList) {
			List<String> hashtagsArquivo = Arrays.asList(arquivo.getHashtags().split(","));
			for (String hashtagArquivo : hashtagsArquivo) {
				if (hashtagArquivo.toUpperCase().equals(hashtag.toUpperCase()))
					linha = adicionarNovoIndiceParaHashtag(arquivo, linha);
			}
		}
		return linha;
	}

	public static String adicionarNovoIndiceParaHashtag(Arquivo arquivo, String linha) {
		int indice = 0;
		for (ArquivoIndice arquivoIndice : arquivoIndiceList) {
			if (arquivoIndice.getIdTwitter() == arquivo.getIdTwitter())
				indice = arquivoIndice.getIndice();
		}
		String hashtags = linha.substring(14);
		if (!hashtags.contains(Integer.toString(indice))) {
			if (hashtags.equals(""))
				return linha.concat(Integer.toString(indice));
			return linha.concat("," + Integer.toString(indice));
		}
		return linha;

	}

	private static void criarArquivoHashtags() throws IOException {
		FileWriter writer;
		writer = new FileWriter("arquivo_hashtags.txt", true);
		BufferedWriter buffWriter = new BufferedWriter(writer);
		List<String> HashtagsTimes = obterListaHashtags();
		for (String hashtag : HashtagsTimes) {
			writer.write(String.format("%-14.14s", hashtag) + "\n");
		}
		writer.flush();
		buffWriter.close();
		writer.close();
	}

	private static List<String> obterListaHashtags() {
		List<String> retorno = new ArrayList<String>();
		retorno.add("calor");
		retorno.add("cerrado");
		retorno.add("desmatamento");
		retorno.add("INPE");
		retorno.add("prayforamazonia");
		retorno.add("Amazônia");
		retorno.add("SOS");
		retorno.add("Ciência");
		retorno.add("Bolsonaro");
		retorno.add("Pantanal");
		retorno.add("MeioAmbiente");
		return retorno;
	}

	public void mostrarHashtagMaisComentada() throws IOException {
		InputStream file;
		int indicesHashtagMaisComentada = 0;
		String hashtagMaisComentada = "";
		try {
			file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null) {
					String hashtag = line.substring(0, 14);
					List<String> indices = null;
					try{
						indices = Arrays.asList(line.substring(15).split(","));
					}catch(Exception e){
					}
					if(indices!=null)
					if(indices.size() > indicesHashtagMaisComentada) {
						indicesHashtagMaisComentada = indices.size();
						hashtagMaisComentada = hashtag;
					}
				}
			}

			System.out.println("Hashtag: " + hashtagMaisComentada.trim() + " " + indicesHashtagMaisComentada + " tweets");

			buffer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public int filesize() {
		try {
			return (int) arquivo.length();
		} catch (IOException e) {

		}
		return 0;
	}

	private long buscaRegistro(long me) {
		try {
			String fatia = null;
			arquivo.seek(me);
			String leitura = arquivo.readLine();
			fatia = leitura.substring(6, 25);
			fatia = fatia.trim();
			return Long.parseLong(fatia);
		} catch (IOException x) {
			x.printStackTrace();
		}
		return 0;
	}

	public void Arquivo_Java() {
		try {
			arquivo = new RandomAccessFile("indicerandom.txt", "rw");
		} catch (IOException e) {
		}
	}

	public int buscaBinaria(long chave) {
		Arquivo_Java();
		long pIni = 0, pFim = filesize(), pMeio = pFim / 2;
		//System.out.println("ini =" + pIni + " pFim =" + pFim + " pMeio =" + pMeio+"\n");
		try {
		} catch (Exception err) {
			err.printStackTrace();
		}
		while (pIni < pFim && chave != buscaRegistro(pMeio)) {

			if (chave < buscaRegistro(pMeio)) {
				pFim = pMeio;
				pMeio = (pMeio / linesize) / 2;
				pMeio = pMeio * linesize + pMeio;

			} else {
				pIni = pMeio;
				if (pIni + pFim < filesize() && pIni + pFim < 2*pMeio) {
					pMeio = (pIni + pFim) / linesize;
					pMeio = pMeio * linesize + pMeio;
				} else {
					pMeio = (pIni + pFim) / 2 / linesize+1;
					pMeio = pMeio * linesize + pMeio;
				}
			}
		}
		Long indice = pMeio / linesize +1;
		return Integer.valueOf(indice.toString())+1;
	}

	public void buscaPorId(int id) {
		RandomAccessFile registroleitura;
		try {
			registroleitura = new RandomAccessFile("testexxx.txt", "rw");
			registroleitura.seek((id-1) * 591);
			String leitura = registroleitura.readLine();
			System.out.println(leitura);
		} catch (IOException e) {
		}
	}

	public void buscarTweetsPorHashtag(String hashtag) throws IOException {
		InputStream file;
		try {
			file = new FileInputStream("arquivo_hashtags.txt");
			InputStreamReader file_reader = new InputStreamReader(file);
			BufferedReader buffer = new BufferedReader(file_reader);

			String line = "";
			while (line != null) {
				line = buffer.readLine();
				if (line != null && line.substring(0,14).trim().toUpperCase().equals(hashtag.trim().toUpperCase())) {
					List<String> indices = Arrays.asList(line.substring(15).split(","));
					for(String indice : indices) {
						if (isInteger(indice)){
							buscaPorId(Integer.parseInt(indice));
						}
					}
				}
			}
			buffer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

}