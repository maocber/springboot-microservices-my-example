package academy.digitallab.store.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	public List<Product> findByCategory(Category category);

}
