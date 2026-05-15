package com.api.pocproductsapi.service

import com.api.pocproductsapi.dto.product.CreateProductRequest
import com.api.pocproductsapi.entity.Product
import com.api.pocproductsapi.entity.User
import com.api.pocproductsapi.repository.ProductRepository
import com.api.pocproductsapi.repository.UserRepository
import com.api.pocproductsapi.service.auth.AuthorizationService
import com.api.pocproductsapi.exception.ForbiddenException
import com.api.pocproductsapi.exception.ProductCodeExistsException

import spock.lang.Specification

class ProductServiceSpec extends Specification {

    ProductRepository repository = Mock()
    UserRepository userRepository = Mock()
    AuthorizationService authorizationService = Mock()

    ProductService service = new ProductService(repository,
            userRepository,
            authorizationService)

    def "should create product successfully"() {

        given:
        def request = new CreateProductRequest(name: "Laptop",
                code: "LP-001",
                description: "Gaming laptop",
                price: BigDecimal.valueOf(1200),
                imageUr: "image.png")

        def user = new User(email: "admin@test.com")

        when:
        def response = service.create(request, "admin@test.com")

        then:
        1 * userRepository.findByEmail("admin@test.com") >> Optional.of(user)

        1 * authorizationService.isAdmin(user) >> true

        1 * repository.existsByCode("LP-001") >> false

        1 * repository.save(_ as Product) >> { Product product ->
            product.id = 1
            product
        }

        response.name == "Laptop"
        response.code == "LP-001"
        response.description == "Gaming laptop"
    }

    def "should throw forbidden exception when user is not admin"() {

        given:
        def request = new CreateProductRequest(name: "Laptop",
                code: "LP-001")

        def user = new User(email: "user@test.com")

        when:
        service.create(request, "user@test.com")

        then:
        1 * userRepository.findByEmail("user@test.com") >> Optional.of(user)

        1 * authorizationService.isAdmin(user) >> false

        thrown(ForbiddenException)
    }

    def "should throw exception when product code already exists"() {

        given:
        def request = new CreateProductRequest(name: "Laptop",
                code: "LP-001")

        def user = new User(email: "admin@test.com")

        when:
        service.create(request, "admin@test.com")

        then:
        1 * userRepository.findByEmail("admin@test.com") >> Optional.of(user)

        1 * authorizationService.isAdmin(user) >> true

        1 * repository.existsByCode("LP-001") >> true

        thrown(ProductCodeExistsException)
    }

    def "should return product by id"() {

        given:
        def product = Product.builder()
                .id(1)
                .name("Laptop")
                .code("LP-001")
                .description("Gaming laptop")
                .price(BigDecimal.valueOf(1200))
                .build()

        when:
        def response = service.getById(1)

        then:
        1 * repository.findById(1) >> Optional.of(product)

        response.id == 1
        response.name == "Laptop"
        response.code == "LP-001"
    }

    def "should delete product successfully"() {

        given:
        def user = new User(email: "admin@test.com")

        when:
        service.deleteProduct(1, "admin@test.com")

        then:
        1 * userRepository.findByEmail("admin@test.com") >> Optional.of(user)

        1 * authorizationService.isAdmin(user) >> true

        1 * repository.deleteById(1)
    }

    def "should throw forbidden exception when deleting product as non-admin"() {

        given:
        def user = new User(email: "user@test.com")

        when:
        service.deleteProduct(1, "user@test.com")

        then:
        1 * userRepository.findByEmail("user@test.com") >> Optional.of(user)

        1 * authorizationService.isAdmin(user) >> false

        thrown(ForbiddenException)
    }
}