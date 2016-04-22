package server;

import db.models.User;
import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class AccountServiceTest {

    private AccountService testedService = new ExampleAccountService();
 /*   @Test
    boolean databaseConnectionTest() throws MappingException, HibernateException{
        try {
            return testedService.testConnect();
        }
        catch (MappingException e) {
            return false;
        }
        catch (HibernateException e1) {
            return false;
        }
    }*/
    @Test
    boolean addUserTest() {
        boolean passed;
        passed = testedService.addUser(new User("User_test", "userpass")); //Правильные данные
        passed = (!testedService.addUser(new User("User_test", "usert")) && passed); //Такой логин уже есть
        passed = (!testedService.addUser(new User("User_test2", "u")) && passed);//Слабый пароль
        passed = (!testedService.addUser(new User("", "usert")) && passed);//Пустой логин
        return passed;
    }

}
