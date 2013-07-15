package ld.vocab_express.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ld.vocab_express.util.DescriptionsFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class VEDaoImpl implements VEDao {
	
	protected String endpointUri;
	
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
	
	
	public List<String> getVocabs() throws DaoException {
		ArrayList<String> result = new ArrayList<String>();
		return result;
		
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
	
	

}
