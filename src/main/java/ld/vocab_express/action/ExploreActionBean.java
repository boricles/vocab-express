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
import org.apache.log4j.Logger;

@UrlBinding("/public/explore")
public class ExploreActionBean extends BaseActionBean {
	
		protected VEDao vocabDao = new VEDaoImpl();
		private String sparqlEndPoint;
		private String omitWKVocabularies;
		private String format;
		
		private String model;
		
		private String graph;

		
		static Logger logger = Logger.getLogger(ExploreActionBean.class);
		
		@DefaultHandler
	    public Resolution view() {
			if (format != null && format.equals("model")) {
				logger.info("Class ExploreActionBean -> format=model");
		        return new ForwardResolution("/WEB-INF/jsp/generate.jsp");
			}
			if (format != null && format.equals("stats")) {
				logger.info("Class ExploreActionBean -> format=stats");
				return new ForwardResolution("/WEB-INF/jsp/explore.jsp");
			}
			logger.info("Class ExploreActionBean -> format=stats");
			return new ForwardResolution("/WEB-INF/jsp/explore.jsp");
	    }

		public void setSparqlEndPoint(String sparqlEndPoint) {
			this.sparqlEndPoint = sparqlEndPoint;
			vocabDao.setEndPointUri(sparqlEndPoint);
		}
		
		public String getSparqlEndPoint() {
			return this.sparqlEndPoint;
		}	
		
		
		public void setOmitWKVocabularies(String omitWKVocabularies) {
			this.omitWKVocabularies = omitWKVocabularies;
			vocabDao.setOmitWKVocabularies(omitWKVocabularies);
		}

		public String getOmitWKVocabularies() {
			return this.omitWKVocabularies;
		}
		
		public String getModel() {
			try {
				return vocabDao.getModel();
			}
			catch (DaoException ex) {
				ex.printStackTrace();
			}
			return "";
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

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
			vocabDao.setFormat(format);
		}
		
		public String getGraph() {
			return graph;
		}

		public void setGraph(String graph) {
			this.graph = graph;
			vocabDao.setGraph(graph);
		}		

}
