## This is an API for flight tickets client app in FlightManagerClient repo
## Short decription
Tech stack: Java 15, Spring MVC, Spring Data, Hibernate, Spring Security, Lombok, JUnit, Mockito, PostgreSQL.
Allowed operations for users are defined by jwt token info about roles. There are 2 roles: ROLE_USER and ROLE_ADMIN. Simple users can only make get requests to watch flights, flights info and buy new tickets. Admins can make all CRUD operations with flight and flight info, but they can not buy tickets.

Endpoints:

	for Flights:
| URL | Description | 
|:---|:---|
| /flights/add | add flight |
| /flights/get | get flights |
| /flights/delete/{id} | delete flight by id and it's fligth info too |
| /flights/change/{id} | change flight data(departure, destination, image) |
	
	for Flight_Info(class with more detailed info about flight):
| URL | Description | 
|:---|:---|
| /flightinfo/{id} | add flight info for flight with id=:id |
| /flightinfo/get/{id} | get flight info for flight |
| /flightinfo/get-flight-info-between/{id} | get all flight info's between 2 params(minimal flight cost and maximum flight cost) |
| /flightinfo/delete/{id} | "/flightinfo/delete/{id}" - delete flight info for flight |
| /flightinfo/change/{id} | change flight info for flight |
	
	for Ticket:
| URL | Description | 
|:---|:---|
| /buy-ticket/{id} | add ticket to db. It's contains user's, flight and flight info data. If real mail was provided during registration you will receive created ticket |
	
	for Users:
| URL | Description | 
|:---|:---|
| /users/get | get registered users |
| /signup | sign up with username and password |
| /role/add | add new role to db |
| /role/addtouser | add new role to user by username |
| /token/refresh | refresh jwt token if access token is out of date |
	
P.S date format in flight_info is "yyyy-MM-dd HH:mm". To type date use something like "2023-04-05 14:00". If typed date is expired flight info will not be added. If flight is added server checks if it's stll relevant and delete flight info if it expired with every "/flightinfo/get/{id}" request.

P.S do not upload big files - server can decline such note. Available formats are jpeg, jpg, png.

## You can run client, database and server locally via Docker
just follow this commands:

docker pull gleb647/dockerized-backend\
docker pull gleb647/dockerized-client\
docker pull gleb647/postgres

docker run -p 3000:3000 gleb647/dockerized-client\
docker run --name postgres -e POSTGRES_PASSWORD=1234 -p 5432:5432 gleb647/postgres\
docker volume create static\
docker run -p 8080:8080 -v static:/data -e TZ=Europe/Moscow gleb647/dockerized-backend
