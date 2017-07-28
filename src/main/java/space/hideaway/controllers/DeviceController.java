package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import space.hideaway.model.Device;
import space.hideaway.model.Site;
import space.hideaway.model.User;
import space.hideaway.services.DeviceService;
import space.hideaway.services.UserManagementImpl;
import space.hideaway.validation.NewDeviceValidator;

import java.util.UUID;

/**
 * Created by Justin on 6/27/2017.
 */

@Controller
public class DeviceController {

    private final UserManagementImpl userManagement;
    private final DeviceService deviceService;
    private final NewDeviceValidator newDeviceValidator;

    @Autowired
    public DeviceController(UserManagementImpl userManagement,
                            DeviceService deviceService,
                            NewDeviceValidator newDeviceValidator){
        this.newDeviceValidator = newDeviceValidator;
        this.deviceService = deviceService;
        this.userManagement = userManagement;
    }

    @RequestMapping(value = "/settings/device", params = {"deviceID"})
    public String loadDevice(Model model, @RequestParam("deviceID") UUID deviceID) {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();

        if (!deviceService.isCorrectUser(currentLoggedInUser, deviceID.toString())) {
            //TODO create this page for user has no access to site settings.
            return "error/no-access";
        }

        if (!model.containsAttribute("device"))
        {
            model.addAttribute("device", deviceService.findByKey(deviceID.toString()));
        }
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "settings/device";
    }

    @RequestMapping(value = "/settings/device", params = {"update"}, method = RequestMethod.POST)
    public String updateDevice(@ModelAttribute("device") Device device) {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        device.setUserID(currentLoggedInUser.getId());
        deviceService.save(device);
        return "redirect:/settings/device?deviceID=" + device.getId().toString();
    }

    @RequestMapping(value = "/settings/device", params = {"delete"}, method = RequestMethod.POST)
    public String deleteDevice(@ModelAttribute("device") Device device) {
        deviceService.delete(device);
        return "redirect:/settings/";
    }

    @RequestMapping(value = "/settings/device/new", method = RequestMethod.GET)
    public String newDevice(Model model){
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("device", new Device());
        return "/newDevice";
    }

    @RequestMapping(value = "/settings/device/new", method = RequestMethod.POST)
    public String addDevice(@ModelAttribute("device") Device device){
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        device.setUserID(currentLoggedInUser.getId());

        deviceService.save(device);
        return "redirect:/dashboard";
    }


}