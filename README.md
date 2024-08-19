# Funds-Transfer

```
Endpoints

1. Create Account

	•	Endpoint: /accounts
	•	Method: POST
	•	Request Body:
	•	Content-Type: application/json
	•	Schema:

{
"userName": "string",
"accountType": "string",
"balance": "number",
"currency": "string"
}
```
```
Description: This endpoint creates a new account

	•	Response:
	•	Status Code: 201 Created
	•	Body:


{
"id": "long",
"accountNumber": "string",
"accountName": "string",
"accountType": "string",
"balance": "number",
"createdDate": "string",
"lastModifiedDate": "string"
}
```
