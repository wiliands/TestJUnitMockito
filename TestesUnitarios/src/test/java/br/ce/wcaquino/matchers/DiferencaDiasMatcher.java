package br.ce.wcaquino.matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DiferencaDiasMatcher extends TypeSafeMatcher<Date> {
	
	private Integer diasDiferenca;
	
	public DiferencaDiasMatcher(Integer diasDiferenca) {
		this.diasDiferenca = diasDiferenca;
	}

	@Override
	public void describeTo(Description description) {
		Calendar data = Calendar.getInstance();
		data.add(Calendar.DAY_OF_WEEK, diasDiferenca);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		description.appendText(String.format("O dia correto deveria ser: %s", df.format(data.getTime())));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diasDiferenca));
	}

}
