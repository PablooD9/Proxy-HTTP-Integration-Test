package com.proxy.pageobjects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.proxy.util.SeleniumUtils;

public class PageObjectNavigation extends PageObject {

	/**
	 * CLicka una de las opciones principales (a href) y comprueba que se vaya a la
	 * vista con el elemento de tipo type con el texto Destino
	 * 
	 * @param driver:
	 *            apuntando al navegador abierto actualmente.
	 * @param textOption:
	 *            Texto de la opción principal.
	 * @param criterio:
	 *            "id" or "class" or "text" or "@attribute" or "free". Si el valor
	 *            de criterio es free es una expresion xpath completa.
	 * @param textoDestino:
	 *            texto correspondiente a la b�squeda (comprueba si se ha cargado la p�gina con �xito)
	 */
	public static void clickOption(WebDriver driver, String textOption, String criterio, String textoDestino) 
	{
		// CLickamos en la opción que queremos, y esperamos la carga
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "@href", textOption,
																						getTimeout());
		// Tiene que haber un solo elemento.
		assertTrue(elementos.size() == 1);
		// Ahora lo clickamos
		elementos.get(0).click();
		// Esperamos a que sea visible un elemento concreto
		elementos = SeleniumUtils.EsperaCargaPagina(driver, criterio, textoDestino, getTimeout());
		// Tiene que haber un solo elemento.
		assertTrue(elementos.size() == 1);
	}
	
	
	/**
	 * Selecciona el enlace de idioma correspondiente al texto textLanguage
	 * 
	 * @param driver:
	 *            apuntando al navegador abierto actualmente.
	 * @param textLanguage:
	 *            el texto que aparece en el enlace de idioma ("English" o
	 *            "Spanish")
	 */
	public static void changeIdiom(WebDriver driver, String textLanguage) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "language-switch", getTimeout());
		elementos.get(0).click();
	}
	
	
	public static void desplegarPost(WebDriver driver, String textoOpcion) 
	{
		// clickamos la opci�n de Mis post
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "posts", getTimeout());
		elementos.get(0).click();
		
		// Esperamos a que aparezca el men�
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "desplieguePosts", getTimeout());
		// SeleniumUtils.esperarSegundos(driver, 2);
		
		// CLickamos la opci�n textoOpcion
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", textoOpcion, getTimeout());
		elementos.get(0).click();
	}
	

	public static void desplegarUsuarios(WebDriver driver, String textoOpcion) 
	{
		// clickamos la opción de Usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "usuarios", getTimeout());
		elementos.get(0).click();
		
		// Esperamos a que aparezca el menú
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "despliegueUsuarios", getTimeout());
		// SeleniumUtils.esperarSegundos(driver, 2);
		
		// CLickamos la opción de 'textoOpcion'
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", textoOpcion, getTimeout());
		elementos.get(0).click();
	}
	
	public static void desplegarNotas(WebDriver driver, String textoOpcion) 
	{
		// clickamos la opción Gestión de notas.
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "notas", getTimeout());
		elementos.get(0).click();
		
		// Esperamos a que aparezca el menú de opciones.
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "despliegueGestNotas", getTimeout());
		// SeleniumUtils.esperarSegundos(driver, 2);
		
		// CLickamos la opción textoOpcion
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", textoOpcion, getTimeout());
		elementos.get(0).click();
	}
	
}
