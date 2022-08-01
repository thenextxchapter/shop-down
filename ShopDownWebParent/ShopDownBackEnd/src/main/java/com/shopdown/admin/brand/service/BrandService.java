package com.shopdown.admin.brand.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.shopdown.admin.brand.exception.BrandNotFoundException;
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

	public Brand save(Brand brand) {
		return brandRepo.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepo.findById(id).get();
		} catch (NoSuchElementException exception) {
			throw  new BrandNotFoundException("Could not find any brand with ID " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = brandRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

		brandRepo.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);

		Brand brandByName = brandRepo.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null) return "DuplicateName";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "DuplicateName";
			}
		}

		return "OK";
	}
}
