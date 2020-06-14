package com.proxy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.proxy.pageobjects.PageObject;
import com.proxy.pageobjects.PageObjectSignUp;

import io.github.bonigarcia.wdm.WebDriverManager;

public class IntegrationTests {

	static WebDriver driver;
	
	@Before
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = getDriver();
		
		// Navegamos a la página principal.
		String URLHome = "http://localhost:8090";
		driver.navigate().to(URLHome);
	}
	
	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}
	
	// Al finalizar la última prueba
	@AfterClass 
	static public void end() {
		// Borramos todo el contenido de la BBDD de pruebas.
		driver.navigate().to("http://localhost:8090/deleteAllDBTest");
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}
	
	@Test
	public void testSuccessfulLogin() {
		// Vamos a la ventana de registro.
		driver.navigate().to("http://localhost:8090/signup");
		// Rellenamos los campos de registro.
		PageObjectSignUp.fillForm(driver, "usuarioTest", "emailTest@test.com", "passTest123");
		// Se debe visualizar la pantalla de Login una vez registrados.
		PageObject.checkElement(driver, "text", "Crear una cuenta");
	}
	
	/*
	 * 
	 * =========> Métodos privados <========
	 * 
	 */
	private WebDriver getDriver() {
		return new ChromeDriver();
	}
}
