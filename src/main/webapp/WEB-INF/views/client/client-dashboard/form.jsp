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
	<acme:input-integer code="client.dashboard.form.label.completenessFirstPercentile" path="completenessFirstPercentile" readonly="true"/>
	<acme:input-integer code="client.dashboard.form.label.completenessSecondPercentile" path="completenessSecondPercentile" readonly="true"/>	
	<acme:input-integer code="client.dashboard.form.label.completenessThirdPercentile" path="completenessThirdPercentile" readonly="true"/>
	<acme:input-integer code="client.dashboard.form.label.completenessFourthPercentile" path="completenessFourthPercentile" readonly="true"/>
	<acme:input-double code="client.dashboard.form.label.contractBudgetAverage" path="contractBudgetAverage" readonly="true" placeholder="--"/>
	<acme:input-double code="client.dashboard.form.label.contractBudgetDeviation" path="contractBudgetDeviation" readonly="true" placeholder="--"/>
	<acme:input-double code="client.dashboard.form.label.minimumBudget" path="minimumBudget" readonly="true" placeholder="--"/>
	<acme:input-double code="client.dashboard.form.label.maximumBudget" path="maximumBudget" readonly="true" placeholder="--"/>

</acme:form>

