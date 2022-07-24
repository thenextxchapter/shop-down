package com.shopdown.admin.category.service;

import java.util.List;

import com.shopdown.admin.category.repository.CategoryRepository;
import com.shopdown.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	public List<Category> listAll() {
		return (List<Category>) categoryRepo.findAll();
	}

}
