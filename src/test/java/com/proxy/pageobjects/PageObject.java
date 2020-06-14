package com.proxy.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.proxy.util.SeleniumUtils;

public class PageObject {

	protected static int timeout = 7;

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		PageObject.timeout = timeout;
	}

	
	/**
	 * Espera por la visibilidad de un texto correspondiente a la propiedad key en el idioma locale en la vista actualmente cargandose en driver..
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param key: clave del archivo de propiedades.
	 * @return Se retornará la lista de elementos resultantes de la búsqueda.
	 */
	static public List<WebElement> checkKey(WebDriver driver, String key) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "text", key, getTimeout());
		return elementos;
	}
	/**
	 *  Espera por la visibilidad de un elemento/s en la vista actualmente cargandose en driver..
	 * 
	 * @param driver: apuntando al navegador abierto actualmente.
	 * @param type: 
	 * @param text:
	 * @return Se retornará la lista de elementos resultantes de la búsqueda.
	 */
	static public List<WebElement> checkElement(WebDriver driver, String type, String text) {
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, type, text, getTimeout());
		return elementos;		
	}
	
}
