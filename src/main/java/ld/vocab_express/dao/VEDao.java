package ld.vocab_express.dao;

import java.util.List;

public interface VEDao {

	List<VElement> getClasses() throws DaoException;
	List<String> getGraphs() throws DaoException;	
	List<String> getProperties() throws DaoException;	
	List<String> getInstances(String clazz) throws DaoException;	
	List<String> getVocabs() throws DaoException;	
	
	void setEndPointUri(String endPointUri);
	
}
