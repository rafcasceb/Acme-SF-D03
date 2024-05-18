
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Invoice extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "^IN-[0-9]{4}-[0-9]{4}$", message = "{validation.invoice.code}")
	@NotBlank
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@PastOrPresent
	@NotNull
	private Date				registrationTime;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				dueDate;

	@NotNull
	private Money				quantity;

	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "1.0", inclusive = true)
	@Digits(integer = 1, fraction = 2)
	private double				tax;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				published;

	// Derived Attributes -------------------------------------------------------------------------------


	@Transient
	public Money getValue() {
		Double amount = this.quantity.getAmount() + this.quantity.getAmount() * this.tax;
		Money value = new Money();
		value.setAmount(amount);
		value.setCurrency(this.quantity.getCurrency());
		return value;
	}


	// Relationships  ------------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Sponsorship sponsorship;

}
