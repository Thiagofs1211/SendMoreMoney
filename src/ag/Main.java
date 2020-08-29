package ag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		/*
		 * Tipo de seleção:
		 * 
		 * 0 - Roleta
		 * 1 - Torneio 2
		 * 2 - Torneio 3
		 */
		
		int tamanhoPopulacao = 100;
		int numeroFilhos = 80;
		int geracoes = 50;
		int execucoes = 1000;
		int taxaMutacao = 2;
		int tipoSelecao = 0;
		
		int acertos = 0;
		
		for(int k = 0; k < execucoes; k++) {
			
			List<Individuo> populacao = new ArrayList<Individuo>();
		
			//Cria e avalia a populacao Inicial
			for(int i = 0; i < tamanhoPopulacao; i++) {
				Individuo aux = new Individuo();
				aux.geraIndividuo();
				aux.avaliaIndividuo100000();
				populacao.add(aux);
			}
			
			mostraPopulacao(populacao);
			
			//Executa o AG o numero de gerações
			for(int i = 0; i < geracoes; i++) {
				AG(populacao,tamanhoPopulacao,numeroFilhos,taxaMutacao, tipoSelecao);
			}
			
			mostraPopulacao(populacao);
			System.out.print("Execução "+k);
			mostraMelhorIndividuo(populacao);
			
			if(verificaSolucaoOtima(populacao.get(0))) {
				acertos++;
			}
		}
		
		System.out.println("\nNúmero total de acertos SEND+MORE (resultado ótimo): "+acertos);
		System.out.println("Porcentagem de acerto: "+acertos/10+"%");

	}
	
	public static void mostraPopulacao(List<Individuo> populacao) {
		for(Individuo ind : populacao) {
			for(int i=0; i < ind.getIndividuo().length; i++) {
				System.out.print(ind.getIndividuo()[i]+" ");
			}
			System.out.println("Aptidao: " + ind.getAptidao());
		}
	}
	
	public static void AG(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos, int taxaMutacao, int tipoSelecao) {
		int pai1 = 0, pai2 = 0;
		Random random = new Random();
		//List<Integer> pais = new ArrayList<Integer>();
		
		//mostraPopulacao(populacao);
		
		//Criação dos Filhos
		for(int i = 0; i < numeroFilhos/2; i++) {
			
			//Método da Roleta
			if(tipoSelecao == 0) {
				int numeroMaximoRoleta = montarRoleta(populacao);
				pai1 = roleta(populacao, numeroMaximoRoleta);
				pai2 = roleta(populacao, numeroMaximoRoleta);
			} else if(tipoSelecao == 1) { //Método do torneio de tour 2
				pai1 = torneio2(populacao, tamanhoPopulacao);
				pai2 = torneio2(populacao, tamanhoPopulacao);
				
				/*//Regra para não selecionar os mesmos pais
				while(pais.contains(pai1)) {
					pai1 = torneio2(populacao, tamanhoPopulacao);
				}
				pais.add(pai1);
				
				while(pais.contains(pai2)) {
					pai2 = torneio2(populacao, tamanhoPopulacao);
				}
				pais.add(pai2);*/
			} else if(tipoSelecao == 2) { // Método do torneio de tour 3
				pai1 = torneio3(populacao, tamanhoPopulacao);
				pai2 = torneio3(populacao, tamanhoPopulacao);
			}
			
			Individuo filho1 = new Individuo();
			Individuo filho2 = new Individuo();
			
			filho1 = populacao.get(pai1);
			filho2 = populacao.get(pai2);
			
			//Cruzamento utilizando método ciclico
			int numero = random.nextInt(10);
			int primeiro = filho1.getIndividuo()[numero];
			while(primeiro != filho2.getIndividuo()[numero]) {
				int auxTroca = filho1.getIndividuo()[numero];
				filho1.getIndividuo()[numero] = filho2.getIndividuo()[numero];
				filho2.getIndividuo()[numero] = auxTroca;
				
				for(int j = 0; j < populacao.get(pai1).getIndividuo().length; j++) {
					if(filho1.getIndividuo()[j] == filho1.getIndividuo()[numero] && j != numero) {
						numero = j;
						break;
					}
				}
			}
			//ultima troca
			int auxTroca = filho1.getIndividuo()[numero];
			filho1.getIndividuo()[numero] = filho2.getIndividuo()[numero];
			filho2.getIndividuo()[numero] = auxTroca;
			//Fim cruzamento
			
			//Mutacao
			int chance = random.nextInt(100);
			if(chance < taxaMutacao) {
				mutacao(filho1);
			}
			chance = random.nextInt(100);
			if(chance < taxaMutacao) {
				mutacao(filho2);
			}
			
			filho1.avaliaIndividuo100000();
			filho2.avaliaIndividuo100000();
			
			
			populacao.add(filho1);
			populacao.add(filho2);
		}
		
		reinsercao(populacao, tamanhoPopulacao, numeroFilhos);
	}
	
	public static int torneio2(List<Individuo> populacao, int tamanhoPopulacao) {
		Random random = new Random();
		int ind1 = random.nextInt(tamanhoPopulacao);
		int ind2 = random.nextInt(tamanhoPopulacao);
		
		if(populacao.get(ind1).getAptidao() > populacao.get(ind2).getAptidao()) {
			return ind1;
		} else {
			return ind2;
		}
	}
	
	public static int torneio3(List<Individuo> populacao, int tamanhoPopulacao) {
		Random random = new Random();
		int ind1 = random.nextInt(tamanhoPopulacao);
		int ind2 = random.nextInt(tamanhoPopulacao);
		int ind3 = random.nextInt(tamanhoPopulacao);
		
		if(populacao.get(ind1).getAptidao() > populacao.get(ind2).getAptidao() && populacao.get(ind1).getAptidao() > populacao.get(ind3).getAptidao()) {
			return ind1;
		} else if(populacao.get(ind2).getAptidao() > populacao.get(ind1).getAptidao() && populacao.get(ind2).getAptidao() > populacao.get(ind3).getAptidao()) {
			return ind2;
		} else {
			return ind3;
		}
	}
	
	//Monta a roleta e retorna a ultima casa da roleta
	public static int montarRoleta(List<Individuo> populacao) {
		int casaRoleta = 0;
		for(Individuo pop: populacao) {
			casaRoleta += pop.getAptidao();
			pop.setCasasRoleta(casaRoleta);
		}
		return casaRoleta;
	}
	
	//Executa a roleta para selecionar um pai
	public static int roleta(List<Individuo> populacao, int casaRoleta) {
		Random random = new Random();
		int sorteio = random.nextInt(casaRoleta);
		int i = 0;
		for(Individuo pop : populacao) {
			i++;
			if(pop.getCasasRoleta() > sorteio) {
				break;
			}
		}
		return i-1;
	}
	
	
	public static void mutacao(Individuo ind) {
		Random random = new Random();
		int posicao1 = random.nextInt(10);
		int posicao2 = random.nextInt(10);
		int aux;
		while(posicao2 != posicao1) {
			posicao2 = random.nextInt(10);
		}
		aux = ind.getIndividuo()[posicao1];
		ind.getIndividuo()[posicao1] = ind.getIndividuo()[posicao2];
		ind.getIndividuo()[posicao2] = aux;
	}
	
	public static void reinsercao(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos) {
		Collections.sort(populacao);
		
		//removendo o excesso de população
		for(int i = tamanhoPopulacao+numeroFilhos-1; i > tamanhoPopulacao; i--) {
			populacao.remove(i);
		}
		populacao.remove(tamanhoPopulacao);
	}
	
	public static void mostraMelhorIndividuo(List<Individuo> populacao) {
		System.out.println("\nMelhor Individuo: ");
		for(int i = 0; i < populacao.get(0).getIndividuo().length; i++) {
			System.out.print(populacao.get(0).getIndividuo()[i] + " ");
		}
		System.out.println("Aptidao: "+populacao.get(0).getAptidao());
	}
	
	public static boolean verificaSolucaoOtima(Individuo individuo) {
		int send = 1000 * individuo.getIndividuo()[0] + 100 * individuo.getIndividuo()[1] + 10 * individuo.getIndividuo()[2] + individuo.getIndividuo()[3];
		int more = 1000 * individuo.getIndividuo()[4] + 100 * individuo.getIndividuo()[5] + 10 * individuo.getIndividuo()[6] + individuo.getIndividuo()[1];
		int money = 10000 * individuo.getIndividuo()[4] + 1000 * individuo.getIndividuo()[5] + 100 * individuo.getIndividuo()[2] + 10 * individuo.getIndividuo()[1] + individuo.getIndividuo()[7];
		
		if(send + more - money == 0) {
			return true;
		} else {
			return false;
		}
	}

}
