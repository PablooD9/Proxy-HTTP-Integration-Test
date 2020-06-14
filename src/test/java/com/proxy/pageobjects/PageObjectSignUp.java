package com.proxy.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectSignUp extends PageObject {
	
	static public void fillForm(WebDriver driver, String namep, String emailp, String passp) 
	{
		WebElement usuario = driver.findElement(By.name("name"));
		usuario.click();
		usuario.clear(); // limpia el campo usuario
		usuario.sendKeys( namep ); // copia "namep" en el campo usuario
		
		WebElement email = driver.findElement(By.name("email"));
		email.click();
		email.clear();
		email.sendKeys( emailp );
		
		WebElement pass = driver.findElement(By.name("password"));
		pass.click();
		pass.clear();
		pass.sendKeys( passp );
		
		// Pulsar el boton de Registro
		By boton = By.id("register-button");
		driver.findElement(boton).click();
	}
	
}
