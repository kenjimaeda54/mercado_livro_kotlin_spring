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

 - Para realizar a questao de autenticaçao no codigo useii o spring security
 - Para isso preciso implementar a classe UserNamePasswordAuthenticationFilter é passar o authenticationMager
 - Abaixo como implementar
 - Para fazer authenticacao usamos o jwt ele precisa de algo para identificar o usuario nesse caso estamos recuperando o seu id e passando atraves do UserNamePasswordAuthenticationToken essa classe que auxilira criar o token para jwtt
 - 
   
``` kotlin

//vai possuir dois metos que podemos sobescrever

//esse mmetodo e na tentaiva de autenticar se falhar iremos disparar um erro
//attempAuthenticaion

override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try 
            val attemptLogin = jacksonObjectMapper().readValue(request.inputStream, LoginCustomerRequest::class.java)
            val id = customerRepository.findByEmail(attemptLogin.email)?.id
            val authToken = UsernamePasswordAuthenticationToken(id, attemptLogin.password)
            return authenticationManager.authenticate(authToken)
        } catch (exception: AuthenticationException) {
            throw AuthenticationException(message = "Failed attempt login", httpCode = HttpStatus.BAD_REQUEST.value())
        }

    }


//esse metodo e chamado apos o  sucesso e inserimos o token na rota para fazzer depara com o token colocado nos endpoints 
  override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
       val id = (authResult.principal as UserCustomDetails).username.toInt()
       val token  = jwtUtil.generateToken(id)


       response.addHeader("Authorization","Bearer $token")
    }

```
