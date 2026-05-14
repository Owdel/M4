
public abstract class PaymentFramework {
 
    protected static final double VAT_RATE       = 0.12;
    protected static final double PWD_DISCOUNT   = 0.20;
    protected static final double PROMO_DISCOUNT = 0.10;
 
    public enum PaymentMethod { CASH, CREDIT_CARD, DEBIT_CARD, E_WALLET }
 
    protected abstract boolean validatePayment(double amount);
    protected abstract boolean validatePaymentMethod(PaymentMethod method);
    protected abstract void finalizeTransaction(double total, String receipt);
    protected abstract void onTransactionComplete(String receipt);
    protected abstract void processRefundInDB(String transactionId, double amount);
 
    protected String formatAmount(double amount) {
        return String.format("PHP %.2f", amount);
    }
 
    protected double[] applyVATInclusive(double priceWithVAT) {
        double base = priceWithVAT / (1 + VAT_RATE);
        double vat  = priceWithVAT - base;
        return new double[]{base, vat};
    }
 
    protected double[] applyVATExclusive(double basePrice) {
        double vat   = basePrice * VAT_RATE;
        double total = basePrice + vat;
        return new double[]{basePrice, vat, total};
    }
 
    protected double applyPWDSeniorDiscount(double basePrice, boolean isPWDOrSenior) {
        if (!isPWDOrSenior)
            throw new IllegalStateException("PWD/Senior discount requires eligible customer.");
        return basePrice * (1 - PWD_DISCOUNT);
    }
 
    protected double applyPromoDiscount(double grossPrice, boolean isNormalCustomer) {
        if (!isNormalCustomer)
            throw new IllegalStateException("Promo discount is for normal customers only.");
        return grossPrice * (1 - PROMO_DISCOUNT);
    }
 
    private void guardDiscountConflict(boolean isPWDOrSenior, boolean applyPromo) {
        if (isPWDOrSenior && applyPromo)
            throw new IllegalArgumentException(
                "Discount conflict: PWD/Senior and Promo discounts cannot be combined.");
    }
 
    public void processRefund(String transactionId, double amount) {
        if (transactionId == null || transactionId.isBlank())
            throw new IllegalArgumentException("Transaction ID is required for a refund.");
        if (amount <= 0)
            throw new IllegalArgumentException("Refund amount must be greater than zero.");
 
        processRefundInDB(transactionId, amount);
 
        String receipt = String.format(
            "=== REFUND ===%nTransaction : %s%nRefunded    : %s%n==============%n",
            transactionId, formatAmount(amount));
 
        System.out.print(receipt);
        onTransactionComplete(receipt);
    }
 
    public void processInvoice(double basePrice,
                               boolean vatInclusive,
                               boolean isPWDOrSenior,
                               boolean applyPromo,
                               PaymentMethod method) {
 
        guardDiscountConflict(isPWDOrSenior, applyPromo);
 
        if (!validatePaymentMethod(method))
            throw new IllegalStateException("Invalid or inactive payment method: " + method);
 
        double finalTotal;
        StringBuilder log = new StringBuilder();
 
        if (isPWDOrSenior) {
            finalTotal = applyPWDSeniorDiscount(basePrice, true);
            log.append(String.format("Base Price : %s%n", formatAmount(basePrice)));
            log.append(String.format("Discount   : -%s (20%% PWD/Senior)%n",
                                     formatAmount(basePrice - finalTotal)));
            log.append(String.format("VAT        : EXEMPT%n"));
 
        } else if (vatInclusive) {
            double[] parts = applyVATInclusive(basePrice);
            finalTotal = basePrice;
            if (applyPromo) finalTotal = applyPromoDiscount(finalTotal, true);
            log.append(String.format("Gross Price: %s (VAT-inclusive)%n", formatAmount(basePrice)));
            log.append(String.format("Net of VAT : %s%n", formatAmount(parts[0])));
            log.append(String.format("VAT (12%%) : %s%n", formatAmount(parts[1])));
 
        } else {
            double[] parts = applyVATExclusive(basePrice);
            finalTotal = parts[2];
            if (applyPromo) finalTotal = applyPromoDiscount(finalTotal, true);
            log.append(String.format("Base Price : %s%n", formatAmount(basePrice)));
            log.append(String.format("VAT (12%%) : %s%n", formatAmount(parts[1])));
        }
 
        if (applyPromo && !isPWDOrSenior)
            log.append(String.format("Promo Disc : -10%%%n"));
 
        log.append(String.format("Method     : %s%n", method));
        log.append(String.format("TOTAL DUE  : %s%n", formatAmount(finalTotal)));
 
        if (!validatePayment(finalTotal))
            throw new IllegalStateException("Payment validation failed: insufficient funds.");
 
        finalizeTransaction(finalTotal, log.toString());
 
        String receipt = "=== INVOICE ===\n" + log + "===============\n";
        System.out.print(receipt);
        onTransactionComplete(receipt);
    }
}