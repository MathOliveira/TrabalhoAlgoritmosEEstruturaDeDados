package br.ucs.trabalho.util;

import java.io.*;

public class ConverteArquivo {

	public void serial() {
		StringBuffer sb;
		try {
			RandomAccessFile file = new RandomAccessFile(new File("indicerandom.txt"), "rw");
			BufferedReader arq = new BufferedReader(new FileReader("arquivo_indice_id.txt"));
			file.seek(0);
			String str;
			int ind = 1;

			do {
				str = arq.readLine();
				sb = new StringBuffer(str);
				sb.setLength(590);
				file.writeBytes(sb.toString());
				file.writeBytes("\n");
				System.out.println(ind + " " + str);
				ind++;

			} while (arq.ready());
			arq.close();
			file.close();

		} catch (IOException xx) {
			xx.printStackTrace();
		}

		try {
			RandomAccessFile file = new RandomAccessFile(new File("testexxx.txt"), "rw");
			BufferedReader arq = new BufferedReader(new FileReader("teste.txt"));
			file.seek(0);
			String str;
			int ind = 1;

			do {
				str = arq.readLine();
				sb = new StringBuffer(str);
				sb.setLength(590);
				file.writeBytes(sb.toString());
				file.writeBytes("\n");
				System.out.println(ind + " " + str);
				ind++;

			} while (arq.ready());
			arq.close();
			file.close();

		} catch (IOException xx) {
			xx.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ConverteArquivo a = new ConverteArquivo();
		a.serial();

	}

}
