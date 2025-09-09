package com.example.spring2.service.Impl;

import com.example.spring2.dto.request.CustomerRequest;
import com.example.spring2.dto.response.CustomerResponse;
import com.example.spring2.entity.Cart;
import com.example.spring2.entity.Customer;
import com.example.spring2.exception.CustomerNotFoundException;
import com.example.spring2.exception.EmailAlreadyInUseException;
import com.example.spring2.mapper.CustomerMapper;
import com.example.spring2.repository.CartRepository;
import com.example.spring2.repository.CustomerRepository;
import com.example.spring2.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CustomerResponse addCustomer(CustomerRequest request) { //Parametre olarak bir CustomerRequest alır. Geriye CustomerResponse döner.
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) { //Verilen e-posta ile zaten bir müşteri var mı kontrol eder. .isPresent() → Eğer e-posta zaten kullanılıyorsa true döner.
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        Customer customer = customerMapper.toCustomer(request);
        Customer savedCustomer = customerRepository.save(customer);
        cartRepository.save(createCartForCustomer(savedCustomer));
        return customerMapper.toCustomerResponse(savedCustomer);

        /*
        Customer customer = customerMapper.toCustomer(request); //CustomerRequest → Customer dönüşümünü yapar. Bu işlemle DTO, veritabanına uygun Entity'ye dönüştürülür.
        Customer savedCustomer = customerRepository.save(customer); // Yeni müşteri veritabanına kaydedilir. save() metodu, JPA sayesinde otomatik olarak müşteri nesnesini veri tabanına kaydeder ve kaydedilen (ID atanmış) nesneyi geri döner.
        Cart cart = new Cart();
        cart.setCustomer(savedCustomer); // Müşteri ile sepeti ilişkilendir
        cart.setTotalPrice(0.0);
        cartRepository.save(cart); // Yeni sepeti veritabanına kaydet
        return customerMapper.toCustomerResponse(savedCustomer); // Veritabanına kaydedilen müşteri, CustomerResponse formatına dönüştürülerek dış dünyaya sunulur.
         */
    }

    private Cart createCartForCustomer(Customer customer) {
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0.0);
        return cart;
    }

    @Override
    public CustomerResponse getCustomer(Long customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return customerMapper.toCustomerResponse(customer);

    }

    @Override
    public List<CustomerResponse> getCustomersByPrefix(String prefix) {
        List<Customer> customers = customerRepository.findByNameStartingWith(prefix);
        return customers.stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }
}
