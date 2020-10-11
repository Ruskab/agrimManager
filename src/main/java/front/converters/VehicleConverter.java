package front.converters;

import front.dtos.Vehicle;
import front.gateways.VehicleGateway;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import static front.util.SessionUtil.getAuthToken;

@FacesConverter("front.converters.VehicleConverter")
public class VehicleConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                VehicleGateway vehicleGateway = new VehicleGateway(getAuthToken());
                return vehicleGateway.read(value);
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid vehicle."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null) {
            return String.valueOf(((Vehicle) value).getId());
        } else {
            return null;
        }
    }

}
