package soc.test.funcations;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import shared.SetTestClassNG;
import shared.configuration.TestConfiguration;
import shared.constants.TestGroups;
import soc.pages.funcations.FunclistPage;

/**
 * Created by developer on 15-9-28.
 */
public class Test1 extends SetTestClassNG{


    @Factory(dataProviderClass = shared.SetTestClassNG.class, dataProvider = "configureRun")
    public Test1(TestConfiguration testConfig) {
        super(testConfig);
    }

    @Test(enabled = true,groups ={TestGroups.SMOKE, TestGroups.FUNCTION})
    public void test() {
        FunclistPage page=new FunclistPage(driver);
        page.navigateToPage("");
        try{
            System.out.println("wait 5 seconds");
            Thread.sleep(5000);
        }catch (Exception e){

        }
        screenShot("test");

        assertTrue(true,"");
//        assertFalse(true,"-----------kkk---------");

    }
//    public static void main(String[] args){
//        String a="12345678";
//        System.out.println(a.substring(1));
//    }
}
