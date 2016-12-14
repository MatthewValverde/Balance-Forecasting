package chitoiu.com.balance.forecasting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import chitoiu.com.balance.forecasting.listeners.ISubscriptionListener;

/**
 * Created by Matthew on 9/6/2016.
 */
public class SubscriptionsActivity extends AppCompatActivity
        implements BillingProcessor.IBillingHandler, ISubscriptionListener {
    protected BillingProcessor billingProcessor;
    protected boolean isBillingInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        billingProcessor =
                new BillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this);
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        isBillingInitialized = true;
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
    }

    @Override
    public void purchaseMonthlySubscription() {
        if (!isBillingInitialized) return;
        billingProcessor.subscribe(this, "YOUR MONTHLY SUBSCRIPTION ID");
    }

    @Override
    public void purchaseYearlySubscription() {
        if (!isBillingInitialized) return;
        billingProcessor.subscribe(this, "YOUR YEARLY SUBSCRIPTION ID");
    }

    @Override
    public void getSubscriptionDetails() {
        if (!isBillingInitialized) return;
        SkuDetails skuDetails =
                billingProcessor.getSubscriptionListingDetails("YOUR SUBSCRIPTION ID");
        //skuDetails.productId;
    }

    @Override
    public void cancelSubscription(String subscriptionId) {
        if (!isBillingInitialized) return;
        billingProcessor.updateSubscription(this, "YOUR SUBSCRIPTION ID", null);
    }
}
