
package acme.roles;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "user_account_id")
})
public class Manager extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Length(max = 75)
	@NotBlank
	private String				degree;

	@Length(max = 100)
	@NotBlank
	private String				overview;

	@Length(max = 100)
	@NotBlank
	private String				certifications;

	@URL
	@Length(max = 255)
	private String				link;

}
