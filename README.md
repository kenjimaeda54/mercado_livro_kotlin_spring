# Mercado Livro
API para marktplace de livros. Possivel comprar livros,cadastrar usuarios,vincular varios livros ao um usuario.

## Feature
- Ideal das repostas fornecidas ao cliente nao serem iguais a entidade do banco, pois assim se futuramente desejar adiiconar ou ocultar uma propriedade fica mais simples, pois esta centrado em uma classe que nao represeta database
- Voce pode usar as extensions para manipular,mas tambem existe a possibilidade de criarmos o nosso é fornecer a injeçao dependencia usando anotaçao Component

```kotlin
@Component
class PurchaseMapper(
    private  val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toModel(request: PostPurchaseRequest): PurchaseModel  {
        val books = bookService.findAllBooksId(request.booksId)
        val customer = customerService.getOnlyCustomerById(request.customerId)

        return PurchaseModel(
            price = books.sumOf { it.price },
            customer = customer,
            books = books
        )
    }

}

```
