/**
 * @author Sassine El-Asmar (github.com/Sassine)
 * 
 */
package com.github.sassine.api;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StreamUseCase {

	public StreamUseCase() {
		var list = List.of("A", "B", "C", "D", "1", "1", "A", "B");
		Consumer<Object> consumer = out::print;
		printEachValue(list, consumer);
	}

	public void printEachValue(List<String> list, Consumer<Object> consumer) {
		pMsg("forEachTraditional usign > forEach");
		this.forEachTraditional(list, consumer);
		pMsg("forEachParallel usign > parallelStream");
		this.forEachParallel(list, consumer);
		pMsg("changingValueWhileIterating usign > list.set");
		this.changingValueWhileIterating(consumer);
		pMsg("filterSpecificValueOrMatch usign > filter");
		this.filterSpecificValueOrMatch(list, consumer);
		pMsg("distinctValuesAndObjects usign > distinct");
		this.distinctValuesAndObjects(list, consumer);
		pMsg("transformingValueToObjectAndMore usign > map");
		this.transformingValueToObjectAndMore(list, consumer);
		pMsg("transformingTheValuesAndCollectingToANewList usign > collect");
		this.transformingTheValuesAndCollectingToANewList(list, consumer);
		pMsg("findElement usign > findFirst & findAny");
		this.findFirstOrAnyElement(list, consumer);
		pMsg("limitAndSkipElements usign > skip & limit");
		this.limitAndSkipElements(list, consumer);
		pMsg("matchElementAndCount usign > anyMatch & count");
		this.matchElementAndCount(list, consumer);
	}

	void skp() {
		out.println();
	}

	void pMsg(String title) {
		out.println("\n\n+ --- %s ---+".formatted(title));
	}

	/*
	 * Ambas as versões irão percorrer a lista e imprimir todos os elementos
	 * 
	 * Both versions will iterate through the list and print all elements
	 */
	private void forEachTraditional(List<String> list, Consumer<Object> consumer) {
		list.forEach(consumer);
		skp();
		list.stream().forEach(consumer);
	}

	/*
	 * Desta forma é executada em várias threads e nesta situação a ordem é
	 * indefinida
	 * 
	 * This way it is executed in several threads and in this situation the order is
	 * undefined
	 */
	private void forEachParallel(List<String> list, Consumer<Object> consumer) {
		list.parallelStream().forEach(consumer);
	}

	/*
	 * Ambas as versões permitem que altera o valor da lista original, mas não é
	 * recomendado uma vez que a modificação de elementos pode alterar toda o seu
	 * comportamento
	 * 
	 * Both versions can change the value of the original list, but are not
	 * recommended as modifying elements can change their entire behavior.
	 */
	private void changingValueWhileIterating(Consumer<Object> consumer) {
		var list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.forEach(i -> {
			list.set(1, "K");
		});
		list.forEach(consumer);
		skp();
		list.stream().forEach(i -> {
			list.set(1, "B");
		});
		list.stream().forEach(consumer);
	}

	/*
	 * Podemos utilizar o filter para executar apenas a condicional separando por
	 * verdadeiro ou então podem escrever uma regra mais complexa de filtro e
	 * determinar uma ação durante a filtragem sem precisar chamar um foreach
	 * depois.
	 * 
	 * We can use the filter to just execute the conditional separating it by true
	 * or else we can write a more complex filter rule and determine an action
	 * during the filtering without having to call a foreach afterwards
	 * 
	 */
	private void filterSpecificValueOrMatch(List<String> list, Consumer<Object> consumer) {
		list.stream().filter(value -> "B".equals(value)).forEach(consumer);
		skp();
		list.stream().filter(value -> {
			if (value.matches("[0-9]")) {
				// call method/class/function...
				out.print("value matched");
				return true;
			} else {
				// call other method/class/function...
				return false;
			}
		}).findAny();
	}

	/*
	 * Se estiver no java 8 utilizar o distinct é uma otima alternativa para remover
	 * valores e objetos duplicados quando a função equals estiver implementada, mas
	 * se estiver utilizando java mais novos, Set.copyOf é mais pratico e talvez até
	 * mais perfomatico
	 * 
	 * If you are on java 8 using distinct is a great alternative to remove
	 * duplicate values and objects when the equals function is implemented, but if
	 * you are using newer java, Set.copyOf is more practical and maybe even more
	 * performant
	 * 
	 */
	private void distinctValuesAndObjects(List<String> list, Consumer<Object> consumer) {
		list.stream().distinct().forEach(consumer);
		skp();
		Set.copyOf(list).forEach(consumer);
		skp();
		var listPersdon = List.of(new Person("Sassine"), new Person("Sassine2"), new Person("Sassine"),
				new Person("Sessin"));
		listPersdon.stream().distinct().forEach(consumer);
		skp();
		Set.copyOf(listPersdon).forEach(consumer);
	}

	/*
	 * Utilizamos o Map para manipular os valores de entrada podendo resultar em uma
	 * nova Lista de Objetos criados com os valores de entrada ou apenas alterar o
	 * valor para outro
	 * 
	 * We use Map to manipulate the input values, which can result in a new List of
	 * Objects created with the input values or just change the value to another
	 * 
	 */
	private void transformingValueToObjectAndMore(List<String> list, Consumer<Object> consumer) {
		list.stream().map(Person::new).forEach(consumer);
		skp();
		list.stream().map(Person::new).map(p -> p.getName()).forEach(consumer);
		skp();
		list.stream().map(Person::new).map(p -> {
			String name = p.getName();
			if ("1".equals(name))
				return "Number";
			return name;
		}).forEach(consumer);
	}

	/*
	 * Utilizamos Collect para juntar todo o conjunto de elementos capturado apos a
	 * operação de "Filter,Distinc,Map..." e transformar em uma List, Set, Map...
	 * 
	 * We use Collect to gather the entire set of elements captured after the
	 * "Filter,Distinc,Map..." operation and transform it into a List, Set, Map...
	 * 
	 */
	private void transformingTheValuesAndCollectingToANewList(List<String> list, Consumer<Object> consumer) {
		var collectPerson = list.stream().map(Person::new).collect(Collectors.toList());
		collectPerson.forEach(consumer);
		skp();
		var collectPersonAndDistinctUsginSetListType = list.stream().map(Person::new).collect(Collectors.toSet());
		collectPersonAndDistinctUsginSetListType.forEach(consumer);
		skp();
		var collectFiltred = list.stream().filter(value -> "B".equals(value)).collect(Collectors.toList());
		collectFiltred.forEach(consumer);
		skp();
		var collecDistinctValue = list.stream().distinct().collect(Collectors.toList());
		collecDistinctValue.forEach(consumer);
	}

	/*
	 * Quando precisamos recuperar o primeiro valor da lista para darmos sequencia a
	 * outra chamada ou qualquer outra coisa, temos os metodos findFirst e findAny,
	 * que podem ser utilizados direto na stream, ou após
	 * "Filter, Map, Distinct....", findAny irá encontrar o elemento da melhor forma
	 * ou seja com mais perfomance quando utilizando em stream paralela
	 * 
	 * When we need to retrieve the first value from the list to proceed with
	 * another call or anything else, we have the findFirst and findAny methods,
	 * which can be used directly in the stream, or after
	 * "Filter, Map, Distinct....", findAny will find the element in the best way
	 * that is with more performance when using in parallel stream
	 * 
	 */
	private void findFirstOrAnyElement(List<String> list, Consumer<Object> consumer) {
		Optional<String> findAny = list.stream().findAny();
		if (findAny.isPresent())
			out.print(findAny.get());
		skp();
		Optional<String> findFirst = list.stream().filter("1"::equals).findFirst();
		if (findFirst.isPresent())
			out.print(findFirst.get());
	}

	/*
	 * Com limit conseguimos dizer a quantidade de elementos que iremos percorrer da
	 * lista, mesmo que o tamanho que definirmos seja maior que a quantidade da
	 * lista e com Skip conseguimos também pular alguns elementos como se fosse um
	 * mecanismo de paginação
	 * 
	 * With limit we can say the number of elements that we will go through the
	 * list, even if the size we define is greater than the number of the list and
	 * with Skip we can also skip some elements as if it were a paging mechanism
	 * 
	 */
	private void limitAndSkipElements(List<String> list, Consumer<Object> consumer) {
		list.stream().limit(3).forEach(consumer);
		skp();
		list.stream().skip(3).limit(100).forEach(consumer);
	}

	/*
	 * Quando precisamos validar se um valor existe na lista sem precisar manipular
	 * o restante da lista, podemos utilizar o anyMatch, temos também o count que
	 * nos permite saber quantos valores estão na lista como também conseguimos
	 * validar a partir dele se o numero do contador for maior que 0 ou valor existe
	 * na lista POREM o anyMatch é muito mais perfomatico que o count !!
	 * 
	 * When we need to validate that a value exists in the list without having to
	 * manipulate the rest of the list, we can use anyMatch, we also have count that
	 * allows us to know how many values are in the list as we can also validate
	 * from it if the counter number is greater than 0 or value exists in the list
	 * BUT anyMatch is much more performant than count !!
	 * 
	 */
	private void matchElementAndCount(List<String> list, Consumer<Object> consumer) {
		out.println("List contains B value ? " + list.stream().anyMatch("B"::equals));
		out.println("List contains how many B ? " + list.stream().filter("B"::equals).count());
		out.println("List contains B value by count ? " + (list.stream().filter("B"::equals).count() > 0));
	}

	/* Mock Person */
	private class Person {
		private String name;

		public Person(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Person other = (Person) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private StreamUseCase getEnclosingInstance() {
			return StreamUseCase.this;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + "] ";
		}

	}
}
