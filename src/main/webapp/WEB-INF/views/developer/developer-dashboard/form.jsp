<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-integer code="developer.dashboard.form.label.totalNumberTrainingModulesWithUpdateMoment" path="totalNumberTrainingModulesWithUpdateMoment" readonly="true" placeholder ="--"/>
	<acme:input-integer code="developer.dashboard.form.label.totalNumberTrainingSessionsWithLink" path="totalNumberTrainingSessionsWithLink" readonly="true" placeholder ="--"/>
	
	<acme:input-double code="developer.dashboard.form.label.averageTimeTrainingModules" path="averageTimeTrainingModules" readonly="true" placeholder="--"/>
	<acme:input-double code="developer.dashboard.form.label.deviationTimeTrainingModules" path="deviationTimeTrainingModules" readonly="true" placeholder="--"/>
	<acme:input-integer code="developer.dashboard.form.label.minimumTimeTrainingModules" path="minimumTimeTrainingModules" readonly="true" placeholder="--"/>
	<acme:input-integer code="developer.dashboard.form.label.maximumTimeTrainingModules" path="maximumTimeTrainingModules" readonly="true" placeholder="--"/>

</acme:form>