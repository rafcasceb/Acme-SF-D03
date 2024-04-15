<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="developer.training-module.form.label.code" path="code"/>
	<acme:input-textbox code="developer.training-module.form.label.details" path="details"/>
	<acme:input-select path="difficultyLevel" code="developer.training-module.form.label.difficultyLevel" choices="${difficultyLevels}"/>
	<acme:input-url code="developer.training-module.form.label.link" path="link"/>
	<acme:input-integer code="developer.training-module.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-select code="developer.training-module.form.label.project" path="project" choices="${projects}"/>
	<jstl:choose>
		<jstl:when test="${_command == 'show' && published == true}">
			<acme:button code="developer.training-module.form.button.training-sessions" action="/developer/training-session/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="developer.training-module.form.button.update" action="/developer/training-module/update"/>
			<acme:submit code="developer.training-module.form.button.delete" action="/developer/training-module/delete"/>
			<acme:submit code="developer.training-module.form.button.publish" action="/developer/training-module/publish"/>
			<acme:button code="developer.training-module.form.button.training-sessions" action="/developer/training-session/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.training-module.form.button.create" action="/developer/training-module/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>