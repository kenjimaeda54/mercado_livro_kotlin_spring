# Mercado Livro
API para marketplace de livros. Possível comprar livros, cadastrar usuários, vincular vários livros ao um usuário.


## Feature
- Ideal das repostas fornecidas ao cliente não serem iguais à entidade do banco, pois se futuramente desejar adicionar ou ocultar uma propriedade fica mais simples, pois está centrado em uma classe que não representa database
- Você pode usar as extensões para manipular, mas também existe a possibilidade de criarmos o nosso é fornecer a injeção dependência usando anotação Component


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

##

 - Para realizar a questão de autenticação no código usei o spring security
 - Para isso preciso implementar a classe UserNamePasswordAuthenticationFilter é passar o authenticationMager
 - Abaixo como implementar
 - Para fazer autenticação usamos o jwt, precisamos colocar uma chave para identificar no depara, nesse caso usamos o id o usuário.
 - [Para saber mais](https://jwt.io/introduction)
 
   
``` kotlin

//vai possuir dois metos que podemos sobescrever

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

     //esse mmetodo e na tentaiva de autenticar se falhar iremos disparar um erro
     //attempAuthenticaion


    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val attemptLogin = jacksonObjectMapper().readValue(request.inputStream, LoginCustomerRequest::class.java)
            val id = customerRepository.findByEmail(attemptLogin.email)?.id
            //estou usando o id para não ficar pasando dado sensivel
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
}


```

##

- Para filtrar qual o token da request é permitir autorização implementei o BasicAuthenticationFilter.
- Para aplicação funcionar esperamos seja fornecido Authorization Bearer (token dinâmico), por isso faço autorizaton.startsWith("Bearer") assim garanto que existe um token.
- A função getAuth é para verificar se o token está correto.
- JwUtil e uma implementação minha que vai construir o token e também verificar se valido.
  

```kotlin

class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userCustomDetailsService: UserCustomDetailsService
): BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
         val authorization = request.getHeader("Authorization")
         if(authorization != null && authorization.startsWith("Bearer ")) {
             val auth = getAuth(authorization.split(" ")[1])
             SecurityContextHolder.getContext().authentication = auth
         }
         chain.doFilter(request,response)
    }

    private fun  getAuth(token: String): UsernamePasswordAuthenticationToken {
        if(jwtUtil.isInvalidToken(token)) {
            throw AuthenticationException(message = "Token Invalid", httpCode = 999)
        }
        val subject = jwtUtil.getId(token)
        val customer = userCustomDetailsService.loadUserByUsername(subject)
            //esse token tambem interfere na validdaçao do preautorizze
        return UsernamePasswordAuthenticationToken(customer,null,customer.authorities)
    }


}

```
##

- Para pegar as propriedades do application.yaml posso usar annotação @Value

``` kotlin
// no application.yaml

jwt:
   secret: valor sem aspas     
   expiration: valor sem aspas

//na aplicaçao

//pegando do application
@Value("\${jwt.secret}")
private val secretKey: String? = null

@Value("\${jwt.expiration}")
private val expiration: Long? = null

```


##

- Alumas dicas valiosas abaixo
- Caso preciso determinar que um usuário só pode ter acesso ao eu recurso posso usar anotação @UserCanOnlyAccessYourResource
- Trabalhei com migrations no banco de dados
- Usando migrations e útil, pois assim versiono o banco de dados garantido que toda vez que aplicação subir, estará com o banco com as versões que desejamos
- Depois implementando o migrations não posso alterar diretamente no banco, mas apenas no código

```kotlin

//precisa ser essas pastas dentro do resources

   db.migration
     | ==> suas migrations


//como conetar com o banco usando jpa

spring:
  jpa:
    hibernate:
      dll-auto: update
  datasource:
    url: jdbc:mysql://<url>/<database>?createDatabaseIfNotExist=true
    username: usuario
    password: senha'


```

##

### Como usar o repositorio
- Preciso que tenha um servidor mysql rodando na máquina
- Dentro do application.yml implementa o datasource com a URL do banco, usuário é senha
- Após isto só usar o Intellij para iniciar aplicação

```kotlin

  jpa:
    hibernate:
      dll-auto: update
  datasource:
    url: jdbc:mysql://<url>/<database>?createDatabaseIfNotExist=true
    username: usuario
    password: senha'


```
