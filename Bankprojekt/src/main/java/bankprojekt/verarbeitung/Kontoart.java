package bankprojekt.verarbeitung;

/**
 * Kontoarten für ein Bankprojekt
 */
public enum Kontoart {
	/**
	 * ein Girokonto
	 */
	GIROKONTO("total hoher Dispo"), 
	/**
	 * Sparbuch
	 */
	SPARBUCH("viele Zinsen"), 
	/**
	 * ein Festgeldkonto, TODO: später implementieren
	 */
	FESTGELDKONTO("vielleicht später");
	
	private String werbebotschaft;
	
	Kontoart(String werbung)   //Default-Sichtbarkeit des KOnstruktors in einer enum: private
	{
		this.werbebotschaft = werbung;
	}

	/**
	 * Liefert die zur Kontoart padsende Werbung
	 * @return the werbebotschaft
	 */
	public String getWerbebotschaft() {
		return this.werbebotschaft;
	}
	
	
	
}
