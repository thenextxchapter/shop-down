package com.shopdown.admin.category.export;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.shopdown.admin.AbstractExporter;
import com.shopdown.common.entity.Category;
import com.shopdown.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Category ID", "Name"};
		String[] fieldMapping = {"id", "name"};

		csvWriter.writeHeader(csvHeader);

		for (Category category : listCategories) {
			category.setName(category.getName());
			csvWriter.write(category, fieldMapping);
		}

		csvWriter.close();
	}
}
