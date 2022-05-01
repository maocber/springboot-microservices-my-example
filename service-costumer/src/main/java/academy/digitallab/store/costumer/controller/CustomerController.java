package academy.digitallab.store.costumer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import academy.digitallab.store.costumer.repository.entity.Customer;
import academy.digitallab.store.costumer.repository.entity.Region;
import academy.digitallab.store.costumer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(name="/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	@GetMapping
	public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId" , required = false) Long regionId ) {
		List<academy.digitallab.store.costumer.repository.entity.Customer> customers = new ArrayList<Customer>();
		if (regionId == null) {
			customers = customerService.findCustomerAll();
			if (customers.isEmpty())
				return ResponseEntity.notFound().build();
		}else {
			Region region = new Region();
			region.setId(regionId);
			customers = customerService.findCustomersByRegion(region);
			if ( null == customers ) {
                log.error("Customers with Region id {} not found.", regionId);
                return  ResponseEntity.notFound().build();
			}
		}
		return  ResponseEntity.ok(customers);
	}
	
	@GetMapping
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
		Customer customer = customerService.getCustomer(id);
		if(customer == null) {
			log.info("No se encontro al cliente con id: {}", id);
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(customer);
	}
	
	@PostMapping
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
		log.info("Se inserta el cliente {}", customer);
		if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
		
		Customer customerDB = customerService.createCustomer (customer);
        return  ResponseEntity.status( HttpStatus.CREATED).body(customerDB);
	}
	
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
		Customer customerDB = customerService.getCustomer(id);
		if (customerDB == null) {
			return ResponseEntity.notFound().build();
		}else {
			customer.setId(id);
			customer=customerService.updateCustomer(customer);
	        return  ResponseEntity.ok(customer);
		}
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
		log.info("Fetching & Deleting Customer with id {}", id);

        Customer customer = customerService.getCustomer(id);
        if ( null == customer ) {
            log.error("No se encuentra el cliente con el id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        customer = customerService.deleteCustomer(customer);
        return  ResponseEntity.ok(customer);
	}
	
	
	
	private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
