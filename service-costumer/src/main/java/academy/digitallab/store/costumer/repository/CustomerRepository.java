package academy.digitallab.store.costumer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.digitallab.store.costumer.repository.entity.Customer;
import academy.digitallab.store.costumer.repository.entity.Region;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	public Customer findByNumberID(String numberID);
    public List<Customer> findByLastName(String lastName);
    public List<Customer> findByRegion(Region region);
}
