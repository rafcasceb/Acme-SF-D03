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
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code" readonly="True"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.startDate" path="startDate" readonly="True"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.endDate" path="endDate" readonly="True"/>
	<acme:input-select code="sponsor.sponsorship.form.label.type" path="type"  choices="${types}" readonly="True"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount" readonly="True"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link" readonly="True"/>
	<acme:input-email code="sponsor.sponsorship.form.label.email" path="email" readonly="True"/>
	<acme:input-select code="sponsor.sponsorship.form.label.project" path="project"  choices="${projects}" readonly="True"/>
	<acme:input-checkbox code="sponsor.sponsorship.form.label.published" path="published" readonly="True" />
	
</acme:form>
