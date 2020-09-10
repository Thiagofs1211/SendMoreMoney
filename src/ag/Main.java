package ag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		/*
		 * Tipo de Avaliação:
		 * 
		 * 0 - 1000000 - (send + more - money)
		 * 1 - (pior individuo) - (send + more - money)
		 * 
		 * Tipo de seleção:
		 * 
		 * 0 - Roleta
		 * 1 - Torneio 2
		 * 2 - Torneio 3
		 * 3 - Ranking Linear
		 * 4 - Torneio Estocástico
		 * 
		 * Tipo de crossover:
		 * 
		 * 0 - Ciclico
		 * 1 - PMX
		 * 2 - PBX
		 * 
		 * Tipo de reinserção:
		 * 
		 * 0 - Reinserção Ordenada
		 * 1 - Reinserção pura com elitismo de 20%
		 * 2 - Reinserção com roleta com 20% de elitismo
		 */
		
		int tamanhoPopulacao = 100;
		int numeroFilhos = 80;
		int geracoes = 50;
		int execucoes = 1000;
		int taxaMutacao = 20;
		
		int tipoAvaliacao = 1;
		
		
		int tipoSelecao = 0;
		int tipoCrossover = 2;
		int tipoReinsercao = 0;
		
		float acertos = 0;
		
		long start = System.currentTimeMillis();
		
		for(int k = 0; k < execucoes; k++) {
			
			List<Individuo> populacao = new ArrayList<Individuo>();
		
			//Cria e avalia a populacao Inicial
			for(int i = 0; i < tamanhoPopulacao; i++) {
				Individuo aux = new Individuo();
				aux.geraIndividuo();
				aux.avaliaIndividuo100000();
				populacao.add(aux);
			}
			
			Collections.sort(populacao);
			if(tipoAvaliacao == 1) {
				for(Individuo ind: populacao) {
					ind.avaliaIndividuoPiorIndividuo(populacao.get(tamanhoPopulacao-1));
				}
			}
			//mostraPopulacao(populacao);
			
			//Executa o AG o numero de gerações
			for(int i = 0; i < geracoes; i++) {
				AG(populacao,tamanhoPopulacao,numeroFilhos,taxaMutacao, tipoSelecao, tipoAvaliacao, tipoCrossover, tipoReinsercao);
			}
			
			//mostraPopulacao(populacao);
			//System.out.print("Execução "+k);
			mostraMelhorIndividuo(populacao);
			
			if(verificaSolucaoOtima(populacao.get(0))) {
				acertos++;
			}
		}
		
		long elapsed = (System.currentTimeMillis() - start)/1000;
		
		System.out.println("\nNúmero total de acertos SEND+MORE (resultado ótimo): "+acertos);
		System.out.println("Porcentagem de acerto: "+acertos/10+"%");
		System.out.println("Tempo de execução: "+elapsed+"s");

	}
	
	public static void mostraPopulacao(List<Individuo> populacao) {
		int cont = 0;
		for(Individuo ind : populacao) {
			cont++;
			System.out.print(cont + " - ");
			for(int i = 0; i < ind.getIndividuo().length; i++) {
				System.out.print(ind.getIndividuo()[i]+" ");
			}
			System.out.println("Aptidao: " + ind.getAptidao());
		}
	}
	
	public static void AG(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos, int taxaMutacao, int tipoSelecao, int tipoAvaliacao, int tipoCrossover, int tipoReinsercao) {
		int pai1 = 0, pai2 = 0;
		Random random = new Random();
		
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
			} else if(tipoSelecao == 3) { // Método Ranking Lienar
				pai1 = rankingLinear(populacao);
				pai2 = rankingLinear(populacao);
			} else if(tipoSelecao ==4) { // Método do torneio estocástico
				int numeroMaximoRoleta = montarRoleta(populacao);
				pai1 = torneioEstocatico2(populacao, numeroMaximoRoleta);
				pai2 = torneioEstocatico2(populacao, numeroMaximoRoleta);
			}
			
			Individuo filho1 = new Individuo();
			Individuo filho2 = new Individuo();
			for(int j = 0; j < populacao.get(pai1).getIndividuo().length; j++) {
				filho1.getIndividuo()[j] = populacao.get(pai1).getIndividuo()[j];
				filho2.getIndividuo()[j] = populacao.get(pai2).getIndividuo()[j];
			}
			
			if(tipoCrossover == 0) { //Ciclico
				Individuo paiCopia1 = populacao.get(pai1);
				
				//Cruzamento utilizando método ciclico
				int numero = random.nextInt(10);
				int primeiro = filho1.getIndividuo()[numero];
				while(primeiro != filho2.getIndividuo()[numero]) {
					int auxTroca = filho1.getIndividuo()[numero];
					filho1.getIndividuo()[numero] = filho2.getIndividuo()[numero];
					filho2.getIndividuo()[numero] = auxTroca;
					
					for(int j = 0; j < paiCopia1.getIndividuo().length; j++) {
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
			} else if(tipoCrossover == 1) { //PMX
				
				Individuo paiCopia1 = populacao.get(pai1);
				Individuo paiCopia2 = populacao.get(pai2);
				
				int corte1 = random.nextInt(7) + 1;
				int corte2 = random.nextInt(9) + 1;
				
				while(corte2 <= corte1) {
					corte2 = random.nextInt(8) + 1;
				}
				
				for(int j = corte1; j < corte2; j++) {
					//Salva o valor a ser trocado do filho 1
					int valorTroca1 = filho1.getIndividuo()[j];
					//Salva o valor a ser trocado do filho 2
					int valorTroca2 = filho2.getIndividuo()[j];
					
					//Troca os valores
					filho1.getIndividuo()[j] = valorTroca2;
					filho2.getIndividuo()[j] = valorTroca1;
					
					//Troca o valor fora do intervalo
					for(int k = 0; k < paiCopia1.getIndividuo().length; k++) {
						if((k < corte1 || k > corte2) && filho1.getIndividuo()[k] == valorTroca2) {
							for(int l = 0; l < paiCopia2.getIndividuo().length; l++) {
								if((l < corte1 || l > corte2) && filho2.getIndividuo()[l] == valorTroca1) {
									int aux = filho1.getIndividuo()[k];
									filho1.getIndividuo()[k] = filho2.getIndividuo()[l];
									filho2.getIndividuo()[l] = aux;
								}
							}
						}
					}
				}
			} else if(tipoCrossover == 2) { //PBX
				for(int j = 0; j < 10; j++) {
					filho1.getIndividuo()[j] = -1;
					filho2.getIndividuo()[j] = -1;
				}
				int posicao1 = random.nextInt(10);
				int posicao2 = random.nextInt(10);
				while(posicao1 == posicao2) {
					posicao2 = random.nextInt(10);
				}
				int posicao3 = random.nextInt(10);
				while(posicao3 == posicao1 || posicao3 == posicao2) {
					posicao3 = random.nextInt(10);
				}
				int posicao4 = random.nextInt(10);
				while(posicao4 == posicao1 || posicao4 == posicao2 || posicao4 == posicao3) {
					posicao4 = random.nextInt(10);
				}
				List<Integer> troca1 = new ArrayList<Integer>();
				List<Integer> troca2 = new ArrayList<Integer>();
				
				filho1.getIndividuo()[posicao1] = populacao.get(pai2).getIndividuo()[posicao1];
				troca1.add(populacao.get(pai2).getIndividuo()[posicao1]);
				filho2.getIndividuo()[posicao1] = populacao.get(pai1).getIndividuo()[posicao1];
				troca2.add(populacao.get(pai1).getIndividuo()[posicao1]);
				
				filho1.getIndividuo()[posicao2] = populacao.get(pai2).getIndividuo()[posicao2];
				troca1.add(populacao.get(pai2).getIndividuo()[posicao2]);
				filho2.getIndividuo()[posicao2] = populacao.get(pai1).getIndividuo()[posicao2];
				troca2.add(populacao.get(pai1).getIndividuo()[posicao2]);
				
				filho1.getIndividuo()[posicao3] = populacao.get(pai2).getIndividuo()[posicao3];
				troca1.add(populacao.get(pai2).getIndividuo()[posicao3]);
				filho2.getIndividuo()[posicao3] = populacao.get(pai1).getIndividuo()[posicao3];
				troca2.add(populacao.get(pai1).getIndividuo()[posicao3]);
				
				filho1.getIndividuo()[posicao4] = populacao.get(pai2).getIndividuo()[posicao4];
				troca1.add(populacao.get(pai2).getIndividuo()[posicao4]);
				filho2.getIndividuo()[posicao4] = populacao.get(pai1).getIndividuo()[posicao4];
				troca2.add(populacao.get(pai1).getIndividuo()[posicao4]);
				
				Collections.sort(troca1);
				Collections.sort(troca2);
				
				for(int j = 0; j < 10; j++) {
					if(j != posicao1 && j != posicao2 && j != posicao3 && j != posicao4) {
						if(!troca1.contains(populacao.get(pai1).getIndividuo()[j])) {
							filho1.getIndividuo()[j] = populacao.get(pai1).getIndividuo()[j];
						} else {
							for(int k = 0; k < troca2.size(); k++) {
								if(!verificaIndividuoRepetido(filho1.getIndividuo(), troca2.get(k))) {
									filho1.getIndividuo()[j] = troca2.get(k);
									break;
								}
							}
						}
						if(!troca2.contains(populacao.get(pai2).getIndividuo()[j])) {
							filho2.getIndividuo()[j] = populacao.get(pai2).getIndividuo()[j];
						} else {
							for(int k = 0; k < troca1.size(); k++) {
								if(!verificaIndividuoRepetido(filho2.getIndividuo(), troca1.get(k))) {
									filho2.getIndividuo()[j] = troca1.get(k);
									break;
								}
							}
						}
					}
				}
			}
						
			populacao.add(filho1);
			populacao.add(filho2);
		}
		//Mutacao
		for(int i = 0; i < taxaMutacao; i++) {
			int escolhaMutacao = random.nextInt(numeroFilhos) + tamanhoPopulacao;
			mutacao(populacao.get(escolhaMutacao));
		}
		
		for(Individuo ind : populacao) {
			ind.avaliaIndividuo100000();
		}
		
		int pior = posicaoPiorIndividuo(populacao);
		Individuo piorIndividuo = new Individuo();
		for(int j = 0; j < populacao.get(pai1).getIndividuo().length; j++) {
			piorIndividuo.getIndividuo()[j] = populacao.get(pior).getIndividuo()[j];
		}
		
		if(tipoAvaliacao == 1) {
			for(Individuo ind : populacao) {
				ind.avaliaIndividuoPiorIndividuo(piorIndividuo);
			}
		}
		
		if(tipoReinsercao == 0) { //Reinserção Ordenada
			reinsercaoOrdenada(populacao, tamanhoPopulacao, numeroFilhos);
		} else if(tipoReinsercao == 1) { // Reinserção com eletismo de 20%
			reinsercaoElitismo(populacao, tamanhoPopulacao, numeroFilhos);
			Collections.sort(populacao);
		} else if(tipoReinsercao == 2) { // Reinserção com roleta com elitismo de 20%
			reinsercaoEletismoRoleta(populacao, tamanhoPopulacao);
			Collections.sort(populacao);
		}
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
	
	public static int rankingLinear(List<Individuo> populacao) {
		Collections.sort(populacao);
		int soma = 0;
		for(int i = populacao.size() - 1; i >= 0; i--) {
			soma += i;
			populacao.get(i).setCasaRoletaRankingLinear(soma);
		}
		
		Random random = new Random();
		int sorteio = random.nextInt(soma);
		
		for(int i = 0; i < populacao.size(); i++) {
			if(populacao.get(i).getCasaRoletaRankingLinear() <= sorteio) {
				return i;
			}
		}
		return 0;
	}
	
	public static int torneioEstocatico2(List<Individuo> populacao, int casaRoleta) {
		int sorteio1 = roleta(populacao, casaRoleta);
		int sorteio2 = roleta(populacao, casaRoleta);
		if(populacao.get(sorteio1).getAptidao() > populacao.get(sorteio2).getAptidao()) {
			return sorteio1;
		} else {
			return sorteio2;
		}
	}
	
	
	public static void mutacao(Individuo ind) {
		Random random = new Random();
		int posicao1 = random.nextInt(10);
		int posicao2 = random.nextInt(10);
		int aux;
		while(posicao2 == posicao1) {
			posicao2 = random.nextInt(10);
		}
		aux = ind.getIndividuo()[posicao1];
		ind.getIndividuo()[posicao1] = ind.getIndividuo()[posicao2];
		ind.getIndividuo()[posicao2] = aux;
	}
	
	public static void reinsercaoOrdenada(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos) {
		Collections.sort(populacao);
		
		//removendo o excesso de população
		for(int i = tamanhoPopulacao+numeroFilhos-1; i > tamanhoPopulacao; i--) {
			populacao.remove(i);
		}
		populacao.remove(tamanhoPopulacao);
	}
	
	public static void reinsercaoElitismo(List<Individuo> populacao, int tamanhoPopulacao, int numeroFilhos) {
		
		for(int i = (int)( 0.2 * tamanhoPopulacao), j = tamanhoPopulacao; i < 0.8 * tamanhoPopulacao; i++, j++) {
			populacao.get(i).setIndividuo(populacao.get(j).getIndividuo());
			populacao.get(i).setAptidao(populacao.get(j).getAptidao());
		}
		
		//removendo o excesso de população
		for(int i = tamanhoPopulacao+numeroFilhos-1; i > tamanhoPopulacao; i--) {
			populacao.remove(i);
		}
		populacao.remove(tamanhoPopulacao);
	}
	
	public static void reinsercaoEletismoRoleta(List<Individuo> populacao, int tamanhoPopulacao){
		
		List<Individuo> novaPopulacao = new ArrayList<Individuo>();
		for(int i = 0; i < 0.2 * tamanhoPopulacao; i++) {
			novaPopulacao.add(populacao.get(0));
			populacao.remove(0); //remove da população os pais que ja foram salvos para a proxima geração
		}
		
		int casasRoleta = montarRoleta(populacao);
		
		List<Integer> sorteados = new ArrayList<>();
		
		for(int i = 0; i < 0.8 * tamanhoPopulacao; i++) {
			int sorteio = roleta(populacao, casasRoleta);
			while(sorteados.contains(sorteio)) {
				sorteio = roleta(populacao, casasRoleta);
			}
			novaPopulacao.add(populacao.get(sorteio));
			sorteados.add(sorteio);
		}
		
		int novoTamanhoPopulacao = populacao.size();
		for(int i = tamanhoPopulacao; i < novoTamanhoPopulacao; i ++) {
			populacao.remove(0);
		}
		
		for(int i = 0; i < novaPopulacao.size(); i ++) {
			populacao.get(i).setIndividuo(novaPopulacao.get(i).getIndividuo());
			populacao.get(i).setAptidao(novaPopulacao.get(i).getAptidao());
		}
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
	
	public static int posicaoPiorIndividuo(List<Individuo> populacao) {
		int pior = 0;
		for(int i = 1; i < populacao.size(); i++) {
			if(populacao.get(pior).getAptidao() > populacao.get(i).getAptidao()) {
				pior =  i;
			}
		}
		return pior;
	}
	
	public static boolean verificaIndividuoRepetido(int[] ind, int valor) {
		for(int i = 0; i < 10; i++) {
			if(ind[i] == valor) {
				return true;
			}
		}
		return false;
	}

}
