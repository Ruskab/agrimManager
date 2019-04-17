package client_beans.util.converters;

import api.dtos.ClientDto;
import client_beans.clients.ClientGateway;
import org.junit.platform.commons.util.StringUtils;
import org.primefaces.component.calendar.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@FacesConverter("client_beans.util.converters.ClientConverter")
public class ClientConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                ClientGateway clientGateway = new ClientGateway();
                return clientGateway.read(value);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid client."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if(value != null) {
            return String.valueOf(((ClientDto) value).getId());
        }
        else {
            return null;
        }
    }

    private String extractPattern(UIComponent component, FacesContext context) {
        if (component instanceof Calendar) {
            Calendar calendarComponent = (Calendar) component;
            return calendarComponent.getPattern();
        }
        return null;
    }

}
