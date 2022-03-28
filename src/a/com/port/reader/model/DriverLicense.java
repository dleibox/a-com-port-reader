package a.com.port.reader.model;

public class DriverLicense {

	private String name;
	private String licenseNumber;
	private String jurisdictionCode;

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
}
