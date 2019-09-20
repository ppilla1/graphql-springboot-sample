# graphql-springboot-sample

### Swagger UI ###
http://localhost:8080/swagger-ui.html

### Sample GraphQL Queries ###

#1#

{
    allBooks{
        isn
        authors
    }
}

#2#

{
    books(id: "efe59fe4-50d2-4245-aa4c-a3e14b4040c3"){
      title
      authors
    }
}

#3#

{
    allBooks{
        isn
        authors
    }

    books(id: "efe59fe4-50d2-4245-aa4c-a3e14b4040c3"){
      title
      authors
    }

}