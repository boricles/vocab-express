package ld.vocab_express.dao;

import java.util.List;

public interface VEDao {

	
	List<String> getGraphs() throws DaoException;	
	List<VElement> getClasses() throws DaoException;
	List<VElement> getVocabs() throws DaoException;	
	List<VElement> getStatistics() throws DaoException;
	List<String> getProperties() throws DaoException;	
	List<String> getInstances(String clazz) throws DaoException;
	
	String getModel() throws DaoException;
	
	void setEndPointUri(String endPointUri);
	
	void setOmitWKVocabularies(String omitWKVocabularies);
	
	void setFormat(String format);
	
	void setGraph(String graph);
	
}
