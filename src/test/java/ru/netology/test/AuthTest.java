package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGeneratorForRegistration.UserInfo;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.API.signUp;
import static ru.netology.data.DataGeneratorForRegistration.*;

public class AuthTest {

    UserInfo generator = getUserInfo();

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    public void registration(String login, String password) {
        $("[name=login]").setValue(login);
        $("[name=password]").setValue(password);
        $(".button").click();
    }

    @Test
    public void shouldSignInIfExistentUser() {

        signUp(generator);
        registration(generator.getLogin(), generator.getPassword());
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    public void shouldNotSignInIfNonExistentUser() {

        registration(generator.getLogin(), generator.getPassword());
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    public void shouldNotSignInIfBlockedUser() {

        generator.setStatus("blocked");
        signUp(generator);
        registration(generator.getLogin(), generator.getPassword());
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! " + "Пользователь заблокирован"))
                .shouldBe(visible);
    }

    @Test
    public void shouldNotSignInIfInvalidLogin() {

        signUp(generator);
        registration(getInvalidLogin(), generator.getPassword());
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible);
    }

    @Test
    public void shouldNotSignInIfInvalidPassword() {

        signUp(generator);
        registration(generator.getLogin(), getInvalidPassword());
        $(".notification__title").shouldHave(text("Ошибка"))
                .shouldBe(visible);
        $(".notification__content").shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible);
    }
}
