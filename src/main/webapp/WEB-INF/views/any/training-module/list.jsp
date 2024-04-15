<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.training-module.list.label.code" path="code" width="30%"/>
	<acme:list-column code="any.training-module.list.label.details" path="details" width="40%"/>
	<acme:list-column code="any.training-module.list.label.difficulty-level" path="difficultyLevel" width="30%"/>
	
	
</acme:list>	