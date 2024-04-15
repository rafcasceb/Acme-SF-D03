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
	<acme:input-integer code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeStatic" path="totalNumberCodeAuditsTypeStatic" readonly="true"/>
	<acme:input-integer code="auditor.dashboard.form.label.totalNumberCodeAuditsTypeDynamic" path="totalNumberCodeAuditsTypeDynamic" readonly="true"/>
	
	<acme:input-double code="auditor.dashboard.form.label.averageNumberRecords" path="averageAuditRecords" readonly="true" placeholder="--"/>
	<acme:input-double code="auditor.dashboard.form.label.deviationNumberRecords" path="deviationAuditRecords" readonly="true" placeholder="--"/>
	<acme:input-integer code="auditor.dashboard.form.label.minimumNumberRecords" path="minimumAuditRecords" readonly="true" placeholder="--"/>
	<acme:input-integer code="auditor.dashboard.form.label.maximumNumberRecords" path="maximumAuditRecords" readonly="true" placeholder="--"/>
	<acme:input-double code="auditor.dashboard.form.label.averagePeriodLength" path="averageRecordPeriod" readonly="true" placeholder="--"/>
	<acme:input-double code="auditor.dashboard.form.label.deviationPeriodLength" path="deviationRecordPeriod" readonly="true" placeholder="--"/>
	<acme:input-integer code="auditor.dashboard.form.label.minimumPeriodLength" path="minimumRecordPeriod" readonly="true" placeholder="--"/>
	<acme:input-integer code="auditor.dashboard.form.label.maximumPeriodLength" path="maximumRecordPeriod" readonly="true" placeholder="--"/>
</acme:form>

