package com.shopdown.admin.user;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shopdown.common.entity.User;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class UserCsvExporter {

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormatter.format(new Date());
		String fileName = "users_" + timeStamp + ".csv";

		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"User ID", "First Name", "Last Name", "Email", "Roles", "Enabled"};
		String[] fieldMapping = {"id", "firstName", "lastName", "email", "roles", "enabled"};

		csvWriter.writeHeader(csvHeader);

		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}

		csvWriter.close();
	}

}
