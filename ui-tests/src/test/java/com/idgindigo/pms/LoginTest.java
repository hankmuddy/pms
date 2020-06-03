package com.idgindigo.pms;

import com.idgindigo.pms.requirements.Application;
import com.idgindigo.pms.steps.LoginSteps;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.ManagedPages;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.pages.Pages;
import net.thucydides.junit.runners.ThucydidesRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

/**
 * @author VMelymuka
 * @since 31.07.13 16:44
 */
@Story(Application.Login.class)
@RunWith(ThucydidesRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    //    @ManagedPages(defaultUrl = "http://localhost:8080/app/login")// only this url matters
    @ManagedPages(defaultUrl = "http://88.198.41.177:8081/app/login")// only this url matters
    public Pages pages;

    @Steps
    public LoginSteps loginSteps;

    @Issue("#WIKI-1")
    @Test
    public void enteringValidAdminCredentialsShouldDisplayHotelListPage() {
        loginSteps.atLoginPage();
//        loginSteps.logins("admin", "password");
        loginSteps.adminLogins("admin", "ptnpnh2014");
        loginSteps.adminShouldSee("Hotels");
    }

    @Issue("#WIKI-2")
    @Test
    public void enteringValidHotelCredentialsShouldDisplayLoading() {
        loginSteps.atLoginPage();
        loginSteps.hotelLogins("test1", "test1", "password");
        loginSteps.userShouldSee("НАСТРОЙКИ");
    }

    @Issue("#WIKI-3")
    @Test
    public void errorMessageShouldAppear() {
        loginSteps.atLoginPage();
        loginSteps.hotelLogins("hotel", "login", "password");
        loginSteps.userShouldSee("");
    }
}
