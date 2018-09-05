import java.util.StringTokenizer;



import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Data type to hold the various fields provided by a single data field
 * @author David
 *
 */
public class OwnerData {

	// data provided from CSV export
	public String accountNumber;
	public String yearBuilt; 
	public String parcelNumber; 
	public String ownerName;
	public String address;
	public String zipcode;
	public String subdivision; 
	public String lot;
	public String stateUse;
	public String totalValue;
	public String landValue;
	public String buildingValue;
	public String cauvValue;
	public String taxableVal;
	public String squareFeet;
	public String bedrooms;
	public String baths;
	public String acres;
	public String city;
	public String municipal;
	public String township;
	public String district;
	public String schoolDistrict;
	public String neighborhood;
	public String politicalJurisdictionCode;
	public String politicalDistrictCode;
	public String politicalJurisdiction;
	public String mailName;
	public String mailAddress1;
	public String mailAddress2;
	public String mailAddress3;
	public String mailZip;
	
	// Website location to parse
	public String websiteURL = "http://www.co.warren.oh.us/property_Search/summary.aspx?account_nbr=";
	
	public String heatingSystem;
	
	// data found online
	private boolean isHeatTypeForcedAir;
	
	// other variables for data checking
	private boolean isYearBetween2000And2005;
	
	private boolean dataMeetsClientSpecifications;
	
	public OwnerData(String lineOfData, WebDriver webpage) throws Exception {
		parseStringData(lineOfData);
		checkIfDataMeetsClientRequirements(webpage);
	}
	
	public boolean getDataMeetsClientSpecifications () {
		return dataMeetsClientSpecifications;
	}
	
	public boolean getIsHeatTypeForcedAir() {
		return isHeatTypeForcedAir;
	}
	
	private void parseStringData(String data) throws Exception {
		StringTokenizer tokenizedData = new StringTokenizer(data, ",");
		accountNumber = tokenizedData.nextToken();
		yearBuilt = tokenizedData.nextToken();
		parcelNumber = tokenizedData.nextToken(); // grab
		ownerName = tokenizedData.nextToken(); 
		address = tokenizedData.nextToken(); // grab
		zipcode = tokenizedData.nextToken(); // grab
		subdivision = tokenizedData.nextToken(); 
		lot = tokenizedData.nextToken(); 
		stateUse = tokenizedData.nextToken();
		totalValue = tokenizedData.nextToken();
		landValue = tokenizedData.nextToken();
		buildingValue = tokenizedData.nextToken(); // grab
		cauvValue = tokenizedData.nextToken();
		taxableVal = tokenizedData.nextToken();
		squareFeet = tokenizedData.nextToken(); 
		bedrooms = tokenizedData.nextToken();
		baths = tokenizedData.nextToken();
		acres = tokenizedData.nextToken();
		city = tokenizedData.nextToken(); // grab
		municipal = tokenizedData.nextToken();
		township = tokenizedData.nextToken();
		district = tokenizedData.nextToken();
		schoolDistrict = tokenizedData.nextToken();
		neighborhood = tokenizedData.nextToken();
		politicalJurisdictionCode = tokenizedData.nextToken();
		politicalDistrictCode = tokenizedData.nextToken();
		politicalJurisdiction = tokenizedData.nextToken();
		mailName = tokenizedData.nextToken();
		mailAddress1 = tokenizedData.nextToken();
		mailAddress2 = tokenizedData.nextToken();
		mailAddress3 = tokenizedData.nextToken();
		mailZip = tokenizedData.nextToken();
		
		if (tokenizedData.hasMoreTokens()) {
			throw new Exception("More tokens to parse - construction of OwnerData object failed");
		}
	}
	
	private void checkIfDataMeetsClientRequirements(WebDriver webpage) {
		boolean meetsClientSpecs = false;
		int yearBuiltAsInt = Integer.parseInt(yearBuilt);
		if (yearBuiltAsInt >= 2000 && yearBuiltAsInt <= 2005) {
			isYearBetween2000And2005 = true;
			checkWebForForcedAir(webpage);
		}
	}
	
	private void checkWebForForcedAir(WebDriver webpage) {
		
		// grab webpage;
		
		
		webpage.get(websiteURL + accountNumber);
		
		
		WebElement element = (WebElement) ((JavascriptExecutor) webpage).executeScript("javascript:__doPostBack('ctl00$ContentPlaceHolderContent$lbBuildingDetails','')");
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			
			webpage.close();
			e.printStackTrace();
		}
		
		try {
			WebElement elementOfHeatingSystem = webpage.findElement(By.id("ContentPlaceHolderContent_lblResBldgHeatingSystem"));
			heatingSystem = elementOfHeatingSystem.getText();
			if (heatingSystem.equals("FORCED AIR")) {
				isHeatTypeForcedAir = true;
				dataMeetsClientSpecifications = true;
			} else {
				isHeatTypeForcedAir = false;
				dataMeetsClientSpecifications = false;
			}
		} catch (Exception e) {
			System.out.println("Exception caught in data object: no heating system data");
			heatingSystem = "N.A";
			isHeatTypeForcedAir = false;
			dataMeetsClientSpecifications = false;
		}
		
		
		
	}
	
	
	
}

