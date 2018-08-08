/***************************************************************************************
*	Title: PotatoesProject - CodeUtils Class Source Code
*	Code version: 2.0
*	Author: Luis Moura (https://github.com/LuisPedroMoura)
*	Date: July-2018
*	Availability: https://github.com/LuisPedroMoura/PotatoesProject
*
***************************************************************************************/

package potatoesGrammar.utils;

import typesGrammar.utils.Code;
import typesGrammar.utils.HierarchyDiGraph;
import typesGrammar.utils.Type;

public class CodeUtils {
	
	HierarchyDiGraph<Type,Double> typesGraph;
	


	public static double simplifyCodeWithConvertions(Variable var) {
		Double factor = 1.0;
		Double conversionFactor = 1.0;
		while (conversionFactor != null) {
			factor *= conversionFactor;
			conversionFactor = simplifyCodeWithConvertionsPrivate(var);
		}
		return factor;
	}
	
	private static Double simplifyCodeWithConvertionsPrivate(Variable var) {
		for (int numCode : .numCodes) {
			for (int denCode : this.denCodes) {
				Type numType = Type.getBasicTypesCodesTable().get(numCode);
				Type denType = Type.getBasicTypesCodesTable().get(denCode);
				double conversionFactor = typesGraph.getEdgeCost(typesGraph.getEdge(numType, denType));
				if (conversionFactor != Double.POSITIVE_INFINITY){
					numCodes.remove(numCode);
					denCodes.remove(denCode);
					return conversionFactor;
				}
			}
		}
		return null;
	}
}
