
package acme.entities.risks;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "reference"),
})
public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "^R-[0-9]{3}$", message = "{validation.risk.code}")
	@NotBlank
	private String				reference;

	@Temporal(TemporalType.DATE)
	@PastOrPresent
	@NotNull
	private Date				identificationDate;

	@Digits(integer = 1, fraction = 2)
	private double				impact;

	@Digits(integer = 1, fraction = 2)
	private double				probability;

	@Length(max = 100)
	@NotBlank
	private String				description;

	@URL
	@Length(max = 255)
	private String				link;


	@Transient
	public double getValue() {
		return this.impact * this.probability;
	}

}
