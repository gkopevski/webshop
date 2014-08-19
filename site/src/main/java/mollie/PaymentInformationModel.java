 
package mollie;

public class PaymentInformationModel {

	//zaednicki
	private String id;
	private String mode;
	private String createdDatetime;
	private float amount;
	private String description;
	private String method;
	private String metadata;
	private String details;
	

	//performing payment
	private String state;
	private String redirectUrl;
	private String webhookUrl;
	
	//receiving processing
	private String status;
	private String cancelledDatetime;
	private String paidDatetime;
	private String expiredDatetime;
	
	
	
	

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getWebhookUrl() {
		return webhookUrl;
	}

	public void setWebhookUrl(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}

	public String getPaidDatetime() {
		return paidDatetime;
	}

	public void setPaidDatetime(String paidDatetime) {
		this.paidDatetime = paidDatetime;
	}

	public String getExpiredDatetime() {
		return expiredDatetime;
	}

	public void setExpiredDatetime(String expiredDatetime) {
		this.expiredDatetime = expiredDatetime;
	}

	private PaymentLinksModel links;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(String createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public PaymentLinksModel getLinks() {
		return links;
	}

	public void setLinks(PaymentLinksModel links) {
		this.links = links;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCancelledDatetime() {
		return cancelledDatetime;
	}

	public void setCancelledDatetime(String cancelledDatetime) {
		this.cancelledDatetime = cancelledDatetime;
	}
	
	
	
	
	
}

