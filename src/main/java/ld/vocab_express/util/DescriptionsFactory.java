package ld.vocab_express.util;

import com.hp.hpl.jena.query.QuerySolution;

public class DescriptionsFactory {

		public static String getResourceDescription(QuerySolution querySolution, String var){
			if(querySolution.get(var).isLiteral())
				return querySolution.getLiteral(var).getLexicalForm().toString();
			return querySolution.getResource(var).getURI().toString();
		}

}
