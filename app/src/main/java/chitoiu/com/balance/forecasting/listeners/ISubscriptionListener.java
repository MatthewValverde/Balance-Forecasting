package chitoiu.com.balance.forecasting.listeners;

/**
 * Created by Matthew on 9/6/2016.
 */
public interface ISubscriptionListener {

    void purchaseMonthlySubscription();

    void purchaseYearlySubscription();

    void getSubscriptionDetails();

    void cancelSubscription(String subscriptionId);
}
