package ld.vocab_express.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import ld.vocab_express.action.ExploreActionBean;
import ld.vocab_express.dao.VElement;
import ld.vocab_express.util.Constants;
import ld.vocab_express.util.DescriptionsFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class VEDaoImpl implements VEDao, Constants {
	
	protected String endpointUri;
	protected List<VElement> vocabs = null;
	protected List<VElement> classes = null;
	protected List<String> properties = null;
	protected Vector<Integer> usagesVector = null;
	protected boolean omitWKVocabularies = false;
	protected String format;
	
	protected String graph;
	
	protected String model = null;
	
	static Logger logger = Logger.getLogger(VEDaoImpl.class);
	
	
	public VEDaoImpl() {
		
	}
	
	public VEDaoImpl(String endpointUri) {
		this.endpointUri = endpointUri;
	}
	
	
	public void setEndPointUri (String endpointUri) {
		this.endpointUri = endpointUri;
	}
	
	public void setOmitWKVocabularies(String omitWKVocabularies) {
		if (omitWKVocabularies.equalsIgnoreCase("true"))
				this.omitWKVocabularies = true;
	}
	
	protected boolean isFromAllowedVocab(String uriOfResource) {
		if (omitWKVocabularies) {
			for (String vocab : WKVocabularies) {
				if (uriOfResource.contains(vocab))
					return false;		
			}
		}
		return true;
	}
	
	protected void buildClasses() throws DaoException {
		logger.info("Class VEDaoImpl -> buildClasses: init");
		ArrayList<VElement> result = new ArrayList<VElement>();
		QueryExecution execution = null;
		int limit=1000, offset=0;
		try {
				String originalQuery = createGetClassesQuery();
				while (limit>0) {
					
					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));
					
					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
					logger.info("Class VEDaoImpl -> buildClasses: querying " + query);
					ResultSet queryResult = execution.execSelect();
					int count = 0;
					
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						
						String uriOfClass = DescriptionsFactory.getResourceDescription(solution,"s");
						if (isFromAllowedVocab(uriOfClass)) {
							VElement elem = new VElement();
							elem.setUri(uriOfClass);
							elem.setUsage(getInstancesOfClass(elem.getUri()));
							result.add(elem);
							insertVocab(elem.getUri());
						}
						count++;
					
					}
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
			}
			
			if (result.isEmpty() && vocabs == null)
					vocabs =  new ArrayList<VElement>();
			
			//Sort the arraylist
			Collections.sort(result, new UsageComparator());
			classes = result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			if (execution!=null)
				execution.close();
			logger.info("Class VEDaoImpl -> buildClasses: end ");
		}		
	}
	
	protected void buildProperties() throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		QueryExecution execution = null;
		int limit=1000, offset=0;
		
		try {
				String originalQuery = createGetPropertiesQuery();
				while (limit>0) {
					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));

					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
					
					ResultSet queryResult = execution.execSelect();
					int count = 0;
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						String uriOfProp = DescriptionsFactory.getResourceDescription(solution,"prop");
						if (isFromAllowedVocab(uriOfProp)) {
							result.add(uriOfProp);
							insertVocab(uriOfProp);
						}
						count++;
					}
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
				}
				properties = result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}
	
	
	public List<VElement> getStatistics() throws DaoException {
		if (classes==null)
			buildClasses();
		if (properties==null)
			buildProperties();
		buildVocabs(); //by default once we have the classes
		ArrayList<VElement> result = new ArrayList<VElement>();
		int max = 0;
		if (!usagesVector.isEmpty())
			max = Collections.max(usagesVector);
		String strMax = "";
		int pos=-1;
		
		for (int i=0; i<vocabs.size(); i++){
			
			String uri = vocabs.get(i).getUri();
			String usage = vocabs.get(i).getUsage();
			
			if (Integer.parseInt(usage) == max) {
				pos=i;
				if (pos!=-1) strMax = uri;
				
				VElement elem = new VElement();
				elem.setUri("Key vocab: " + strMax);
				elem.setUsage(Integer.toString(max));
				result.add(elem);
			}
		}
		
		return result;
	}

	public List<VElement> getVocabs() throws DaoException {
		if (vocabs==null)
			buildVocabs();
		return vocabs;
	}
	
	public void buildVocabs() throws DaoException {
		
		if (usagesVector == null)
			usagesVector = new Vector<Integer>();
			
		ArrayList<VElement> result = new ArrayList<VElement>();
		
		if (vocabs==null)
			return;
		
		for (int i=0; i<vocabs.size(); i++) {
			
			VElement elem = new VElement();
			String uri = vocabs.get(i).getUri();
			String usage = vocabs.get(i).getUsage();
			elem.setUri(uri);
			elem.setUsage(usage);
			
			//Add usages to Vector
			int usage2int = Integer.parseInt(usage);
			usagesVector.add(new Integer(usage2int));
			
			result.add(elem);
			
		}
		//Sort the arraylist
		Collections.sort(result, new UsageComparator());
		vocabs = result;
	}
	
	
	public List<VElement> getClasses() throws DaoException {
		if (classes==null)
			buildClasses();
		return classes;
	}
	
	
	private int indexOfVal(List<VElement> vocabs, String val){
		for (int i=0; i<vocabs.size(); i++){
			if (vocabs.get(i).getUri().equals(val)) return i;	 //comparing value to val
		}
		return -1;
	}
	
	
	private void insertVocab(String value) {
    	int index = value.lastIndexOf('#'); //URL: position of the last '#'
    	if (index == -1) {
    		index = value.lastIndexOf('/'); //if not, position of '/'
    	}
    	
    	if (index != -1) {
    		value = value.substring(0,index);
    	}

    	VElement ve = new VElement();
		ve.setUri(value);

		if (vocabs == null) {
			vocabs =  new ArrayList<VElement>();
	    	if (vocabs.size() == 0){
				ve.setUsage("1");
				vocabs.add(ve);
	    	}
		}
    	else { //vocabs.size() != 0

	    	int pos = indexOfVal(vocabs, value); 
	    	if (pos != -1){ //if it exists -> increase usage
	    		int usage = Integer.parseInt(vocabs.get(pos).getUsage());
	    		vocabs.get(pos).setUsage(Integer.toString(usage+1)); //increase usage each time value appears in the namespace
	    	}
	    	else{
	    		ve.setUsage("1");//if if doesnt appear -> add it
	    		vocabs.add(ve);
	    	}
    	}
	}

	
	String getInstancesOfClass(String clazz) throws DaoException {
		String result = "0";
		
		String query = createGetNumberInstancesQuery(clazz);

		if (graph!=null && !graph.isEmpty())
			query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
		else
			query = query.replace(GRAPH_PARAMETER,"");
		
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, query);
		try {
			ResultSet queryResult = execution.execSelect();
			while (queryResult.hasNext()) {
				QuerySolution solution = queryResult.next();
				result = DescriptionsFactory.getResourceDescription(solution,"c");
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();
		}
		
	}
	
	public List<String> getGraphs() throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, createGetGraphsQuery());
		try {
			ResultSet queryResult = execution.execSelect();
			while (queryResult.hasNext()) {
				QuerySolution solution = queryResult.next();
				String uriOfGraph = DescriptionsFactory.getResourceDescription(solution,"g");
				if (isFromAllowedVocab(uriOfGraph)) 
					result.add(uriOfGraph);
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}
	
	public List<String> getProperties() throws DaoException {
		if (properties==null)
			buildProperties();
		return properties;
	}

	public List<String> getInstances(String clazz) throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		
		QueryExecution execution = null;
		int limit=1000, offset=0;
		
		try {
				String originalQuery = createGetInstancesQuery(clazz);
				while (limit>0) {
					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));
										
					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
			
					ResultSet queryResult = execution.execSelect();
					int count = 0;
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						result.add(DescriptionsFactory.getResourceDescription(solution,"inst"));
						count++;
					}
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
				}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}
	
	public String getModel() throws DaoException {
		if (model == null || model.isEmpty())
			buildModel();
		return model;
	}
	
	protected String obtainClassType(String clazz) throws DaoException {
		String classType = "";
		QueryExecution execution = null;
		int limit=1000, offset=0;
		
		
		try {
				String originalQuery = createClassTypeQuery(clazz);
				while (limit>0) {
					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));

					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
			
					ResultSet queryResult = execution.execSelect();
					int count = 0;					
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						String result = DescriptionsFactory.getResourceDescription(solution,"type");
						if (result != null && !result.isEmpty())
							classType += "<" + clazz + ">" + " <" + rdfNS + "type" + "> " + "<" + result + "> . \n";
						count++;						
					}
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
				}
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();
		}		
		return classType;
	}
	

	protected String obtainPropertyRange(String clazz, String prop) throws DaoException {
		String propertyRange = "";
		
		QueryExecution execution = null;
		int limit=1000, offset=0;
		
		try {
			String originalQuery = createPropertyRangeQuery(clazz,prop);
			while (limit>0) {

					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));
					
					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
				
					ResultSet queryResult = execution.execSelect();
					int count = 0;
					
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						String result = DescriptionsFactory.getResourceDescription(solution,"range");
						if (result != null && !result.isEmpty()) {
							propertyRange += "<" + prop + ">" + " <" + rdfsNS + "range" + "> " + "<" + result + "> . \n";
						}
						count++;						
					}
					
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
					
			}
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();
		}		
		return propertyRange;
	}

	protected boolean notFromWKVocabs(String element) {
		for (String vocab : WKVocabularies) {
			if (element.contains(vocab))
				return false;		
		}
		return true;
		
	}

	protected String obtainClassProperties(String clazz) throws DaoException {
		String classProps = "";
		QueryExecution execution = null;
		int limit=1000, offset=0;
		
		try {
				String originalQuery = createClassPropertiesQuery(clazz);
				while (limit>0) {
					String query = originalQuery.replace(OFFSET_PARAMETER, Integer.toString(offset));
					query = query.replace(LIMIT_PARAMETER, Integer.toString(limit));
					
					if (graph!=null && !graph.isEmpty())
						query = query.replace(GRAPH_PARAMETER," FROM <" + graph + "> ");
					else
						query = query.replace(GRAPH_PARAMETER,"");
					
					execution = QueryExecutionFactory.sparqlService(endpointUri, query);
					
					ResultSet queryResult = execution.execSelect();
					int count = 0;
					while (queryResult.hasNext()) {
						QuerySolution solution = queryResult.next();
						String result = DescriptionsFactory.getResourceDescription(solution,"prop");
						if (result != null && !result.isEmpty()) {
							if (notFromWKVocabs(result)) {
								classProps += "<" + result + ">" + " <" + rdfsNS + "domain" + "> " + "<" + clazz + "> . \n";
								classProps += obtainPropertyRange(clazz,result);
							}
						}
						count++;
					}
					if (count>0)
						offset += count;
					if (count<limit)
						limit=0;
				}
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();
		}		
		return classProps;
	}
	
	
	protected String obtainClassInformation(String clazz) throws DaoException {
		return obtainClassType(clazz) + obtainClassProperties(clazz);
		
	}
	
	protected void buildModel() throws DaoException {
		if (classes==null)				
			buildClasses();
		
		//if (properties==null)
		//	buildProperties();
		
		if (model == null)
			model = new String();		
		
		for (VElement clazz : classes) {
			//Process class
			model += obtainClassInformation(clazz.uri);
		}
	}

	String createGetClassesQuery() {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?s :graph WHERE {[] a ?s }  LIMIT :limit OFFSET :offset ");
		return query.toString();		
	}
	
	String createGetGraphsQuery() {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o } } ");// LIMIT :limit OFFSET :offset ");
		return query.toString();		
	}
	
	String createGetPropertiesQuery() {
		//StringBuilder query = new StringBuilder("SELECT DISTINCT ?prop WHERE { [] a ?clazz . ?clazz ?prop [] . }");
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?prop :graph WHERE { [] ?prop [] }  LIMIT :limit OFFSET :offset ");
		return query.toString();		
	}

	String createGetInstancesQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?inst :graph WHERE { ");
		query.append("?inst a ");
		query.append("<" + clazz + "> ");
		query.append("}  LIMIT :limit OFFSET :offset ");
		return query.toString();		
	}

	String createGetNumberInstancesQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT (COUNT(?s) AS ?c) :graph WHERE {");
		query.append("?s a <" + clazz +">");
		query.append("}");
		return query.toString();		
	}
	
	String createClassTypeQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT ?type :graph WHERE { <" +clazz+ "> a ?type }  LIMIT :limit OFFSET :offset ");
		return query.toString();
	}

	String createClassPropertiesQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?prop :graph WHERE { ?inst a <" +clazz+ "> . ?inst ?prop ?c2 }  LIMIT :limit OFFSET :offset ");
		return query.toString();
	}
	
	String createPropertyRangeQuery(String clazz, String prop) {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?range :graph WHERE { ?inst a <" +clazz+ "> . ?inst <"+prop+"> ?inst2. ?inst2 a ?range }  LIMIT :limit OFFSET :offset ");
		
		return query.toString();
	}
	
	
	String createGetStatisticsQuery(String clazz) { /* Future New Statistics */
		return null;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}
}
