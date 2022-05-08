/**
 * @author Sassine El-Asmar (github.com/Sassine)
 * 
 */
package com.github.sassine.api;

import static java.lang.System.out;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class OptionalUseCase {

	private static final String VALUE_FOUND = "value found";
	private static final String VALUE_FOUND_S2 = "value found > %s";
	private static final String VALUE_NOT_FOUND = "value not found";
	private static final String VALUE_FOUND_S = "\nvalue found > %s";

	public static void main(String[] args) {
		new OptionalUseCase();
	}

	public OptionalUseCase() {
		pMsg("returnEmptyOptional usign > empty");
		this.returnEmptyOptional();
		pMsg("returnOptionalFixValue usign > of");
		this.returnOptionalFixValue();
		pMsg("returnDynamicOptional usign > ofNullable");
		this.returnDynamicOptional();
		pMsg("getOptionalValue usign > get");
		this.getOptionalValue();
		pMsg("returnDefaultValueWhenNull usign > orElse");
		this.returnDefaultValueWhenNull();
		pMsg("returnCustomExecutionIfNull usign > orElseThrow");
		this.returnCustomExecutionIfNull();

		pMsg(" JDK 9+ ");
		pMsg("ifPresentOrElse");
		this.ifPresentOrElse();
		pMsg("getValueOr");
		getValueOr();
		pMsg("optionalStream");
		optionalStream();
	}

	private void msg(String content) {
		out.print(content);
	}

	void pMsg(String title) {
		out.println("\n\n+ --- %s ---+".formatted(title));
	}

	/*
	 * Quando manipulamos Optional ele nos entrega uma função que é os isPresent que
	 * é responsavel por nos informar se existe um valor ou se é nulo true = tem
	 * valor e false = quando é nulo
	 * 
	 * When we manipulate Optional it gives us a function that is the isPresent that
	 * is responsible for informing us if there is a value or if it is null true =
	 * has value and false = when it is null
	 * 
	 */
	private boolean methodExampleReceivesOptional(Optional<String> opt) {
		return opt.isPresent();
	}

	/*
	 * Sempre que tiver um metodo que o retorno podera ser nulo inves de retornar
	 * null retorne um Optional empty
	 * 
	 * Whenever you have a method that the return can be null instead of returning
	 * null return an Optional empty
	 * 
	 */
	private void returnEmptyOptional() {
		Optional<String> opt = Optional.empty();
		if (methodExampleReceivesOptional(opt))
			msg(VALUE_FOUND);
		else
			msg(VALUE_NOT_FOUND);
	}

	/*
	 * Quando tiver certeza que o valor a ser retornado não será nulo, ou quando
	 * você for enviar um valor para um metodo/função que recebea Optional, você
	 * devera criar ele utilizando of
	 * 
	 * When you are sure that the value to be returned will not be null, or when you
	 * are going to send a value to a method/function that receives Optional, you
	 * should create it using of
	 */
	private void returnOptionalFixValue() {
		var opt = Optional.of("Sassine");
		// methodExampleReceivesOptional("Sassine"); NOT WORKING;
		if (methodExampleReceivesOptional(opt))
			msg(VALUE_FOUND);
		else
			msg(VALUE_NOT_FOUND);
	}

	/*
	 * Neste caso não temos certeza se o valor irá exister ou sera null, vai
	 * depender do valor gerado então utilizamos o ofNullable (esse é quem irá te
	 * acompanhar para evitar e tratar todos seus NullPointers agora
	 * 
	 * In this case we are not sure if the value will exist or will be null, will
	 * depending on the value generated then we use ofNullable (this is the one who
	 * will track to avoid and treat all your NullPointers now
	 * 
	 */
	private void returnDynamicOptional() {
		Random r = new Random();
		var n = r.nextInt((2 - 1) + 1) + 1;
		Optional<String> dynamicNullable = Optional.ofNullable(n == 1 ? null : "Sassine");
		if (methodExampleReceivesOptional(dynamicNullable))
			msg(VALUE_FOUND);
		else
			msg(VALUE_NOT_FOUND);
	}

	/*
	 * Quando temos a confirmação de que o valor existe com o "ifPresent" podemos
	 * recuperar o valor contido no Optional e manipularmos da forma que quisermos
	 * utilizando a chamada ".get()"
	 * 
	 * When we have confirmation that the value exists with "ifPresent" we can
	 * retrieve the value contained in the Optional and manipulate it the way we
	 * want using the ".get()" call
	 * 
	 */
	private void getOptionalValue() {
		Optional<String> valueOpt = Optional.of("Sassine");
		if (methodExampleReceivesOptional(valueOpt))
			msg(VALUE_FOUND_S2.formatted(valueOpt.get()));
		else
			msg(VALUE_NOT_FOUND);
	}

	/*
	 * Com optional OfNullable se o seu valor for nulo conseguimos falar para ele
	 * nos entregar um valor padrão fixado ou até mesmo chamar outra função para
	 * recuperar outro valor utilizando "orElse ou orElseGet"
	 * 
	 * With optional OfNullable if its value is null we can talk to it give us a
	 * fixed default value or even call another function to retrieve another value
	 * using "orElse or orElseGet"
	 * 
	 */
	private void returnDefaultValueWhenNull() {
		String ex = null;
		String valueOpt = Optional.ofNullable(ex).orElse("Sassine - Default");
		msg(VALUE_FOUND_S2.formatted(valueOpt));
		String valueOpt2 = Optional.ofNullable(ex).orElse(getName());
		msg(VALUE_FOUND_S.formatted(valueOpt2));
		String valueOpt3 = Optional.ofNullable(ex).orElseGet(Person::getName);
		msg(VALUE_FOUND_S.formatted(valueOpt3));
	}

	/*
	 * Conseguimos fazer com que o Optional lance um exception conforme a gente
	 * definir quando o valor for nulo utilizando "orElseThrow"
	 * 
	 * We managed to get the Optional to throw an exception as we did set when value
	 * is null using "orElseThrow"
	 * 
	 */
	private void returnCustomExecutionIfNull() {
		try {
			String ex = null;
			Optional.ofNullable(ex).orElseThrow(() -> new RuntimeException("value not found - exception"));
		} catch (RuntimeException e) {
			msg(e.getMessage());
		}
	}

	/* _______________________ J A V A 9+ */

	/*
	 * Para simplificar os ifPresent + get, no java 9 foi disponibilizado o
	 * ifPresentOrElse que simplifica a implementação e a tomada de decisão
	 * 
	 * To simplify ifPresent + get, in java 9 the * ifPresentOrElse was made
	 * available, which simplifies implementation and decision making
	 * 
	 */
	private void ifPresentOrElse() {
		String ex = null;
		Optional.ofNullable(ex).ifPresentOrElse(v -> msg(VALUE_FOUND_S.formatted(v)), () -> msg(VALUE_NOT_FOUND));
		Optional.ofNullable("Sassine").ifPresentOrElse(v -> msg(VALUE_FOUND_S.formatted(v)),
				() -> msg(VALUE_NOT_FOUND));
	}

	/*
	 * Com "or" se o 1 valor não estiver presente ele executa uma ação ... neste
	 * exemplo buscar o valor existente na função getName
	 * 
	 * With "or" if the 1 value is not present it performs an action ... in this
	 * example fetch the existing value in the getName function
	 * 
	 */
	private void getValueOr() {
		String ex = null;
		Optional.ofNullable(ex).or(() -> Optional.of(getName())).ifPresent(this::msg);
	}

	/*
	 * Optional também permite o uso de stream, se um valor estiver presente ele
	 * retorna o stream caso contrario retorna uma stream vazia!
	 * 
	 * Optional also allows the use of stream, if a value is present it returns the
	 * stream otherwise it returns an empty stream!
	 * 
	 */
	private void optionalStream() {
		var list = List.of("AB", "CD");
		Optional.ofNullable(list).stream().forEach(l -> {
			l.forEach(this::msg);
		});
		List<String> x = null;
		Optional.ofNullable(x).stream().forEach(l -> {
			l.forEach(this::msg);
		});

	}

	private String getName() {
		return "Sassine - Function";
	}

	private static class Person {

		public static String getName() {
			return "Sassine - Class";
		}
	}

}
