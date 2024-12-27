package com.mercadolivro.enums

enum class Errors(val code: String,val message: String) {

    //error access
    ML000(code = "ML-000", message = "ACCESS DENIED"),

    //bad request generico
    ML001(code = "ML-001", message = "Invalid request"),
    ML002(code = "ML-002", message = "Missing property look documentation"),

    //bad request not readable
    ML301(code = "ML-301", message = "We cannot read the property,it is mandatory"),

    //tdoos erros referente  a livro vai ser de 100 a 199
    //com o %s eu ao passar .format() vai ser inserido dinamicamente
    ML101(code = "ML-101", message = "Book {%s} not exit"),
    ML102(code = "ML-102", message = "Can't update book with status {%s}"),

    //customers
    ML201(code = "ML-201", message = "Customer {%s} not exit")
}