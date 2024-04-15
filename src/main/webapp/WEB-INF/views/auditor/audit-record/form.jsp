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
	<acme:input-textbox code="auditor.auditrecord.label.code" path="code" />
	<acme:input-url code="auditor.auditrecord.label.link" path="link" />
	<acme:input-select code="auditor.auditrecord.label.mark" path="mark" choices="${marks}" />
	<acme:input-moment code="auditor.auditrecord.label.initialMoment" path="initialMoment" />
	<acme:input-moment code="auditor.auditrecord.label.finalMoment" path="finalMoment" />
	<acme:input-select code="auditor.auditrecord.label.audit" path="audit" choices="${codeaudits}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="auditor.auditrecord.form.button.update" action="/auditor/audit-record/update"/>
			<acme:submit code="auditor.auditrecord.form.button.delete" action="/auditor/audit-record/delete"/>
			<acme:submit code="auditor.auditrecord.form.button.publish" action="/auditor/audit-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditrecord.form.button.create" action="/auditor/audit-record/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
