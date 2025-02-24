package uk.ac.soton.git.comp2211g17.viewmodel;

import javafx.beans.binding.Binding;
import javafx.beans.property.*;
import org.fxmisc.easybind.EasyBind;
import org.jooq.Condition;
import uk.ac.soton.git.comp2211g17.model.query.DatabaseManager;
import uk.ac.soton.git.comp2211g17.view.Utils;

import static org.jooq.impl.DSL.epoch;
import static uk.ac.soton.git.comp2211g17.generated.Tables.SRV;

public class BounceViewModel {
	public final BooleanProperty pagesMode = new SimpleBooleanProperty(true);

	public final IntegerProperty pagesCount = new SimpleIntegerProperty(1);
	public final ReadOnlyStringProperty pagesText = Utils.propBound(new SimpleStringProperty(), EasyBind.map(pagesCount, pages -> {
		if (pages.intValue() == 1) return "when 1 page or less was viewed...";
		return "when " + pages +  " pages or less were viewed...";
	}));

	public final BooleanProperty timeSpentMode = new SimpleBooleanProperty(false);

	public final IntegerProperty timeSpentSeconds = new SimpleIntegerProperty(60);
	public final ReadOnlyStringProperty timeSpentText = Utils.propBound(new SimpleStringProperty(), EasyBind.map(timeSpentSeconds, seconds -> {
		if (seconds.intValue() == 1) return "when 1 second or less was spent on the website...";
		return "when " + seconds + " seconds or less were spent on the website...";
	}));

	private final Binding<Condition> bounceCondition = EasyBind.combine(pagesMode, pagesCount, timeSpentSeconds, (mode, pages, seconds) -> {
		if (mode) {
			return SRV.PAGESVIEWED.le(pages.shortValue());
		} else {
			return epoch(SRV.EXITDATE).sub(epoch(SRV.ENTRYDATE)).le(seconds.intValue());
		}
	});

	public BounceViewModel() {
		pagesMode.addListener((observable, oldValue, newValue) -> timeSpentMode.set(!newValue));
		timeSpentMode.addListener((observable, oldValue, newValue) -> pagesMode.set(!newValue));

		bounceCondition.addListener((observable, oldValue, newValue) -> {
			DatabaseManager.getInstance().bounceCondition = newValue;
			DatabaseManager.getInstance().reload();
		});
	}

	public int getPagesCount() {
		return pagesCount.get();
	}

	public IntegerProperty pagesCountProperty() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount.set(pagesCount);
	}

	public String getPagesText() {
		return pagesText.get();
	}

	public ReadOnlyStringProperty pagesTextProperty() {
		return pagesText;
	}

	public int getTimeSpentSeconds() {
		return timeSpentSeconds.get();
	}

	public IntegerProperty timeSpentSecondsProperty() {
		return timeSpentSeconds;
	}

	public void setTimeSpentSeconds(int timeSpentSeconds) {
		this.timeSpentSeconds.set(timeSpentSeconds);
	}

	public String getTimeSpentText() {
		return timeSpentText.get();
	}

	public ReadOnlyStringProperty timeSpentTextProperty() {
		return timeSpentText;
	}

	public boolean isPagesMode() {
		return pagesMode.get();
	}

	public BooleanProperty pagesModeProperty() {
		return pagesMode;
	}

	public void setPagesMode(boolean pagesMode) {
		this.pagesMode.set(pagesMode);
	}

	public boolean isTimeSpentMode() {
		return timeSpentMode.get();
	}

	public BooleanProperty timeSpentModeProperty() {
		return timeSpentMode;
	}

	public void setTimeSpentMode(boolean timeSpentMode) {
		this.timeSpentMode.set(timeSpentMode);
	}
}
