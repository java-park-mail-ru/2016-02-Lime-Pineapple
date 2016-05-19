package db.services;

import db.models.User;
import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
//import net.sf.hibernate.HibernateException;
//import net.sf.hibernate.MappingException;
import db.services.impl.DBAccountServiceImpl;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService testedService = new DBAccountServiceImpl();
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
    // TODO: split into smaller tests
    @Test
    public void addUserTest() {
        boolean passed;
        passed = testedService.addUser(new User("User_test", "userpass")) != 0; //Правильные данные
        passed = (testedService.addUser(new User("User_test", "usert")) !=0 && passed); //Такой логин уже есть
        passed = (testedService.addUser(new User("User_test2", "u")) != 0 && passed);//Слабый пароль
        passed = (testedService.addUser(new User("", "usert")) !=0 && passed);//Пустой логин
        assertTrue("addUserTest hasn't passed the test", passed);
    }

}
