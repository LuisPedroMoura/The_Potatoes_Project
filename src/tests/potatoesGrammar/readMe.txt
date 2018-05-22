cenas a testar
-> code
	-> vars
	-> structures
	-> fun
		*control_flow_statement
		*structures
		*crazy operations
		

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
			* op com vars booleanas ou strings
			
			
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

	//-------------
	//ERROS--------
	//-------------
		
		//-------------------------------------------------------------------
		//tudo vazio---------------------------------------------------------
		//-------------------------------------------------------------------

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
				->  code{ fun_with_content(boolean_var == true) } -> e3


	//-------------
	//CERTOS-------
	//-------------
	
		//-----------------
		//funçoes vazias---
		//-----------------
		->  code{ fun }	-> r1
		->  code{ fun }	-> r2 ERRO_SEMANTICO

	
	
		//-------------------------------------
		//funçoes com control_flow_statement---
		//-------------------------------------
		
			//if's
			->  code{ fun_with_content (op with numbers) } -> r3
			->  code{ fun_with_content (op with booleans) } -> r4
			
			//for's
			->  code{ fun_with_content } -> r5 refazer
			
			//when
			
			//while
			
			
		//-------------------------------------
		//funçoes com structures---------------
		//-------------------------------------
		
		
			
			
			
	
	