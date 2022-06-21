package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.service.RoomService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.RoomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller @RequestMapping("admin/room/")
public class RoomController {

    @Autowired private RoomService roomService;

    @Autowired private RoomValidator roomValidator;

    public static String DIR = System.getProperty("user.dir")+"/eFitness-webapp/src/main/resources/static/images/room/";

    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getRooms(Model model){
        model.addAttribute("rooms", this.roomService.getAll());
        return "admin/room/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addRoom (Model model) {
        model.addAttribute("room", new Room());
        return "admin/room/form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addRoom(@ModelAttribute("room") Room room, @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult, Model model) {
        this.roomValidator.validate(room, bindingResult);
        if(!bindingResult.hasErrors()) {
            room.setPhoto(savePhoto(multipartFile, room));
            roomService.save(room);
            return getRooms(model);
        }
        return "admin/room/form";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteRoom(@PathVariable("id")Long id, Model model){
        model.addAttribute("room", this.roomService.getRoomById(id));
        return "confirmDeleteRoom";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteRoomConfirmed(@PathVariable("id")Long id, Model model){
        this.roomService.deleteById(id);
        return getRooms(model);
    }


    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyRoom(@PathVariable("id") Long idRoom, Model model){
        model.addAttribute("room", this.roomService.getRoomById(idRoom));
        return "admin/room/modify";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyRoom(@PathVariable("id") Long idRoom, @ModelAttribute("course") Room room,
                             @RequestParam("image")MultipartFile multipartFile, Model model, BindingResult bindingResult){
        this.roomValidator.validate(room,bindingResult);
        if(!bindingResult.hasErrors()) {
            room.setPhoto(modifyPhoto(multipartFile, idRoom, room));
            this.roomService.modifyById(idRoom, room);
            return "admin/room/list";
        }
        return "admin/room/modify";
    }

    public String savePhoto(MultipartFile multipartFile, Room room){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(room);
        FileManager.store(multipartFile, uploadDir);
        return fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, Room newRoom){
        Room oldRoom = this.roomService.getRoomById(id);
        if(! multipartFile.isEmpty()){
            FileManager.removeImgAndDir(getUploadDir(oldRoom), oldRoom.getPhoto());
            return savePhoto(multipartFile, newRoom);
        }
        FileManager.dirChangeName(getUploadDir(oldRoom), getUploadDir(newRoom));
        return oldRoom.getPhoto();
    }

    public String getUploadDir(Room room){
        return DIR + room.getName().replaceAll("\\s", "");
    }
}
