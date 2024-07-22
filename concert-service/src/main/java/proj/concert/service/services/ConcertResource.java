package proj.concert.service.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.PerformerMapper;
import proj.concert.service.mapper.BookingMapper;
import proj.concert.service.mapper.SeatMapper;
import proj.concert.service.util.TheatreLayout;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.core.Response;
import proj.concert.service.jaxrs.ConcertSubscription;


@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {


    private static final ConcurrentHashMap<LocalDateTime, LinkedList<ConcertSubscription>> subscriptions = new ConcurrentHashMap<>();

    private static final ExecutorService thread = Executors.newSingleThreadExecutor();

    @GET
    @Path("/concerts/{id}")
    public Response getConcertbyId(@org.jboss.resteasy.annotations.jaxrs.PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Concert c;
        try {
            em.getTransaction().begin();

            // set the lockmode to read to aovid concurrency
            c = em.find(Concert.class, id, LockModeType.PESSIMISTIC_READ);

            // if the concert does not exist, return not found
            if (c == null) {
                return Response.status(404).build();
            }

            // concert to concertdto
            ConcertDTO dtoConcert = ConcertMapper.toDto(c);

            return Response.ok(dtoConcert).build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }
    }

    @GET
    @Path("/concerts")
    public Response getAllConcerts() {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        List<Concert> concerts;
        List<ConcertDTO> dtoConcerts = new ArrayList<>();
        try {
            em.getTransaction().begin();

            // set the lockmode to read to avoid concurrency
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class).setLockMode(LockModeType.PESSIMISTIC_READ);
            concerts = concertQuery.getResultList();

            // check if gets any concerts
            if (concerts.isEmpty()) {
                return Response.status(204).build();
            }

            // concert to concertdtos
            for (Concert c: concerts) {
                dtoConcerts.add(ConcertMapper.toDto(c));
            }

            return Response.ok().entity(dtoConcerts).build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        List<Concert> concerts;
        List<ConcertSummaryDTO> dtoCS = new ArrayList<>();
        try {
            em.getTransaction().begin();

            // set the lockmode to read to avoid concurrency
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class).setLockMode(LockModeType.PESSIMISTIC_READ);
            concerts = concertQuery.getResultList();

            // check if gets any concerts
            if (concerts.isEmpty()) {
                return Response.status(204).build();
            }

            // convert to concertsummarydto
            for (Concert c: concerts) {
                ConcertSummaryDTO dtoConcertSum = new ConcertSummaryDTO(
                        c.getId(),
                        c.getTitle(),
                        c.getImageName());
                dtoCS.add(dtoConcertSum);
            }
            GenericEntity<List<ConcertSummaryDTO>> out = new GenericEntity<>(dtoCS){};
            Response.ResponseBuilder builder = Response.ok(out);

            return builder.build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }

    @GET
    @Path("/performers/{id}")
    public Response getbyPerformerId(@org.jboss.resteasy.annotations.jaxrs.PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Performer p;
        try {
            em.getTransaction().begin();

            // set the lockmode to read to avoid concurrency
            p = em.find(Performer.class, id, LockModeType.PESSIMISTIC_READ);

            // check if the performer exists
            if (p == null) {
                return Response.status(404).build();
            }

            // convert to performerdto
            PerformerDTO dtoPerformer = PerformerMapper.toDto(p);
            Response.ResponseBuilder builder = Response.ok(dtoPerformer);

            return builder.build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }

    @GET
    @Path("/performers")
    public Response getAllPerformers() {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        List<Performer> performers;
        List<proj.concert.common.dto.PerformerDTO> dtoPerformers = new ArrayList<>();
        try {
            em.getTransaction().begin();
            TypedQuery<Performer> performerQuery = em.createQuery("select p from Performer p", Performer.class).setLockMode(LockModeType.PESSIMISTIC_READ);
            performers = performerQuery.getResultList();

            // check if gets any performers
            if (performers.isEmpty())
                return Response.status(204).build();

            // convert to performerdtos
            for (Performer p: performers) {
                dtoPerformers.add(PerformerMapper.toDto(p));
            }
            Response.ResponseBuilder builder = Response.ok().entity(dtoPerformers);

            return builder.build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }


    @POST
    @Path("/login")
    public Response logIn(UserDTO userDto) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        User user;
        UUID uuid = UUID.randomUUID();

        try {
            em.getTransaction().begin();

            // check username and password; set the lockmode to read to aovid concurrency
            TypedQuery<User> userQuery = em.createQuery("select u from User u where u.username = :username and u.password = :password", User.class).setLockMode(LockModeType.PESSIMISTIC_READ);
            userQuery.setParameter("username", userDto.getUsername());
            userQuery.setParameter("password", userDto.getPassword());
            user = userQuery.getSingleResult();
            if (user == null)
                return Response.status(403).build();

            // create a cookie named "auth" for the user if username and password are both correct
            NewCookie c = new NewCookie("auth", uuid.toString());
            user.setUUID(uuid);
            return Response.ok().cookie(c).build();

        } catch (NoResultException e) {
            return Response.status(401).build();
        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }

    @POST
    @Path("/bookings")
    public Response makeBooking(@CookieParam("auth") Cookie auth, BookingRequestDTO bookingRequestDTO) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        if (auth == null)
            return Response.status(401).build();
        List<Seat> seats = new ArrayList<>();

        try {
            em.getTransaction().begin();

            // check user
            TypedQuery<User> userQuery = em.createQuery("select u from User u where u.uuid = :uuid", User.class).
                    setParameter("uuid", UUID.fromString(auth.getValue()));
            User user = userQuery.getSingleResult();
            if (user == null)
                return Response.status(401).build();

            // check concert and dates
            long cId = bookingRequestDTO.getConcertId();
            LocalDateTime date = bookingRequestDTO.getDate();
            Concert c = em.find(Concert.class, cId);
            if (c == null || !c.getDates().contains(date))
                return Response.status(400).build();
            em.getTransaction().commit();

            // check seats
            em.getTransaction().begin();
            List<String> seatsToBooked = bookingRequestDTO.getSeatLabels();
            TypedQuery<Seat> seatQuery = em.createQuery("select s from Seat s where s.label IN :label and s.date = :date and s.isBooked = false ", Seat.class).
                    setParameter("label", bookingRequestDTO.getSeatLabels()).setParameter("date", bookingRequestDTO.getDate());
            seats = seatQuery.getResultList();
            if (seats.size() != seatsToBooked.size())
                return Response.status(403).build();

            // make booking
            Booking booking = new Booking(user, cId, date);
            for (Seat s: seats) {
                s.setIsBooked(true);
            }
            booking.getSeats().addAll(seats);
            em.persist(booking);


            int remainingSeats = em.createQuery("SELECT COUNT(seat) FROM Seat seat WHERE seat.date = :date AND seat.isBooked = false", Long.class)
                    .setParameter("date", bookingRequestDTO.getDate())
                    .getSingleResult()
                    .intValue();

            thread.submit(() -> {
                // Calculate booking percentage
                double numSeats = TheatreLayout.NUM_SEATS_IN_THEATRE;
                double bookingPercent = 1.0 - (remainingSeats / numSeats);
                // Iterate through subscriptions and send notifications if applicable
                for (ConcertSubscription sub : subscriptions.get(bookingRequestDTO.getDate())) {
                    if (bookingPercent >= sub.percent) {
                        sub.response.resume(Response.ok(new ConcertInfoNotificationDTO(remainingSeats)).build());
                    }
                }
            });

            em.getTransaction().commit();
//            this.notifyInfoToUser(bookingRequestDTO, seats.size());
            return Response.created(URI.create("/concert-service/bookings/" + booking.getId()))
                    .cookie(new NewCookie("auth", auth.getValue())).build();
        } catch (NoResultException e) {
            return Response.status(401).build();
        } finally {

            em.close();
        }
    }




    @GET
    @Path("/bookings/{id}")
    public Response getBookingbyId(@org.jboss.resteasy.annotations.jaxrs.PathParam("id") Long id, @CookieParam("auth") Cookie auth) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        if (auth == null)
            return Response.status(401).build();
        try {
            em.getTransaction().begin();
            TypedQuery<User> userQuery = em.createQuery("select u from User u where u.uuid = :uuid", User.class).
                    setParameter("uuid", UUID.fromString(auth.getValue()));
            User user = userQuery.getSingleResult();
            if (user == null)
                return Response.status(401).build();

            // get booking
            TypedQuery<Booking> bookingQuery = em.createQuery("select b from Booking b where b.id =: id", Booking.class).
                    setParameter("id", id);
            Booking booking = bookingQuery.getSingleResult();

            // check if the booking belongs to the user
            if (user != booking.getUser())
                return Response.status(403).build();

            // convert to bookingdto
            BookingDTO bookingDTO = BookingMapper.toDto(booking);
            return Response.ok(bookingDTO).build();

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
    }

    @GET
    @Path("bookings")
    public Response getallUserBookings(@CookieParam("auth") Cookie auth) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        if (auth == null)
            return Response.status(401).build();

        try {
            em.getTransaction().begin();

            // check user
            TypedQuery<User> userQuery = em.createQuery("select u from User u where u.uuid = :uuid", User.class).
                    setParameter("uuid", UUID.fromString(auth.getValue()));
            User user = userQuery.getSingleResult();
            if (user == null)
                return Response.status(401).build();

            // get booking
            List<BookingDTO> bookingDTOS = new ArrayList<>();
            for (Booking booking : user.getBookings()) {
                BookingDTO bookingDTO = BookingMapper.toDto(booking);
                bookingDTOS.add(bookingDTO);
            }
            return Response.ok().entity(bookingDTOS).build();

        } finally {
            em.getTransaction().commit();
            em.close();
        }

    }

    @GET
    @Path("/seats/{date}")
    public Response getSeats(@QueryParam("status") BookingStatus status, @org.jboss.resteasy.annotations.jaxrs.PathParam("date") LocalDateTimeParam date) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        List<Seat> seats;
        try {
            em.getTransaction().begin();

            // check dates
            List<SeatDTO> seatDTOS = new ArrayList<>();
            TypedQuery<Seat> seatQuery = em.createQuery("select s from Seat s where s.date= :date", Seat.class).
                    setParameter("date", date.getLocalDateTime());
            seats = seatQuery.getResultList();

            // check seats status
            for (Seat s: seats) {
                if (status == BookingStatus.Any)
                    seatDTOS.add(SeatMapper.toDto(s));
                if ((status == BookingStatus.Booked && s.seatIsBooked()) || (status == BookingStatus.Unbooked && !s.seatIsBooked()))
                    seatDTOS.add(SeatMapper.toDto(s));
            }
            return Response.ok().entity(seatDTOS).build();
        } finally {
            em.getTransaction().commit();
            em.close();
        }
    }


    @POST
    @Path("/subscribe/concertInfo")
    public void subscribe(@Suspended AsyncResponse asyncResponse, @CookieParam("auth") Cookie auth, ConcertInfoSubscriptionDTO subscription) {
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // If user not found
        if (auth == null) {
            asyncResponse.resume(Response.status(401).build());
            return;
        }

        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, subscription.getConcertId());
            if (concert == null || !concert.getDates().contains(subscription.getDate())) {
                asyncResponse.resume(Response.status(400).build());
                return;
            }



            em.getTransaction().commit();
        } finally {
            em.close();
        }synchronized (subscriptions) { // Race condition between the two statements
            if (!subscriptions.contains(subscription.getDate())) {
                subscriptions.put(subscription.getDate(), new LinkedList<>());
            }
        }
        subscriptions.get(subscription.getDate()).add(new ConcertSubscription(asyncResponse,subscription.getPercentageBooked()));
    }


}
