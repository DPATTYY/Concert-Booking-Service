# Summary of Team Contribution and Discussion

Team Members:

Name:   Jr Prospero UPI:    rpro506         Github: @rpro506
Name:   Dharm Patel UPI:    dpat356         Github: @DPATTY-OG 
Name:   Saurav Krishnakumar UPI: skri486    Github: @SauravK12


-> At the start of the project as a whole team we had a meeting to dicuss how to implement the domain models, dto mappers, DTO's and concert resouces and was put into issues tab in githyub

-> Through this discussion we broke the tasks up in the following:

    -> Jr: 
        -> Define the attributes and methods for each class
        -> Create mapper class for perfromer
        -> Define REST endpoints for creating, updating, deleting, and retrieving concert details, resverations, and performer information
        -> Use apportiate HTTP methods (GET, POST, PUT, DELETE) and status codes
    
    ->  Dharm:
            -> Create intial tasks on issues
            -> Ensured relationships between entities are correctly represnted and implement the rest of the classes
            -> Create mapper class for Concert
            -> Implement data transfer objects to manage data exchanges between the client and the sever
    
    -> Saurav:
            -> Annotate classes with JPA annotations and implment the other classes
            -> Create a conceptual diagram of the model to visualise enity relationships
            -> Create mapper class for Booking and Seat
            -> Ensure endpoints are logically organised


-> Note: 

    -> Throughout the development process teammates very not limited to their tasks as we all helped each other whenver aid was needed weather it be bugs or how to approach the problem.
    -> The development of this project was primarily done in person at university, therefore we did not have alot of commication on Github and the issues tab. However for anything else we had discord channel in case we need to communicate in the off chance we were not
    collaborating on campus.

# Strategies we used to reduce the change of concurrency errors in program execution

-> In the methods of the ConcertResource class, we utilised the setLockMode method while creating queries in the entity manager to prevent concurrency errors.

-> The use of pessimistic lock modes ensures that the persistence manager acquires long-term read or write locks on the database data linked to the entity state. This approach effectively prevents concurrency issues.

-> We used cookies to manage user sessions to reduce potential interference between concurrent user interactions, specifically with double booking.

-> Use of database transactions such as; em.getTransaction().begin(), and em.getTransaction().commit(). Ensuring all operations during a transaction completes, or none of them completes.git 


# How the domain model is organised

->We established a relationship between the Concert and Performer classes in the domain model. This was mapped to the entity using the @JoinTable annotation. We defined a many-to-many relationship between these two tables using the @ManyToMany annotation.

->We applied FetchType.LAZY to enhance performance by preventing unnecessary data fetching.

->We utilised CascadeType.PERSIST to save associated entities when creating one-to-many or many-to-many relationships between tables.

->A new Booking domain model class was created, which includes attributes necessary for the booking service of the resource class. It also stores seat, concert ID, and user information.

-> In concert class we etablished a  many-to-many relationship between concert and performers as a join table, also had lazy fetchtype to save loading and persist so it can get the data for both entities.

->All attributes in the relevant DTOs are saved in the domain model class.  

->In a whole we use JPA annotations for fetching strategies, cascading behaviours, and entity relationships.

->To sum up, JPA annotations are used by the domain model to specify entity associations, fetching tactics, and cascade behaviours. Our choices regarding eager vs. lazy fetching are acceptable for each entity's intended purpose. Constructors, method overrides, and appropriate encapsulation all improve overall quality. All in all, the domain model offers our concert service application a strong base, with distinct relationships between entities and effective methods for accessing data.