package mollie;

public class PaymentLinksModel {

    private String paymentUrl;
    private String redirectUrl;



    public String getPaymentUrl() {
        return paymentUrl;
    }
    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }


}