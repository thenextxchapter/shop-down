package com.shopdown.admin.brand.service;

import java.util.List;

import com.shopdown.admin.brand.repository.BrandRepository;
import com.shopdown.admin.category.repository.CategoryRepository;
import com.shopdown.common.entity.Brand;
import com.shopdown.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private BrandRepository brandRepo;

	public List<Brand> listAll() {
		return (List<Brand>) brandRepo.findAll();
	}

	public List<Category> listCategories() {
		return (List<Category>) catRepo.findAll();
	}
}
