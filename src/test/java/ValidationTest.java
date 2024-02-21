import edu.occ.occbio.Validation;
import edu.occ.occbio.database.DatabaseUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    @BeforeAll
    static void databaseSetup(){
        DatabaseUtility.connectToDatabase();
    }

    @Test
    void userIdShouldBeFourDigits(){
        assertEquals(true, Validation.validateUserIdDigits("1234"));
        assertEquals(false, Validation.validateUserIdDigits("123"));
    }

    @Test
    void isThereDuplicatUserId(){
        // user id 1111 exists in the database but not 1234
        assertTrue(Validation.isThereDuplicateUserId("1111"));
        assertFalse(Validation.isThereDuplicateUserId("1234"));
    }

    @Test
    void isThisModelAvailable(){
        // model id 1 is available in the database but not 10
        assertTrue(Validation.isThisModelAvailable("1"));
        assertFalse(Validation.isThisModelAvailable("10"));
    }

    @Test
    void DoesThisModelExist(){
        // model id 1 exists in the database but not 1099
        assertTrue(Validation.doesModelexist("1"));
        assertFalse(Validation.doesModelexist("1099"));
    }

    @Test
    void IsStudentREgistered(){
        // user id 3000 is registered in the database but not 9999
        assertTrue(Validation.isStudnetRegistered("3000"));
        assertFalse(Validation.isStudnetRegistered("9999"));
    }

    @Test
    void IsStudentSuspended(){
        // user id 3005 is suspended but not 3006
        assertTrue(Validation.isStudentSuspended("3005"));
        assertFalse(Validation.isStudentSuspended("3006"));
    }

    @Test
    void didStudentRentAlready(){
        // user id 3002 already rented a model but not 3008
        assertTrue(Validation.didStudentRentAlready("3002"));
        assertFalse(Validation.isStudentSuspended("3008"));
    }

    @Test
    void didStudentLendThisModel(){
        // user id 3002 rented model id 5. user id 3002 did not rent model id 6.
        assertTrue(Validation.didStudentLendThisModel("3002","5"));
        assertFalse(Validation.didStudentLendThisModel("3002","6"));
    }

}
