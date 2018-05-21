cenas a testar
-> header_declaration
-> class_declaration
-> class_content
	->structures
	-> fun
		*control_flow_statement
		*structures
		
		

//-------------------------
//ERROS A CORRIGIR---------
//-------------------------
	
	//SEMANTICOS
		* vars nao inicializadas (ver r9)
		* repetiçao de nomes (de vars, ...) (ver r9)
		//em funçoes
			* argumento void v1 (ver r9)
			* returns em funçoes void e vice versa (ver r9)
		//em operacoes
			* operaçoes com tipos dif 
			
			
//******************************************************************************************************	
//cenas testadas****************************************************************************************
//******************************************************************************************************

ficheiros
	e -> error -> é suposto dar erro sintatico!
	r -> right -> é suposto estar correto!
	
	REVER -> provavelmente devia dar erro e nao da...
	ERRO_SEMANTICO -> tem de dar erro semantico

//------------------------------------------------------------------------------------------------------
//CONTEUDOS DA CLASSE-----------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------

	//-------------------------------------------------------------------
	//tudo vazio---------------------------------------------------------
	//-------------------------------------------------------------------

	//-------------
	//ERROS--------
	//-------------
	-> control_flow_statement_condition		-> e1
	-> control_flow_statement_for_loop		-> e2
	-> control_flow_statement_while_loop	-> mais do mesmo...
	-> control_flow_statement_when			-> mais do mesmo...

	//-------------------------------------------------------------------
	//com conteudo-------------------------------------------------------
	//-------------------------------------------------------------------
		
		//-------------------------------------
		//funçoes com control_flow_statement---
		//-------------------------------------
			-> class_declaration class_content{ fun_with_content(boolean_var == true) } -> e3


	//-------------
	//CERTOS-------
	//-------------
	
		//-----------------
		//funçoes vazias---
		//-----------------
		-> class_declaration class_content{ fun }	-> r1
		-> class_declaration class_content{ fun }	-> r2 ERRO_SEMANTICO

	
	
		//-------------------------------------
		//funçoes com control_flow_statement---
		//-------------------------------------
		
			//if's
			-> class_declaration class_content{ fun_with_content (op with numbers) } -> r3
			-> class_declaration class_content{ fun_with_content (op with booleans) } -> r4
			
			//for's
			-> class_declaration class_content{ fun_with_content } -> r5 REVER
			
			
	
	