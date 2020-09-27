package front.beans;

import api.dtos.MechanicDto;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SessionBean {

    private MechanicDto mechanicDto;

    @PostConstruct
    public void init() {
    }

    public MechanicDto getMechanicDto() {
        return mechanicDto;
    }

    public void setMechanicDto(MechanicDto mechanicDto) {
        this.mechanicDto = mechanicDto;
    }
}
