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
	<acme:input-integer code="administrator.dashboard.form.label.numberAuditor" path="numberAuditor" readonly="true" placeholder="--"/>
	<acme:input-integer code="administrator.dashboard.form.label.numberClient" path="numberClient" readonly="true" placeholder="--"/>	
	<acme:input-integer code="administrator.dashboard.form.label.numberDev" path="numberDev" readonly="true" placeholder="--"/>
	<acme:input-integer code="administrator.dashboard.form.label.numberMan" path="numberMan" readonly="true" placeholder="--"/>
	<acme:input-integer code="administrator.dashboard.form.label.numberSpon" path="numberSpon" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.ratioOfNoticeWithLinkAndEmail" path="ratioOfNoticeWithLinkAndEmail" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.ratioOfObjetiveCritical" path="ratioOfObjetiveCritical" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.ratioOfObjetiveNonCritical" path="ratioOfObjetiveNonCritical" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.avgValueInRisks" path="avgValueInRisks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.stanDevValueInRisks" path="stanDevValueInRisks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.maximumValueInRisks" path="maximumValueInRisks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.minimumValueInRisks" path="minimumValueInRisks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.avgNumberOfClaimsLastTenWeeks" path="avgNumberOfClaimsLastTenWeeks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.stanDevNumberOfClaimsLastTenWeeks" path="stanDevNumberOfClaimsLastTenWeeks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.maximumNumberOfClaimsLastTenWeeks" path="maximumNumberOfClaimsLastTenWeeks" readonly="true" placeholder="--"/>
	<acme:input-double code="administrator.dashboard.form.label.minimumNumberOfClaimsLastTenWeeks" path="minimumNumberOfClaimsLastTenWeeks" readonly="true" placeholder="--"/>
</acme:form>

