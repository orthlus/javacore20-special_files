import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception {
		String csvFile = "data.csv";
		String jsonFile = "data.json";
		List<Employee> employees = readCsv(csvFile);
		writeJsonFile(employees, jsonFile);

		String xmlFile = "data.xml";
		String jsonFile2 = "data2.json";
		employees = readXmlFile(xmlFile);
		writeJsonFile(employees, jsonFile2);
	}

	private static List<Employee> readXmlFile(String xmlFile) throws IOException {
		String fileContent = Files.readString(Path.of(xmlFile));
		XmlMapper xml = new XmlMapper();
		return xml.readValue(fileContent, new TypeReference<>() {
		});
	}

	private static List<Employee> readCsv(String fileName) throws Exception {
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
		ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
		strategy.setType(Employee.class);
		strategy.setColumnMapping(columnMapping);
		CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(new FileReader(fileName))
				.withMappingStrategy(strategy)
				.build();
		return csvToBean.parse();
	}

	private static Path writeJsonFile(List<Employee> data, String fileName) throws Exception {
		JsonMapper json = new JsonMapper();
		String valueAsString = json.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		Path file = Path.of(fileName);
//		Files.deleteIfExists(file);
		Files.writeString(file, valueAsString);
		return file;
	}
}
