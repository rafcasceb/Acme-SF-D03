
package acme.features.auditor.auditorDashboard;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.audits.AuditType;
import acme.forms.AuditorDashboard;
import acme.roles.Auditor;

@Service
public class AuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int auditorId;
		AuditorDashboard dashboard;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();
		Collection<Double> auditingRecordsPerAudit = this.repository.auditingRecordsPerAudit(auditorId);

		Map<AuditType, Integer> totalAuditTypes;
		Double averageAuditRecords;
		Double deviationAuditRecords;
		Integer minimumAuditRecords;
		Integer maximumAuditRecords;
		Double averageRecordPeriod;
		Double deviationRecordPeriod;
		Double minimumRecordPeriod;
		Double maximumRecordPeriod;

		totalAuditTypes = this.repository.totalAuditTypes(auditorId);
		averageAuditRecords = this.repository.averageAuditingRecords(auditorId);
		deviationAuditRecords = this.computeDeviation(auditingRecordsPerAudit);
		minimumAuditRecords = this.repository.minAuditingRecords(auditorId);
		maximumAuditRecords = this.repository.maxAuditingRecords(auditorId);
		averageRecordPeriod = this.repository.averageRecordPeriod(auditorId);
		deviationRecordPeriod = this.repository.deviationRecordPeriod(auditorId);
		minimumRecordPeriod = this.repository.minimumRecordPeriod(auditorId);
		maximumRecordPeriod = this.repository.maximumRecordPeriod(auditorId);

		dashboard = new AuditorDashboard();
		dashboard.setTotalAuditTypes(totalAuditTypes);
		dashboard.setAverageAuditRecords(averageAuditRecords);
		dashboard.setDeviationAuditRecords(deviationAuditRecords);
		dashboard.setMinimumAuditRecords(minimumAuditRecords);
		dashboard.setMaximumAuditRecords(maximumAuditRecords);
		dashboard.setAverageRecordPeriod(averageRecordPeriod);
		dashboard.setDeviationRecordPeriod(deviationRecordPeriod);
		dashboard.setMinimumRecordPeriod(minimumRecordPeriod);
		dashboard.setMaximumRecordPeriod(maximumRecordPeriod);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboard object) {
		assert object != null;

		Dataset dataset;
		Integer totalNumberCodeAuditsTypeStatic;
		Integer totalNumberCodeAuditsTypeDynamic;

		totalNumberCodeAuditsTypeStatic = object.getTotalAuditTypes().get(AuditType.STATIC);
		totalNumberCodeAuditsTypeDynamic = object.getTotalAuditTypes().get(AuditType.DYNAMIC);

		dataset = super.unbind(object, //
			"averageAuditRecords", "deviationAuditRecords", //
			"minimumAuditRecords", "maximumAuditRecords", //
			"averageRecordPeriod", "deviationRecordPeriod", "minimumRecordPeriod", "maximumRecordPeriod");

		dataset.put("totalNumberCodeAuditsTypeDynamic", totalNumberCodeAuditsTypeDynamic);
		dataset.put("totalNumberCodeAuditsTypeStatic", totalNumberCodeAuditsTypeStatic);

		super.getResponse().addData(dataset);
	}

	public Double computeDeviation(final Collection<Double> values) {
		Double res;
		Double aux;
		res = null;
		if (!values.isEmpty()) {
			Double average = this.calculateAverage(values);
			aux = 0.0;
			for (final Double value : values)
				aux += Math.pow(value - average, 2);
			res = Math.sqrt(aux / values.size());
		}
		return res;
	}

	private Double calculateAverage(final Collection<Double> values) {
		double sum = 0.0;
		for (Double value : values)
			sum += value;
		return sum / values.size();
	}

}
