package front.converters;

import org.junit.platform.commons.util.StringUtils;
import org.primefaces.component.calendar.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@FacesConverter("front.converters.LocalDateConverter")
public class LocalDateConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(extractPattern(uiComponent, facesContext));
        try {
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null || (value instanceof String && StringUtils.isBlank((String) value))) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(extractPattern(uiComponent, facesContext));
        return formatter.format((LocalDate) value);
    }

    private String extractPattern(UIComponent component, FacesContext context) {
        if (component instanceof Calendar) {
            Calendar calendarComponent = (Calendar) component;
            return calendarComponent.getPattern();
        }
        return null;
    }

}
