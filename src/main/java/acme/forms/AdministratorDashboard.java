
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------

	// Each role an index and each value the number of roles of that index reference
	private int[]				principalRolesCounter;

	private Double				ratioOfNoticeWithLinkAndEmail;

	private Double				ratioOfObjetiveCritical;
	private Double				ratioOfObjetiveNonCritical;

	private Double				avgValueInRisks;
	private Double				stanDevValueInRisks;
	private Double				maximumValueInRisks;
	private Double				minimumValueInRisks;

	private double				avgNumberOfClaimsLastTenWeeks;
	private double				stanDevNumberOfClaimsLastTenWeeks;
	private int					maximumNumberOfClaimsLastTenWeeks;
	private int					minimumNumberOfClaimsLastTenWeeks;

}
