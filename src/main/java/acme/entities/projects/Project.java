
package acme.entities.projects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.roles.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "published"), //
	@Index(columnList = "manager_id"), //
	@Index(columnList = "code"), //
	@Index(columnList = "id, manager_id, published")
})
public class Project extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private boolean				published;

	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{3}-[0-9]{4}$", message = "{validation.project.code}")
	@NotBlank
	private String				code;

	@Length(max = 75)
	@NotBlank
	private String				title;

	@Length(max = 100)
	@NotBlank
	private String				abstractDescription;

	private boolean				fatalErrorPresent;

	@Min(0)
	@Max(300)
	private int					estimatedCostInHours;

	@URL
	@Length(max = 255)
	private String				link;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;

}
