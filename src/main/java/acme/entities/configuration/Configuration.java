
package acme.entities.configuration;

import javax.persistence.Entity;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Configuration extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}$", message = "{validation.configuration.default}")
	protected String			defaultCurrency;

	@NotBlank
	@Pattern(regexp = "^([A-Z]{3})(,\\s*[A-Z]{3})*$", message = "{validation.configuration.accepted}")
	protected String			acceptedCurrencies;

	@NotBlank
	@Length(max = 255)
	protected String			spamTerms;

	@DecimalMin(value = "0.0", inclusive = true)
	@DecimalMax(value = "1.0", inclusive = true)
	@Digits(integer = 1, fraction = 2)
	private double				spamThreshold;
}
