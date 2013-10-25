<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="vocab-express">
  <s:layout-component name="body">
  <h1>Exploring <a href="${actionBean.sparqlEndPoint}">${actionBean.sparqlEndPoint}</a></h1>

  <h2>Summary</h2>
  <ul>
  	<li><h3><a href="#statistics">Statistics</a></h3></li>
    <li><h3><a href="#graphs">Graphs</a></h3></li>
  	<li><h3><a href="#vocabularies">Vocabularies</a></h3></li>
    <li><h3><a href="#classes">Classes</a></h3></li> 
  	<li><h3><a href="#properties">Properties</a></h3></li>  	  	
  </ul>


  <br/><a name="statistics"/>
      <table>
    	<tr>
    		<th>Statistics</th>
    		<th>Value</th>
    	</tr>
    	<c:forEach var="statistic" items="${actionBean.statistics}">
    		<tr>
    			<td>${statistic.uri}</td>
    			<td>${statistic.usage}</td>
    		</tr>
    	</c:forEach>
    </table>
      
  <br/><a name="graphs"/>
    <table>
    	<tr>
    		<th>Graphs</th>
    	</tr>
    	<c:forEach var="graph" items="${actionBean.graphs}">
    		<tr>
    			<td>${graph}</td>
    		</tr>
    	</c:forEach>
    </table>
  
   <br/><a name="vocabs"/>
    <table>
    	<tr>
    		<th>Vocabularies</th>
    		<th>Usage</th>
    	</tr>
    	<c:forEach var="vocab" items="${actionBean.vocabs}">
    		<tr>
    			<td>${vocab.uri}</td>
    			<td>${vocab.usage}</td>
    		</tr>
    	</c:forEach>
    </table>
 
  <br/><a name="classes"/>
    <table>
    	<tr>
    		<th>Class</th>
    		<th>Number of Instances</th>
    	</tr>
    	<c:forEach var="clazz" items="${actionBean.classes}">
    		<tr>
    			<td>${clazz.uri}</td>
    			<td>${clazz.usage}</td>
    		</tr>
    	</c:forEach>
    </table>
  
  <br/><a name="properties"/>        
    <table>
    	<tr>
    		<th>Properties</th>
    	</tr>
    	<c:forEach var="property" items="${actionBean.properties}">
    		<tr>
    			<td>${property}</td>
    		</tr>
    	</c:forEach>
    </table>
      
    
  </s:layout-component>
</s:layout-render>

