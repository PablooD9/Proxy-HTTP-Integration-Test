package com.proxy.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectLogin extends PageObject {
	
	static public void fillForm(WebDriver driver, String usuariop, String passwordp) 
	{
		WebElement usuario = driver.findElement(By.name("username"));
		usuario.click();
		usuario.clear(); // limpia el campo usuario
		usuario.sendKeys( usuariop ); // copia "usuariop" en el campo usuario
		
		WebElement password = driver.findElement(By.name("password"));
		password.click();
		password.clear();
		password.sendKeys( passwordp );
		
		// Pulsar el boton de Login.
		By boton = By.id("login-button");
		driver.findElement(boton).click();
	}
	
}
