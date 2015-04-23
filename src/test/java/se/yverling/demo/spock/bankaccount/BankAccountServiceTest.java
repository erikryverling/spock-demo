package se.yverling.demo.spock.bankaccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.spockframework.util.Assert.fail;
import static se.yverling.demo.spock.bankaccount.IdGenerator.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BankAccountServiceTest {
    private static final String UUID = "444b378f-bf1c-4878-86e7-ca27c77a7414";
    private static final String NUMERIC_ID = "87";
    private static final String DEFAULT_ID = UUID;

    @Mock
    IdGenerator generatorMock;

    BankAccountService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service = new BankAccountService(generatorMock);
    }

    @Test
    public void shouldCreateBankAccountSuccessfully() {
        try {
            service.createBankAccount(UUID);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException was thrown");
        }
    }

    @Test
    public void shouldGenerateBankAccountWithDefaultIdSuccessfully() {
        when(generatorMock.generate()).thenReturn(DEFAULT_ID);

        service.generateBankAccount();

        verify(generatorMock, times(1)).generate();

        BankAccount bankAccount = service.getBankAccount(DEFAULT_ID);

        assertThat(bankAccount).isInstanceOf(BankAccount.class);
        assertThat(bankAccount.getId()).isEqualTo(DEFAULT_ID);
    }

    @Test
    public void shouldGenerateBankAccountWithUuidSuccessfully() {
        when(generatorMock.generate(eq(Type.UUID))).thenReturn(UUID);

        service.generateBankAccount(Type.UUID);

        verify(generatorMock, times(1)).generate(eq(Type.UUID));

        BankAccount bankAccount = service.getBankAccount(UUID);

        assertThat(bankAccount).isInstanceOf(BankAccount.class);
        assertThat(bankAccount.getId()).isEqualTo(UUID);
    }

    @Test
    public void shouldGenerateBankAccountWithNumericIdSuccessfully() {
        when(generatorMock.generate(eq(Type.NUMERIC))).thenReturn(NUMERIC_ID);

        service.generateBankAccount(Type.NUMERIC);

        verify(generatorMock, times(1)).generate(eq(Type.NUMERIC));

        BankAccount bankAccount = service.getBankAccount(NUMERIC_ID);

        assertThat(bankAccount).isInstanceOf(BankAccount.class);
        assertThat(bankAccount.getId()).isEqualTo(NUMERIC_ID);
    }

    @Test
    public void shouldGetBankAccountByIbanSuccessfully() {
        service.createBankAccount(UUID);

        BankAccount bankAccount = service.getBankAccount(UUID);

        assertThat(bankAccount).isInstanceOf(BankAccount.class);
        assertThat(bankAccount.getId()).isEqualTo(UUID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTryingToCreateDuplicateBankAccounts() {
        service.createBankAccount(UUID);
        service.createBankAccount(UUID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfBankAccountIsNotFoundByIban() {
        service.getBankAccount(UUID);
    }
}