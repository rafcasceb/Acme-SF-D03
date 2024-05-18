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
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityMust" path="totalNumberUserStoriesPriorityMust" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityShould" path="totalNumberUserStoriesPriorityShould" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityCould" path="totalNumberUserStoriesPriorityCould" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.totalNumberUserStoriesPriorityWont" path="totalNumberUserStoriesPriorityWont" readonly="true" placeholder="--"/>
	
	<acme:input-double code="manager.dashboard.form.label.averageEstimatedCostUserStories" path="averageEstimatedCostUserStories" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.deviationEstimatedCostUserStories" path="deviationEstimatedCostUserStories" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.minimumEstimatedCostUserStories" path="minimumEstimatedCostUserStories" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.maximumEstimatedCostUserStories" path="maximumEstimatedCostUserStories" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.averageCostProjects" path="averageCostProjects" readonly="true" placeholder="--"/>
	<acme:input-double code="manager.dashboard.form.label.deviationCostProjects" path="deviationCostProjects" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.minimumCostProjects" path="minimumCostProjects" readonly="true" placeholder="--"/>
	<acme:input-integer code="manager.dashboard.form.label.maximumCostProjects" path="maximumCostProjects" readonly="true" placeholder="--"/>
</acme:form>

