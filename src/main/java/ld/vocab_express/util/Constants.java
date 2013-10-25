package ld.vocab_express.util;

public interface Constants {
	
	public static String[] WKVocabularies = new String[]{"http://www.openlinksw.com/schemas/virtrdf",
															  "http://www.w3.org/2002/07/owl",
															  "http://www.w3.org/ns/sparql-service-description",
															  "http://www.w3.org/2000/01/rdf-schema",
															  "http://www.openlinksw.com/schemas",
															  "http://www.w3.org/1999/02/22-rdf-syntax-ns",
															  "http://www.w3.org/2004/02/skos/core" };

	public static String rdfNS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	public static String rdfsNS = "http://www.w3.org/2000/01/rdf-schema#";	
	
	public static String OFFSET_PARAMETER = ":offset";
	
	public static String LIMIT_PARAMETER = ":limit";	
	
	public static String GRAPH_PARAMETER = ":graph";
}
