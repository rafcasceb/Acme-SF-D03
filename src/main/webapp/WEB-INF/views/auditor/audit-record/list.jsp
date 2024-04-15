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

<acme:list>
	<acme:list-column code="auditor.auditrecord.label.code" path="code" width="10%"/>
	<acme:list-column code="auditor.auditrecord.label.mark" path="mark"/>
	<acme:list-column code="auditor.auditrecord.label.published" path="published" width="10%"/>
</acme:list>

	<jstl:if test="${_command != 'list-for-code-audits'}">
		<acme:button code="auditor.auditrecord.list.button.create" action="/auditor/audit-record/create"/>
	</jstl:if>
	
