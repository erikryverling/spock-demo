package se.yverling.demo.spock.bankaccount;

import spock.lang.Specification
import static se.yverling.demo.spock.bankaccount.IdGenerator.Type;

class BankAccountServiceSpecification extends Specification {
    final UUID = "444b378f-bf1c-4878-86e7-ca27c77a7414"
    final NUMERIC_ID = "87"
    final DEFAULT_ID = UUID

    def generatorMock = Mock(IdGenerator)
    def service = new BankAccountService(generatorMock)


    def "Should create bank account successfully"() {
        when:
        service.createBankAccount(UUID)

        then:
        notThrown(IllegalArgumentException)
    }

    def "Should generate bank account with default id successfully"() {
        given:
        1 * generatorMock.generate() >> DEFAULT_ID

        when:
        service.generateBankAccount()

        then:
        def account = service.getBankAccount(DEFAULT_ID)

        account instanceof BankAccount
        account.id == DEFAULT_ID
    }

    def "Should generate bank account with UUID successfully"() {
        given:
        1 * generatorMock.generate(Type.UUID) >> UUID

        when:
        service.generateBankAccount(Type.UUID)

        then:
        def account = service.getBankAccount(UUID)

        account instanceof BankAccount
        account.id == UUID
    }

    def "Should generate bank account with numeric id successfully"() {
        given:
        1 * generatorMock.generate(Type.NUMERIC) >> NUMERIC_ID

        when:
        service.generateBankAccount(Type.NUMERIC)

        then:
        def account = service.getBankAccount(NUMERIC_ID)

        account instanceof BankAccount
        account.id == NUMERIC_ID
    }

    def "Should get bank account by IBAN successfully"() {
        given:
        service.createBankAccount(UUID)

        when:
        def account = service.getBankAccount(UUID)

        then:
        account instanceof BankAccount
        account.id == UUID
    }

    def "Should throw exception when trying to create duplicate bank accounts"() {
        when:
        service.createBankAccount(UUID);
        service.createBankAccount(UUID);

        then:
        thrown(IllegalArgumentException)
    }


    def "Should throw exception if bank account is not found by IBAN"() {
        when:
        service.getBankAccount(UUID)

        then:
        thrown(IllegalArgumentException)
    }
}