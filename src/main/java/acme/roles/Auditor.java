
package acme.roles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Auditor extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Length(max = 75)
	@NotBlank
	private String				firm;

	@Column(unique = true)
	@Length(max = 25)
	@NotBlank
	private String				professionalID;

	@Length(max = 100)
	@NotBlank
	private String				certifications;

	@URL
	@Length(max = 255)
	private String				link;

}
