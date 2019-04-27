package util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Banco {

	Map<BigInteger, String> banco;
	
	public Banco(){
		
		banco = new HashMap<BigInteger, String>();
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
