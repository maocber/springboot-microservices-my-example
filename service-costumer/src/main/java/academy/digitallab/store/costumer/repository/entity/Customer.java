package academy.digitallab.store.costumer.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name="tbl_customers")
@Data
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@NotEmpty(message="La identificaci�n no puede ir vacia")
	@Size(min=8, max=8, message="El tama�o del numero del documento debe ser 8")
	@Column(name="number_id", unique=true, length=8, nullable=false)
	private String numberID;
	
	@NotEmpty(message="El nombre no puede ir vacia")
	@Column(name="first_name", nullable=false)
	private String firstName;
	
	@NotEmpty(message="El apellido no puede ir vacia")
	@Column(name="last_name", nullable=false)
	private String lastName;
	
	@NotEmpty(message = "el correo no puede estar vac�o")
    @Email(message = "no es un direcci�n de correo bien formada")
    @Column(unique=true, nullable=false)
	private String email;
	
	@Column(name="photo_url")
	private String photoUrl;
	
	@NotNull(message = "la regi�n no puede ser vacia")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Region region;
	
	
    private String state;

}
