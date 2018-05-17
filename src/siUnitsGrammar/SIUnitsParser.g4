parser grammar SIUnitsParser;
options{
	tokenVocab = siUnitsLexer;
}

@header{
	package projeto.si_units_grammar;
}

define			: unit_or_factor '[' unit_symbol ']' ':' (NUMERIC_TYPE | operation)
	  			| unit_name ':' constructor
	  			;
	  			
operation	: PARENTHESIS_OPEN operation PARENTHESIS_CLOSE	#operation_parenthesis
			| operation op=(MULTIPLY | DIVIDE) 				#operation_mult_div
			| operation op=(ADD | SUBTRACT) operation		#operation_add_sub
			| operation POWER								#operation_power
			| type											#operation_expr
			| NUMBER										#operation_NUMBER
			;
			
type :
				
	   
unit_or_factor  : unit_name
			    | FACTOR 		//FACTOR = 'factor'
			    ;
			   
unit_name		: ID;

unit_symbol     : ID;

constructor		: unit_name ((MULTIPLY|DIVISION) unit_name)+




/*

quantity: NUMBER PREFIX? dimention
		;
	*/	
/**
 * [LM]
 * Em principio a nota�ao obragat�ria tem de ser usando apenas potencias
 * positivas e uma �nica barra de divisao, ou seja, para: (kg m^2)/(s^3 A^2)
 * (kg m^2)/(s^3 A^2) 	-> CORRETO
 * kg m^2/s^3 A^2	  	-> ERRADO
 * (kg m^2)				-> ERRADO
 * (kg)/...				-> ERRADO
 * kg m^2 s^-3 A^-2		-> ERRADO na nossa linguagem, matematicamente estaria certo...
 * kg/((s^3 A^2)/m^2)	-> ERRADO na nossa linguagem, errado no SI, correto na matematica
 * 
 * m^2/s^2 -> CORRETO ... (m/s)^2 -> ERRADO na nossa linguagem, errado no SI, correto na matematica
 */
/*
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
*/
