package com.proxy;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.proxy.pageobjects.PageObject;
import com.proxy.pageobjects.PageObjectConfiguration;
import com.proxy.pageobjects.PageObjectLogin;
import com.proxy.pageobjects.PageObjectNavigation;
import com.proxy.pageobjects.PageObjectSignUp;
import com.proxy.util.SeleniumUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTests {

	static WebDriver driver;
	private static String userAgentHeader;
	
	@Before
	public void setUp() {
		userAgentHeader = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
		WebDriverManager.chromedriver().proxy("http://localhost:8080").setup();
		driver = getDriver();
		// Lo dejamos con las opciones con los valores por defecto.
		PageObjectConfiguration.setToDefault(driver);
	}
	
	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
		driver.navigate().to("http://localhost:8090/");
		// Lo dejamos con las opciones con los valores por defecto.
		PageObjectConfiguration.setToDefault(driver);
		driver.close();
	}
	
	// Al finalizar la última prueba
	@AfterClass 
	static public void end() {
		driver = getDriver();
		// Borramos todo el contenido de la BBDD de pruebas.
		driver.navigate().to("http://localhost:8090/deleteAllDBTest");
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}
	
	@Test
	public void test1_1SuccessfulLogin() {
		// Vamos a la ventana de registro.
		driver.navigate().to("http://localhost:8090/signup");
		
		// Registramos un nuevo usuario
		registerNewUser();
		// Se debe visualizar la pantalla de Login una vez registrados.
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Crear una cuenta", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
		
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Nos autenticamos.
		PageObjectLogin.fillForm(driver, "emailTest@test.com", "passTest123");
		// Se debe visualizar la pantalla de Configuración una vez autenticados.
		elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Bienvenid@", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test1_2UnsuccessfulLogin() {
		// Vamos a la ventana de Login.
		driver.navigate().to("http://localhost:8090/login");
		// Nos autenticamos erróneamente.
		PageObjectLogin.fillForm(driver, "emailTest@test.com", "passBADTest123");
		// Comprobamos que seguimos en la pantalla de Login con un mensaje de error en pantalla.
		List<WebElement> elements = PageObject.checkElement(driver, "text", "El correo o la contraseña son incorrectos");
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test2DisconnectUserLoggedIn() {
		// Vamos a la ventana de Login.
		driver.navigate().to("http://localhost:8090/login");
		// Nos autenticamos.
		PageObjectLogin.fillForm(driver, "emailTest@test.com", "passTest123");
		// Se debe visualizar la pantalla de Configuración una vez autenticados.
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Bienvenid@", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
		
		SeleniumUtils.esperarSegundos(driver, 1);
		
		// Clicamos en el enlace para desconectarnos.
		PageObjectNavigation.clickOption(driver, "logout", "text", "Crear una cuenta");
	}
	
	@Test
	public void test3_1setBasicOption1ToDefault() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "-", false, false, false, false);
		// Comprobamos cuál es la cabecera "User-Agent" enviada en las peticiones.
		driver.navigate().to("http://localhost:8090/testUserAgent");
		// Se debe visualizar la cabecera User-Agent utilizada (la de por defecto).
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", userAgentHeader, PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test3_2setBasicOption1BrowserButNotOS() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "Google Chrome", false, false, false, false);
		// Comprobamos cuál es la cabecera "User-Agent" enviada en las peticiones.
		driver.navigate().to("http://localhost:8090/testUserAgent");
		// Se debe visualizar la cabecera User-Agent utilizada (la de por defecto).
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", userAgentHeader, PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test3_3setBasicOption1BrowserAndOS() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "Windows", "Opera", false, false, false, false);
		// Comprobamos cuál es la cabecera "User-Agent" enviada en las peticiones.
		driver.navigate().to("http://localhost:8090/testUserAgent");
		// Se debe visualizar la cabecera User-Agent utilizada (al elegir Opera como navegador, la cabecera deberá contener "OPR/").
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "OPR/", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test4setBasicOption2SpanishHosts() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "-", true, false, false, false);
		// Navegamos hacia un sitio web que esté incluido en la lista de hosts españoles maliciosos.
		driver.navigate().to("http://www.media-match.com");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "BLOCKED BY PROXY :)".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "BLOCKED BY PROXY :)", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test5setBasicOption3MaliciousHosts() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "-", false, true, false, false);
		// Navegamos hacia un sitio web que esté incluido en la lista de hosts maliciosos.
		driver.navigate().to("http://2amsports.com");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "BLOCKED BY PROXY :)".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "BLOCKED BY PROXY :)", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test6setBasicOption4TrackerHosts() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "-", false, false, true, false);
		// Navegamos hacia un sitio web que esté incluido en la lista de hosts que rastrean usuarios.
		driver.navigate().to("http://publicidad.elmundo.es");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "BLOCKED BY PROXY :)".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "BLOCKED BY PROXY :)", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test7setBasicOption5PornographicHosts() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillBasicOptions(driver, "-", "-", false, false, false, true);
		// Navegamos hacia un sitio web que esté incluido en la lista de hosts con contenido pornográfico.
		driver.navigate().to("http://www.xvideos.com");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "BLOCKED BY PROXY :)".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "BLOCKED BY PROXY :)", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test8setAdvancedOption1SecurityHeaders() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones avanzadas de configuración y pulsamos el botón "Guardar preferencias".
		List<String> securityHeaders = new ArrayList<String>();
		securityHeaders.add("Content-Security-Policy");
		securityHeaders.add("Strict-Transport-Security");
		securityHeaders.add("X-Content-Type-Options");
		securityHeaders.add("X-Frame-Options");
		securityHeaders.add("X-Xss-Protection");
		PageObjectConfiguration.fillAdvancedOptions(driver, securityHeaders, false);
		// Navegamos hacia un sitio web que no implemente las cabeceras de seguridad especificadas.
		driver.navigate().to("http://www.uniovi.es");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "Website does not implement the security headers".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Website does not implement the security headers", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test9setAdvancedOption2Cookies() {
		// Navegamos hacia un sitio web de pruebas.
		driver.navigate().to("http://localhost:8090/testCookieHeader");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "Cookies are sent.".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Cookies are sent.", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
		
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones avanzadas de configuración y pulsamos el botón "Guardar preferencias".
		PageObjectConfiguration.fillAdvancedOptions(driver, new ArrayList<String>(), true);
		// Navegamos hacia un sitio web de pruebas.
		driver.navigate().to("http://localhost:8090/testCookieHeader");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "Cookies are not sent or recieved.".
		elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Cookies are not sent or recieved.", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test10_1addSecurityException() {
		// Vamos a la ventana de Configuración.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Rellenamos las opciones avanzadas de configuración y pulsamos el botón "Guardar preferencias".
		List<String> securityHeaders = new ArrayList<String>();
		securityHeaders.add("Content-Security-Policy");
		securityHeaders.add("Strict-Transport-Security");
		securityHeaders.add("X-Content-Type-Options");
		securityHeaders.add("X-Frame-Options");
		securityHeaders.add("X-Xss-Protection");
		PageObjectConfiguration.fillAdvancedOptions(driver, securityHeaders, false);
		// Navegamos hacia un sitio web que no implemente las cabeceras de seguridad especificadas.
		driver.navigate().to("http://www.uniovi.es");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "Website does not implement the security headers".
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Website does not implement the security headers", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
		// Añadimos la excepción de seguridad
		String href = "http://localhost:8090/addSecurityException?host=www.uniovi.es";
		PageObjectNavigation.clickOption(driver, href, "text", "Ahora puede visitar el siguiente sitio web");
		// Vamos a la pantalla de configuración nuevamente a comprobar que se ha añadido correctamente la excepción de seguridad.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		
		PageObjectConfiguration.openManageSecurityExceptions(driver);
		elements = SeleniumUtils.EsperaCargaPagina(driver, "class", "secExceptionInfo", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	@Test
	public void test10_2checkIfSecurityExceptionWasAdded() {
		// A raíz del test anterior, tenemos una excepción sobre el sitio web "www.uniovi.es".
		// Vamos a comprobar primero que seguimos teniendo dicha excepción de seguridad.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		PageObjectConfiguration.openManageSecurityExceptions(driver);
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "class", "secExceptionInfo", PageObject.getTimeout());
		assertEquals(elements.size(), 1);

		// Navegamos hacia el sitio web www.uniovi.es, y comprobamos que podemos visualizarlo correctamente
		// (es decir, no deberíamos ver el texto "Website does not implement the security headers")
		driver.navigate().to("http://www.uniovi.es");
		SeleniumUtils.textoNoPresentePagina(driver, "Website does not implement the security headers");
	}
	
	@Test
	public void test10_3deleteSecurityExceptionAndTryToNavigateAgain() {
		// A raíz del test anterior, tenemos una excepción sobre el sitio web "www.uniovi.es".
		// Vamos a comprobar primero que seguimos teniendo dicha excepción de seguridad.
		driver.navigate().to("http://localhost:8090/");
		SeleniumUtils.esperarSegundos(driver, 1);
		PageObjectConfiguration.openManageSecurityExceptions(driver);
		List<WebElement> elements = SeleniumUtils.EsperaCargaPagina(driver, "class", "secExceptionInfo", PageObject.getTimeout());
		assertEquals(elements.size(), 1);

		// Borramos la excepción de seguridad
		PageObjectConfiguration.deleteSecurityException(driver, "www.uniovi.es");
		
		// Navegamos hacia el sitio web www.uniovi.es, y comprobamos que está bloqueado.
		driver.navigate().to("http://www.uniovi.es");
		SeleniumUtils.esperarSegundos(driver, 1);
		// Se debe visualizar el texto "Website does not implement the security headers".
		elements = SeleniumUtils.EsperaCargaPagina(driver, "text", "Website does not implement the security headers", PageObject.getTimeout());
		assertEquals(elements.size(), 1);
	}
	
	
	
	/*
	 * 
	 * =========> Métodos privados <========
	 * 
	 */
	private void registerNewUser() {
		// Rellenamos los campos de registro.
		PageObjectSignUp.fillForm(driver, "usuarioTest", "emailTest@test.com", "passTest123");
	}
	
	private static WebDriver getDriver() {
		ChromeOptions chromeOptions = new ChromeOptions();
		List<String> args = new ArrayList<String>();
		args.add("user-agent="+userAgentHeader);
		chromeOptions.addArguments(args);
		return new ChromeDriver(chromeOptions);
	}
}
