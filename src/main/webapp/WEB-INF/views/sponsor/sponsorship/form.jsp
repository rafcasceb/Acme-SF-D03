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
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.startDate" path="startDate"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.endDate" path="endDate"/>
	<acme:input-select code="sponsor.sponsorship.form.label.type" path="type"  choices="${types}"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link"/>
	<acme:input-email code="sponsor.sponsorship.form.label.email" path="email"/>
	<acme:input-select code="sponsor.sponsorship.form.label.project" path="project"  choices="${projects}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
			<acme:submit code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
			<acme:submit code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list-for-sponsorship?sponsorshipId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
		</jstl:when>	
	
	</jstl:choose>
	
</acme:form>
