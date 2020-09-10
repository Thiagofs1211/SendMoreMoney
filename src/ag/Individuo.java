package ag;

import java.util.Random;

public class Individuo implements Comparable<Individuo> {
	
	private int[] individuo = new int[10];
	private long aptidao;
	private long casasRoleta;
	private long casaRoletaRankingLinear;
	
	@Override
	public int compareTo(Individuo individuo) {
		if(this.aptidao > individuo.getAptidao()) {
			return -1;
		}
		if(this.aptidao < individuo.getAptidao()) {
			return 1;
		}
		return 0;
	}
	
	public int[] getIndividuo() {
		return individuo;
	}
	
	public void setIndividuo(int[] individuo) {
		this.individuo = individuo;
	}
	
	public long getAptidao() {
		return aptidao;
	}
	
	public void setAptidao(long aptidao) {
		this.aptidao = aptidao;
	}
	
	public long getCasasRoleta() {
		return casasRoleta;
	}

	public void setCasasRoleta(long casasRoleta) {
		this.casasRoleta = casasRoleta;
	}
	
	public void geraIndividuo() {
		int[] aux = new int[10];
		Random random = new Random();
		int numero;
		boolean igual = false;
		boolean saiuZero = false;
		
		for(int i = 0; i < 10; i++) {
			numero = random.nextInt(10);
			for(int j = 0;j<10;j++) {
				if(aux != null && numero == aux[j]) {
					igual = true;
					if(numero != 0 || saiuZero) {
						i--;
					}
					if(numero == 0) {
						saiuZero = true;
					}
					break;
				}
				igual = false;
			}
			if(!igual) {
				aux[i] = numero;
			}
		}
		this.individuo = aux;
	}
	
	public void avaliaIndividuo100000() {
		double send, more, money;
		
		send = 1000 * (double)this.individuo[0] + 100 * (double)this.individuo[1] + 10 * (double)this.individuo[2] + (double)this.individuo[3];
		more = 1000 * (double)this.individuo[4] + 100 * (double)this.individuo[5] + 10 * (double)this.individuo[6] + (double)this.individuo[1];
		money = 10000 * (double)this.individuo[4] + 1000 * (double)this.individuo[5] + 100 * (double)this.individuo[2] + 10 * (double)this.individuo[1] + (double)this.individuo[7];
	
		this.aptidao = (long) (100000 - Math.abs((send + more) - money));
	}
	
	public void avaliaIndividuoPiorIndividuo(Individuo piorIndividuo) {
		double send, more, money;
		
		send = 1000 * (double)this.individuo[0] + 100 * (double)this.individuo[1] + 10 * (double)this.individuo[2] + (double)this.individuo[3];
		more = 1000 * (double)this.individuo[4] + 100 * (double)this.individuo[5] + 10 * (double)this.individuo[6] + (double)this.individuo[1];
		money = 10000 * (double)this.individuo[4] + 1000 * (double)this.individuo[5] + 100 * (double)this.individuo[2] + 10 * (double)this.individuo[1] + (double)this.individuo[7];
		
		double sendPior = 1000 * (double)piorIndividuo.getIndividuo()[0] + 100 * (double)piorIndividuo.getIndividuo()[1] + 10 * (double)piorIndividuo.getIndividuo()[2] + (double)piorIndividuo.getIndividuo()[3];
		double morePior = 1000 * (double)piorIndividuo.getIndividuo()[4] + 100 * (double)piorIndividuo.getIndividuo()[5] + 10 * (double)piorIndividuo.getIndividuo()[6] + (double)piorIndividuo.getIndividuo()[1];
		double moneyPior = 10000 * (double)piorIndividuo.getIndividuo()[4] + 1000 * (double)piorIndividuo.getIndividuo()[5] + 100 * (double)piorIndividuo.getIndividuo()[2] + 10 * (double)piorIndividuo.getIndividuo()[1] + (double)piorIndividuo.getIndividuo()[7];
	
		this.aptidao = (long) (Math.abs((sendPior + morePior) - moneyPior) + 1 - Math.abs((send + more) - money));
	}
	
	public void avaliaIndividuoCoca() {
		double coca, cola, soda;
		
		coca = 1000 * (double)this.individuo[0] + 100 * (double)this.individuo[1] + 10 * (double)this.individuo[0] + (double)this.individuo[2];
		cola = 1000 * (double)this.individuo[0] + 100 * (double)this.individuo[1] + 10 * (double)this.individuo[3] + (double)this.individuo[2];
		soda = 1000 * (double)this.individuo[4] + 100 * (double)this.individuo[1] + 10 * (double)this.individuo[5] + (double)this.individuo[2];
		
		this.aptidao = (long) Math.abs((coca+cola)-soda);
	}

	public long getCasaRoletaRankingLinear() {
		return casaRoletaRankingLinear;
	}

	public void setCasaRoletaRankingLinear(long casaRoletaRankingLinear) {
		this.casaRoletaRankingLinear = casaRoletaRankingLinear;
	}
}
