package a.com.port.reader.model;

public class DriverLicense {

	private String documentType;
	private String name;
	private String licenseNumber;
	private String jurisdictionCode;
	private String countryCode;

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getLicenseNumber() {
		return this.licenseNumber;
	}

	public void setJurisdictionCode(String jurisdictionCode) {
		this.jurisdictionCode = jurisdictionCode;
	}

	public String getJurisdictionCode() {
		return this.jurisdictionCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}
}
