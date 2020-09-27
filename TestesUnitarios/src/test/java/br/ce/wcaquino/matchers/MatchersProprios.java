package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DiferencaDiasMatcher ehHojeComDiferencaDias(Integer diasDiferenca) {
		return new DiferencaDiasMatcher(diasDiferenca);
	}

	public static DiferencaDiasMatcher ehHoje() {
		return new DiferencaDiasMatcher(0);
	}

}
