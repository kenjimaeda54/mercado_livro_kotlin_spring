package com.mercadolivro.enums

enum class Errors(val code: String,val message: String) {
    //tdoos erros referente  a livro vai ser de 100 a 199
    //com o %s eu ao passar .format() vai ser inserido dinamicamente
    ML101(code = "ML-101", message = "Book {%s} not exit"),
    ML102(code = "ML-102", message = "Can't update book with status {%s}"),

    //customers
    ML201(code = "ML-201", message = "Customer {%s} not exit")
}