package com.shopdown.admin.category.service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.shopdown.admin.category.exception.CategoryNotFoundException;
import com.shopdown.admin.category.repository.CategoryRepository;
import com.shopdown.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else  if (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		List<Category> rootCategories = categoryRepo.listRootCategories(sort);
		return listHierarchicalCategories(rootCategories, sortDir);
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copy(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = subCategory.getParent().getName() + "/" + subCategory.getName();
				hierarchicalCategories.add(Category.copy(subCategory, name));

				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}

		return hierarchicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
			Category parent, int subLevel, String sortDir){
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;

		for (Category subCategory : children) {
			String name = "";
			name += parent.getParent().getName()+"/"+subCategory.getParent().getName()+"/"+subCategory.getName(); //changed this line

			hierarchicalCategories.add(Category.copy(subCategory, name));

			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
	}

	public Category save(Category category) {
		return categoryRepo.save(category);
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepo.listRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));

				Set<Category> children = sortSubCategories(category.getChildren());

				for (Category subCategory : children) {
					String name = subCategory.getParent().getName() + "/" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));
					listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
				}
			}
		}

		return categoriesUsedInForm;
	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());

		for (Category subCategory : children) {
			String name = "";
			name += parent.getParent().getName()+"/"+subCategory.getParent().getName()+"/"+subCategory.getName(); //changed this line
//			name += subCategory.getName();

			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepo.findById(id).get();
		} catch (NoSuchElementException exception) {
			throw new CategoryNotFoundException("Could not find any category with ID " + id);
		}
	}

	public String checkUnique(Integer id, String name, String alias) {
		/* If ID == null then we're creating a new category */
		boolean isCreatingNew = (id == null || id == 0);

		/* Category object based on name */
		Category categoryByName = categoryRepo.findByName(name);

		/* Check if we're creating a new category */
		if (isCreatingNew) {
			/* check if the categoryByName object is not null, meaning that there's
			* already an existing category in the database */
			if (categoryByName != null) {
				return "DuplicateName";
			} else {
				Category categoryByAlias = categoryRepo.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			/* When we come into this else block the categoryByName object is not null */
			if (categoryByName != null && categoryByName.getId() != id) {
				/* We're checking if there's another category in the database
				* that has the name and alias as the category being edited */
				return "DuplicateName";
			}

			Category categoryByAlias = categoryRepo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}

		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

		/* This method will return the sorted set categories */
	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		/* Tree set is an implementation of the sort set */
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildren.addAll(children);

		return sortedChildren;
	}

	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		categoryRepo.updateEnabledStatus(id, enabled);
	}

}
