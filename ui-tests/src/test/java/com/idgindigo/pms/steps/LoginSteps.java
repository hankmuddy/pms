package com.idgindigo.pms.steps;

import com.idgindigo.pms.pages.LoginPage;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author VMelymuka
 * @since 31.07.13 16:37
 */
public class LoginSteps extends ScenarioSteps {
    LoginPage loginPage;

    public LoginSteps(Pages pages) {
        super(pages);
    }

    @Step
    public void enters(String hotelId, String login, String password) {
        if (hotelId != null) {
            loginPage.enterHotelId(hotelId);
        }
        loginPage.enterLogin(login);
        loginPage.enterPassword(password);
    }

    @Step
    public void doesLogin() {
        loginPage.submitLoginForm();
    }

    @Step
    public void adminShouldSee(String definition) {
        assertThat(loginPage.getAdminDefinitions()).contains(definition);
    }

    @Step
    public void userShouldSee(String definition) {
        assertThat(loginPage.getHotelDefinitions()).contains(definition);
    }

    @Step
    public void atLoginPage() {
        loginPage.open();
    }

    @Step
    public void adminLogins(String login, String password) {
        enters(null, login, password);
        doesLogin();
    }

    @Step
    public void userLogins(String hotelId, String login, String password) {
        enters(hotelId, login, password);
        doesLogin();
    }

    @Step
    public void hotelLogins(String hotelId, String login, String password) {
        enters(hotelId, login, password);
        doesLogin();
    }
}
