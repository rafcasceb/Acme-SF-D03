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
	<acme:input-textbox code="auditor.codeaudit.label.code" path="code" />
	<acme:input-moment code="auditor.codeaudit.label.execution" path="execution" />
	<acme:input-select code="auditor.codeaudit.label.type" path="type" choices="${auditTypes}" />
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish')}">
		<acme:input-textbox code="auditor.codeaudit.label.modeMark" path="modeMark" placeholder="validation.codeaudit.mode.empty" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="auditor.codeaudit.label.correctiveActions" path="correctiveActions" />
	<acme:input-url code="auditor.codeaudit.label.link" path="link" />
	<acme:input-select code="auditor.codeaudit.label.project" path="project" choices="${projects}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:button code="auditor.codeaudit.form.button.auditrecords" action="/auditor/audit-record/list-for-code-audits?codeAuditId=${id}"/>
			<acme:submit code="auditor.codeaudit.form.button.update" action="/auditor/code-audit/update"/>
			<acme:submit code="auditor.codeaudit.form.button.delete" action="/auditor/code-audit/delete"/>
			<acme:submit code="auditor.codeaudit.form.button.publish" action="/auditor/code-audit/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.codeaudit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
