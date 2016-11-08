package br.com.ximp.boletao;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AtualizaCodigoBarras {

	public static void main(String[] args) throws ParseException {
		
		
		System.out.println(Charset.defaultCharset());
//		10492.31077 32000.200041 00002.783637 1 69230000010000
//		String linhaDigitalvel="10492 31077 32000 200041 00002 200129 5 65870000010000";
		String linhaDigitalvel="10492.31077 32000.200041 00002.783637 1 69230000010000";
		
		String novoVencimentoStr = "26/09/2016";
		String novoValorStr="100.00";
		
		linhaDigitalvel=limpaLinhaDigitavel(linhaDigitalvel);
		System.out.println(linhaDigitalvel);
		
//		Calendar novoVencimento = getData(novoVencimentoStr);
		
		BigDecimal novoValor = new BigDecimal(novoValorStr);
		novoValor.setScale(2,BigDecimal.ROUND_DOWN);

		String fatorVencimento = calculaFatorVencimento(novoVencimentoStr);
		novoValorStr=novoValor.toString().replaceAll("\\.", "");
		
		//Fator de vencimento tem 4 digitos e fica entre a posicao
//		System.out.println(fatorVencimento);
//		System.out.println(linhaDigitalvel);
//		Substitui o fator de vencimento
		linhaDigitalvel=linhaDigitalvel.substring(0,33)+fatorVencimento+linhaDigitalvel.substring(37,linhaDigitalvel.length());
//		System.out.println(linhaDigitalvel);
		linhaDigitalvel=linhaDigitalvel.substring(0,linhaDigitalvel.length()-novoValorStr.length())+novoValorStr;
//		System.out.println(linhaDigitalvel);		
		
		
		String preparaDigitoVerificador = linhaDigitalvel.substring(0,4)+fatorVencimento;
		preparaDigitoVerificador +=		linhaDigitalvel.substring(linhaDigitalvel.length()-10,linhaDigitalvel.length());
//		System.out.println(preparaDigitoVerificador);		
		
		preparaDigitoVerificador += linhaDigitalvel.substring(4,9)+linhaDigitalvel.substring(10,20)+linhaDigitalvel.substring(21,31);
//		System.out.println(preparaDigitoVerificador);		
		
		
		
		String digitoVerificadorGeral = getModulo11(preparaDigitoVerificador);
//		System.out.println(digitoVerificadorGeral);
		linhaDigitalvel=linhaDigitalvel.substring(0,linhaDigitalvel.length()-15)+digitoVerificadorGeral+linhaDigitalvel.substring(linhaDigitalvel.length()-14,linhaDigitalvel.length());
		System.out.println(linhaDigitalvel);		

	}
	
    public static String getModulo11(String campo) {
        return getModulo11(campo, 9);
    }
    public static String getModulo11(String campo,int type) {
    	//Modulo 11 - 234567   (type = 7)
    	//Modulo 11 - 23456789 (type = 9)
    	int multiplicador = 2;
		int multiplicacao = 0;
		int soma_campo = 0;
		for (int i = campo.length(); i > 0; i--) {
			multiplicacao = Integer.parseInt(campo.substring(i-1,i)) * multiplicador;
			soma_campo = soma_campo + multiplicacao;
			multiplicador++;
			if (multiplicador > 7 && type == 7) {
				multiplicador = 2;
			} else if (multiplicador > 9 && type == 9) {
				multiplicador = 2;
			}
		}
		int dac = 11 - (soma_campo%11);
        if (dac > 9 && type == 7) {
			dac = 0;
		} else if ((dac == 0 || dac == 1 || dac > 9) && type == 9) {
			dac = 1;
		}

        return ((Integer)dac).toString();
    }
    public static String calculaFatorVencimento(String data) throws ParseException {
		String dataBaseStr = "07/10/1997";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataBase = sdf.parse(dataBaseStr);
		Date vencimento = sdf.parse(data);
		long diferencaMS = vencimento.getTime() - dataBase.getTime();
		long diferencaSegundos = diferencaMS / 1000;
		long diferencaMinutos = diferencaSegundos / 60;
		long diferencaHoras = diferencaMinutos / 60;
		long diferencaDias = diferencaHoras / 24;
		String fatorVencimento = ""+diferencaDias;
		return fatorVencimento;
	}

//	private static Calendar getData(String string) throws ParseException {
//		DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
//		Date dt = df.parse(string);
//		Calendar agora=Calendar.getInstance();
//		agora.setTime(dt);
//		return agora;
//	}



	private static String limpaLinhaDigitavel(String linhaDigitalvel) {
		linhaDigitalvel=linhaDigitalvel.trim();
		linhaDigitalvel=linhaDigitalvel.replaceAll(" ", "");
		linhaDigitalvel=linhaDigitalvel.replaceAll("\\.", "");
		if(linhaDigitalvel.length()!=47){
			System.out.println("Linha digitavel invalida"+linhaDigitalvel.length());
			System.out.println(linhaDigitalvel);
			System.exit(0);
			}
		return linhaDigitalvel;
	}
}
