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
    <acme:input-integer code="sponsor.dashboard.form.label.totalNumberInvoicesTaxEqualOrLessThan21" path="totalNumberInvoicesTaxEqualOrLessThan" readonly="true"/>
    <acme:input-integer code="sponsor.dashboard.form.label.totalNumberSponsorshipsWithLink" path="totalNumberSponsorshipsWithLink" readonly="true"/>
    <acme:input-double code="sponsor.dashboard.form.label.averageAmountSponsorshipsEUR" path="averageAmountSponsorships" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.stdevAmountSponsorships" path="stdevAmountSponsorships" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.minimumAmountSponsorships" path="minimumAmountSponsorships" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.maximumAmountSponsorships" path="maximumAmountSponsorships" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.averageQuantityInvoices" path="averageQuantityInvoices" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.stdevQuantityInvoices" path="stdevQuantityInvoices" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.minimumQuantityInvoices" path="minimumQuantityInvoices" readonly="true" placeholder="--"/>
    <acme:input-double code="sponsor.dashboard.form.label.maximumQuantityInvoices" path="maximumQuantityInvoices" readonly="true" placeholder="--"/>
</acme:form>

