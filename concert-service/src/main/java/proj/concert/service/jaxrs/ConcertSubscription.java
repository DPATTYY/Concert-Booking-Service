package proj.concert.service.jaxrs;

import javax.ws.rs.container.AsyncResponse;

/**
 * Represents a subscription for concert notifications
 */
public class ConcertSubscription {
    public final AsyncResponse response;
    public final double percent;

    /**
     * Constructs a ConcertSubscription object
     *
     * @param response - Asynchronous response object to be resumed
     * @param bookingPercent - Booking percentage threshold for notification
     */
    public ConcertSubscription(AsyncResponse response, int bookingPercent) {
        this.response = response;
        // Convert booking percentage to a double between 0 and 1
        this.percent = bookingPercent / 100.0;
    }
}
