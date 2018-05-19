cenas a testar
-> header_declaration
-> class_declaration
-> class_content
	->structures
	-> fun
		*control_flow_statement
		*structures
	
cenas testadas

ficheiros
	e -> error (da erro)
	r -> right (compila)
	
	REVER -> provavelmente devia dar erro e nao da...
	ERRO_SEMANTICO -> tem de dar erro semantico

//------------------------------------------------------------------------------------------------------
//DECLARAÇOES DA CLASSE---------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------
//tudo vazio---------------------------------------------------------
//-------------------------------------------------------------------

//-------------
//ERROS--------
//-------------
-> header_declaration					-> e1
-> class_declaration 					-> e2
-> header_declaration class_declaration	-> e3
-> class_content#2						-> e4_2


//-------------
//CERTOS-------
//-------------
-> class_content#1 						-> r1 REVER -> declaraçao da classe
-> header_declaration class_content		-> r2 REVER -> declaraçao da classe
-> class_declaration class_content		-> r3 ERRO_SEMANTICO -> nome da classe 
-> header_declaration class_declaration class_content -> r4


problemas neste bloco:	*duas classes 
						*nao ha declaraçao de nenhuma classe
o problema das duas classes resolve-se fazendo a primeira regra assim:
			program	: code EOF	
					;
					
-> class_content class_content						-> r5 REVER
-> class_declaration class_content class_content	-> r6 REVER
-> class_declaration class_content class_declaration class_content	-> r7 REVER


//------------------------------------------------------------------------------------------------------
//CONTEUDOS DA CLASSE-----------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------

//-------------------------------------------------------------------
//tudo vazio---------------------------------------------------------
//-------------------------------------------------------------------

//-------------
//ERROS--------
//-------------
-> control_flow_statement_condition		-> e5
-> control_flow_statement_for_loop		-> e6
-> control_flow_statement_while_loop	->
-> control_flow_statement_when			->

-> fun(void) : void	->
-> fun(void) : type ->
-> fun(type) : void ->
-> fun(type) : type ->
-> fun(type, type ...) : void ->
-> fun(type, type ...) : type ->

//-------------------------------------------------------------------
//com conteudo-------------------------------------------------------
//-------------------------------------------------------------------

//-------------
//ERROS--------
//-------------
-> class_declaration class_content{ control_flow_statement_condition } 	-> e9
-> class_declaration class_content{ control_flow_statement_for_loop } 	->
-> class_declaration class_content{ control_flow_statement_while_loop }	->
-> class_declaration class_content{ control_flow_statement_when } 		->


//-------------
//CERTOS-------
//-------------
-> class_declaration class_content{ fun(void) : void }	->
-> class_declaration class_content{ fun(void) : type }	->
-> class_declaration class_content{ fun(type) : void }	->
-> class_declaration class_content{ fun(type) : type }	->
-> class_declaration class_content{ fun(type, type ...) : void } ->
-> class_declaration class_content{ fun(type, type ...) : type } ->
