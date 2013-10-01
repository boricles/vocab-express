package ld.vocab_express.action;

import java.util.List;

import ld.vocab_express.dao.VEDaoImpl;
import ld.vocab_express.dao.DaoException;
import ld.vocab_express.dao.VEDao;
import ld.vocab_express.dao.VElement;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/public/explore")
public class ExploreActionBean extends BaseActionBean {
	 
	
		protected VEDao vocabDao = new VEDaoImpl();
		private String sparqlEndPoint;
	
		@DefaultHandler
	    public Resolution view() {
	        return new ForwardResolution("/WEB-INF/jsp/explore.jsp");
	    }

		public void setSparqlEndPoint(String sparqlEndPoint) {
			this.sparqlEndPoint = sparqlEndPoint;
			vocabDao.setEndPointUri(sparqlEndPoint);
		}
		
		public String getSparqlEndPoint() {
			return this.sparqlEndPoint;
		}	
		
		public List<VElement> getClasses() {
			try {
				return vocabDao.getClasses();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		public List<VElement> getVocabs() {
			try {
				return vocabDao.getVocabs();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		public List<VElement> getStatistics() {
			try {
				return vocabDao.getStatistics();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		public List<String> getGraphs() {
			try {
				return vocabDao.getGraphs();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		public List<String> getProperties() {
			try {
				return vocabDao.getProperties();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		

}
