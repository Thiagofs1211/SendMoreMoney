package ag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		int tamanhoPopulacao = 100;
		int numeroFilhos = 60;
		int geracoes = 100;
		int execucoes = 100;
		
		int acertos = 0;
		int acertos100 = 0;
		int acertos500 = 0;
		int acertosC = 0;
		int acertosC100 = 0;
		int acertosC500 = 0;
		
		for(int k = 0; k < execucoes; k++) {
			
			List<Individuo> populacao = new ArrayList<Individuo>();
		
			//Cria e avalia a populacao Inicial
			for(int i = 0; i < tamanhoPopulacao; i++) {
				Individuo aux = new Individuo();
				aux.geraIndividuo();
				aux.avaliaIndividuo();
				populacao.add(aux);
			}
			
			//mostraPopulacao(populacao);
			
			//Executa o AG o numero de gerações
			for(int i = 0; i < geracoes; i++) {
				AG(populacao,tamanhoPopulacao,numeroFilhos,0);
			}
			
			//mostraPopulacao(populacao);
			//System.out.print("Execução "+k);
			//mostraMelhorIndividuo(populacao);
			
			if(populacao.get(0).getAptidao() == 0) {
				acertos++;
			}
			
			if(populacao.get(0).getAptidao() <= 100) {
				acertos100++;
			}
			
			if(populacao.get(0).getAptidao() <= 500) {
				acertos500++;
			}
		}
		
		for(int k = 0; k < execucoes; k++) {
			
			List<Individuo> populacao = new ArrayList<Individuo>();
		
			//Cria e avalia a populacao Inicial
			for(int i = 0; i < tamanhoPopulacao; i++) {
				Individuo aux = new Individuo();
				aux.geraIndividuo();
				aux.avaliaIndividuoCoca();
				populacao.add(aux);
			}
			
			//mostraPopulacao(populacao);
			
			//Executa o AG o numero de gerações
			for(int i = 0; i < geracoes; i++) {
				AG(populacao,tamanhoPopulacao,numeroFilhos,1);
			}
			
			//mostraPopulacao(populacao);
			//System.out.print("Execução "+k);
			//mostraMelhorIndividuo(populacao);
			
			if(populacao.get(0).getAptidao() == 0) {
				acertosC++;
			}
			
			if(populacao.get(0).getAptidao() <= 100) {
				acertosC100++;
			}
			
			if(populacao.get(0).getAptidao() <= 500) {
				acertosC500++;
			}
		}
		
		System.out.println("\nNúmero total de acertos SEND+MORE (resultado ótimo): "+acertos);
		System.out.println("Porcentagem de acerto: "+acertos+"%");
		
		System.out.println("\nNúmero total de acertos SEND+MORE (resultado com aptidão <= 100): "+acertos100);
		System.out.println("Porcentagem de acerto: "+acertos100+"%");
		
		System.out.println("\nNúmero total de acertos SEND+MORE (resultado com aptidão <= 500): "+acertos500);
		System.out.println("Porcentagem de acerto SEND+MORE: "+acertos500+"%");
		
		System.out.println("\nNúmero total de acertos COCA+COLA (resultado ótimo): "+acertosC);
		System.out.println("Porcentagem de acerto COCA+COLA: "+acertosC+"%");
		
		System.out.println("\nNúmero total de acertos (resultado com aptidão <= 100): "+acertosC100);
		System.out.println("Porcentagem de acerto: "+acertosC100+"%");
		
		System.out.println("\nNúmero total de acertos COCA+COLA (resultado com aptidão <= 500): "+acertosC500);
		System.out.println("Porcentagem de acerto COCA+COLA: "+acertosC500+"%");

	}
	
	public static void mostraPopulacao(List<Individuo> populacao) {
		for(Individuo ind : populacao) {
			for(int i=0; i < ind.getIndividuo().length; i++) {
				System.out.print(ind.getIndividuo()[i]+" ");
			}
			System.out.println("Aptidao: " + ind.getAptidao());
		}
	}
	
	public static void AG(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos, int cocaOrMoney) {
		int pai1, pai2;
		Random random = new Random();
		List<Integer> pais = new ArrayList<Integer>();
		
		for(int i = 0; i < numeroFilhos/2; i++) {
			pai1 = torneio2(populacao, tamanhoPopulacao);
			pai2 = torneio2(populacao, tamanhoPopulacao);
			
			//Regra para não selecionar os mesmos pais
			while(pais.contains(pai1)) {
				pai1 = torneio2(populacao, tamanhoPopulacao);
			}
			pais.add(pai1);
			
			while(pais.contains(pai2)) {
				pai2 = torneio2(populacao, tamanhoPopulacao);
			}
			pais.add(pai2);
			
			Individuo filho1 = new Individuo();
			Individuo filho2 = new Individuo();
			
			filho1 = populacao.get(pai1);
			filho2 = populacao.get(pai2);
			
			//Cruzamento
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
			if(chance < 3) { //3% de chance de mutação filho 1
				mutacao(filho1);
			}
			chance = random.nextInt(100);
			if(chance < 3) { //3% de chance de mutação filho 2
				mutacao(filho2);
			}
			
			if(cocaOrMoney == 0) {
				filho1.avaliaIndividuo();
				filho2.avaliaIndividuo();
			}
			
			if(cocaOrMoney == 1) {
				filho1.avaliaIndividuoCoca();
				filho2.avaliaIndividuoCoca();
			}
			
			populacao.add(filho1);
			populacao.add(filho2);
		}
		
		reinsercao(populacao, tamanhoPopulacao, numeroFilhos);
	}
	
	public static int torneio2(List<Individuo> populacao, int tamanhoPopulacao) {
		Random random = new Random();
		int ind1 = random.nextInt(tamanhoPopulacao);
		int ind2 = random.nextInt(tamanhoPopulacao);
		
		if(populacao.get(ind1).getAptidao() < populacao.get(ind2).getAptidao()) {
			return ind1;
		} else {
			return ind2;
		}
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
		for(int i = tamanhoPopulacao; i < tamanhoPopulacao+numeroFilhos; i++) {
			populacao.remove(tamanhoPopulacao);
		}
	}
	
	public static void mostraMelhorIndividuo(List<Individuo> populacao) {
		System.out.println("\nMelhor Individuo: ");
		for(int i = 0; i < populacao.get(0).getIndividuo().length; i++) {
			System.out.print(populacao.get(0).getIndividuo()[i] + " ");
		}
		System.out.println("Aptidao: "+populacao.get(0).getAptidao());
	}

}
