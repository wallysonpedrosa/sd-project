package util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Log {

	Map<BigInteger, String> banco;
	
	public Log(){
		
		banco = new HashMap<BigInteger, String>();
		banco.put(new BigInteger("1"), "A");
		banco.put(new BigInteger("2"), "B");
	}
	
	public void setValor(BigInteger chave, String valor) {
		banco.put(chave,valor);
	}
	
	public String getValor(BigInteger chave) {
		return banco.get(chave);
	}
	
	public void deletaValor(BigInteger chave) {
		if(banco.containsKey(chave)) {
			banco.remove(chave);
		}
	}
	
	public void atualizaValor(BigInteger chave, String valor) {
		if(banco.containsKey(chave)) {
			banco.replace(chave, valor);
		}
	}
	
	public boolean existeElemento(BigInteger chave){
		
		if(banco.containsKey(chave)){
			return true;
		} else {
			return false;
		}
		
	}
	
}
