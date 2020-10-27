package br.ucs.trabalho.util;

public class StringTransformer {

    public static String padLeft(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    public static String padRight(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    public static String removerCaracteresInvalidos(String entrada){
        return entrada.replaceAll("[^\\p{L}\\p{N}\\p{Z}\\p{Sm}\\p{Sc}\\p{Sk}\\p{Pi}\\p{Pf}\\p{Pc}\\p{Mc}]","");
    }

}
