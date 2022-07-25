package com.shopdown.admin.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepo.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName()));

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					String name = "\u00A0\u00A0" + subCategory.getName();
					categoriesUsedInForm.add(new Category(name));
					listChildren(categoriesUsedInForm, subCategory, 1);
				}
			}
		}

		return categoriesUsedInForm;
	}

	private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "\u00A0\u00A0";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(new Category(name));

			listChildren(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

}
