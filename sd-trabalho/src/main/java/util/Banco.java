package util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Banco {

	Map<BigInteger, String> banco;
	
	public Banco(){
		
		banco = new HashMap<BigInteger, String>();
		banco.put(new BigInteger("1"), "A");
		banco.put(new BigInteger("2"), "B");
	}
	
	public  synchronized void setValor(BigInteger chave, String valor) {
		banco.put(chave,valor);
	}
	
	public synchronized String getValor(BigInteger chave) {
		return banco.get(chave);
	}
	
	public synchronized  void deletaValor(BigInteger chave) {
		if(banco.containsKey(chave)) {
			banco.remove(chave);
		}
	}
	
	public synchronized  void atualizaValor(BigInteger chave, String valor) {
		if(banco.containsKey(chave)) {
			banco.replace(chave, valor);
		}
	}
	
	public synchronized boolean existeElemento(BigInteger chave){
		
		if(banco.containsKey(chave)){
			return true;
		} else {
			return false;
		}
		
	}
	
}
