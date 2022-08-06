package com.shopdown.admin.product.service;

import java.util.List;

import com.shopdown.admin.product.repository.ProductRepository;
import com.shopdown.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	@Autowired private ProductRepository repo;

	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
}
