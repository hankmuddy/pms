package com.idgindigo.pms.pages;

import ch.lambdaj.function.convert.Converter;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ch.lambdaj.Lambda.convert;

/**
 * @author VMelymuka
 * @since 01.08.13 13:06
 */
//@DefaultUrl("http://88.198.41.177:8081/app/login")//url does not matter
public class LoginPage extends PageObject {
    private Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @FindBy(name = "hotel")
    private WebElement hotelIdField;

    @FindBy(name = "login")
    private WebElement emailField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(name = "submit")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterHotelId(String keyword) {
        element(hotelIdField).type(keyword);
    }

    public void enterLogin(String keyword) {
        element(emailField).type(keyword);
    }

    public void enterPassword(String keyword) {
        element(passwordField).type(keyword);
    }

    public void submitLoginForm() {
        element(submitButton).click();
    }

    public List<String> getAdminDefinitions() {
        List<WebElement> results;
        results = getDriver().findElements(By.xpath("//a[contains(@class,'x-tab')]"));
        for (WebElement result : results) {
            System.out.println("result.getText() = " + result.getText());
        }
//        for (WebElement result : results=getDriver().findElements(By.xpath("//span[@unselectable='on']"))) {
//            System.out.println("//span[@unselectable='on'] => " + result.getText());
//        }

        logger.info("Results = " + results);
        return convert(results, toStrings());
    }

    public List<String> getHotelDefinitions() {
        List<WebElement> results;
        results = getDriver().findElements(By.xpath("//*[@id=\"ext-comp-1023-btnInnerEl\"]"));

        for (WebElement result : results) {
            System.out.println("result.getText() = " + result.getText());
        }

        logger.info("Results = " + results);
        return convert(results, toStrings());
    }

    private Converter<WebElement, String> toStrings() {
        return new Converter<WebElement, String>() {
            public String convert(WebElement from) {
                return from.getText();
            }
        };
    }
}


