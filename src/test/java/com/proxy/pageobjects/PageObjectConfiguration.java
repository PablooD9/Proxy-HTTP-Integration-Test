package com.proxy.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PageObjectConfiguration extends PageObject {

	static public void fillBasicOptions(WebDriver driver, String OS, String browser, boolean spHosts, boolean malHosts, boolean trackHosts, boolean pornHosts) {
		Select selectOS = new Select(driver.findElement(By.id("op1_os")));
		selectOS.selectByVisibleText(OS);
		
		Select selectBrowser = new Select(driver.findElement(By.id("op1_browser")));
		selectBrowser.selectByVisibleText(browser);
		
		WebElement element = driver.findElement(By.id("op2"));
		if (element.isSelected() != spHosts) {
			checkCheckbox(driver, element, spHosts);
		}
		element = driver.findElement(By.id("op3"));
		if (element.isSelected() != malHosts) {
			checkCheckbox(driver, element, malHosts);
		}
		element = driver.findElement(By.id("op4"));
		if (element.isSelected() != trackHosts) {
			checkCheckbox(driver, element, trackHosts);
		}
		element = driver.findElement(By.id("op5"));
		if (element.isSelected() != pornHosts) {
			checkCheckbox(driver, element, pornHosts);
		}
		
		// Pulsar el botón para guardar preferencias.
		By boton = By.id("savePrefState1");
		if (driver.findElement(boton).getText().contains("Guardar configuración")) { // Si hay cambios...
			driver.findElement(boton).click();
		}
	}
	
	static public void fillAdvancedOptions(WebDriver driver, List<String> securityHeaders, boolean delCookies) {
		if (securityHeaders != null) {
			Select select = new Select(driver.findElement(By.id("op6")));
			for (String secHeader : securityHeaders) {
				select.selectByVisibleText(secHeader);
			}
		}
		
		WebElement element = driver.findElement(By.id("op7"));
		if (element.isSelected() != delCookies) {
			checkCheckbox(driver, element, delCookies);
		}
		
		// Pulsar el botón para guardar preferencias.
		By boton = By.id("savePrefState1");
		if (driver.findElement(boton).getText().contains("Guardar configuración")) { // Si hay cambios...
			driver.findElement(boton).click();
		}
	}
	
	static private void checkCheckbox(WebDriver driver, WebElement element, boolean value) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].checked='" + value + "';", element);
	}
	
	static public void setToDefault(WebDriver driver) {
		driver.navigate().to("http:/localhost:8090/");
		fillBasicOptions(driver, "-", "-", false, false, false, false);
		fillAdvancedOptions(driver, null, false);
		deselectAllSecurityHeaders(driver);
	}
	
	static private void deselectAllSecurityHeaders(WebDriver driver) {
		Select select = new Select(driver.findElement(By.id("op6")));
		select.deselectAll();
	}
	
}
