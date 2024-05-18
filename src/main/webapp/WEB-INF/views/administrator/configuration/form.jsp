<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.configuration.form.label.defaultCurrency" path="defaultCurrency"/>	
	<acme:input-textbox code="administrator.configuration.form.label.acceptedCurrencies" path="acceptedCurrencies"/>
	<acme:input-textbox code="administrator.configuration.form.label.spamTerms" path="spamTerms"/>
	<acme:input-textbox code="administrator.configuration.form.label.spamThreshold" path="spamThreshold"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="administrator.configuration.form.button.update" action="/administrator/configuration/update"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>
