package ld.vocab_express.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import ld.vocab_express.dao.VElement;
import ld.vocab_express.util.DescriptionsFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class VEDaoImpl implements VEDao {
	
	protected String endpointUri;
	protected List<VElement> vocabs = new ArrayList<VElement>();
	protected Vector<Integer> usagesVector = new Vector<Integer>();
	/* Almu cod nuevo */protected StringBuffer statisticsBuffer = new StringBuffer();
	/* Almu cod nuevo */protected StringBuffer vocabulariesBuffer = new StringBuffer();
	
	public VEDaoImpl() {
		
	}
	
	public VEDaoImpl(String endpointUri) {
		this.endpointUri = endpointUri;
	}
	
	public void setEndPointUri (String endpointUri) {
		this.endpointUri = endpointUri;
	}
	
	public List<VElement> getClasses() throws DaoException {
		ArrayList<VElement> result = new ArrayList<VElement>();
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, createGetClassesQuery());
		try {
			ResultSet queryResult = execution.execSelect();
			while (queryResult.hasNext()) {
				QuerySolution solution = queryResult.next();
				
				VElement elem = new VElement();
				elem.setUri(DescriptionsFactory.getResourceDescription(solution,"s"));
				elem.setUsage(getInstancesOfClass(elem.getUri()));
				result.add(elem);
				insertVocab(elem.getUri());
			}
			
			//Sort the arraylist
			Collections.sort(result, new UsageComparator());
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
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
    	
    	if (vocabs.size() == 0){
			ve.setUsage("1");
			vocabs.add(ve);
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
	
	
	public List<VElement> getVocabs() throws DaoException {
		
		ArrayList<VElement> result = new ArrayList<VElement>();
		
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
		return result;
	}
	

	public List<VElement> getStatistics() throws DaoException {
		
		ArrayList<VElement> result = new ArrayList<VElement>();
		int max = Collections.max(usagesVector);
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
		

	
	String getInstancesOfClass(String clazz) throws DaoException {
		String result = "0";
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, createGetNumberInstancesQuery(clazz));
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
				result.add(DescriptionsFactory.getResourceDescription(solution,"g"));
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}
	
	public List<String> getProperties() throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, createGetPropertiesQuery());
		try {
			ResultSet queryResult = execution.execSelect();
			while (queryResult.hasNext()) {
				QuerySolution solution = queryResult.next();
				result.add(DescriptionsFactory.getResourceDescription(solution,"prop"));
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}

	public List<String> getInstances(String clazz) throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		QueryExecution execution = QueryExecutionFactory.sparqlService(endpointUri, createGetInstancesQuery(clazz));
		try {
			ResultSet queryResult = execution.execSelect();
			while (queryResult.hasNext()) {
				QuerySolution solution = queryResult.next();
				result.add(DescriptionsFactory.getResourceDescription(solution,"inst"));
			}
			return result;
		} catch (Exception e) {
			throw new DaoException("Unable to execute SPARQL query", e);
		} finally {
			execution.close();	
		}
	}
	
	String createGetClassesQuery() {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?s WHERE {[] a ?s }");
		return query.toString();		
	}
	
	String createGetGraphsQuery() {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o } }");
		return query.toString();		
	}
	
	String createGetPropertiesQuery() {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?prop WHERE { [] a ?clazz . ?clazz ?prop [] . }");
		return query.toString();		
	}

	String createGetInstancesQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT DISTINCT ?inst WHERE { ");
		query.append("?inst a ");
		query.append("<" + clazz + "> ");
		query.append("}");
		return query.toString();		
	}

	String createGetNumberInstancesQuery(String clazz) {
		StringBuilder query = new StringBuilder("SELECT (COUNT(?s) AS ?c) WHERE {");
		query.append("?s a <" + clazz +">");
		query.append("}");
		return query.toString();		
	}
	

	String createGetStatisticsQuery(String clazz) { /* Future New Statistics */
		return null;
	}
}
