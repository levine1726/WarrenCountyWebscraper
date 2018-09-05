import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *  
 * @author David Levine
 *
 */
public class CSVFileParser {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\David\\Documents\\Geckowebdriver\\geckodriver.exe");
		// Create BufferedReader and get access to CSV File
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter file location: ");
		String fileToParse = "";
		try {
			fileToParse = console.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("Please enter exported csv file name: ");
		String fileToWriteName = "";
		try {
			fileToWriteName = console.readLine();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
		
		BufferedReader csvFile = null;
		try {
			csvFile = new BufferedReader(new FileReader("data\\" + fileToParse));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Open new csv file and prepare header
		FileWriter csvWriter = null;
		try {
			csvWriter = new FileWriter("ExportedFiles\\" + fileToWriteName);
			writeHeader(csvWriter);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		// Toss first line of file
		try {
			csvFile.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int filesPassed = 0;
		int filesFailed = 0;
		int totalFilesProcessed = 0;
		WebDriver webpage = new FirefoxDriver();
		String ownerDataFromFile;
		try {
			ownerDataFromFile = csvFile.readLine();
			
			while (ownerDataFromFile != null) {
				// parse file into OwnerData, and write to new CSV file
				OwnerData data;
				
				try {
					
					data = new OwnerData(ownerDataFromFile, webpage);
					Thread.sleep(1000);
					if (data.getDataMeetsClientSpecifications()) {
						// write CSV content to file
						filesPassed++;
						writeDataToFile(csvWriter, data);
					} else {
						filesFailed++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				totalFilesProcessed++;
				System.out.println("Files examined: " + totalFilesProcessed);
				ownerDataFromFile = csvFile.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Files passed: " + filesPassed);
		System.out.println("Files failed " + filesFailed);
		System.out.println("Total files examined: " + totalFilesProcessed);
		
		try {
			console.close();
			csvFile.close();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static void writeHeader(FileWriter csvWriter) throws IOException {
		String header = "Owner Name,Parcel Number,Address,City,Zipcode,True Value,Building Value,Construction Year,Heating Sstem,Mail Name,Mail Address 1,Mail Address 2,Mail Address 3,Mail Zip,Account Number,\n";
		csvWriter.write(header);
	}
	
	private static void writeDataToFile(FileWriter csvWriter, OwnerData data) {
		StringBuilder dataString = new StringBuilder();
		dataString.append(data.ownerName);
		dataString.append(",");
		dataString.append(data.parcelNumber);
		dataString.append(",");
		dataString.append(data.address);
		dataString.append(",");
		dataString.append(data.city);
		dataString.append(",");
		dataString.append(data.zipcode);
		dataString.append(",");
		dataString.append(data.totalValue);
		dataString.append(",");
		dataString.append(data.buildingValue);
		dataString.append(",");
		dataString.append(data.yearBuilt);
		dataString.append(",");
		dataString.append(data.heatingSystem);
		dataString.append(",");
		dataString.append(data.mailName);
		dataString.append(",");
		dataString.append(data.mailAddress1);
		dataString.append(",");
		dataString.append(data.mailAddress2);
		dataString.append(",");
		dataString.append(data.mailAddress3);
		dataString.append(",");
		dataString.append(data.mailZip);
		dataString.append(",");
		dataString.append(data.accountNumber);
		dataString.append(",");
		dataString.append("\n");
		try {
			csvWriter.write(dataString.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
