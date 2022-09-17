# API Documentation 

## `GET /users`

Fetch all users.


## `POST /sign_up`

Create a new user.

##### PARAMS:

*  **`user`**


## `POST /login`

Authenticate user.

##### PARAMS:

*  **`user`**


## `GET /users/{id}`

Returns a user with the given id.

##### PARAMS:

*  **`id`** value must be an integer.


## `DELETE /users/{id}`

Deletes the user with the given id.

##### PARAMS:

*  **`id`** value must be an integer.


## `GET /scores`

Fetch all scores.


## `GET /scores/weekly`

Returns scores of last 7 days.


## `POST /scores/{id}`

Create a new score for the user with the given id.

##### PARAMS:

*  **`score`**
*  **`id`** value must be an integer.



## `DELETE /scores/{id}`

Deletes the score with the given id.

##### PARAMS:

*  **`id`** value must be an integer.
