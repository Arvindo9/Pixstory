package com.app.pixstory;

import com.app.pixstory.utils.Utils;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UnitTest {

    @Test
    public void testIsMobileValid() {
        String testMobile = "9876543210";
        Assert.assertThat(String.format("Mobile validity test failed for %s", testMobile), Utils.isValidMobile(testMobile), is(true));
    }

    @Test
    public void testIsEmailValid(){
        String testEmail = "kamlesh@18pixels.com";
        Assert.assertThat(String.format("Email validity test failed for %s ", testEmail), Utils.checkEmailForValidity(testEmail), is(true));
    }
}
