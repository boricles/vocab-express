<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="vocab-express - exploring the vocabulary used in a dataset">
  <s:layout-component name="body">
  
		<div id="logo">
		<a href="http://www.isoco.com/"><img src="http://terrazas.name/villazon/boris/data/img/isoco.gif" width="216" alt="iSOCO Logo"></a>
		  
			<a href="http://www.deri.ie/"><img src="http://terrazas.name/villazon/boris/data/img/deri.gif" width="186" alt="DERI Logo"></a>
			<a href="http://www.oeg-upm.net/"><img src="http://terrazas.name/villazon/boris/data/img/oeg.png" width="73" alt="Ontology Engineering Group Logo"></a>  
		  
		</div>
		
		<div id="header">
		  <h1>vocab-express</h1>
		  <div id="tagline">exploring the vocabulary used in a dataset</div>
		</div>
		
		<div id="authors">
		  <a href="http://boris.villazon.terrazas.name/">Boris Villazon-Terrazas</a><br>
		  <a href="http://lab.isoco.net/people/agonzalez">Almudena Gonzalez</a><br>
		  <a href="http://mhausenblas.info/">Michael Hausenblas</a><br>
		</div>

			<div id="content">
			<br>
			<p>vocab-express is a simple tool for exploring the vocabulary used in a dataset. You only need to provide the SPARQL endpoint or the VoID file description of the dataset. A few examples</p>
			<ul>
				<li><a href="http://vocab-express.nodester.com/explore?sparqlEndPoint=http://sparql.data.southampton.ac.uk/" target="_blank">data southampton</a></li>
				<li><a href="http://vocab-express.nodester.com/explore?sparqlEndPoint=http://data.fundacionctic.org/sparql" target="_blank">data fundaction ctic</a></li>
				<!-- <li><a href="http://vocab-express.nodester.com/explore?sparqlEndPoint=http://data-gov.ie/sparql" target="_blank">data-gov.ie</a></li> -->
				<li><a href="http://vocab-express.nodester.com/explore?sparqlEndPoint=http://geo.linkeddata.es/sparql" target="_blank">geo.linkeddata.es</a></li>
			</ul>
			
			<h2 id="news">News</h2>
			
			<ul>
			  <li><strong>2013-06-06: Version 0.0.5 released.</strong> We ported it to Java.</li>
			  <li><strong>2012-03-05: Version 0.0.3 released.</strong> This version improves the visualization of the information.</li>
			  <li><strong>2012-03-01: Version 0.0.2 released.</strong> This version provides some improvements.</li>
			  <li><strong>2011-08-27: Version 0.0.1 released.</strong> First alpha version.</li>  
			</ul>
			
			<h2><a name="support" id="support"></a>Support and feedback</h2>
			
			<p>You can contact the authors via email:<br>
			  <a href="mailto:boris.villazon at terrazas.name">Boris Villazon-Terrazas</a><br>
			  <a href="mailto:agonzalez at isoco.com">Almudena Gonzalez</a><br>			  
			  <a href="mailto:michael.hausenblas at deri.org">Michael Hausenblas</a><br>
			</p>
			
			
			<h2 id="news">Limitations</h2>
			<p>Currently the application only works with SPARQL endpoints that output results in JSON format.</p>
			</div>
  
  </s:layout-component>
</s:layout-render>
