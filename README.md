## Short description
Simple app implemented in Kotlin and Spring Boot

## Business motivation: 
Retrieve account balance in given currency

## Setup: 
Application runs on port 8123

## API used for retrieving exchange rates
http://api.nbp.pl/api/exchangerates/rates

## Sample usage: 
To retrieve account balance for accountId `123456` and currency `GBP` make a call: 

curl http://localhost:8123/accounts/123456/balance?currency=gbp

Expected response should have following structure: 
```
{
    "amount": 20919.27,
    "currency": "GBP"
}
```

## Input parameters

`accountId` should be a string containing exactly 6 digits e.g. `123456`

`currency` should be a string containing exactly 3 letters e.g. `USD`

### *TODO LIST*

- implement swagger
- integration test
- cache test
- security 
- change controller validation for annotations
- consider Customer entity and redesign bounded contexts

