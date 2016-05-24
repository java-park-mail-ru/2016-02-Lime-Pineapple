package db.services;

import db.models.User;
//import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
//import net.sf.hibernate.HibernateException;
//import net.sf.hibernate.MappingException;
//import db.services.impl.DBAccountServiceImpl;
import org.junit.Test;
import static org.junit.Assert.*;


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
        assertTrue(testedService.addUser(new User("User_test", "userpass")) != 0); //Правильные данные
        assertTrue(testedService.addUser(new User("User_test", "usert")) !=0); //Такой логин уже есть
        assertTrue(testedService.addUser(new User("User_test2", "u")) != 0);//Слабый пароль
        assertTrue(testedService.addUser(new User("", "usert")) !=0);//Пустой логин
    }

}
