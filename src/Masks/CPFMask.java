package Masks;

public class CPFMask {
	public static String CPFFormat(String cpf) {
		String a = cpf.substring(0, 3);
		String b = cpf.substring(3, 6);
		String c = cpf.substring(6, 9);
		String d = cpf.substring(9);
		return a+"."+b+"."+c+"-"+d;
	}
	
	public static String cleanCPF(String cpf) {
		String clean = cpf.replace(".", "");
		clean = clean.replace("-", "");
		return clean;
	}
}
