parser grammar SIUnitsParser;
options{
	tokenVocab = siUnitsLexer;
}

@header{
	package projeto.si_units_grammar;
}

quantity: NUMBER PREFIX? dimention
		;
		
/**
 * [LM]
 * Em principio a notaçao obragatória tem de ser usando apenas potencias
 * positivas e uma única barra de divisao, ou seja, para: (kg m^2)/(s^3 A^2)
 * (kg m^2)/(s^3 A^2) 	-> CORRETO
 * kg m^2/s^3 A^2	  	-> ERRADO
 * (kg m^2)				-> ERRADO
 * (kg)/...				-> ERRADO
 * kg m^2 s^-3 A^-2		-> ERRADO na nossa linguagem, matematicamente estaria certo...
 * kg/((s^3 A^2)/m^2)	-> ERRADO na nossa linguagem, errado no SI, correto na matematica
 * 
 * m^2/s^2 -> CORRETO ... (m/s)^2 -> ERRADO na nossa linguagem, errado no SI, correto na matematica
 */

dimention 	: (parenthesis | mult) SOLIDUS (parenthesis | mult)
	  		| mult
	  		;

// The duplication in rule mult, below, avoids the notation: (m)/s		
parenthesis :	LEFT_PAR mult SPACE mult RIGHT_PAR
			;
			
mult	: mult SPACE mult
		| BASE_UNIT POWER
		| BASE_UNIT
		| DERIVED_UNIT
		;